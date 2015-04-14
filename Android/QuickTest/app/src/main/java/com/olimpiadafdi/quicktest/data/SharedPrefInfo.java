package com.olimpiadafdi.quicktest.data;

public class SharedPrefInfo {
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

    // Number of successful answers
    public static final String KEY_ANSWERS = "gameAnswers";

    // Number of successful answers
    public static final String KEY_TOTALTIME = "gameTime";

    public SharedPrefInfo(){}

}
