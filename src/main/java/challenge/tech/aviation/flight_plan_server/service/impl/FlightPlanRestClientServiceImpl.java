package challenge.tech.aviation.flight_plan_server.service.impl;

import challenge.tech.aviation.flight_plan_server.constants.AeronauticalDataType;
import challenge.tech.aviation.flight_plan_server.dto.FiledRouteDto;
import challenge.tech.aviation.flight_plan_server.dto.FlightPlanDto;
import challenge.tech.aviation.flight_plan_server.dto.FlightPlanRouteDataDto;
import challenge.tech.aviation.flight_plan_server.dto.RouteElementDto;
import challenge.tech.aviation.flight_plan_server.exception.FlightPlanRouteDataNotFoundException;
import challenge.tech.aviation.flight_plan_server.exception.MissingRouteElementSeqNumException;
import challenge.tech.aviation.flight_plan_server.service.FlightPlanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Primary
@Service
public class FlightPlanRestClientServiceImpl implements FlightPlanService {

    @Value("${external.flight.data.path.flight-manager.display-all}")
    private String flightManagerDisplayAllPath;

    @Value("${external.flight.data.path.aeronautical-data.search}")
    private String aeronauticalDataSearchPath;

    private RestClient flightManagerRestClient;
    private RestClient aeronauticalDataRestClient;
    private ObjectMapper objectMapper;

    public FlightPlanRestClientServiceImpl(RestClient flightManagerRestClient, RestClient aeronauticalDataRestClient, ObjectMapper objectMapper) {
        this.flightManagerRestClient = flightManagerRestClient;
        this.aeronauticalDataRestClient = aeronauticalDataRestClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<FlightPlanDto> displayAllFlightPlans() {
        return flightManagerRestClient.get()
                .uri(flightManagerDisplayAllPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new HttpClientErrorException(response.getStatusCode(), "Client error: " + response.getStatusText());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Server error: " + response.getStatusText());
                })
                .body(new ParameterizedTypeReference<List<FlightPlanDto>>() {});
    }

    @Override
    public FlightPlanRouteDataDto searchFlightPlanRouteData(String id) {
        log.info("FlightPlanId: " + id);
        List<FlightPlanRouteDataDto> flightPlanRouteDataList = getAllFlightPlanRouteData();

        if (flightPlanRouteDataList == null) {
            return FlightPlanRouteDataDto.FlightPlanRouteDataDtoBuilder()
                    ._id(id)
                    .aircraftIdentification("")
                    .filedRoute(new FiledRouteDto(new ArrayList<>()))
                    .build();
        }

        return flightPlanRouteDataList.stream()
                .filter(flightPlanRouteData -> flightPlanRouteData.get_id().equals(id))
                .findAny()
                .map(this::populatePositionPointCoordinates)
                .orElseThrow(() -> new FlightPlanRouteDataNotFoundException(id));
    }

    private List<FlightPlanRouteDataDto> getAllFlightPlanRouteData() {
        return flightManagerRestClient.get()
                .uri(flightManagerDisplayAllPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new HttpClientErrorException(response.getStatusCode(), "Client error: " + response.getStatusText());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Server error: " + response.getStatusText());
                })
                .body(new ParameterizedTypeReference<List<FlightPlanRouteDataDto>>() {});
    }

    private FlightPlanRouteDataDto populatePositionPointCoordinates(FlightPlanRouteDataDto flightPlanRouteData) {
        // In case routeElement list is not sorted by "seqNo"
        List<RouteElementDto> sortedRouteElement = new ArrayList<>();

        if (flightPlanRouteData.getFiledRoute() != null && flightPlanRouteData.getFiledRoute().getRouteElement() != null) {
            List<RouteElementDto> routeElementList = flightPlanRouteData.getFiledRoute().getRouteElement();
            populateSortedRouteElementFromRouteElementList(sortedRouteElement, routeElementList, flightPlanRouteData.get_id());
        }

        if (flightPlanRouteData.getFiledRoute() != null) {
            flightPlanRouteData.getFiledRoute().setRouteElement(sortedRouteElement);
        }
        return flightPlanRouteData;
    }

    private void populateSortedRouteElementFromRouteElementList(List<RouteElementDto> sortedRouteElement,
                                                                List<RouteElementDto> routeElementList,
                                                                String flightPlanId) {
        for (RouteElementDto routeElement : routeElementList) {
            String designatedPoint = routeElement.getPosition() != null
                    && routeElement.getPosition().getDesignatedPoint() != null
                    ? routeElement.getPosition().getDesignatedPoint()
                    : " ";

            List<String> pointCoordinateList = searchAeronauticalDataTypeAndTerm(AeronauticalDataType.FIXES, designatedPoint);
            String pointCoordinate = pointCoordinateList != null && !pointCoordinateList.isEmpty()
                    ? pointCoordinateList.get(0)
                    : "";
            routeElement.setPointCoordinate(pointCoordinate);

            int seqNum = Optional.ofNullable(routeElement.getSeqNum())
                    .orElseThrow(() -> new MissingRouteElementSeqNumException(flightPlanId));

            sortedRouteElement.add(seqNum, routeElement);
        }
    }

    private List<String> searchAeronauticalDataTypeAndTerm(AeronauticalDataType aeronauticalDataType, String aeronauticalDataTerm) {
        String dataJsonStr = aeronauticalDataRestClient.get()
                .uri(uriBuilder -> uriBuilder.path(aeronauticalDataSearchPath + "/{type}/{term}")
                        .build(aeronauticalDataType.getType(), aeronauticalDataTerm))
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new HttpClientErrorException(response.getStatusCode(), "Client error: " + response.getStatusText());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Server error: " + response.getStatusText());
                })
                .body(String.class);

        List<String> data = new ArrayList<>();
        try {
            data = objectMapper.readValue(dataJsonStr, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
