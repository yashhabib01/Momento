package com.example.momento;

public class CommentObject {
    private String image;
    private String name;
    private String time;
    private String comment;

    public CommentObject(String image, String name, String time, String comment) {
        this.image = image;
        this.name = name;
        this.time = time;
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public boolean equals(Object o) {

        boolean same = false;
        if(o != null && o instanceof  CommentObject){
            same = this.getComment() == ((CommentObject) o) .comment;
        }
        return same;

    }

    @Override
    public int hashCode() {
        int result  = 17;
        result = 31* result + (this.comment == null ? 0  : this.comment.hashCode());
        return result;
    }
}
