package com.example.badminton_app_admin.Model;

import com.example.badminton_app_admin.Model.Booking;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String total;
    private List<Booking> courts;

    public Request() {
    }

    public Request(String phone, String name, String total, List<Booking> courts) {
        this.phone = phone;
        this.name = name;
        this.total = total;
        this.courts = courts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Booking> getCourts() {
        return courts;
    }

    public void setCourts(List<Booking> courts) {
        this.courts = courts;
    }
}
