package challenge.tech.aviation.flight_plan_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightPlanDto {

    private String _id;
    private String aircraftIdentification;

}
