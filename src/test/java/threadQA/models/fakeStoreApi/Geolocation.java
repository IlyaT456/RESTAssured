package threadQA.models.fakeStoreApi;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Geolocation {

    @JsonProperty("lat")
    private String lat;

    @JsonProperty("long")
    private String glong;
}
