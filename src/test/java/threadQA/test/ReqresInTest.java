package threadQA.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import threadQA.models.ReqresIn.*;
import threadQA.specifications.Specifications;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresInTest {

    @Test
    @DisplayName("Имена файлов 'avatar' и ID совпадают")
    public void testCheckAvatarAndId() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());
        int page = 2;

        List<UserData> users = given()
                .when()
                .queryParam("page", page)
                .get("/api/users")
                .then().log().body()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @DisplayName("Успешная регистрация")
    @Test
    public void testSuccessfulRegistration() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());
        Integer expectedId = 4;
        String expectedToken = "QpwL5tke4Pnpja7X4";

        Register registerData = new Register("eve.holt@reqres.in", "pistol");

        SuccessReg SuccessReg = given()
                .body(registerData)
                .when()
                .post("/api/register")
                .then().log().body()
                .extract().as(SuccessReg.class);

        Assertions.assertEquals(expectedId, SuccessReg.getId());
        Assertions.assertEquals(expectedToken, SuccessReg.getToken());
    }

    @DisplayName("Успешная регистрация")
    @Test
    public void testUnSuccessfulRegistration() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecError400());
        String expectedError = "Missing password";

        Register registerData = new Register("sydney@fife", "");

        UnRegister unRegister = given()
                .body(registerData)
                .when()
                .post("/api/register")
                .then().log().body()
                .extract().as(UnRegister.class);

        Assertions.assertEquals(expectedError, unRegister.getError());
    }

    @DisplayName("Сортеровка цвета по годам")
    @Test
    public void testSortColorsByYearDesc() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());
        List<ColorsData> colorsData = given()
                .when()
                .get("/api/unknown")
                .then().log().body()
                .extract().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = colorsData.stream().map(ColorsData::getYear).sorted().collect(Collectors.toList());
        List<Integer> sortedYear = years.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(sortedYear, years);
    }

    @DisplayName("Удаление пользоватля")
    @Test
    public void testDeleteUser() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecUniqueStatus(204));
        Integer userId = 2;
        given().pathParam("id", userId)
                .when()
                .delete("/api/users/{id}")
                .then().log().body();
    }

    @Test
    public void testTime() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());
        Integer userId = 2;
        UserTime userTime = new UserTime("morpheus", "zion resident");

        UserTimeResponce responce = given()
                .pathParam("id", userId)
                .body(userTime)
                .when()
                .put("/api/users/{id}")
                .then().log().body()
                .extract().as(UserTimeResponce.class);

        String regex = "\\..*";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");
        Assertions.assertEquals(currentTime, responce.getUpdatedAt().replaceAll(regex, ""));
    }
}
