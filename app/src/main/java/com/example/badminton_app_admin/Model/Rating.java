package com.example.badminton_app_admin.Model;

public class Rating {
    private String userName;
    private String orderId;
    private String menuId;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String orderId, String menuId, String rateValue, String comment, String userName) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.rateValue = rateValue;
        this.comment = comment;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String userPhone) {
        this.orderId = userPhone;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
