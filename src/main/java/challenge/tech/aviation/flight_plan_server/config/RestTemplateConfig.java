package challenge.tech.aviation.flight_plan_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${external.flight.data.apikey.value}")
    private String apikeyValue;

    @Value("${external.flight.data.apikey.key}")
    private String apikeyKey;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.connectTimeout(Duration.ofMillis(5000))
                .readTimeout(Duration.ofMillis(5000))
                .defaultHeader(apikeyKey, apikeyValue)
                .build();
    }
}
