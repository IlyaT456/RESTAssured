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
public class UserRoot {

    @JsonProperty("email")
    public String email;

    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;

    @JsonProperty("name")
    private Name name;

    @JsonProperty("address")
    private Address address;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("__v")
    public Integer v;
}
