package com.papringan.online.models;

public class ShippingOption {
    private int id;
    private String code;
    private String courier;
    private String service;
    private String description;
    private int cost;
    private String etd;

    public ShippingOption(int id, String code, String courier, String service, String description, int cost, String etd) {
        this.id = id;
        this.code = code;
        this.courier = courier;
        this.service = service;
        this.description = description;
        this.cost = cost;
        this.etd = etd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }
}
