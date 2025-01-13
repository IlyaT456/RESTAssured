package threadQA.steps;


import io.restassured.http.ContentType;
import threadQA.models.fakeStoreApi.*;
import threadQA.specifications.Specifications;

import java.util.List;

import static io.restassured.RestAssured.given;
import static threadQA.endpoints.ThreadQAEndpoints.*;

public class ThreadQAUserSteps {

    public UserRoot generateNewUser() {
        Name name = new Name("Маркиз", "Пушистик");
        Geolocation geolocation = new Geolocation("-33.3549", "84.14966");

        Address address = Address.builder()
                .city("Москва")
                .street("34 кота")
                .number(5)
                .zipcode("12926-3874")
                .geolocation(geolocation).build();

        return UserRoot.builder()
                .name(name)
                .phone("7777")
                .email("MPcat.cu")
                .username("MPH")
                .password("123pass")
                .address(address).build();
    }

    public UserRoot getUser(int userId) {

        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        return given()
                .when()
                .pathParam("userId", userId)
                .get(GET_USERS_BY_ID.endpoint)
                .then()
                .extract()
                .as(UserRoot.class);
    }

    public List<UserRoot> getUsersLimit(int limit) {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        return given().queryParam("limit", limit)
                .when()
                .get(USERS.endpoint)
                .then()
                .extract()
                .jsonPath().getList("", UserRoot.class);
    }

    public List<UserRoot> getUsersAndSort() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());
        String sortType = "desc";

        return given().queryParam("sort", sortType)
                .when()
                .get(USERS.endpoint)
                .then()
                .extract().jsonPath().getList("", UserRoot.class);
    }

    public List<UserRoot> getUsers() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        return given().get(USERS.endpoint)
                .then().log().body()
                .extract().jsonPath().getList("", UserRoot.class);
    }

    public int createNewUser() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());
        UserRoot getUser = generateNewUser();

        return given().body(getUser)
                .when()
                .post(USERS.endpoint)
                .then()
                .extract().jsonPath().getInt("id");
    }

    public UserRoot updateUser(UserRoot getUser, int userId) {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        return given().pathParam("id", userId)
                .body(getUser)
                .when()
                .put(GET_USERS_BY_ID.endpoint)
                .then()
                .extract().as(UserRoot.class);
    }

    public void deleteUser(int userId) {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        given().pathParam("id", userId)
                .when()
                .delete(GET_USERS_BY_ID.endpoint);
    }

    public String authUser(AuthData authData){
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        return given().contentType(ContentType.JSON)
                .body(authData)
                .when()
                .post(AUTH_LOGIN.endpoint)
                .then()
                .extract().jsonPath().getString("token");
    }
}
