package com.example.tomho.specforu.datastrucuture;

public class SearchShop {

    private String shopName;
    private String address;
    private String shopID;

    public SearchShop(String shopName, String address, String shopID) {
        this.shopName = shopName;
        this.address = address;
        this.shopID = shopID;
    }

    // Getter

    public String getShopName() {
        return shopName;
    }

    public String getAddress() {
        return address;
    }

    public String getShopID() {
        return shopID;
    }

    // Setter

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }
}
