package com.secor.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.secor.orderservice.WebClients.InventoryWebClient;
import com.secor.orderservice.WebClients.PaymentWebClient;
import com.secor.orderservice.WebClients.ProductWebClient;
import com.secor.orderservice.config.KafkaProducer;
import com.secor.orderservice.dto.*;
import com.secor.orderservice.entity.OrderHeader;
import com.secor.orderservice.entity.OrderLine;
import com.secor.orderservice.mapper.OrderMapper;
import com.secor.orderservice.repository.OrderHeaderRepository;
import com.secor.orderservice.repository.OrderLineRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServices {
    private static final Logger log = LoggerFactory.getLogger(OrderServices.class);

    OrderHeaderRepository orderHeaderRepository;
    OrderLineRepository orderLineRepository;

    InventoryWebClient inventoryWebClient;
    PaymentWebClient paymentWebClient;
    ProductWebClient productWebClient;
    KafkaProducer kafkaProducer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String ORDER_STATUS_KEY_PREFIX = "order-status-";

    OrderServices(OrderHeaderRepository orderHeaderRepository,
                  OrderLineRepository orderLineRepository,
                  InventoryWebClient inventoryWebClient,
                  PaymentWebClient paymentWebClient,
                  ProductWebClient productWebClient,
                  KafkaProducer kafkaProducer)
    {
     this.orderHeaderRepository=orderHeaderRepository;
        this.orderLineRepository=orderLineRepository;
        this.inventoryWebClient=inventoryWebClient;
        this.paymentWebClient=paymentWebClient;
        this.productWebClient=productWebClient;
        this.kafkaProducer=kafkaProducer;
    }


    public Mono <OrderDetailsResponseDto> getOrderDetailsById(Long orderId)

    {


        Mono<OrderHeader> orderHeaderMono = Mono.justOrEmpty(orderHeaderRepository.findById(orderId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found for id: " + orderId)));

        Mono<PaymentDto> paymentMono = paymentWebClient.getPaymentDetails(orderId)
                .onErrorResume(error -> {
                    // Log the error and provide fallback data
                    log.error("Error fetching payment details for orderId {}: {}", orderId, error.getMessage());
                    return Mono.empty(); // Fallback to empty if payment details cannot be fetched
                });

        return orderHeaderMono.flatMap(orderHeader -> {
            OrderHeaderDto orderHeaderDto = OrderMapper.INSTANCE.toHeaderDTO(orderHeader);
            List<OrderLineDto> orderLineDtos = OrderMapper.INSTANCE.toLineDTOList(orderHeader.getOrderLine());
            Mono<List<OrderLineDto>> resolvedOrderLinesMono = Flux.fromIterable(orderLineDtos)
                    .flatMap(orderLine ->
                            productWebClient.getProductDetails(orderLine.getProductId())
                                    .map(product -> {
                                        orderLine.setProductName(product.getName());
                                        orderLine.setProductDescription(product.getDescription());
                                        return orderLine;
                                    })
                                    .onErrorResume(productError -> {
                                        log.error("Error fetching product details for productId {}: {}",
                                                orderLine.getProductId(), productError.getMessage());
                                        return Mono.just(orderLine); // Return the order line as-is
                                    }))
                    .collectList();
            return Mono.zip(resolvedOrderLinesMono, paymentMono.defaultIfEmpty(new PaymentDto()))
                    .map(tuple -> {
                        List<OrderLineDto> resolvedOrderLines = tuple.getT1();
                        PaymentDto paymentDto = tuple.getT2();

                        // Create and return the final response DTO
                        return new OrderDetailsResponseDto(orderHeaderDto, resolvedOrderLines, paymentDto);
                    });
        });
    }

public OrderResponseDto createOrder(OrderHeader orderHeader, List<OrderLine> orderLines) throws JsonProcessingException {
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (OrderLine orderLine : orderLines) {
        totalAmount = totalAmount.add(orderLine.getSubtotal());

    }
    List<InventoryReservationDto> inventoryReserveList=new ArrayList<>();
    orderHeader.setTotalAmount(totalAmount);
    orderHeader.setOrderDate(LocalDateTime.now());
    orderHeader.setStatus("pending");
    orderHeader.setCreatedAt(LocalDateTime.now());
    OrderHeader savedOrderHeader = orderHeaderRepository.save(orderHeader);
    orderLines.forEach(line -> {line.setOrderHeader(savedOrderHeader);
        InventoryReservationDto dto=new InventoryReservationDto(savedOrderHeader.getOrderId(),line.getProductId(), line.getQuantity());
        inventoryReserveList.add(dto);
        });
    orderLineRepository.saveAll(orderLines);

    kafkaProducer.publishReserveInventory(inventoryReserveList);
return new OrderResponseDto("Order is being processed", savedOrderHeader.getOrderId(), "In-Progress");
}
    public Mono<OrderResponseDto> placeOrder(OrderHeader orderHeader, List<OrderLine> orderLines, String secToken,String cookieName)
    {
        String redisKey = cookieName;
        return Mono.defer(() -> {
            // Check if the status is already cached

            Object cachedOrderStatus = redisTemplate.opsForValue().get(redisKey);
            if (cachedOrderStatus != null) {
                // Return cached response if order status is already present in Redis
                return Mono.just(getCachedResponse(redisKey));
            }


            return Mono.fromCallable(
                    () -> {
                        BigDecimal totalAmount = BigDecimal.ZERO;
                        for (OrderLine orderLine : orderLines) {
                            totalAmount = totalAmount.add(orderLine.getSubtotal());

                        }
                        orderHeader.setTotalAmount(totalAmount);
                        orderHeader.setOrderDate(LocalDateTime.now());
                        orderHeader.setStatus("pending");
                        orderHeader.setCreatedAt(LocalDateTime.now());
                        OrderHeader savedOrderHeader = orderHeaderRepository.save(orderHeader);
                        orderLines.forEach(line -> line.setOrderHeader(savedOrderHeader));
                        orderLineRepository.saveAll(orderLines);
                        return savedOrderHeader;
                    }).flatMap(order -> {
                // Step 2: Trigger Payment API Call Asynchronously
                return makePayment(order,redisKey)
                        .then(Mono.just(order)); // Ensure the order object is returned after payment
            }).map(order -> {
                // Step 3: Construct the order response
                OrderResponseDto responseDto = new OrderResponseDto("Order is being processed", order.getOrderId(), "In-Progress");

                // Step 4: Cache the response in Redis for 30 minutes before payment is completed

                redisTemplate.opsForValue().set(redisKey, responseDto, Duration.ofSeconds(300)); // Cache for 30 minutes

                // Set the cookie in the response header
             //   response.addCookie(new Cookie("secToken", secToken));

                return responseDto;
            });
        });
    }
    private Mono<PaymentDto> makePayment(OrderHeader order,String redisKey) {
        // Create PaymentRequestDto
        PaymentDto paymentRequest = new PaymentDto();
        paymentRequest.setOrderId(order.getOrderId());
        paymentRequest.setAmount(order.getTotalAmount());
        paymentRequest.setPaymentMethod("Card");
        log.info("value of rediskey is "+redisKey);
        // Use WebClient to call the payment API

        return paymentWebClient.makePayment(paymentRequest).doOnSuccess(response->{
            log.info("inside the code");
            order.setStatus("COMPLETED");
            OrderHeader savedOrder=   orderHeaderRepository.save(order);
            OrderResponseDto responseDto = new OrderResponseDto("Order Processed Successfully", order.getOrderId(), "Completed");
            try {
                redisTemplate.opsForValue().set(redisKey, responseDto, Duration.ofSeconds(300));
                log.info("Successfully updated Redis cache with key={}, value={}", redisKey, responseDto);
            } catch (Exception e) {
                log.error("Failed to update Redis cache: {}", e.getMessage());
            }
            try {
                kafkaProducer.publishOrderDatum(savedOrder);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }).doOnError(error->{ order.setStatus("PAYMENT_FAILED");
            OrderHeader savedOrder= orderHeaderRepository.save(order);
            OrderResponseDto responseDto = new OrderResponseDto("Order Processing Failed as payment failed", order.getOrderId(), "Failed");
            redisTemplate.opsForValue().set(redisKey, responseDto, Duration.ofSeconds(300)); // Cache for 30 minutes

            try {
                kafkaProducer.publishOrderDatum(savedOrder);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private OrderResponseDto getCachedResponse(String redisKey) {
        Object cachedObject = redisTemplate.opsForValue().get(redisKey);
        if (cachedObject instanceof LinkedHashMap) {
            // Convert LinkedHashMap to OrderResponseDto
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // For LocalDateTime
            return objectMapper.convertValue(cachedObject, OrderResponseDto.class);
        }
        return (OrderResponseDto) cachedObject;
    }

    public PaymentDto getPaymentDto(Long orderId)
    {
        OrderHeader orderHeader=orderHeaderRepository.findById(orderId).get();
        PaymentDto paymentRequest = new PaymentDto();
        paymentRequest.setOrderId(orderHeader.getOrderId());
        paymentRequest.setAmount(orderHeader.getTotalAmount());
        paymentRequest.setPaymentMethod(orderHeader.getPaymentMethod());
        return paymentRequest;
    }

public void setOrderStatus(Long orderId,String status)
{
    OrderHeader order = orderHeaderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    order.setStatus(status);
    orderHeaderRepository.save(order);
}
}
