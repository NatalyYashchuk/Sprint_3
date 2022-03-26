package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class Test_OrderAccept {
    private ArrayList<String> courierData ;
    private List<Integer> courierListToDelete = new ArrayList<>();
    private List<Integer> trackOrderListToDelete = new ArrayList<>();
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
        courierListToDelete.add(courierId);

        trackOrder = Test_OrderListGet.getTrackOfOrderMetroStation("1");
        trackOrderListToDelete.add(trackOrder);
        id  = Test_OrderListGet.getOrderId(trackOrder);
    }

    @Test
    @DisplayName("COurier accept an Order. Result 200")
    @Description("Create a courier, Create an Order, Accept An Order")
    public void testCourierAcceptAnOrder() {
        Response response = Test_OrderListGet.courierAcceptOrder(id,courierId);
        response.then().assertThat().statusCode(200);
    }

    @After
    public void standClear() {
        Test_CourierCreate.deleteAllCouriers(courierListToDelete);
        deleteAllOrders(trackOrderListToDelete);
    }

    @Step("Order cancel doesn't work properly.")
    public static void deleteAllOrders(List<Integer> trackOrdersListToDelete) {

        for(Integer orderTrack: trackOrdersListToDelete) {
            Response responseCourierDelete = Test_OrderCreateColorParametrized.orderCancel(orderTrack);

        }

        trackOrdersListToDelete.clear();
    }



}
