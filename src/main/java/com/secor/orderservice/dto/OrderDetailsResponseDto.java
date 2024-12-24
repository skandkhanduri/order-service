package com.secor.orderservice.dto;

import java.util.List;

public class OrderDetailsResponseDto {

    private OrderHeaderDto orderHeader;
    private List<OrderLineDto> orderLines;
    PaymentDto paymentDto;

    public OrderDetailsResponseDto(OrderHeaderDto orderHeader, List<OrderLineDto> orderLines, PaymentDto paymentDto) {
        this.orderHeader = orderHeader;
        this.orderLines = orderLines;
        this.paymentDto = paymentDto;
    }
    public OrderDetailsResponseDto() {

    }

    public void setOrderHeader(OrderHeaderDto orderHeader) {
        this.orderHeader = orderHeader;
    }

    public void setOrderLines(List<OrderLineDto> orderLines) {
        this.orderLines = orderLines;
    }

    public void setPaymentDto(PaymentDto paymentDto) {
        this.paymentDto = paymentDto;
    }

    public OrderHeaderDto getOrderHeader() {
        return orderHeader;
    }

    public List<OrderLineDto> getOrderLines() {
        return orderLines;
    }

    public PaymentDto getPaymentDto() {
        return paymentDto;
    }
}
