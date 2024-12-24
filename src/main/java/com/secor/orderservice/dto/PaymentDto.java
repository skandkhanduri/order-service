package com.secor.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

	private Long paymentId;
	private Long orderId;
	private BigDecimal amount;
	private String paymentMethod;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
