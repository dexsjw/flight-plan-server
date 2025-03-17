package challenge.tech.aviation.flight_plan_server.service.impl;

import challenge.tech.aviation.flight_plan_server.constants.AeronauticalDataType;
import challenge.tech.aviation.flight_plan_server.dto.FlightPlanDto;
import challenge.tech.aviation.flight_plan_server.dto.FlightPlanRouteDataDto;
import challenge.tech.aviation.flight_plan_server.dto.RouteElementDto;
import challenge.tech.aviation.flight_plan_server.service.FlightPlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FlightPlanServiceImpl implements FlightPlanService {

    @Value("${external.flight.data.path.flight-manager.display-all}")
    private String flightManagerDisplayAllPath;

    private WebClient flightManagerWebClient;
    private WebClient aeronauticalDataWebClient;
    private ObjectMapper objectMapper;

    public FlightPlanServiceImpl(WebClient flightManagerWebClient, WebClient aeronauticalDataWebClient, ObjectMapper objectMapper) {
        this.flightManagerWebClient = flightManagerWebClient;
        this.aeronauticalDataWebClient = aeronauticalDataWebClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<FlightPlanDto> displayAllFlightPlans() {
        return flightManagerWebClient.get()
                .uri(flightManagerDisplayAllPath)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<FlightPlanDto>>() {})
                .timeout(Duration.ofMillis(10000))
                .block();
                // TODO: error handling (NotFoundException)
    }

    @Override
    public FlightPlanRouteDataDto searchFlightPlanRouteData(String id) {
        log.info("FlightPlanId: " + id);
        List<FlightPlanRouteDataDto> flightPlanRouteDataList = flightManagerWebClient.get()
                .uri(flightManagerDisplayAllPath)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<FlightPlanRouteDataDto>>() {})
                .timeout(Duration.ofMillis(10000))
                .block();
                // TODO: error handling (NotFoundException)
        if (flightPlanRouteDataList == null) {
            return new FlightPlanRouteDataDto();
        }
        return flightPlanRouteDataList.stream()
                .filter(flightPlanRouteData -> flightPlanRouteData.get_id().equals(id))
                .findAny()
                .map(this::populatePositionPointCoordinates)
                .orElseGet(FlightPlanRouteDataDto::new);
                // TODO: error handling (NoFlightPlanRouteDataException)
    }

    private FlightPlanRouteDataDto populatePositionPointCoordinates(FlightPlanRouteDataDto flightPlanRouteData) {
        // In case routeElement list is not sorted by "seqNo"
        List<RouteElementDto> sortedRouteElement = new ArrayList<>();

        if (flightPlanRouteData.getFiledRoute() != null && flightPlanRouteData.getFiledRoute().getRouteElement() != null) {
            List<RouteElementDto> routeElementList = flightPlanRouteData.getFiledRoute().getRouteElement();

            for (RouteElementDto routeElement : routeElementList) {
                String designatedPoint = routeElement.getPosition() != null && routeElement.getPosition().getDesignatedPoint() != null
                        ? routeElement.getPosition().getDesignatedPoint()
                        : " ";

                String pointCoordinate = "";
                if (isAeronauticalDataTypeAndTermExist(AeronauticalDataType.FIXES, designatedPoint)) {
                    List<String> pointCoordinateList = searchAeronauticalDataTypeAndTerm(AeronauticalDataType.FIXES, designatedPoint);

                    pointCoordinate = pointCoordinateList != null
                            ? pointCoordinateList.get(0)
                            : "";
                }

                routeElement.setPointCoordinate(pointCoordinate);

                int seqNum = Optional.ofNullable(routeElement.getSeqNum())
                        .orElse(0);
                        // TODO: error handling (NoRouteElementSeqNumException)

                sortedRouteElement.add(seqNum, routeElement);
            }
        }

        if (flightPlanRouteData.getFiledRoute() != null) {
            flightPlanRouteData.getFiledRoute().setRouteElement(sortedRouteElement);
        }
        return flightPlanRouteData;
    }

    private boolean isAeronauticalDataTypeAndTermExist(AeronauticalDataType aeronauticalDataType, String aeronauticalDataTerm) {
        return Boolean.parseBoolean(aeronauticalDataWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/exist/{type}/{term}")
                        .build(aeronauticalDataType.getType(), aeronauticalDataTerm))
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(10000))
                .block());
                // TODO: error handling
    }

    private List<String> searchAeronauticalDataTypeAndTerm(AeronauticalDataType aeronauticalDataType, String aeronauticalDataTerm) {
        String dataJsonStr = aeronauticalDataWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search/{type}/{term}")
                        .build(aeronauticalDataType.getType(), aeronauticalDataTerm))
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(10000))
                .block();
                // TODO: error handling (NotFoundException)

        List<String> data = new ArrayList<>();
        try {
            data = objectMapper.readValue(dataJsonStr, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
