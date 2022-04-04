package com.example;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;

public class Utils {

    public static ArrayList<String> getCourierData(Integer quantity, Integer signs){
        ArrayList<String> courierDataArray = new ArrayList<>();

        for(int i = 0; i< quantity; i++) {
            String courierLogin = RandomStringUtils.randomAlphabetic(signs);
            String courierPassword = RandomStringUtils.randomAlphabetic(signs);
            String courierFirstName = RandomStringUtils.randomAlphabetic(signs);

            courierDataArray.add(courierLogin);
            courierDataArray.add(courierPassword);
            courierDataArray.add(courierFirstName);
        }
        return courierDataArray;
    }

}
