package com.papringan.online.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private int id;
    private int customerId;
    private String address;
    private String phone;
    private ArrayList<OrderItem> orderItems;
    private PaymentMethod paymentMethod;
    private String shippingCourier;
    private String shippingServiceName;
    private String shippingEtd;
    private int shippingCost;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(int id, int customerId, String address, String phone, ArrayList<OrderItem> orderItems, PaymentMethod paymentMethod, String shippingCourier, String shippingServiceName, String shippingEtd, int shippingCost, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.address = address;
        this.phone = phone;
        this.orderItems = orderItems;
        this.paymentMethod = paymentMethod;
        this.shippingCourier = shippingCourier;
        this.shippingServiceName = shippingServiceName;
        this.shippingEtd = shippingEtd;
        this.shippingCost = shippingCost;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(int id, int customerId, String address, String phone, PaymentMethod paymentMethod, String shippingCourier, String shippingServiceName, String shippingEtd, int shippingCost, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.shippingCourier = shippingCourier;
        this.shippingServiceName = shippingServiceName;
        this.shippingEtd = shippingEtd;
        this.shippingCost = shippingCost;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShippingCourier() {
        return shippingCourier;
    }

    public void setShippingCourier(String shippingCourier) {
        this.shippingCourier = shippingCourier;
    }

    public String getShippingServiceName() {
        return shippingServiceName;
    }

    public void setShippingServiceName(String shippingServiceName) {
        this.shippingServiceName = shippingServiceName;
    }

    public String getShippingEtd() {
        return shippingEtd;
    }

    public void setShippingEtd(String shippingEtd) {
        this.shippingEtd = shippingEtd;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    public int getTotal() {
        int total = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getSubtotal();
        }
        total += this.shippingCost;
        return total;
    }
}
