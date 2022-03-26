import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;

public class Utils {

    public static ArrayList<String> getCourierData(Integer quantity, Integer signs){
        ArrayList<String> courierDataArray = new ArrayList<>();

        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        for(int i = 0; i< quantity; i++) {
            String courierLogin = RandomStringUtils.randomAlphabetic(10);
            // с помощью библиотеки RandomStringUtils генерируем пароль
            String courierPassword = RandomStringUtils.randomAlphabetic(10);
            // с помощью библиотеки RandomStringUtils генерируем имя курьера
            String courierFirstName = RandomStringUtils.randomAlphabetic(10);

            courierDataArray.add(courierLogin);
            courierDataArray.add(courierPassword);
            courierDataArray.add(courierFirstName);
        }
        return courierDataArray;
    }

    public static ArrayList<String> getCourierFirstName(Integer quantity, Integer signs){
        ArrayList<String> courierFirstNameArray = new ArrayList<>();
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        for(int i = 0; i< quantity; i++) {
            // с помощью библиотеки RandomStringUtils генерируем имя курьера
            String courierFirstName = RandomStringUtils.randomAlphabetic(signs);
            courierFirstNameArray.add(courierFirstName);
        }
        return courierFirstNameArray;
    }

    public static ArrayList<String> getCourierPassword(Integer quantity, Integer signs){
        ArrayList<String> courierPasswordArray = new ArrayList<>();
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        for(int i = 0; i< quantity; i++) {
            // с помощью библиотеки RandomStringUtils генерируем имя курьера
            String courierPassword = RandomStringUtils.randomAlphabetic(signs);
            courierPasswordArray.add(courierPassword);
        }
        return courierPasswordArray;
    }

    public static ArrayList<String> getCourierLogin(Integer quantity, Integer signs){
        ArrayList<String> courierLoginArray = new ArrayList<>();
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        for(int i = 0; i< quantity; i++) {
            // с помощью библиотеки RandomStringUtils генерируем имя курьера
            String courierLogin = RandomStringUtils.randomAlphabetic(signs);
            courierLoginArray.add(courierLogin);
        }
        return courierLoginArray;
    }



}
