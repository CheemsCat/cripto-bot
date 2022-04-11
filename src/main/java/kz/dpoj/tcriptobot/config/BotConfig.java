package kz.dpoj.tcriptobot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${telegrambot.username}")
    String botUserName;

    @Value("${telegrambot.token}")
    String token;

    @Value("${telegrambot.path}")
    String path;
}
