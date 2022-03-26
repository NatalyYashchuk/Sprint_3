package com.example;

import com.example.Order;
import com.example.OrderCancelPutBody;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


@RunWith(Parameterized.class)
public  class Test_OrderCreateColorParametrized {
    private  List<String> colorList;              //changing parameter
    private List<Integer> idListToDelete = new ArrayList<>();
    private Integer track;
     private Order order;

    @Before
    public void setUp() {RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        order = Order.defaultOrderGet();
        order.setColor(colorList);

    }

    public Test_OrderCreateColorParametrized(List<String> colorList){
        this.colorList = colorList;
    }

    @Parameterized.Parameters
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
    public void orderCreateSuccessfully(){
        Response responsePostOrder = sendPostRequestOrderCreate(order);
        System.out.println(responsePostOrder.body().asString());
        responsePostOrder.then().assertThat()
                .statusCode(201);
    }

    @Test
    @DisplayName(" Order track received successfully /api/v1/orders")
    @Description("It's possible to receive track of the just created track.")
    public void orderCreateSuccessfullyHasTrack(){
        Response responsePostOrder = sendPostRequestOrderCreate(order);
        Order orderBody = responsePostOrder.as(Order.class);
        Integer track = orderBody.getTrack();

        idListToDelete.add(track);
    }

    @After
    @Step("Cancel all Orders from the list to clear the TestStand.Doesn't work properly")
    public void cancelAllOrders (){

        for(Integer cancelTrack: idListToDelete) {
            Response responseCancelOrder = orderCancel(cancelTrack);

        }

        idListToDelete.clear();
    }

    @Step("Send Post request to create an com.example.Order to /api/v1/orders")
    public  static  Response sendPostRequestOrderCreate(Order order) {
        Response createOrderResponse = given().filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .header("Content-type", "application/json")
                .and().body(order).when().post("/api/v1/orders");
        return createOrderResponse;
    }

    @Step("Cancel  Order by put to /api/v1/orders/cancel")
    public static Response orderCancel(Integer track){
        OrderCancelPutBody order = new OrderCancelPutBody(track);
        System.out.println("Track to cancel = "  + track);
        Response orderCancelingResponse = given()
                .header("Content-type", "application/json")
                .and().body(order).when().put("/api/v1/orders/cancel");
        return orderCancelingResponse;
    }
}
