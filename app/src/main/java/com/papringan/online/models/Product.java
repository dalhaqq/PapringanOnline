package com.papringan.online.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class Product implements Parcelable {
    private int id;
    private String name;
    private int price;
    private String description;
    private String image;
    private int stock;
    private int weight;
    private int dimensionX;
    private int dimensionY;
    private int dimensionZ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(int id, String name, int price, String description, String image, int stock, int weight, int dimensionX, int dimensionY, int dimensionZ, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.stock = stock;
        this.weight = weight;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.dimensionZ = dimensionZ;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDimensionX() {
        return dimensionX;
    }

    public void setDimensionX(int dimensionX) {
        this.dimensionX = dimensionX;
    }

    public int getDimensionY() {
        return dimensionY;
    }

    public void setDimensionY(int dimensionY) {
        this.dimensionY = dimensionY;
    }

    public int getDimensionZ() {
        return dimensionZ;
    }

    public void setDimensionZ(int dimensionZ) {
        this.dimensionZ = dimensionZ;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.price);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeInt(this.stock);
        dest.writeInt(this.weight);
        dest.writeInt(this.dimensionX);
        dest.writeInt(this.dimensionY);
        dest.writeInt(this.dimensionZ);
        dest.writeSerializable(this.createdAt);
        dest.writeSerializable(this.updatedAt);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.price = source.readInt();
        this.description = source.readString();
        this.image = source.readString();
        this.stock = source.readInt();
        this.weight = source.readInt();
        this.dimensionX = source.readInt();
        this.dimensionY = source.readInt();
        this.dimensionZ = source.readInt();
        this.createdAt = (LocalDateTime) source.readSerializable();
        this.updatedAt = (LocalDateTime) source.readSerializable();
    }

    protected Product(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readInt();
        this.description = in.readString();
        this.image = in.readString();
        this.stock = in.readInt();
        this.weight = in.readInt();
        this.dimensionX = in.readInt();
        this.dimensionY = in.readInt();
        this.dimensionZ = in.readInt();
        this.createdAt = (LocalDateTime) in.readSerializable();
        this.updatedAt = (LocalDateTime) in.readSerializable();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
