package com.secor.orderservice.repository;

import com.secor.orderservice.entity.OrderHeader;
import com.secor.orderservice.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

	public Integer deleteByOrderHeader(OrderHeader orderHeader);
}
