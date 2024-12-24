package com.secor.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClients
{

    @Bean
    public WebClient webClient_1(WebClient.Builder webClientBuilder)
    {
        return webClientBuilder
                .filter(new LoggingWebClientFilter())
                .build();
    }
}
