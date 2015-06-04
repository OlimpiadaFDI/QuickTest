package com.olimpiadafdi.quicktest.data;

import java.util.ArrayList;

public class Storage {

    private static final String VERSION = "4.5";

    private static Storage ourInstance = new Storage();

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
    }

    // -------------------------------------------------------------------------------

    private String registerURL = "http://www.google.es";
    private String nick;
    private String resultErrorLogin;
    private ArrayList<Integer> questionsAlreadyAsked = new ArrayList<Integer>();
    private Question question;
    private ArrayList<Badge> listBadges = new ArrayList<Badge>();

    public String getVersion(){
        return VERSION;
    }

    public String getRegisterURL() {
        return registerURL;
    }

    public void setNick(String s){
        this.nick = s;
    }

    public String getNick(){
        return this.nick;
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

    public ArrayList<Badge> getListBadges() {
        return listBadges;
    }

    public void setListBadges(ArrayList<Badge> listBadges) {
        this.listBadges = listBadges;
    }
}
