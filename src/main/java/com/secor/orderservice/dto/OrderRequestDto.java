package com.secor.orderservice.dto;

import java.util.List;


import com.secor.orderservice.entity.OrderHeader;
import com.secor.orderservice.entity.OrderLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
	private OrderHeaderDto orderHeader;
    private List<OrderLineDto> orderLines;

    public void setOrderHeader(OrderHeaderDto orderHeader) {
        this.orderHeader = orderHeader;
    }

    public void setOrderLines(List<OrderLineDto> orderLines) {
        this.orderLines = orderLines;
    }

    public OrderHeaderDto getOrderHeader() {
        return orderHeader;
    }

    public List<OrderLineDto> getOrderLines() {
        return orderLines;
    }
}
