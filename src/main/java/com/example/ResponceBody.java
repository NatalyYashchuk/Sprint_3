package com.example;

public class ResponceBody {
    private Boolean ok;
    private String message;
    private String id;
    private String ordersCount;
    private Integer track;

    public ResponceBody(){

    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(String ordersCount) {
        this.ordersCount = ordersCount;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }
}
