package challenge.tech.aviation.flight_plan_server.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class FlightDataWebClientConfig {

    @Value("${external.flight.data.apikey.value}")
    private String apikeyValue;

    @Value("${external.flight.data.apikey.key}")
    private String apikeyKey;

    @Value("${external.flight.data.url.flight-manager}")
    private String flightManagerUrl;

    @Value("${external.flight.data.url.aeronautical-data}")
    private String aeronauticalDataUrl;

    private HttpClient createHttpClient() {
        return HttpClient.create()
                .responseTimeout(Duration.ofMillis(5000))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
    }

    @Bean
    public WebClient flightManagerWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(flightManagerUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .defaultHeader(apikeyKey, apikeyValue)
                .build();
    }

    @Bean
    public WebClient aeronauticalDataWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(aeronauticalDataUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .defaultHeader(apikeyKey, apikeyValue)
                .build();
    }
}
