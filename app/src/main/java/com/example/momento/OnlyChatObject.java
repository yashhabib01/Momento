package com.example.momento;

public class OnlyChatObject {
    private String profileImage;
    private String name;
    private String uid;
    private Boolean check;

    public OnlyChatObject(String profileImage, String name, String uid, Boolean check) {
        this.profileImage = profileImage;
        this.name = name;
        this.uid = uid;
        this.check = check;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    @Override
    public boolean equals(Object o) {

        boolean same = false;
        if(o != null && o instanceof  MyFeedObject){
            same = this.getName() == ((OnlyChatObject) o) .name;
        }
        return same;

    }

    @Override
    public int hashCode() {
        int result  = 17;
        result = 31* result + (this.name == null ? 0  : this.name.hashCode());
        return result;
    }


}
