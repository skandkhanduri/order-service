package com.secor.orderservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.secor.orderservice.dto.InventoryReservationDto;
import com.secor.orderservice.dto.PaymentDto;
import com.secor.orderservice.entity.OrderHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducer
{
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static final String TOPIC = "order-events";
    private static final String InventoryReserverTopic = "inventory-reserve-events";
    private static final String InventoryUnReserverTopic = "inventory-unreserve-events";
    private static final String PaymentTopic = "payment-initiation-topic";
    @Autowired //DEPENDENCY INJECTION PROMISE FULFILLED AT RUNTIME
    private KafkaTemplate<String, String> kafkaTemplate ;



    public void publishOrderDatum(OrderHeader orderHeader) throws JsonProcessingException // LOGIN | REGISTER
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String datum =  objectMapper.writeValueAsString(orderHeader);
        logger.info(String.format("#### -> Producing message ->"));
        this.kafkaTemplate.send(TOPIC, datum);
   }

    public void publishReserveInventory(List<InventoryReservationDto> inventoryReservationDtos) throws JsonProcessingException // LOGIN | REGISTER
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String datum =  objectMapper.writeValueAsString(inventoryReservationDtos);

        logger.info(String.format("#### -> Producing message ->"));
        this.kafkaTemplate.send(InventoryReserverTopic, datum);
    }

    public void publishUnReserveInventory( Long orderId) throws JsonProcessingException // LOGIN | REGISTER
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String datum =  objectMapper.writeValueAsString(orderId);

        logger.info(String.format("#### -> Producing message ->"));
        this.kafkaTemplate.send(InventoryUnReserverTopic, datum);
    }
    public void publishPayment(PaymentDto paymentDto) throws JsonProcessingException
    {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String datum =  objectMapper.writeValueAsString(paymentDto);
        logger.info(String.format("#### -> Producing message ->"));
        this.kafkaTemplate.send(PaymentTopic, datum);
    }


}
