package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Test_OrderListGet {
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
    private Integer trackOrder2;
    private Integer id2;
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
        courierId = getCourierIdCertainCourier(courier);

        trackOrder = getTrackOfOrderMetroStation("1");
        id  = getOrderId(trackOrder);

        trackOrder2 = getTrackOfOrderMetroStation("2");
        id2  = getOrderId(trackOrder2);

    }


    @Test
    @DisplayName("Get an com.example.Order List ")
    @Description("Get  order list of the all couriers.")
    public void orderFindInListSuccessfully() {
        Assert.assertNotNull(getResponseOfGetList().getOrders());
    }

    @Test
    @DisplayName("Get a courier order list  with different metro Stations. ")
    @Description("The courier get an order list successfully with metro Station1 and metroStation 2 orders.")
    public void getOrderListOfCertainCourierSuccessfully() {
        Assert.assertNotNull(getResponseCourierGetList(courierId).getOrders());
    }

    @Test
    @DisplayName("Get a courier order list  with accepted and non-accepted orders.")
    @Description("Check whether certain courier accepted and non-accepted orders are in the orders list.")
    public void getOrderListOfCertainCourier() {
    Response response = courierAcceptOrder(id,courierId);
    response.then().assertThat().statusCode(200);
    Integer idCheck1 = id;
    Integer idCheck2 = id2+1;

    List<Integer> ordersTrackList = getOrdersIdFromCourierList(courierId);

    Assert.assertTrue(ordersTrackList.contains(idCheck1));
    Assert.assertTrue(ordersTrackList.contains(idCheck2));
    }

    @Step ("Get an id list of the certain courier orders.")
    private List<Integer> getOrdersIdFromCourierList(Integer courierId) {
        Integer ordersSize = getResponseCourierGetList(courierId).getOrders().size();
        List<Integer> ordersIdList = new ArrayList<>();
        Integer orderIdFromList;
        for (int i = 0; i < ordersSize; i++){
            orderIdFromList = getResponseCourierGetList(courierId).getOrders().get(i).getId();
            ordersIdList.add(orderIdFromList);
        }
        return ordersIdList;
    }


    @Step("Get an orders List   /api/v1/orders")
    public static com.example.OrdersList getResponseOfGetList (){
        OrdersList ordersListGet = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders").body().as(OrdersList.class);

        return ordersListGet;
    }

    @Step("Get an orders List of the certain courier   /api/v1/orders?courierId=id")
    public static com.example.OrdersList getResponseCourierGetList (Integer courierId){
        OrdersList ordersListGet = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders?courierId=" + courierId).body().as(OrdersList.class);

        return ordersListGet;
    }

    @Step ("Accept certain Order by certain courier ")
    public static Response courierAcceptOrder(Integer id, Integer courierId) {
        AcceptPutBody acceptPutBody = new AcceptPutBody(id,courierId);

        String handleAcceptOrder = "/api/v1/orders/accept/" +id + "?courierId=" + courierId;
        System.out.println("handle = " + handleAcceptOrder);
        Response response = given()
                .header("Content-type", "application/json")
                .and().body(acceptPutBody).when().put(handleAcceptOrder);
        return response;
    }

    @Step ("Id Courier get")
    public static Integer getCourierIdCertainCourier (Courier courier) {
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(),courier.getPassword());         // login and get a courier id to the list to Delete
        Integer courierId = Test_CourierLogin.getCourierIdFromLogin(courierLogin);
        return courierId;
    }

    @Step ("Get default order with required metro station id.")
    public static Integer getTrackOfOrderMetroStation(String metroStation) {
        Order order = Order.defaultOrderGet();
        order.setMetroStation(metroStation);
        Response responsePostOrder = Test_OrderCreateColorParametrized.sendPostRequestOrderCreate(order);
        Order orderBody = responsePostOrder.as(Order.class);
        Integer track = orderBody.getTrack();
        return track;
    }

    @Step("Get Order id by track ")
    public static Integer getOrderId (Integer track){
        String handle = "/api/v1/orders/track?t=" + track;
      Integer   id  = given()
                .header("Content-type", "application/json")
                .get(handle).then().extract().path("order.id");


        System.out.println("id= " + id);
        return id;
    }
}
