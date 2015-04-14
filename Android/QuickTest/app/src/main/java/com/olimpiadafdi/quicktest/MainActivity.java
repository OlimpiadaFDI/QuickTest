package com.olimpiadafdi.quicktest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.olimpiadafdi.quicktest.app.LoginFragment;
import com.olimpiadafdi.quicktest.app.MenuFragment;
import com.olimpiadafdi.quicktest.app.MainFragment;
import com.olimpiadafdi.quicktest.app.ScoreFragment;
import com.olimpiadafdi.quicktest.data.SharedPrefInfo;

public class MainActivity extends ActionBarActivity
        implements LoginFragment.loginInterface, MenuFragment.menuInterface,
        MainFragment.mainInterface, ScoreFragment.scoreInterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.container, new LoginFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // -------------------- Mio --------------------

    //Override the login method here
    @Override
    public void userDetails(String nick, String pass, boolean remember) {
        SharedPrefInfo info = new SharedPrefInfo();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(info.PREF_NAME, info.PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        if (!remember) {
            //editor.remove(info.KEY_NICK);
            //editor.remove(info.KEY_PASSWORD);
            editor.clear();
        } else {
            editor.putString(info.KEY_NICK, nick);
            editor.putString(info.KEY_PASSWORD, pass);
        }
        editor.commit();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, new MenuFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    //Override the launchGame method here
    @Override
    public void launchGame() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, new MainFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    //Override the backToMenu method here
    @Override
    public void backToMenu() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, new MenuFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    //Override the backToMenu method here
    @Override
    public void showScores() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, new ScoreFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
