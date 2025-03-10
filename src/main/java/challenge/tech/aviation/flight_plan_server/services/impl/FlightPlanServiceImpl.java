package challenge.tech.aviation.flight_plan_server.services.impl;

import challenge.tech.aviation.flight_plan_server.services.FlightPlanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightPlanServiceImpl implements FlightPlanService {

    @Value("${external.flight.data.url.flight-manager}")
    private String flightManagerUrl;

    @Value("${external.flight.data.path.flight-manager.display-all}")
    private String flightManagerPathDisplayAll;

    private final RestTemplate restTemplate;

    public FlightPlanServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> testEndpoint() {
        return restTemplate.getForEntity(flightManagerUrl + flightManagerPathDisplayAll, String.class);
    }

}
