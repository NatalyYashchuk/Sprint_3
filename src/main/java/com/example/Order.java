package com.example;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private Integer id;
    private Integer courierId;
    private String firstName;
    private  String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private  Integer rentTime;
    private String deliveryDate;
    private Integer track;
    private List<String> color;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private Integer status;



    public Order(Integer id, Integer courierId, String firstName, String lastName, String address,
                 String metroStation, String phone, Integer rentTime, String deliveryDate, Integer track,
                 List<String> color, String comment, String createdAt, String updatedAt, Integer status) {
        this.id = id;
        this.courierId = courierId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.track = track;
        this.color = color;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }




    public  static Order defaultOrderGet (){
       Integer id = (int) Math.round(Math.random());
       Integer courierId = (int) Math.round(Math.random());
        String any = RandomStringUtils.randomAlphabetic(1);
        String anyI = String.valueOf (Math.round(1));


        String firstName = "Kate"+any;
        String lastName = "Monsoon"+any;
        String address = "Krakov, Sunshine str. 32"+any;
        String metroStation = "4";
        String phone = "+7 800 455 45 4"+anyI;
        Integer rentTime = 4;
        String deliveryDate = "2020-06-0"+anyI;
        Integer track = (int) Math.round(Math.random()*1000000);
        List<String> color = new ArrayList<>();
        color.add("BLACK");

        String comment = "Hurry up";
        String createdAt = "2021-09-28T16:48:50.510Z";
        String updatedAt = "2021-09-28T16:48:50.510Z";
        Integer status = 0;


        Order defaultOrder = new Order(id, courierId, firstName, lastName, address,
                metroStation, phone, rentTime, deliveryDate, track,
                 color,  comment,  createdAt,  updatedAt, status);
        return defaultOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setRentTime(Integer rentTime) {
        this.rentTime = rentTime;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public  String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }




}
