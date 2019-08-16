package com.example.tomho.specforu.datastrucuture;

import java.util.List;

public class User {
    private int photo;
    private String userId;
    private String displayName;
    private String email;
    private String dateOfBirth;
    private List<String> shopId;
    private List<String> friendId;

    public User(int photo, String userId, String displayName, String email, String dateOfBirth, List<String> shopId, List<String> friendId) {
        this.photo = photo;
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.shopId = shopId;
        this.friendId = friendId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public List<String> getShopId() {
        return shopId;
    }

    public List<String> getFriendId() {
        return friendId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setShopId(List<String> shopId) {
        this.shopId = shopId;
    }

    public void setFriendId(List<String> friendId) {
        this.friendId = friendId;
    }
}
