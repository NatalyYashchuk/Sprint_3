package com.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
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
    private String firstName;
    int fieldsSet;
    int signsQuantity;

    @Before
    public void setUp() {RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        fieldsSet = 1;
        signsQuantity = 10;

        courierData = Utils.getCourierData(fieldsSet, signsQuantity);//get Data to create Set of Login, Password, FirstName
        login = courierData.get(0);
        password = courierData.get(1);
        firstName = courierData.get(2);

        Courier courier = courierCreate(login, password, firstName);
        courierLogin = new CourierLogin(courier.getLogin(),courier.getPassword());// login and get a courier id to the list to Delete

        sendPostRequestCourierLogin(courierLogin);
        Integer courierId = getCourierIdFromLogin(courierLogin);

        idListToDelete.add(courierId);
        }

    @Test
    @DisplayName("Login return id")
    public void testCourierLoginGetIdSuccessfully() {
        Assert.assertTrue(!idListToDelete.isEmpty());

    }

    @Test
    @DisplayName("Login is successfull with status code 200 /api/v1/courier/login")
    @Description ("Status code is 200 when  a courier login with all obligatory fields successfully.")
    public void testCourierLoginSuccessfully() {
        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //login
        responsePostLogin.then().assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Obligatory field login = null.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Недостаточно данных для входа, and StatusCode = 400 ")
    public void testCourierLoginImpossibleLoginNull() {
        courierLogin.setLogin(null);

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //create courier
        responsePostLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
        .and()
        .statusCode(400);
    }

    @Test
    @DisplayName("Obligatory field login = ' '.Login is impossible.  /api/v1/courier/login")           //Результат не соответствует API
    @Description ("Message: Недостаточно данных для входа, and StatusCode = 404 ")
    public void testCourierLoginImpossibleLoginEmptySpace() {
        courierLogin.setLogin(" ");

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //courier login
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Obligatory field password = null.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")                       //Результат не соответствует API
    public void testCourierLoginImpossiblePasswordNull() {
        courierLogin.setPassword(null);

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //courier login
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }


    @Test
    @DisplayName("Obligatory field password = ' '.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossiblePasswordEmptySpace() {
        courierLogin.setPassword(" ");

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //courier login
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

    }

    @Test
    @DisplayName("Incorrect value Login.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossibleLoginIncorrect() {
        courierLogin.setLogin("Login" + Math.random());

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //courier login
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

    }

    @Test
    @DisplayName("Incorrect value Password.Login is impossible.  /api/v1/courier/login")
    @Description ("Message: Учетная запись не найдена, and StatusCode = 404 ")
    public void testCourierLoginImpossiblePasswordIncorrect() {
        courierLogin.setPassword("Password" + Math.random());

        Response responsePostLogin = sendPostRequestCourierLogin(courierLogin); //courier login
        System.out.println(responsePostLogin.body().asString());
        responsePostLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }


    @After
    public void standClear() {
    Test_CourierCreate.deleteAllCouriers(idListToDelete);
    }

    @Step("Get Courier Id from login.")
    public static Integer getCourierIdFromLogin(CourierLogin courierLogin) {
        ResponseBodyCourierLogin responseBodyCourierLogin = sendPostRequestCourierLogin(courierLogin).as(ResponseBodyCourierLogin.class);
        return responseBodyCourierLogin.getId();
    }

    @Step("Send  Post request com.example.CourierLogin /api/v1/courier/login")
    public static Response sendPostRequestCourierLogin(CourierLogin courierLoginPost)  {
        Response loginResponse = given().header("Content-type", "application/json").and().body(courierLoginPost).when().post("/api/v1/courier/login");
        return loginResponse;
    }


    @Step("Courier create")
    public static Courier courierCreate (String login, String password,String firstname) {
        Courier courier = new Courier(login, password, firstname); //create  a courier
        Response responsePostLogin = Test_CourierCreate.sendPostRequestCourier(courier);
        responsePostLogin.then().assertThat().body("ok", equalTo(true));
        return courier;
    }

}
