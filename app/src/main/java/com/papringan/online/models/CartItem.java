package com.papringan.online.models;

import org.json.JSONObject;

import java.time.LocalDateTime;

public class CartItem {
    private int id;
    private int customer_id;
    private Product product;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isChecked = false;

    public CartItem(int id, int customer_id, Product product, int quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customer_id = customer_id;
        this.product = product;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product.getId());
            jsonObject.put("quantity", quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}