package com.example.identity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.GeoPoint;

public class UserModel {

    private String name,email,password,profession,age,about;
    GeoPoint latitude;

    public UserModel() {
    }

    public UserModel(String email, String name, String profession,String age,GeoPoint latitude,String about) {
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.age = age;
        this.about = about;
        this.latitude = latitude;
    }


    public GeoPoint getLatlan() {
        return latitude;
    }

    public void setLatlan(GeoPoint latlan) {
        this.latitude = latlan;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
