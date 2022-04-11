package kz.dpoj.tcriptobot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BirjaConfig {
    @Bean
    public WebClient localApiClient() {
        return WebClient.create("https://api.exmo.com/v1.1");
    }
}
