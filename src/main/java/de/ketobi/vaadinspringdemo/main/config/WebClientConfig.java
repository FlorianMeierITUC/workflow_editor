package de.ketobi.vaadinspringdemo.main.config;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${chat.base-url}") String chatBaseUrl) {
        return WebClient.builder().baseUrl(chatBaseUrl).build();

    }

}
