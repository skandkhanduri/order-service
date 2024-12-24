package com.secor.orderservice.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "order_items")
public class OrderLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_Item_Id",nullable = false)
	private Long orderItemId;
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	@JsonIgnore
	private OrderHeader orderHeader;
	@Column(name = "product_Id",nullable = false)
	private Long productId;
	@Column(nullable = false)
	private Integer quantity;
	@Column(nullable = false)
	private BigDecimal price;

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public Long getProductId() {
		return productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Transient
	public BigDecimal getSubtotal() {
		return price.multiply(BigDecimal.valueOf(quantity));
	}
}
