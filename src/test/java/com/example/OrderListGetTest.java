package com.example;

import api.CourierClient;
import api.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class OrderListGetTest {
    private ArrayList<String> courierData;
    private List<Integer> idListToDelete = new ArrayList<>();
    private List<Integer> idListToDeleteCouriers = new ArrayList<>();
    private Courier courier;
    private CourierLogin courierLogin;
    private Order order;
    private String login;
    private String password;
    private String firstName;
    private Integer courierId;
    private Integer trackOrder;
    private Integer id;
    private Integer trackOrder2;
    private Integer id2;
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
        idListToDeleteCouriers.add(courierId);

        trackOrder = OrderClient.getTrackOfOrderMetroStation("1");
        id = OrderClient.getOrderId(trackOrder);
        idListToDelete.add(trackOrder);

        trackOrder2 = OrderClient.getTrackOfOrderMetroStation("2");
        id2 = OrderClient.getOrderId(trackOrder2);
        idListToDelete.add(trackOrder2);

    }

    @Test
    @DisplayName("Get an model.Order List ")
    @Description("Get  order list of the all couriers.")
    public void orderFindInListSuccessfully() {
        Assert.assertNotNull(OrderClient.getResponseOfGetList().getOrders());
    }

    @Test
    @DisplayName("Get a courier order list  with different metro Stations. ")
    @Description("The courier get an order list successfully with metro Station1 and metroStation 2 orders.")
    public void getOrderListOfCertainCourierSuccessfully() {
        Assert.assertNotNull("The courier order list is empty",OrderClient.getResponseCourierGetList(courierId).getOrders());
    }

    @Test
    @DisplayName("Get a courier order list  with accepted and non-accepted orders.")
    @Description("Check whether certain courier accepted and non-accepted orders are in the orders list.")
    public void getOrderListOfCertainCourier() {
        OrderClient.courierAcceptOrder(id, courierId);

        Integer idCheck1 = id;
        Integer idCheck2 = id2 + 1;

        List<Integer> ordersTrackList = OrderClient.getOrdersIdFromCourierList(courierId);
        Assert.assertTrue("The courier order list doesn't contains accepted order", ordersTrackList.contains(idCheck1));
        Assert.assertTrue("The courier order list doesn't contains non-accepted order", ordersTrackList.contains(idCheck2));
    }

    @After
    @Step("Cancel all Orders from the list to clear the TestStand.Doesn't work properly")
    public void standClear() {
        CourierClient.deleteAllCouriers(idListToDeleteCouriers);
        OrderClient.deleteAllOrders(idListToDelete);
    }

}
