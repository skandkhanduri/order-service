package com.secor.orderservice.KafkaConsumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secor.orderservice.config.KafkaProducer;
import com.secor.orderservice.dto.InventoryResponseDto;
import com.secor.orderservice.dto.PaymentResponseDto;
import com.secor.orderservice.entity.OrderHeader;
import com.secor.orderservice.repository.OrderHeaderRepository;
import com.secor.orderservice.service.OrderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Consumer;
@Service
public class Consumers {

    private OrderServices orderServices;
    KafkaProducer kafkaProducer;

    Consumers(OrderServices orderServices,KafkaProducer kafkaProducer)
    {
        this.orderServices=orderServices;
        this.kafkaProducer= kafkaProducer;

    }

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "order-events",groupId = "1")
    public void handleInventoryReserved(String responseJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the JSON string into an InventoryResponseDto object
        InventoryResponseDto response = objectMapper.readValue(responseJson, InventoryResponseDto.class);

        if (response.getStatus().equals("Success")) {
            logger.info(response.getMessage());
            kafkaProducer.publishPayment(orderServices.getPaymentDto(Long.valueOf(response.getRequestId())));
        }
        else
        {
            orderServices.setOrderStatus(Long.valueOf(response.getRequestId()),"Failed");
        }

    }

    @KafkaListener(topics = "payment-status-topic",groupId = "1")
    public void handleIPaymentStatus(String responseJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the JSON string into an InventoryResponseDto object
        PaymentResponseDto response = objectMapper.readValue(responseJson, PaymentResponseDto.class);

        if (response.getStatus().equals("Success")) {
            logger.info(response.getMessage());
            orderServices.setOrderStatus(Long.valueOf(response.getRequestId()),"COMPLETED");
        }
        else
        {
            kafkaProducer.publishUnReserveInventory(Long.valueOf(response.getRequestId()));
        }
    }


}
