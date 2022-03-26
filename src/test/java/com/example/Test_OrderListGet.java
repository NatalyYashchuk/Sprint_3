package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Test_OrderList {
    private ArrayList<String> courierData ;
    private List<Integer> idListToDelete = new ArrayList<>();

    private Courier courier;
    private CourierLogin courierLogin;
    int fieldsSet;
    int signsQuantity;

    @Before
    public void setUp(){
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        fieldsSet = 1;
        signsQuantity = 10;

        courierData = Utils.getCourierData(fieldsSet, signsQuantity);
        courier = new Courier(courierData.get(0), courierData.get(1), courierData.get(2));
        courierLogin = new CourierLogin(courier.getLogin(),courier.getPassword());
    }


    @Test
    @DisplayName("Get an com.example.Order List ")
    @Description("Get  order list of the all couriers.")
    public void courierNewCreateSuccessfully() {
        Response responsePost = Test_CourierCreate.sendPostRequestCourier(courier);

        responsePost.then().assertThat().statusCode(201);

        Integer id = Test_CourierLogin.getCourierIdFromLogin(courierLogin); //new courier login and get id
        System.out.println(id);
        Order orderForListCreate = Order.defaultOrderGet();

        Response responsePostOrder = Test_OrderCreateColorParametrized.sendPostRequestOrderCreate(orderForListCreate);
        responsePostOrder.then().assertThat().statusCode(201);

        Order orderForListCreate2 = Order.defaultOrderGet();
        Response responsePostOrder2 = Test_OrderCreateColorParametrized.sendPostRequestOrderCreate(orderForListCreate2);
        responsePostOrder2.then().assertThat().statusCode(201);


//        Response response = getResponseOfGetList();
//        System.out.println(response.body().asString());
        OrdersList ordersListGet = given().filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .header("Content-type", "application/json")
                .get("/api/v1/orders").body().as(OrdersList.class);

        System.out.println(ordersListGet.getPageInfo());
        System.out.println("Orders: " + ordersListGet.getOrders() + "\n"
                + "Stations: " + ordersListGet.getAvailableStations() + "\n"
                + "com.example.PageInfo: " + ordersListGet.getPageInfo());


    }


//    @Step("Get an orders List   /api/v1/orders")
//    public static com.example.OrdersList getResponseOfGetList (){
//        com.example.OrdersList ordersListGet = given()
//                .header("Content-type", "application/json")
//                .get("/api/v1/orders").body().as(ordersList.class);
//
//        return ordersListGet;
//    }



//
//    public static com.example.OrdersList getResponseOfGetList (){
//        com.example.OrdersList ordersListBody = given()
//                .header("Content-type", "application/json")
//                .get("/api/v1/orders").body().as(com.example.OrdersList.class);
//
//        return ordersListBody;
//    }

}
