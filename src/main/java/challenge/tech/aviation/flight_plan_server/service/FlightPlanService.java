package challenge.tech.aviation.flight_plan_server.service;

import challenge.tech.aviation.flight_plan_server.dto.FlightPlanDto;
import challenge.tech.aviation.flight_plan_server.dto.FlightPlanRouteDataDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FlightPlanService {

    ResponseEntity<String> testEndpoint();
    List<FlightPlanDto> displayAllFlightPlans();
    FlightPlanRouteDataDto searchFlightPlanRouteData(String id);

}
