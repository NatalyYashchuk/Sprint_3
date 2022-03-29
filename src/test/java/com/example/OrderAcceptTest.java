package com.example;

import api.CourierClient;
import api.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierLogin;
import model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderAcceptTest {
    private ArrayList<String> courierData;
    private List<Integer> courierListToDelete = new ArrayList<>();
    private List<Integer> trackOrderListToDelete = new ArrayList<>();
    private Courier courier;
    private CourierLogin courierLogin;
    private Order order;
    private String login;
    private String password;
    private String firstName;
    private Integer courierId;
    private Integer trackOrder;
    private Integer id;
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

        courier = CourierClient.courierCreate(login, password, firstName);
        courierId = OrderClient.getCourierIdCertainCourier(courier);
        courierListToDelete.add(courierId);

        trackOrder = OrderClient.getTrackOfOrderMetroStation("1");
        trackOrderListToDelete.add(trackOrder);
        id = OrderClient.getOrderId(trackOrder);
    }

    @Test
    @DisplayName("COurier accept an Order. Result 200")
    @Description("Create a courier, Create an Order, Accept An Order")
    public void testCourierAcceptAnOrder() {
        Response response = OrderClient.courierAcceptOrder(id, courierId);
        response.then().assertThat().statusCode(200);
    }

    @After
    public void standClear() {
        CourierClient.deleteAllCouriers(courierListToDelete);
        OrderClient.deleteAllOrders(trackOrderListToDelete);
    }

}
