package com.olimpiadafdi.quicktest.data;

import java.util.ArrayList;

public class Storage {

    private static final String VERSION = "1.1";

    private static Storage ourInstance = new Storage();
    private ArrayList<Integer> questionsAlreadyAsked = new ArrayList<Integer>();
    private Question question;

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
    }

    public class SharedPrefInfo{
        // Shared pref mode
        public static final int PRIVATE_MODE = 0;

        // Sharedpref file name
        public static final String PREF_NAME = "QuickTestPref";

        // All Shared Preferences Keys
        public static final String IS_LOGIN = "IsLoggedIn";

        // User name (make variable public to access from outside)
        public static final String KEY_NICK = "nick";

        // Email address (make variable public to access from outside)
        public static final String KEY_PASSWORD = "password";

        public SharedPrefInfo(){}
    }

    public String getVersion(){
        return VERSION;
    }

    public SharedPrefInfo getSharedPrefInfo(){
        return new SharedPrefInfo();
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
