package com.example.momento;

public class ChatObject {
    private String user_image;
    private String user_name;
    private String uid;

    public ChatObject(String user_image, String user_name,String uid) {
        this.user_image = user_image;
        this.user_name = user_name;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
