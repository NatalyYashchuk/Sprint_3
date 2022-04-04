package api;

import io.qameta.allure.Step;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import model.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step("Order cancel doesn't work properly.")
    public static void deleteAllOrders(List<Integer> trackOrdersListToDelete) {

        for (Integer orderTrack : trackOrdersListToDelete) {
            Response responseCourierDelete = orderCancel(orderTrack);
        }

        trackOrdersListToDelete.clear();
    }

    @Step("Send Post request to create an model.Order to /api/v1/orders")
    public static Response sendPostRequestOrderCreate(Order order) {
        Response createOrderResponse = given().filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .header("Content-type", "application/json")
                .and().body(order).when().post("/api/v1/orders");
        return createOrderResponse;
    }

    @Step("Cancel  Order by put to /api/v1/orders/cancel")
    public static Response orderCancel(Integer track) {
        OrderCancelPutBody order = new OrderCancelPutBody(track);
        System.out.println("Track to cancel = " + track);
        Response orderCancelingResponse = given()
                .header("Content-type", "application/json")
                .and().body(order).when().put("/api/v1/orders/cancel");
        return orderCancelingResponse;
    }

    @Step("Get an orders List   /api/v1/orders")
    public static OrdersList getResponseOfGetList() {
        OrdersList ordersListGet = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders").body().as(OrdersList.class);

        return ordersListGet;
    }

    @Step("Get an orders List of the certain courier   /api/v1/orders?courierId=id")
    public static OrdersList getResponseCourierGetList(Integer courierId) {
        OrdersList ordersListGet = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders?courierId=" + courierId).body().as(OrdersList.class);

        return ordersListGet;
    }

    @Step("Accept certain Order by certain courier ")
    public static Response courierAcceptOrder(Integer id, Integer courierId) {
        AcceptPutBody acceptPutBody = new AcceptPutBody(id, courierId);

        String handleAcceptOrder = "/api/v1/orders/accept/" + id + "?courierId=" + courierId;
        System.out.println("handle = " + handleAcceptOrder);
        Response response = given()
                .header("Content-type", "application/json")
                .and().body(acceptPutBody).when().put(handleAcceptOrder);
        return response;
    }

    @Step("Id Courier get")
    public static Integer getCourierIdCertainCourier(Courier courier) {
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());         // login and get a courier id to the list to Delete
        Integer courierId = CourierClient.getCourierIdFromLogin(courierLogin);
        return courierId;
    }

    @Step("Get default order with required metro station id.")
    public static Integer getTrackOfOrderMetroStation(String metroStation) {
        Order order = Order.defaultOrderGet();
        order.setMetroStation(metroStation);
        Response responsePostOrder = sendPostRequestOrderCreate(order);
        Order orderBody = responsePostOrder.as(Order.class);
        Integer track = orderBody.getTrack();
        return track;
    }

    @Step("Get Order id by track ")
    public static Integer getOrderId(Integer track) {
        String handle = "/api/v1/orders/track?t=" + track;
        Integer id = given()
                .header("Content-type", "application/json")
                .get(handle).then().extract().path("order.id");

        System.out.println("id= " + id);
        return id;
    }
    @Step("Get an id list of the certain courier orders.")
    public static List<Integer> getOrdersIdFromCourierList(Integer courierId) {
        Integer ordersSize = OrderClient.getResponseCourierGetList(courierId).getOrders().size();
        List<Integer> ordersIdList = new ArrayList<>();
        Integer orderIdFromList;
        for (int i = 0; i < ordersSize; i++) {
            orderIdFromList = OrderClient.getResponseCourierGetList(courierId).getOrders().get(i).getId();
            ordersIdList.add(orderIdFromList);
        }
        return ordersIdList;
    }

}
