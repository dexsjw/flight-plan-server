package challenge.tech.aviation.flight_plan_server.service.impl;

import challenge.tech.aviation.flight_plan_server.dto.FlightPlanDto;
import challenge.tech.aviation.flight_plan_server.service.FlightPlanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
public class FlightPlanServiceImpl implements FlightPlanService {

//    @Value("${external.flight.data.url.flight-manager}")
////    private String flightManagerUrl;

    @Value("${external.flight.data.path.flight-manager.display-all}")
    private String flightManagerPathDisplayAll;

//    private RestTemplate restTemplate;

    private WebClient flightManagerWebClient;

    public FlightPlanServiceImpl(RestTemplate restTemplate, WebClient flightManagerWebClient) {
        this.flightManagerWebClient = flightManagerWebClient;
//        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> testEndpoint() {
//        return restTemplate.getForEntity(flightManagerUrl + flightManagerPathDisplayAll, String.class);
        return flightManagerWebClient.get()
                .uri(flightManagerPathDisplayAll)
                .retrieve()
                .toEntity(String.class)
                .timeout(Duration.ofMillis(10000))
                .block();
    }

    @Override
    public List<FlightPlanDto> displayAllFlightPlans() {
        return flightManagerWebClient.get()
                .uri(flightManagerPathDisplayAll)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<FlightPlanDto>>() {})
                .timeout(Duration.ofMillis(10000))
                .block();
    }

}
