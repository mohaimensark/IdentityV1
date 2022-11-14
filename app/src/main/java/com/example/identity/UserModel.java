package com.example.identity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.GeoPoint;

public class UserModel {

    private String name,email,password,profession,age,about,Profilelink;
    GeoPoint latitude;

    public UserModel() {
    }

    public UserModel(String name, String email,  String profession, String age, String about, GeoPoint latitude, String profilelink) {
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.age = age;
        this.about = about;
        Profilelink = profilelink;
        this.latitude = latitude;
    }

    public String getProfilelink() {
        return Profilelink;
    }

    public void setProfilelink(String profilelink) {
        Profilelink = profilelink;
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
