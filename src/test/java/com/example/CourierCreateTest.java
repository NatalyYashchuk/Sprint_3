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
        Boolean bodyOk = responsePost.then().extract().path("ok");

        Assert.assertEquals("Courier hasn't created successfully", true, bodyOk);
    }

    @Test
    @DisplayName("Return correct response code  /api/v1/courier")
    @Description("Create model.Courier successfully. Return correct response code")
    public void testCourierCreateSuccessfullyResponceCodeCorrect() {
        Response responsePost = CourierClient.sendPostRequestCourier(courier);
        int statusCode = responsePost.then().extract().statusCode();

        Assert.assertEquals("Status code required 201", 201, statusCode);
    }


    @Test
    @DisplayName("Two the same couriers can't be created  /api/v1/courier")
    @Description("It's impossible to create the second courier with the same obligatory field values")
    public void testCourierTheSameCreateImpossible() {
        Response responsePostFirst = CourierClient.sendPostRequestCourier(courier);

        Response responsePostSame = CourierClient.sendPostRequestCourier(courier);
        int statusCode = responsePostSame.then().extract().statusCode();
        String message = responsePostSame.then().extract().path("message");
        String requiredMessage = "Этот логин уже используется. Попробуйте другой.";

        Assert.assertEquals("Status code required 409", 409, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
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

            int statusCode = responsePost.then().extract().statusCode();
            String message = responsePost.then().extract().path("message");
            String requiredMessage = "Недостаточно данных для создания учетной записи";

            Assert.assertEquals("Status code required 400", 400, statusCode);
            Assert.assertEquals("Message dosn't match", requiredMessage, message);
        }
    }

    @Test
    @DisplayName("Can't create a courier with login which is used   /api/v1/courier")
    @Description("It's impossible to create a courier with login  which is used already")
    public void testCourierWithUsedLoginCreateImpossible() {
        Response responsePostFirst = CourierClient.sendPostRequestCourier(courier);

        ArrayList<String> courierData2 = Utils.getCourierData(fieldsSet, signsQuantity);
        courier = new Courier(courierData.get(0), courierData2.get(1), courierData2.get(2));
        Response responsePostSame2 = CourierClient.sendPostRequestCourier(courier);

        int statusCode = responsePostSame2.then().extract().statusCode();
        String message = responsePostSame2.then().extract().path("message");
        String requiredMessage = "Этот логин уже используется. Попробуйте другой.";

        Assert.assertEquals("Status code required 409", 409, statusCode);
        Assert.assertEquals("Message dosn't match", requiredMessage, message);
    }

    @After
    public void clearStand() {
        Integer id = CourierClient.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);
        CourierClient.deleteAllCouriers(idListToDelete);
    }


}
