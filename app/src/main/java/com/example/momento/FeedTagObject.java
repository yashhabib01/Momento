package com.example.momento;

public class FeedTagObject {
    private String image;
    private String uid;
    private String Name;

    public FeedTagObject(String image, String uid , String Name) {
        this.image = image;
        this.uid = uid;
        this.Name = Name;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public boolean equals(Object o) {

        boolean same = false;
        if(o != null && o instanceof  FeedTagObject){
            same = this.getName() == ((FeedTagObject) o) .Name;
        }
        return same;

    }

    @Override
    public int hashCode() {
        int result  = 17;
        result = 31* result + (this.Name == null ? 0  : this.Name.hashCode());
        return result;
    }
}
