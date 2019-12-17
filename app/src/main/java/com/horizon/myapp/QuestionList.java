package com.horizon.myapp;

public class QuestionList {
    private String question;

    public QuestionList(){

    }

    public QuestionList(String question){
        this.question = question;
    }

    public String getQuestion(){
        return question;
    }

    public void setQuestion(String question){
        this.question = question;
    }

}
