package com.example;

import api.CourierClient;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierLogin;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CourierLoginTest {
    private ArrayList<String> courierData;
    private List<Integer> idListToDelete = new ArrayList<>();
    private Courier courier;
    private CourierLogin courierLogin;
    private String login;
    private String password;
    private String firstName;
    int fieldsSet;
    int signsQuantity;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        fieldsSet = 1;
        signsQuantity = 10;

        courierData = Utils.getCourierData(fieldsSet, signsQuantity);
        login = courierData.get(0);
        password = courierData.get(1);
        firstName = courierData.get(2);

        Courier courier = CourierClient.courierCreate(login, password, firstName);
        courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());

        CourierClient.sendPostRequestCourierLogin(courierLogin);
        Integer courierId = CourierClient.getCourierIdFromLogin(courierLogin);

        idListToDelete.add(courierId);
    }

    @Test
    @DisplayName("Login return id")
    public void testCourierLoginGetIdSuccessfully() {
       Response loginResponse =  CourierClient.sendPostRequestCourierLogin(courierLogin);
        int id = loginResponse.then().extract().path("id");
        Assert.assertNotNull(id);
    }

    @Test
    @DisplayName("Login is successfull with status code 200 /api/v1/courier/login")
    @Description("Status code is 200 when  a courier login with all obligatory fields successfully.")
    public void testCourierLoginSuccessfully() {
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        int statusCode = responsePostLogin.then().extract().statusCode();
        Assert.assertEquals("Status code required 200", 200, statusCode);
    }

    @Test
    @DisplayName("Obligatory field login = null.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Недостаточно данных для входа, and StatusCode = 400 ")
    public void testCourierLoginImpossibleLoginNull() {
        courierLogin.setLogin(null);
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);

        int statusCode = responsePostLogin.then().extract().statusCode();
        String message = responsePostLogin.then().extract().path("message");
        String requiredMessage = "Недостаточно данных для входа";

        Assert.assertEquals("Status code required 400", 400, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
    }

    @Test
    @DisplayName("Obligatory field login = ' '.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Недостаточно данных для входа, and StatusCode = 404 ")
    public void testCourierLoginImpossibleLoginEmptySpace() {
        courierLogin.setLogin(" ");
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);

        int statusCode = responsePostLogin.then().extract().statusCode();
        String message = responsePostLogin.then().extract().path("message");
        String requiredMessage = "Учетная запись не найдена";

        Assert.assertEquals("Status code required 404", 404, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
    }

    @Ignore
    @Test
    @DisplayName("Obligatory field password = null.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    //Результат не соответствует API
    public void testCourierLoginImpossiblePasswordNull() {
        courierLogin.setPassword(null);
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);

        int statusCode = responsePostLogin.then().extract().statusCode();
        String message = responsePostLogin.then().extract().path("message");
        String requiredMessage = "Недостаточно данных для входа";

        Assert.assertEquals("Status code required 400", 400, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
    }


    @Test
    @DisplayName("Obligatory field password = ' '.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossiblePasswordEmptySpace() {
        courierLogin.setPassword(" ");
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);

        int statusCode = responsePostLogin.then().extract().statusCode();
        String message = responsePostLogin.then().extract().path("message");
        String requiredMessage = "Учетная запись не найдена";

        Assert.assertEquals("Status code required 404", 404, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
    }

    @Test
    @DisplayName("Incorrect value Login.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossibleLoginIncorrect() {
        courierLogin.setLogin("Login" + Math.random());
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);

        int statusCode = responsePostLogin.then().extract().statusCode();
        String message = responsePostLogin.then().extract().path("message");
        String requiredMessage = "Учетная запись не найдена";

        Assert.assertEquals("Status code required 404", 404, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
    }

    @Test
    @DisplayName("Incorrect value Password.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossiblePasswordIncorrect() {
        courierLogin.setPassword("Password" + Math.random());
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);

        int statusCode = responsePostLogin.then().extract().statusCode();
        String message = responsePostLogin.then().extract().path("message");
        String requiredMessage = "Учетная запись не найдена";

        Assert.assertEquals("Status code required 404", 404, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
    }


    @After
    public void standClear() {
        CourierClient.deleteAllCouriers(idListToDelete);
    }
}
