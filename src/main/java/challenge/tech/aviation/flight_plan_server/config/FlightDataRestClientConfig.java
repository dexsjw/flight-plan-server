package challenge.tech.aviation.flight_plan_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class FlightDataRestClientConfig {

    @Value("${external.flight.data.apikey.value}")
    private String apikeyValue;

    @Value("${external.flight.data.apikey.key}")
    private String apikeyKey;

    @Value("${external.flight.data.url.flight-manager}")
    private String flightManagerUrl;

    @Value("${external.flight.data.url.aeronautical-data}")
    private String aeronauticalDataUrl;

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(5000));
        requestFactory.setReadTimeout(Duration.ofMillis(5000));
        return requestFactory;
    }

    @Bean
    public RestClient flightManagerRestClient(ClientHttpRequestFactory clientHttpRequestFactory) {
        return RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .baseUrl(flightManagerUrl)
                .defaultHeader(apikeyKey, apikeyValue)
                .build();
    }

    @Bean
    public RestClient aeronauticalDataRestClient(ClientHttpRequestFactory clientHttpRequestFactory) {
        return RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .baseUrl(aeronauticalDataUrl)
                .defaultHeader(apikeyKey, apikeyValue)
                .build();
    }

}
