package com.secor.orderservice.WebClients;

import com.secor.orderservice.dto.InventoryResponseDto;
import com.secor.orderservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class ProductWebClient
{

    @Autowired
    private WebClient webClient;
    public Mono<ProductDto> getProductDetails(Long productId)
    {
        String productURL = UriComponentsBuilder.fromHttpUrl("http://localhost:8089/api/products/v1/getProduct")
                .queryParam("productId", productId)
                .toUriString();
        return webClient.get()
                .uri(productURL)
                .retrieve()
                .bodyToMono(ProductDto.class);

    }
}
