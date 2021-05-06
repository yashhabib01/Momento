package com.example.momento;

import java.util.Objects;

public class MyFeedObject {
    private String ImageResources;
    private String name;
    private String time;
    private String Uid;
    private String userImageResources;
    private String postRef;

    public MyFeedObject(String imageResources, String name, String time, String userImageResources,String Uid, String postRef) {
        ImageResources = imageResources;
        this.name = name;
        this.time = time;
        this.Uid  = Uid;
        this.userImageResources = userImageResources;
        this.postRef = postRef;
    }

    public String getPostRef() {
        return postRef;
    }

    public void setPostRef(String postRef) {
        this.postRef = postRef;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getImageResources() {
        return ImageResources;
    }

    public void setImageResources(String imageResources) {
        ImageResources = imageResources;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserImageResources() {
        return userImageResources;
    }

    public void setUserImageResources(String userImageResources) {
        this.userImageResources = userImageResources;
    }

    @Override
    public boolean equals(Object o) {

        boolean same = false;
        if(o != null && o instanceof  MyFeedObject){
            same = this.getImageResources() == ((MyFeedObject) o) .ImageResources;
        }
        return same;

    }

    @Override
    public int hashCode() {
        int result  = 17;
        result = 31* result + (this.ImageResources == null ? 0  : this.ImageResources.hashCode());
        return result;
    }
}
