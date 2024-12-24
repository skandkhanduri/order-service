package com.secor.orderservice.mapper;

import com.secor.orderservice.dto.OrderDetailsResponseDto;
import com.secor.orderservice.dto.OrderHeaderDto;
import com.secor.orderservice.dto.OrderLineDto;
import com.secor.orderservice.dto.OrderRequestDto;
import com.secor.orderservice.entity.OrderHeader;
import com.secor.orderservice.entity.OrderLine;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-24T00:40:50+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderHeaderDto toHeaderDTO(OrderHeader headerEntity) {
        if ( headerEntity == null ) {
            return null;
        }

        OrderHeaderDto orderHeaderDto = new OrderHeaderDto();

        orderHeaderDto.setPaymentMethod( headerEntity.getPaymentMethod() );
        orderHeaderDto.setOrderId( headerEntity.getOrderId() );
        orderHeaderDto.setCustomerId( headerEntity.getCustomerId() );
        orderHeaderDto.setOrderDate( headerEntity.getOrderDate() );
        orderHeaderDto.setStatus( headerEntity.getStatus() );
        orderHeaderDto.setTotalAmount( headerEntity.getTotalAmount() );
        orderHeaderDto.setCreatedAt( headerEntity.getCreatedAt() );
        orderHeaderDto.setUpdatedAt( headerEntity.getUpdatedAt() );

        return orderHeaderDto;
    }

    @Override
    public OrderDetailsResponseDto toHeaderResponseDTO(OrderHeader headerEntity) {
        if ( headerEntity == null ) {
            return null;
        }

        OrderDetailsResponseDto orderDetailsResponseDto = new OrderDetailsResponseDto();

        return orderDetailsResponseDto;
    }

    @Override
    public OrderRequestDto toHeaderReqDTO(OrderHeader headerEntity) {
        if ( headerEntity == null ) {
            return null;
        }

        OrderRequestDto orderRequestDto = new OrderRequestDto();

        return orderRequestDto;
    }

    @Override
    public List<OrderHeaderDto> toHeaderDTOList(List<OrderHeader> entity) {
        if ( entity == null ) {
            return null;
        }

        List<OrderHeaderDto> list = new ArrayList<OrderHeaderDto>( entity.size() );
        for ( OrderHeader orderHeader : entity ) {
            list.add( toHeaderDTO( orderHeader ) );
        }

        return list;
    }

    @Override
    public OrderHeader toEntity(OrderHeaderDto orderHeaderDto) {
        if ( orderHeaderDto == null ) {
            return null;
        }

        OrderHeader orderHeader = new OrderHeader();

        orderHeader.setPaymentMethod( orderHeaderDto.getPaymentMethod() );
        orderHeader.setOrderId( orderHeaderDto.getOrderId() );
        orderHeader.setCustomerId( orderHeaderDto.getCustomerId() );
        orderHeader.setOrderDate( orderHeaderDto.getOrderDate() );
        orderHeader.setStatus( orderHeaderDto.getStatus() );
        orderHeader.setTotalAmount( orderHeaderDto.getTotalAmount() );
        orderHeader.setCreatedAt( orderHeaderDto.getCreatedAt() );
        orderHeader.setUpdatedAt( orderHeaderDto.getUpdatedAt() );

        return orderHeader;
    }

    @Override
    public OrderLineDto toLineDTO(OrderLine orderLineEntity) {
        if ( orderLineEntity == null ) {
            return null;
        }

        OrderLineDto orderLineDto = new OrderLineDto();

        orderLineDto.setOrderItemId( orderLineEntity.getOrderItemId() );
        orderLineDto.setProductId( orderLineEntity.getProductId() );
        orderLineDto.setQuantity( orderLineEntity.getQuantity() );
        orderLineDto.setPrice( orderLineEntity.getPrice() );

        return orderLineDto;
    }

    @Override
    public List<OrderLineDto> toLineDTOList(List<OrderLine> entity) {
        if ( entity == null ) {
            return null;
        }

        List<OrderLineDto> list = new ArrayList<OrderLineDto>( entity.size() );
        for ( OrderLine orderLine : entity ) {
            list.add( toLineDTO( orderLine ) );
        }

        return list;
    }

    @Override
    public OrderLine toLineEntity(OrderLineDto orderLineDto) {
        if ( orderLineDto == null ) {
            return null;
        }

        OrderLine orderLine = new OrderLine();

        orderLine.setOrderItemId( orderLineDto.getOrderItemId() );
        orderLine.setProductId( orderLineDto.getProductId() );
        orderLine.setQuantity( orderLineDto.getQuantity() );
        orderLine.setPrice( orderLineDto.getPrice() );

        return orderLine;
    }

    @Override
    public List<OrderLine> toLineEntityList(List<OrderLineDto> entity) {
        if ( entity == null ) {
            return null;
        }

        List<OrderLine> list = new ArrayList<OrderLine>( entity.size() );
        for ( OrderLineDto orderLineDto : entity ) {
            list.add( toLineEntity( orderLineDto ) );
        }

        return list;
    }
}
