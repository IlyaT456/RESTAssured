package threadQA.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import threadQA.models.ReqresIn.*;
import threadQA.specifications.Specifications;
import threadQA.steps.ReqresInSteps;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresInTest {

    ReqresInSteps reqInSteps = new ReqresInSteps();

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
        int expectedId = 4;
        String expectedToken = "QpwL5tke4Pnpja7X4";

        Register registerData = new Register("eve.holt@reqres.in", "pistol");

        SuccessReg successReg = reqInSteps.registerUser(registerData);

        Assertions.assertEquals(expectedId, successReg.getId());
        Assertions.assertEquals(expectedToken, successReg.getToken());
    }

    @DisplayName("Не успешная регистрация")
    @Test
    public void testUnSuccessfulRegistration() {
        String expectedError = "Missing password";

        Register registerData = new Register("sydney@fife", "");

        UnRegister unRegister = reqInSteps.unRegisterUser(registerData);

        Assertions.assertEquals(expectedError, unRegister.getError());
    }

    @DisplayName("Сортеровка цвета по годам")
    @Test
    public void testSortColorsByYearDesc() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());
        List<ColorsData> colorsData = reqInSteps.getColors();

        List<Integer> years = colorsData.stream().map(ColorsData::getYear).sorted().collect(Collectors.toList());
        List<Integer> sortedYear = years.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(sortedYear, years);
    }

    @DisplayName("Создание нового пользователя")
    @Test
    public void testCreateNewUser() {
        CreateNewUser user = new CreateNewUser("morpheus", "leader");

        CreateNewUserRes newUser = reqInSteps.createNewUser(user);

        Assertions.assertNotNull(newUser.getId());
        Assertions.assertEquals(user.getName(), newUser.getName());

//        String regex = "\\..*";
        String regex = "(T\\d{2}:\\d{2}).*";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");
        Assertions.assertEquals(newUser.getCreatedAt().replaceAll(regex, ""), currentTime);
    }

    @DisplayName("Удаление пользоватля")
    @Test
    public void testDeleteUser() {
        int userId = 2;

        reqInSteps.deleteUser(userId);
    }

    @DisplayName("Проверка времени")
    @Test
    public void testTime() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());
        int userId = 2;
        UserTime userTime = new UserTime("morpheus", "zion resident");

        UserTimeResponce resUserTime = reqInSteps.upDatedTime(userId, userTime);

//        String regex = "\\..*";
        String regex = "(T\\d{2}:\\d{2}).*";

        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");
        Assertions.assertEquals(currentTime, resUserTime.getUpdatedAt().replaceAll(regex, ""));
    }
}
