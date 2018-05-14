package com.example.ignacy.myapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Piotr on 2017-12-30.
 */

public class User {
    String email, name, surname, group, gender, city;
    Integer count;
    Double litres, latitude, longitude;
    String last_date;

    public String getValidToken() {
        return validToken;
    }

    public void setValidToken(String validToken) {
        this.validToken = validToken;
    }

    String validToken;
    Map<String,String> notifications;
    public User() {
        notifications = new HashMap<String,String>();
        count = 0;
        litres = 0.0;
        latitude = 0.0;
        longitude = 0.0;
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
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setNotifications(Map<String,String> notifications) {
        this.notifications = notifications;
    }
    public String getLastDate() {
        return last_date;
    }
    public void setLastDate(String last_date) {
        this.last_date = last_date;
    }
    public Integer getCount() {
        return count;
    }
    public void IncreaseCount(Integer count) {
        this.count += count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public Double getLitres() {
        return litres;
    }
    public void IncreaseLitres(Double litres) {
        this.litres += litres;
    }
    public void setLitres(Double litres) {
        this.litres = litres;
    }
    public void setLocation(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
