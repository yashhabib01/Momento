package com.example.momento;

public class ChatViewObject {

    private String chat;
    private String uid;
    private boolean checkCreate;

    public ChatViewObject(String chat,String uid, boolean checkCreate) {
        this.chat = chat;
        this.checkCreate = checkCreate;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public boolean isCheckCreate() {
        return checkCreate;
    }

    public void setCheckCreate(boolean checkCreate) {
        this.checkCreate = checkCreate;
    }
}
