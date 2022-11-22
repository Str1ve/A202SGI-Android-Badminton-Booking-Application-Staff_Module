package com.example.badminton_app_admin.Model;

public class Booking {
    private String ProductId;
    private String ProductName;
    private String Price;
    private String Timeslot;
    private String MenuId;

    public Booking() {
    }

    public Booking(String productId, String productName, String price, String timeslot, String menuId) {
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Timeslot = timeslot;
        MenuId = menuId;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTimeslot() {
        return Timeslot;
    }

    public void setTimeslot(String timeslot) {
        Timeslot = timeslot;
    }
}
