package com.example.identity;

public class UpdatedGeoData {
    private String latitude,longitude,name,profession;


    public UpdatedGeoData(String latitude, String longitude, String name, String profession) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.profession = profession;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
