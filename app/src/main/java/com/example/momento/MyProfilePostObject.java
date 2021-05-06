package com.example.momento;

public class MyProfilePostObject {
    private String image;
    private String Ui;
    private String postRef;

    public MyProfilePostObject(String image, String ui, String postRef) {
        this.image = image;
        Ui = ui;
        this.postRef = postRef;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUi() {
        return Ui;
    }

    public void setUi(String ui) {
        Ui = ui;
    }

    public String getPostRef() {
        return postRef;
    }

    public void setPostRef(String postRef) {
        this.postRef = postRef;
    }

    @Override
    public boolean equals(Object o) {

        boolean same = false;
        if(o != null && o instanceof  MyFeedObject){
            same = this.getPostRef() == ((MyProfilePostObject) o) .postRef;
        }
        return same;

    }

    @Override
    public int hashCode() {
        int result  = 17;
        result = 31* result + (this.postRef == null ? 0  : this.postRef.hashCode());
        return result;
    }
}
