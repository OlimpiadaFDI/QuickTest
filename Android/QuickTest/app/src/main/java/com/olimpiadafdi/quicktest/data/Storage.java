package com.olimpiadafdi.quicktest.data;

import java.util.ArrayList;

public class Storage {

    private static final String VERSION = "1.5";

    private static Storage ourInstance = new Storage();

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
    }

    // -------------------------------------------------------------------------------

    private String resultErrorLogin;
    private ArrayList<Integer> questionsAlreadyAsked = new ArrayList<Integer>();
    private Question question;

    public String getVersion(){
        return VERSION;
    }

    public void setResultErrorLogin(String s){
        this.resultErrorLogin = s;
    }

    public String getResultErrorLogin(){
        return resultErrorLogin;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<Integer> getQuestionsAlreadyAsked() {
        return questionsAlreadyAsked;
    }
}
