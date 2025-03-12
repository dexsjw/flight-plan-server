package challenge.tech.aviation.flight_plan_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightPlanRouteDataDto extends FlightPlanDto {

    private FiledRouteDto filedRoute;

    @Builder(builderMethodName = "FlightPlanRouteDataDtoBuilder")
    public FlightPlanRouteDataDto(String _id, String aircraftIdentification, FiledRouteDto filedRoute) {
        super(_id, aircraftIdentification);
        this.filedRoute = filedRoute;
    }

}
