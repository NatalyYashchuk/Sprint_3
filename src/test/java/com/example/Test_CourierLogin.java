import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class Test_CourierLogin {
    private ArrayList<String> courierData ;
    private List<Integer> idListToDelete = new ArrayList<>();
    private Courier courier;
    private CourierLogin courierLogin;
    private String login;
    private String password;
    int fieldsSet;
    int signsQuantity;

    @Before
    public void setUp() {RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        fieldsSet = 1;
        signsQuantity = 10;

        courierData = Utils.getCourierData(fieldsSet, signsQuantity);                       //get Data to create Set of Login, Password, FirstName

        courier = new Courier(courierData.get(0), courierData.get(1), courierData.get(2)); //create  a courier
        Response responsePostLogin = Test_CourierCreate.sendPostRequestCourier(courier);
        responsePostLogin.then().assertThat().body("ok", equalTo(true));

        courierLogin = new CourierLogin(courier.getLogin(),courier.getPassword());         //get courier id to the list to Delete
        Response responsePostLoginDel = sendPostRequestCourierLogin(courierLogin);
        responsePostLoginDel.then().assertThat().statusCode(200);
        Integer id = getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);

        }

    @Test
    @DisplayName("Login return id")
    public void courierLoginGetIdSuccessfully() {
        Assert.assertTrue(!idListToDelete.isEmpty());

    }

    @Test
    @DisplayName("Login is successfull with status code 200 /api/v1/courier/login")
    @Description ("Status code is 200 when  a courier login with all obligatory fields successfully.")
    public void courierLoginSuccessfully() {
        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //login
        responsePostLogin.then().assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Obligatory field login = null.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Недостаточно данных для входа, and StatusCode = 400 ")
    public void courierLoginImpossibleLoginNull() {

        courierLogin.setLogin(null);

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //create courier
        responsePostLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
        .and()
        .statusCode(400);

    }
    @Test
    @DisplayName("Obligatory field login = ' '.Login is impossible.  /api/v1/courier/login")           //Результат не соответствует API
    @Description ("Message: Недостаточно данных для входа, and StatusCode = 404 ")
    public void courierLoginImpossibleLoginEmptySpace() {

        courierLogin.setLogin(" ");

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //create courier
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

    }

    @Test
    @DisplayName("Obligatory field password = null.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")                       //Результат не соответствует API
    public void courierLoginImpossiblePasswordNull() {

        courierLogin.setPassword(null);

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //create courier
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

    }


    @Test
    @DisplayName("Obligatory field password = ' '.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void courierLoginImpossiblePasswordEmptySpace() {

        courierLogin.setPassword(" ");

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //create courier
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

    }

    @Test
    @DisplayName("Incorrect value Login.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void courierLoginImpossibleLoginIncorrect() {

        courierLogin.setLogin("Login" + Math.random());

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //create courier
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

    }

    @Test
    @DisplayName("Incorrect value Password.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void courierLoginImpossiblePasswordIncorrect() {

        courierLogin.setPassword("Password" + Math.random());

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //create courier
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

    }


    @After
    public void deleteAllCouriers() {

        for(Integer courierId: idListToDelete) {
            Response responseCourierDelete = Test_CourierCreate.deleteCourier(courierId);
            responseCourierDelete.then().assertThat().body("ok", equalTo(true));
        }

        idListToDelete.clear();
    }

    @Step("Get Courier Id.")
    public static Integer getCourierIdFromLogin(CourierLogin courierLogin) {
        ResponseBodyCourierLogin responseBodyCourierLogin = sendPostRequestCourierLogin(courierLogin).as(ResponseBodyCourierLogin.class);
        return responseBodyCourierLogin.getId();
    }

    @Step("Send  Post request CourierLogin /api/v1/courier/login")
    public static Response sendPostRequestCourierLogin(CourierLogin courierLoginPost)  {
        Response loginResponse = given().header("Content-type", "application/json").and().body(courierLoginPost).when().post("/api/v1/courier/login");
        return loginResponse;
    }


}
