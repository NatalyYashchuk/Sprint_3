package com.example;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class Orders {

    private List<Order> order;

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public Orders (List<Order> order){
        this.order = order;

    }

//    private Integer id;
//    private Integer courierId;
//    private String firstName;
//    private  String lastName;
//    private String address;
//    private String metroStation;
//    private String phone;
//    private  Integer rentTime;
//    private String deliveryDate;
//    private Integer track;
//    private Integer status;
//    private List<String> color;
//    private String comment;
//    private Boolean cancelled;
//    private Boolean finished;
//    private Boolean inDelivery;
//    private String courierFirstName;
//    private String createdAt;
//    private String updatedAt;
//
//
//
//    public Orders(Integer id, Integer courierId, String firstName, String lastName, String address,
//                  String metroStation, String phone, Integer rentTime, String deliveryDate, Integer track, Integer status,
//                  List<String> color, String comment, Boolean cancelled, Boolean finished,Boolean inDelivery,String courierFirstName,
//                  String createdAt, String updatedAt) {
//        this.id = id;
//        this.courierId = courierId;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.address = address;
//        this.metroStation = metroStation;
//        this.phone = phone;
//        this.rentTime = rentTime;
//        this.deliveryDate = deliveryDate;
//        this.track = track;
//        this.status = status;
//        this.color = color;
//        this.comment = comment;
//        this.cancelled = cancelled;
//        this.finished = finished;
//        this.inDelivery = inDelivery;
//        this.courierFirstName = courierFirstName;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//
//    }
//
//    public Boolean getCancelled() {
//        return cancelled;
//    }
//
//    public void setCancelled(Boolean cancelled) {
//        this.cancelled = cancelled;
//    }
//
//    public Boolean getFinished() {
//        return finished;
//    }
//
//    public void setFinished(Boolean finished) {
//        this.finished = finished;
//    }
//
//    public Boolean getInDelivery() {
//        return inDelivery;
//    }
//
//    public void setInDelivery(Boolean inDelivery) {
//        this.inDelivery = inDelivery;
//    }
//
//    public String getCourierFirstName() {
//        return courierFirstName;
//    }
//
//    public void setCourierFirstName(String courierFirstName) {
//        this.courierFirstName = courierFirstName;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getCourierId() {
//        return courierId;
//    }
//
//    public void setCourierId(Integer courierId) {
//        this.courierId = courierId;
//    }
//
//    public void setRentTime(Integer rentTime) {
//        this.rentTime = rentTime;
//    }
//
//    public Integer getTrack() {
//        return track;
//    }
//
//    public void setTrack(Integer track) {
//        this.track = track;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(String updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public  String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getMetroStation() {
//        return metroStation;
//    }
//
//    public void setMetroStation(String metroStation) {
//        this.metroStation = metroStation;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public int getRentTime() {
//        return rentTime;
//    }
//
//    public void setRentTime(int rentTime) {
//        this.rentTime = rentTime;
//    }
//
//    public String getDeliveryDate() {
//        return deliveryDate;
//    }
//
//    public void setDeliveryDate(String deliveryDate) {
//        this.deliveryDate = deliveryDate;
//    }
//
//    public List<String> getColor() {
//        return color;
//    }
//
//    public void setColor(List<String> color) {
//        this.color = color;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//


}
