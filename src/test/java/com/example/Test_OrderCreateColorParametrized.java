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

    @Before
    public void setUp() {RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
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
    @DisplayName("Create an order successfully /api/v1/orders")
    @Description("It's possible to create  a new order.")
    public void orderCreateSuccessfully(){
        Order order = Order.defaultOrderGet();
        order.setColor(colorList);

        Response responsePostOrder = sendPostRequestOrderCreate(order);
        System.out.println(responsePostOrder.body().asString());
        responsePostOrder.then().assertThat()
                .statusCode(201);
    }

    @Test
    @DisplayName("Create an order successfully /api/v1/orders")
    @Description("It's possible to create  a new order.")
    public void orderCreateSuccessfullyHasTrack(){
        Order order = Order.defaultOrderGet();
        order.setColor(colorList);

        Response responsePostOrder = sendPostRequestOrderCreate(order);
        System.out.println(responsePostOrder.body().asString());
        responsePostOrder.then().assertThat().body("track", notNullValue());
        Order orderBody = responsePostOrder.as(Order.class);
        Integer track = orderBody.getTrack();
        System.out.println("track=" + track);
        idListToDelete.add(track);
    }

    @After
    public void cancelAllOrders (){

        for(Integer cancelTrack: idListToDelete) {
            Response responseCancelOrder = orderCancel(cancelTrack);
            responseCancelOrder.then().assertThat().body("ok", equalTo(true));
        }

        idListToDelete.clear();
    }

    @Step("Send Post request to create an Order to /api/v1/orders")
    public  static  Response sendPostRequestOrderCreate(Order order) {
        Response createOrderResponse = given().filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .header("Content-type", "application/json")
                .and().body(order).when().post("/api/v1/orders");
        return createOrderResponse;
    }

    public Response orderCancel(Integer track){
        OrderCancelPutBody order = new OrderCancelPutBody(track);
        System.out.println("Track to cancel = "  + track);
        Response orderCancelingResponse = given().filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .header("Content-type", "application/json")
                .and().body(order).when().put("/api/v1/orders/cancel");
        return orderCancelingResponse;
    }
}
