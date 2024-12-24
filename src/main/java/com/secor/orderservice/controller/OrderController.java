package com.secor.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.secor.orderservice.dto.*;
import com.secor.orderservice.entity.OrderHeader;
import com.secor.orderservice.entity.OrderLine;
import com.secor.orderservice.mapper.OrderMapper;
import com.secor.orderservice.service.OrderServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/order/v1")
public class OrderController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private OrderServices orderServices;
    OrderController(OrderServices orderServices)
    {
        this.orderServices=orderServices;
    }

    @GetMapping("/getOrderDetails")
    public Mono<ResponseEntity<OrderDetailsResponseDto>> getOrderDetailsById(@RequestParam("orderId") Long orderId
                                             ) throws JsonProcessingException {
        System.out.println("Inside the controller");
        return orderServices.getOrderDetailsById(orderId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new OrderDetailsResponseDto()));
                });
    }

    @PostMapping("/placeOrder")
    public Mono<ResponseEntity<OrderResponseDto>> placeOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                             @RequestHeader("Sectoken") String secToken,
                                                             HttpServletRequest request,
                                                             HttpServletResponse httpResponse) {

        Cookie[] cookies = request.getCookies();
        boolean cookieFound = false;
        String cookieName = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("order-status-"+secToken)) {
                    // Return the value of the cookie
                    cookieName=cookie.getName();
                    cookieFound=true;
                }
            }
        }

        // If the cookie is not found, create a new cookie and store it in Redis
        if (!cookieFound) {
          //  String newCookieValue = secToken + "_new_order"; // You can generate a value based on logic
            cookieName="order-status-"+secToken;
            // Set cookie in the HTTP response
            Cookie newCookie = new Cookie(cookieName, null);
            newCookie.setPath("/"); // Set the cookie's path
            newCookie.setMaxAge(300); // Set the cookie's expiration (1 day)
            httpResponse.addCookie(newCookie); // Add the cookie to the response

            // Store the cookie value in Redis with a key, for example, based on secToken
            //redisTemplate.opsForValue().set("orderStatus:" + secToken, null, Duration.ofHours(24)); // Store for 24 hours
        }

        OrderHeader orderheader=OrderMapper.INSTANCE.toEntity(orderRequestDto.getOrderHeader());
        List<OrderLine> orderLines=OrderMapper.INSTANCE.toLineEntityList(orderRequestDto.getOrderLines());
        return orderServices.placeOrder(orderheader, orderLines,secToken,cookieName) // Assuming this returns a Mono<OrderResponseDto>
                .map(ResponseEntity::ok) // Wrap the response in ResponseEntity
                .onErrorResume(error -> {
                    // Handle error and return a fallback response
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new OrderResponseDto("Error placing order", null, error.getMessage())));
                });
    }


    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) throws JsonProcessingException {
        OrderHeader orderheader=OrderMapper.INSTANCE.toEntity(orderRequestDto.getOrderHeader());
        List<OrderLine> orderLines=OrderMapper.INSTANCE.toLineEntityList(orderRequestDto.getOrderLines());
        OrderResponseDto orderResponseDto= orderServices.createOrder(orderheader, orderLines);
        return new ResponseEntity<>(orderResponseDto,HttpStatus.OK);


    }
}
