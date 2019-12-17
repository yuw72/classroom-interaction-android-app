package com.horizon.myapp;

public class CommentList {
    private String comment;

    public CommentList(){

    }
    public CommentList(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

}
