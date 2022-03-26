package com.example;

public class AcceptPutBody {
    private Integer id;
    private Integer courierId;


    public AcceptPutBody(Integer id, Integer courierId){
        this.id = id;
        this.courierId = courierId;
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
}
