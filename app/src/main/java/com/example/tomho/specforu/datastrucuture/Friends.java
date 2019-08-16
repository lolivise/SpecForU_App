package com.example.tomho.specforu.datastrucuture;

public class Friends {

    private int photo;
    private String name;
    private String message;
    private String lastMessageTime;
    private String unreadMessage;

    public Friends(int photo, String name, String message, String lastMessageTime, String unreadMessage){
        this.photo = photo;
        this.name = name;
        this.message = message;
        this.lastMessageTime = lastMessageTime;
        this.unreadMessage = unreadMessage;
    }

    // Getter
    public int getPhoto(){
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public String getUnreadMessage() {
        return unreadMessage;
    }


    // Setter
    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public void setUnreadMessage(String unreadMessage) {
        this.unreadMessage = unreadMessage;
    }
}
