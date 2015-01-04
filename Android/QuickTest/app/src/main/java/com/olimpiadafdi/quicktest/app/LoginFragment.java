package com.olimpiadafdi.quicktest.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.olimpiadafdi.quicktest.R;
import com.olimpiadafdi.quicktest.data.Storage;

public class LoginFragment extends Fragment {

    private Activity activity;
    private EditText editText_nick;
    private EditText editText_pass;
    private CheckBox checkBox_remember;
    private Button button_register;
    private Button button_login;

    public LoginFragment(){}

    public interface loginInterface{
        public void userDetails(String nick, String pass, boolean remember);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        //Asociamos los widgets del layout a variables java
        this.editText_nick = (EditText) rootView.findViewById(R.id.editText_nick);
        this.editText_pass = (EditText) rootView.findViewById(R.id.editText_pass);
        this.checkBox_remember = (CheckBox) rootView.findViewById(R.id.checkBox_remember);
        this.button_register = (Button) rootView.findViewById(R.id.button_register);
        this.button_login = (Button) rootView.findViewById(R.id.button_login);

        Storage.SharedPrefInfo info = Storage.getInstance().getSharedPrefInfo();

        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(info.PREF_NAME, info.PRIVATE_MODE);
        if (pref.contains(info.KEY_NICK) && pref.contains(info.KEY_PASSWORD)){
            this.editText_nick.setText(pref.getString(info.KEY_NICK, null));
            this.editText_pass.setText(pref.getString(info.KEY_PASSWORD, null));
        }

        this.handleButtons();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void handleButtons() {
        View.OnClickListener handlerRegister = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        View.OnClickListener handlerLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = editText_nick.getText().toString();
                String pass = editText_pass.getText().toString();
                boolean remember = checkBox_remember.isChecked();
                try{
                    ((loginInterface) activity).userDetails(nick, pass, remember);
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        };

        button_register.setOnClickListener(handlerRegister);
        button_login.setOnClickListener(handlerLogin);
    }
}
