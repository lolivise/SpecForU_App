package com.example.tomho.specforu.datastrucuture;

public class Shop {

    private String shopName;
    private int photo;
    private String favoriteNum;
    private String specialNum;

    public Shop(String shopName, int photo, String favoriteNum, String specialNum) {
        this.shopName = shopName;
        this.photo = photo;
        this.favoriteNum = favoriteNum;
        this.specialNum = specialNum;
    }

    // Getter

    public String getShopName() {
        return shopName;
    }

    public int getPhoto() {
        return photo;
    }

    public String getFavoriteNum() {
        return favoriteNum;
    }

    public String getSpecialNum() {
        return specialNum;
    }

    // Setter

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setFavoriteNum(String favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public void setSpecialNum(String specialNum) {
        this.specialNum = specialNum;
    }
}
