package com.example;

import api.CourierClient;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierLogin;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


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
        Assert.assertTrue(!idListToDelete.isEmpty());

    }

    @Test
    @DisplayName("Login is successfull with status code 200 /api/v1/courier/login")
    @Description("Status code is 200 when  a courier login with all obligatory fields successfully.")
    public void testCourierLoginSuccessfully() {
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        responsePostLogin.then().assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Obligatory field login = null.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Недостаточно данных для входа, and StatusCode = 400 ")
    public void testCourierLoginImpossibleLoginNull() {
        courierLogin.setLogin(null);
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        responsePostLogin.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Obligatory field login = ' '.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Недостаточно данных для входа, and StatusCode = 404 ")
    public void testCourierLoginImpossibleLoginEmptySpace() {
        courierLogin.setLogin(" ");
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        responsePostLogin.then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Obligatory field password = null.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    //Результат не соответствует API
    public void testCourierLoginImpossiblePasswordNull() {
        courierLogin.setPassword(null);
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        responsePostLogin.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Obligatory field password = ' '.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossiblePasswordEmptySpace() {
        courierLogin.setPassword(" ");
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        responsePostLogin.then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Incorrect value Login.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossibleLoginIncorrect() {
        courierLogin.setLogin("Login" + Math.random());
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        responsePostLogin.then().assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Incorrect value Password.Login is impossible.  /api/v1/courier/login")
    @Description("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossiblePasswordIncorrect() {
        courierLogin.setPassword("Password" + Math.random());
        Response responsePostLogin = CourierClient.sendPostRequestCourierLogin(courierLogin);
        responsePostLogin.then().assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }


    @After
    public void standClear() {
        CourierClient.deleteAllCouriers(idListToDelete);
    }
}
