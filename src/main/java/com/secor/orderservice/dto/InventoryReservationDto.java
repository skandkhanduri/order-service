package com.secor.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class InventoryReservationDto {
    private Long orderId;


    private Long productId;


    private int quantityReserved;

    public InventoryReservationDto(Long orderId, Long productId, int quantityReserved) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantityReserved = quantityReserved;
    }

    public InventoryReservationDto() {

    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }
}
