package com.example.badminton_app_admin.Model;

public class Court {
    private String Name;
    private String Image;
    private String Price;
    private String Timeslot;
    private String Description;
    private String MenuId;

    public Court() {
    }

    public Court(String name, String image, String price, String timeslot, String description, String menuId) {
        Name = name;
        Image = image;
        Price = price;
        Timeslot = timeslot;
        Description = description;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}


