package threadQA.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import threadQA.base.BaseTestApi;
import threadQA.models.fakeStoreApi.*;
import threadQA.specifications.Specifications;
import threadQA.steps.ThreadQAUserSteps;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;


public class ThreadQATest extends BaseTestApi {

    ThreadQAUserSteps tQAuserSteps = new ThreadQAUserSteps();

    @Test
    @DisplayName("Получить всех пользователей")
    public void testGetAllUser() {
        tQAuserSteps.getUsers();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    @DisplayName("Получить одного пользователя 'позитивный тест'")
    public void testGetSingleUser(int userId) {
        UserRoot user = tQAuserSteps.getUser(userId);

        Assertions.assertEquals(userId, user.getId());
        Assertions.assertTrue(user.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 20})
    @DisplayName("Получить одного пользователя 'негативный тест'")
    public void testGetSingleUserNegative(int userId) {
        UserRoot response = tQAuserSteps.getUser(userId);

        Assertions.assertNull(response);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4})
    @DisplayName("Получить ограниченное количество пользователей")
    public void testGetAllUserLimit(int limit) {
        List<UserRoot> users = tQAuserSteps.getUsersLimit(limit);

        Assertions.assertEquals(limit, users.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 20})
    @DisplayName("Получить ограниченное количество пользователей")
    public void testGetAllUserLimiNegative(int limit) {
        List<UserRoot> users = tQAuserSteps.getUsersLimit(limit);

        Assertions.assertNotEquals(limit, users.size());
    }

    @Test
    @DisplayName("Получить всех пользователей и сортировка по возрастанию")
    public void testGetAllUserAndSortDesc() {
        List<UserRoot> sortedResponse = tQAuserSteps.getUsersAndSort();

        List<UserRoot> notSortedResponse = tQAuserSteps.getUsers();

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

    @Test
    @DisplayName("Создание нового пользователя")
    public void testCreateNewUser() {
        int userId = tQAuserSteps.createNewUser();

        Assertions.assertNotNull(userId);
    }

    @Test
    @DisplayName("Обновление информации у пользователя")
    public void testUpdateUser() {
        int userId = 5;
        UserRoot getUser = tQAuserSteps.generateNewUser();
        String oldPassword = getUser.getPassword();

        getUser.setPassword("newPassword");

        tQAuserSteps.updateUser(getUser, userId);
        String newPassword = tQAuserSteps.updateUser(getUser, userId).getPassword();

        Assertions.assertNotEquals(oldPassword, newPassword);
    }

    @Test
    @DisplayName("Удаление пользователя")
    public void testDeleteUser() {
        tQAuserSteps.deleteUser(2);
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void testAuthUser() {
        AuthData authData = new AuthData("mor_2314", "83r5^_");

        tQAuserSteps.authUser(authData);
        String token = tQAuserSteps.authUser(authData);

        Assertions.assertNotNull(token);
    }
}