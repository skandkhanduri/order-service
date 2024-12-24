package com.secor.orderservice.mapper;

import com.secor.orderservice.dto.*;
import com.secor.orderservice.entity.OrderHeader;
import com.secor.orderservice.entity.OrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    OrderHeaderDto toHeaderDTO(OrderHeader headerEntity);
    OrderDetailsResponseDto toHeaderResponseDTO(OrderHeader headerEntity);
    OrderRequestDto toHeaderReqDTO(OrderHeader headerEntity);

    List<OrderHeaderDto> toHeaderDTOList(List<OrderHeader> entity);
    OrderHeader toEntity(OrderHeaderDto orderHeaderDto);

    OrderLineDto toLineDTO(OrderLine orderLineEntity);
    List<OrderLineDto> toLineDTOList(List<OrderLine> entity);

    OrderLine toLineEntity(OrderLineDto orderLineDto);
    List<OrderLine> toLineEntityList(List<OrderLineDto> entity);
}
