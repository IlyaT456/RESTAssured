package threadQA.steps;

import threadQA.models.ReqresIn.*;
import threadQA.specifications.Specifications;

import java.util.List;

import static io.restassured.RestAssured.given;
import static threadQA.endpoints.ReqresInEndpoints.*;

public class ReqresInSteps {

    public CreateNewUserRes createNewUser(CreateNewUser user) {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecNewRes201());

        return given()
                .body(user)
                .post(USERS.endpoint)
                .then()
                .extract().as(CreateNewUserRes.class);
    }

    public void deleteUser(int userId) {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecUniqueStatus(204));

        given().pathParam("id", userId)
                .when()
                .delete(GET_USERS_BY_ID.endpoint);
    }

    public SuccessReg registerUser(Register registerData) {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());

        return given()
                .body(registerData)
                .when()
                .post(REGISTER_USER.endpoint)
                .then().log().body()
                .extract().as(SuccessReg.class);
    }

    public UnRegister unRegisterUser(Register registerData) {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecError400());

        return given()
                .body(registerData)
                .when()
                .post(REGISTER_USER.endpoint)
                .then().log().body()
                .extract().as(UnRegister.class);
    }

    public UserTimeResponce upDatedTime(int userId, UserTime userTime) {
        return given()
                .pathParam("id", userId)
                .body(userTime)
                .when()
                .put(GET_USERS_BY_ID.endpoint)
                .then().log().body()
                .extract().as(UserTimeResponce.class);
    }

    public List<ColorsData> getColors() {
        Specifications.installSpecifications(Specifications.requestSpecReqresIn(), Specifications.responseSpecOk200());

        return given()
                .when()
                .get(GET_COLORS.endpoint)
                .then().log().body()
                .extract().jsonPath().getList("data", ColorsData.class);
    }
}
