package com.example.identity;

import com.google.firebase.firestore.GeoPoint;

public class NewUserModel {

    private String name,email,password,profession,age,about;
   GeoPoint latlan;
    Double latitude,lengitude;

    public NewUserModel(GeoPoint latlan, Double latitude, Double lengitude) {
        this.latlan = latlan;
        this.latitude = latitude;
        this.lengitude = lengitude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getLatlan() {
        return latlan;
    }

    public void setLatlan(GeoPoint latlan) {
        this.latlan = latlan;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLengitude() {
        return lengitude;
    }

    public void setLengitude(Double lengitude) {
        this.lengitude = lengitude;
    }
}
