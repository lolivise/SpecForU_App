package com.example.tomho.specforu.datastrucuture;

public class Profile {

    private int photo;
    private String id;
    private String email;
    private String name;
    private String birth;

    public Profile(int photo, String id, String email, String name, String birth) {
        this.photo = photo;
        this.id = id;
        this.email = email;
        this.name = name;
        this.birth = birth;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

}
