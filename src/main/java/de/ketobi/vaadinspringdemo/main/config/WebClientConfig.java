package de.ketobi.vaadinspringdemo.main.config;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("chatWebClient")
    public WebClient chatWebClient(@Value("${chat.base-url}") String BaseUrl) {
        return WebClient.builder().baseUrl(BaseUrl).build();
    }

    @Bean
    @Qualifier("indexingWebClient")
    public WebClient indexingWebClient(@Value("${indexing.base-url}") String BaseUrl) {
        return WebClient.builder().baseUrl(BaseUrl).build();
    }

    @Bean
    @Qualifier("dataWebClient")
    public WebClient dataWebClient(@Value("${data.base-url}") String BaseUrl) {
        return WebClient.builder().baseUrl(BaseUrl).build();
    }

}
