package com.secor.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto  implements Serializable {
    private String message;
    private Long orderId;
    private String status;

    public OrderResponseDto(String message, Long orderId, String status) {
        this.message = message;
        this.orderId = orderId;
        this.status = status;
    }
    public OrderResponseDto()
    {}

    public String getMessage() {
        return message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
