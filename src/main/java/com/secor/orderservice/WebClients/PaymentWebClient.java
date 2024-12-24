package com.secor.orderservice.WebClients;

import com.secor.orderservice.dto.InventoryResponseDto;
import com.secor.orderservice.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class PaymentWebClient
{

    @Autowired
    private WebClient webClient;
    public Mono<PaymentDto> makePayment(PaymentDto paymentRequest)
    {
        return  webClient.post().uri("http://localhost:8090/api/payments/v1/makePayment").bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(PaymentDto.class);

    }


    public Mono<PaymentDto> getPaymentDetails(Long orderId)
    {
        String paymentURL = UriComponentsBuilder.fromHttpUrl("http://localhost:8090/api/payments/v1/getPaymentByOrderId")
                .queryParam("orderId", orderId)
                .toUriString();
        return webClient.get()
                .uri(paymentURL)
                .retrieve()
                .bodyToMono(PaymentDto.class);

    }
}
