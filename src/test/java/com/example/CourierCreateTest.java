package com.example;

import api.CourierClient;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreateTest {
    private ArrayList<String> courierData;
    private List<Integer> idListToDelete = new ArrayList<>();

    private Courier courier;
    private CourierLogin courierLogin;
    int fieldsSet;
    int signsQuantity;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        fieldsSet = 1;
        signsQuantity = 10;

        courierData = Utils.getCourierData(fieldsSet, signsQuantity);
        courier = new Courier(courierData.get(0), courierData.get(1), courierData.get(2));
        courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
    }

    @Test
    @DisplayName("Create a courier successfully /api/v1/courier")
    @Description("It's possible to create  a new courier.")
    public void testCourierNewCreateSuccessfully() {
        Response responsePost = CourierClient.sendPostRequestCourier(courier);

        responsePost.then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));

        Integer id = CourierClient.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);
    }

    @Test
    @DisplayName("Return correct response code  /api/v1/courier")
    @Description("Create model.Courier successfully. Return correct response code")
    public void testCourierCreateSuccessfullyResponceCodeCorrect() {
        Response responsePost = CourierClient.sendPostRequestCourier(courier);
        System.out.println(responsePost.body().asString());
        responsePost.then().assertThat()
                .statusCode(201);

        Integer id = CourierClient.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);
    }


    @Test
    @DisplayName("Two the same couriers can't be created  /api/v1/courier")
    @Description("It's impossible to create the second courier with the same obligatory field values")
    public void testCourierTheSameCreateImpossible() {
        Response responsePostFirst = CourierClient.sendPostRequestCourier(courier);
        responsePostFirst.then().assertThat().body("ok", equalTo(true));

        Integer id = CourierClient.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);

        Response responsePostSame = CourierClient.sendPostRequestCourier(courier);
        responsePostSame.then()
                .assertThat()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }


    @Test
    @DisplayName("Can't create a courier without an obligatory field /api/v1/courier")
    @Description("It's impossible to create a courier if any obligatory field equals to null. Status code 400")
    public void testCourierWithObligatoryFieldsOnlyCreateSuccessfully() {
        for (int i = 0; i < courierData.size(); i++) {
            courierData = Utils.getCourierData(fieldsSet, signsQuantity);
            String nullValue = courierData.set(i, null);

            courier = new Courier(courierData.get(0), courierData.get(1), courierData.get(2));
            Response responsePost = CourierClient.sendPostRequestCourier(courier);

            System.out.println(responsePost.body().asString());
            responsePost.then()
                    .assertThat()
                    .statusCode(400)
                    .and()
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        }
    }

    @Test
    @DisplayName("Can't create a courier with login which is used   /api/v1/courier")
    @Description("It's impossible to create a courier with login  which is used already")
    public void testCourierWithUsedLoginCreateImpossible() {
        Response responsePostFirst = CourierClient.sendPostRequestCourier(courier);
        responsePostFirst.then().assertThat().body("ok", equalTo(true));

        Integer id = CourierClient.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);

        ArrayList<String> courierData2 = Utils.getCourierData(fieldsSet, signsQuantity);
        courier = new Courier(courierData.get(0), courierData2.get(1), courierData2.get(2));
        Response responsePostSame2 = CourierClient.sendPostRequestCourier(courier);
        responsePostSame2.then()
                .assertThat()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void clearStand() {
        CourierClient.deleteAllCouriers(idListToDelete);
    }


}
