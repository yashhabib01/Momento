package com.example.momento;

import java.util.Objects;

public class TagObject {
    private String name;
    private String image;
    private String uid;
    private Boolean check;


    public TagObject(String name, String image, String uid, Boolean check) {
        this.name = name;
        this.image = image;
        this.uid = uid;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        if(o != null && o instanceof  TagObject){
            same = this.getName() == ((TagObject) o) .name;
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
