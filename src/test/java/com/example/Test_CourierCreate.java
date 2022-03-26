import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Test_CourierCreate {
   private ArrayList<String> courierData ;
   private List<Integer> idListToDelete = new ArrayList<>();

   private Courier courier;
   private CourierLogin courierLogin;
    int fieldsSet;
    int signsQuantity;


    @Before
    public void setUp() {RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    fieldsSet = 1;
    signsQuantity = 10;

    courierData = Utils.getCourierData(fieldsSet, signsQuantity);
    courier = new Courier(courierData.get(0), courierData.get(1), courierData.get(2));
    courierLogin = new CourierLogin(courier.getLogin(),courier.getPassword());

    }



    @Test
    @DisplayName("Create a courier successfully /api/v1/courier")
    @Description ("It's possible to create  a new courier.")
    public void courierNewCreateSuccessfully() {
        Response responsePost = sendPostRequestCourier(courier);

        responsePost.then().assertThat().body("ok",equalTo(true))
        .and()
        .statusCode(201);

        Integer id = Test_CourierLogin.getCourierIdFromLogin(courierLogin); //new courier login and get id
        idListToDelete.add(id);              //add courier id to Delete List
}



    @Test
    @DisplayName("Return correct response code  /api/v1/courier")
    @Description ("Create Courier successfully. Return correct response code")
    public void courierCreateSuccessfullyResponceCodeCorrect() {
        Response responsePost = sendPostRequestCourier(courier);
        System.out.println(responsePost.body().asString());
        responsePost.then().assertThat()
                .statusCode(201);

        Integer id = Test_CourierLogin.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);
    }


    @Test
    @DisplayName("Two the same couriers can't be created  /api/v1/courier")
    @Description ("It's impossible to create the second courier with the same obligatory field values")
    public void courierTheSameCreateImpossible() {
        Response responsePostFirst = sendPostRequestCourier(courier);
                responsePostFirst.then().assertThat().body("ok",equalTo(true));

        Integer id = Test_CourierLogin.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);

        Response responsePostSame = sendPostRequestCourier(courier);
                responsePostSame.then()
                .assertThat().body("message",equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }




    @Test
    @DisplayName("Can't create a courier without an obligatory field /api/v1/courier")
    @Description ("It's impossible to create a courier if any obligatory field equals to null")
    public void courierWithObligatoryFieldsOnlyCreateSuccessfully() {

        for(int i = 0; i < courierData.size(); i++){
            courierData = Utils.getCourierData(fieldsSet, signsQuantity);
            String nullValue= courierData.set(i,null);

            courier = new Courier(courierData.get(0), courierData.get(1), courierData.get(2));
            Response responsePost = sendPostRequestCourier(courier);

            System.out.println(responsePost.body().asString());
            responsePost.then()
                    .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"))
                    .and()
                    .statusCode(400);

        }

    }


    @Test
    @DisplayName("Can't create a courier with login which is used   /api/v1/courier")
    @Description("It's impossible to create a courier with login  which is used already")
    public void courierWithUsedLoginCreateImpossible() {

        Response responsePostFirst = sendPostRequestCourier(courier);
        responsePostFirst.then().assertThat().body("ok",equalTo(true));

        Integer id = Test_CourierLogin.getCourierIdFromLogin(courierLogin);
        idListToDelete.add(id);

         ArrayList<String>  courierData2 = Utils.getCourierData(fieldsSet, signsQuantity);
        courier = new Courier(courierData.get(0), courierData2.get(1), courierData2.get(2));
        Response responsePostSame2 = sendPostRequestCourier(courier);
        System.out.println(responsePostSame2.body().asString());

        responsePostSame2.then()
                .assertThat().body("message",equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

   @After
    public void deleteAllCouriers() {

       for(Integer courierId: idListToDelete) {
           Response responseCourierDelete = deleteCourier(courierId);
           responseCourierDelete.then().assertThat().body("ok", equalTo(true));
       }

       idListToDelete.clear();
    }




    @Step("Send Post request CourierCreate to /api/v1/courier")
    public  static  Response sendPostRequestCourier(Courier courier) {
        Response createCourierResponse = given().header("Content-type", "application/json").and().body(courier).when().post("/api/v1/courier");
        return createCourierResponse;
    }


    @Step("Send Delete Courier request /api/v1/courier/:id")
    public static Response deleteCourier(Integer courierId) {
        DeletePostBody deletePostBody = new DeletePostBody(courierId.toString());
        Response deleteResponce = given().header("Content-type", "application/json").and().body(deletePostBody).when().delete("/api/v1/courier/" + courierId);
        return deleteResponce;
    }

}
