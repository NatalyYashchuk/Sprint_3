package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Test_OrderAccept {
    private ArrayList<String> courierData ;
    private List<Integer> idListToDelete = new ArrayList<>();

    private Courier courier;
    private CourierLogin courierLogin;
    private  Order order;
    private String login;
    private String password;
    private String firstName;
    private Integer courierId;
    private Integer trackOrder;
    private Integer id;
    int fieldsSet;
    int signsQuantity;

    @Before
    public void setUp(){
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        fieldsSet = 1;
        signsQuantity = 10;

        courierData = Utils.getCourierData(fieldsSet, signsQuantity);
        login = courierData.get(0);
        password = courierData.get(1);
        firstName = courierData.get(2);

        courier = Test_CourierLogin.courierCreate(login, password, firstName);
        courierId = Test_OrderListGet.getCourierIdCertainCourier(courier);
        System.out.println("Courier id = " + courierId + "  Courier login" + login + "  Courier password" + password);
        trackOrder = Test_OrderListGet.getTrackOfOrderMetroStation("1");
        System.out.println("Order track =  " + trackOrder);
        id  = Test_OrderListGet.getOrderId(trackOrder);
        System.out.println("Order id = " + id);

    }

    @Test
    @DisplayName("COurier accept an Order. Result 200")
    @Description("Create a courier, Create an Order, Accept An Order")
    public void courierAcceptAnOrder() {
        Response response = Test_OrderListGet.courierAcceptOrder(id,courierId);
        response.then().assertThat().statusCode(200);
    }

}
