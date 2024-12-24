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
public class Address {

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("number")
    private int number;

    @JsonProperty("zipcode")
    private String zipcode;

    @JsonProperty("geolocation")
    private Geolocation geolocation;
}
