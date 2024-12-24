package com.secor.orderservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "orders")
public class OrderHeader {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	@Column(name = "order_Id",nullable = false)
	private Long orderId;
	@Column(name = "customer_Id",nullable = false)
	private Long customerId;
	@Column(name = "order_Date",nullable = false)

	private LocalDateTime orderDate;
	@Column(nullable = false)
	private String status;
	@Column(name = "payment_Method",nullable = false)
	private String paymentMethod;
	@Column(name = "total_Amount",nullable = false)
	private BigDecimal totalAmount;
	@CreationTimestamp
	@Column(name = "created_At",nullable = false)
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@Column(name = "updated_At",nullable = false)
	private LocalDateTime updatedAt;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderHeader", orphanRemoval = true)
	@JsonProperty("orderLine")
	private List<OrderLine> orderLine= new ArrayList<>();

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setOrderLine(List<OrderLine> orderLine) {
		this.orderLine = orderLine;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public String getStatus() {
		return status;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public List<OrderLine> getOrderLine() {
		return orderLine;
	}
}
