package api;

import model.Courier;
import model.CourierLogin;
import model.DeletePostBody;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.ResponseBodyCourierLogin;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierClient {
    @Step("Delete all created couriers")
    public static void deleteAllCouriers(List<Integer> idListToDelete) {
        for (Integer courierId : idListToDelete) {
            Response responseCourierDelete = deleteCourier(courierId);
            responseCourierDelete.then().assertThat().body("ok", equalTo(true));
        }

        idListToDelete.clear();
    }

    @Step("Send Post request CourierCreate to /api/v1/courier")
    public static Response sendPostRequestCourier(Courier courier) {
        Response createCourierResponse = given().header("Content-type", "application/json").and().body(courier).when().post("/api/v1/courier");
        return createCourierResponse;
    }

    @Step("Send Delete model.Courier request /api/v1/courier/:id")
    public static Response deleteCourier(Integer courierId) {
        DeletePostBody deletePostBody = new DeletePostBody(courierId.toString());
        Response deleteResponce = given().header("Content-type", "application/json").and().body(deletePostBody).when().delete("/api/v1/courier/" + courierId);
        return deleteResponce;
    }

    @Step("Get Courier Id from login.")
    public static Integer getCourierIdFromLogin(CourierLogin courierLogin) {
        ResponseBodyCourierLogin responseBodyCourierLogin = sendPostRequestCourierLogin(courierLogin).as(ResponseBodyCourierLogin.class);
        return responseBodyCourierLogin.getId();
    }

    @Step("Send  Post request model.CourierLogin /api/v1/courier/login")
    public static Response sendPostRequestCourierLogin(CourierLogin courierLoginPost) {
        Response loginResponse = given().header("Content-type", "application/json").and().body(courierLoginPost).when().post("/api/v1/courier/login");
        return loginResponse;
    }

    @Step("Courier create")
    public static Courier courierCreate(String login, String password, String firstname) {
        Courier courier = new Courier(login, password, firstname);
        Response responsePostLogin = sendPostRequestCourier(courier);
        responsePostLogin.then().assertThat().body("ok", equalTo(true));
        return courier;
    }
}
