package challenge.tech.aviation.flight_plan_server.service.impl;

import challenge.tech.aviation.flight_plan_server.dto.FlightPlanDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
public class FlightPlanRestClientServiceImplTest {

    @Value("${external.flight.data.path.flight-manager.display-all}")
    private String flightManagerDisplayAllPath;

    @Mock
    private RestClient flightManagerRestClient;

    @Mock
    private RestClient aeronauticalDataRestClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    FlightPlanRestClientServiceImpl flightPlanRestClientService;

    @BeforeEach
    void setUp() {
        flightPlanRestClientService = new FlightPlanRestClientServiceImpl(flightManagerRestClient, aeronauticalDataRestClient, objectMapper);
    }

    @Test
    public void displayAllFlightPlansTest() throws Exception {
        List<FlightPlanDto> expectedAllFlightPlans = new ArrayList<>();
        expectedAllFlightPlans.add(FlightPlanDto.builder()
                ._id("1")
                .aircraftIdentification("SIA123")
                .build());

        when(flightManagerRestClient.get()
                .uri(flightManagerDisplayAllPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<FlightPlanDto>>() {}))
                .thenReturn(expectedAllFlightPlans);

        List<FlightPlanDto> allFlightPlans = flightPlanRestClientService.displayAllFlightPlans();

        assertEquals(expectedAllFlightPlans, allFlightPlans);
        assertEquals(expectedAllFlightPlans.get(0).get_id(), allFlightPlans.get(0).get_id());
    }

}
