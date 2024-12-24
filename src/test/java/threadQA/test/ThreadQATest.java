package threadQA.test;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import threadQA.base.BaseTestApi;
import threadQA.models.fakeStoreApi.*;
import threadQA.specifications.Specifications;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;


public class ThreadQATest extends BaseTestApi {

    @Test
    @DisplayName("Получить всех пользователей")
    public void testGetAllUser() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        given().get("/users");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    @DisplayName("Получить одного пользователя 'позитивный тест'")
    public void testGetSingleUser(int userId) {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        UserRoot response = given()
                .when()
                .pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .extract()
                .as(UserRoot.class);

        Assertions.assertEquals(userId, response.getId());
        Assertions.assertTrue(response.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 20})
    @DisplayName("Получить одного пользователя 'негативный тест'")
    public void testGetSingleUserNegative(int userId) {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        UserRoot response = given()
                .pathParam("userId", userId)
                .when()
                .get("/users/{userId}")
                .then()
                .extract()
                .as(UserRoot.class);

        Assertions.assertNull(response);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4})
    @DisplayName("Получить ограниченное количество пользователей")
    public void testGetAllUserLimit(int limin) {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        List<UserRoot> users = given().queryParam("limit", limin)
                .when()
                .get("/users")
                .then()
                .extract()
                .jsonPath().getList("", UserRoot.class);

        Assertions.assertEquals(limin, users.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 20})
    @DisplayName("Получить ограниченное количество пользователей")
    public void testGetAllUserLimiNegative(int limin) {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        List<UserRoot> users = given().queryParam("limit", limin)
                .when()
                .get("/users")
                .then()
                .extract()
                .jsonPath().getList("", UserRoot.class);

        Assertions.assertNotEquals(limin, users.size());
    }

    @Test
    @DisplayName("Получить всех пользователей и сортировка по возрастанию")
    public void testGetAllUserAndSortDesc() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());
        String sortType = "desc";

        List<UserRoot> sortedResponse = given().queryParam("sort", sortType)
                .when()
                .get("/users")
                .then()
                .extract().jsonPath().getList("", UserRoot.class);

        List<UserRoot> notSortedResponse = given().get("/users")
                .then().log().body()
                .extract().jsonPath().getList("", UserRoot.class);

        List<Integer> sortedResponseId = sortedResponse.stream()
                .map(x -> x.getId()).collect(Collectors.toList());

        List<Integer> sortCodeId = notSortedResponse
                .stream()
                .map(x -> x.getId())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Assertions.assertNotEquals(sortedResponse, notSortedResponse);
        Assertions.assertEquals(sortCodeId, sortedResponseId);
    }

    private UserRoot getTestUser() {
        Name name = new Name("Маркиз", "Пушистик");
        Geolocation geolocation = new Geolocation("-33.3549", "84.14966");

        Address address = Address.builder()
                .city("Гренлат")
                .street("63 Кота")
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

    @Test
    @DisplayName("Создание нового пользователя")
    public void testCreateNewUser() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());
        UserRoot getUser = getTestUser();

        Integer userId = given().body(getUser)
                .when()
                .post("/users")
                .then()
                .extract().jsonPath().getInt("id");

        Assertions.assertNotNull(userId);
    }

    @Test
    @DisplayName("Обновление информации у пользователя")
    public void testUpdateUser() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());
        UserRoot getUser = getTestUser();
        String oldPassword = getUser.getPassword();
        getUser.setPassword("newPassword");

        UserRoot updateUser = given().body(getUser)
                .when()
//                .pathParam("userId", getUser.getId())
//                .put("/users/{userId}")
                .put("/users/5")
                .then()
                .extract().as(UserRoot.class);

        Assertions.assertNotEquals(oldPassword, updateUser.getPassword());
    }

    @Test
    @DisplayName("Удаление пользователя")
    public void testDeleteUser() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());

        given().delete("/users/11");
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void testAuthUser() {
        Specifications.installSpecifications(Specifications.requestSpecThreadQA(), Specifications.responseSpecOk200());
        AuthData authData = new AuthData("mor_2314", "83r5^_");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .when()
                .post("/auth/login")
                .then()
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }
}