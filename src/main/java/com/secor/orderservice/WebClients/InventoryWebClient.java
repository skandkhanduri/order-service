package com.secor.orderservice.WebClients;

import com.secor.orderservice.dto.InventoryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class InventoryWebClient
{

    @Autowired
    private WebClient webClient;
    public Mono<InventoryResponseDto> getStockAvailability(Long productId,Integer quantity)
    {
        String inventoryURL = UriComponentsBuilder.fromHttpUrl("http://localhost:8092/api/Inventory/v1/checkInventoryQuantity")
                .queryParam("productId", productId)
                .queryParam("quantity",quantity)
                .toUriString();
        return webClient.get()
                .uri(inventoryURL)
                .retrieve()
                .bodyToMono(InventoryResponseDto.class);

    }
}
