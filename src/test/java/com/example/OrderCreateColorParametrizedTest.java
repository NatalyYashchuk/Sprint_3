package com.example;

import api.CourierClient;
import api.OrderClient;
import model.Order;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreateColorParametrizedTest {
    private List<String> colorList;
    private List<Integer> idListToDelete = new ArrayList<>();
    private Order order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        order = Order.defaultOrderGet();
        order.setColor(colorList);
    }

    public OrderCreateColorParametrizedTest(List<String> colorList) {
        this.colorList = colorList;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Object[][] data() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Arrays.asList(" ")}

        };
    }

    @Test
    @DisplayName("Create an order successfully. Check status Code = 201 /api/v1/orders")
    @Description("It's possible to create  a new order.")
    public void orderCreateSuccessfully() {
        Response responsePostOrder = OrderClient.sendPostRequestOrderCreate(order);
        System.out.println(responsePostOrder.body().asString());
        responsePostOrder.then().assertThat()
                .statusCode(201);
    }

    @Test
    @DisplayName(" Order track received successfully /api/v1/orders")
    @Description("It's possible to receive track of the just created track.")
    public void orderCreateSuccessfullyHasTrack() {
        Response responsePostOrder = OrderClient.sendPostRequestOrderCreate(order);
        Order orderBody = responsePostOrder.as(Order.class);
        Integer track = orderBody.getTrack();

        idListToDelete.add(track);
    }

    @After
    @Step("Cancel all Orders from the list to clear the TestStand.Doesn't work properly")
    public void standClear() {
        OrderClient.deleteAllOrders(idListToDelete);
    }
}
