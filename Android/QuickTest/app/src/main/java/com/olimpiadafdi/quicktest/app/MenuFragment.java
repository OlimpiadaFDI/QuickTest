package com.olimpiadafdi.quicktest.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.olimpiadafdi.quicktest.R;
import com.olimpiadafdi.quicktest.data.Storage;

public class MenuFragment extends Fragment {

    private Activity activity;
    private TextView textView_version;
    private Button button_newGame;
    private Button button_score;
    private Button button_exit;

    public MenuFragment(){}

    public interface menuInterface{
        public void launchGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        //Asociamos los widgets del layout a variables java
        this.button_newGame = (Button) rootView.findViewById(R.id.button_newGame);
        this.button_score = (Button) rootView.findViewById(R.id.button_score);
        this.button_exit = (Button) rootView.findViewById(R.id.button_exit);
        this.textView_version = (TextView) rootView.findViewById(R.id.textView_version);

        // Escribimos el textView que muestra la versión de la app
        Context context = activity.getApplicationContext();
        String version = activity.getString(R.string.Version, Storage.getInstance().getVersion());
        textView_version.setText(version);

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
        View.OnClickListener handler_newGame = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    ((menuInterface) activity).launchGame();
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        };
        View.OnClickListener handler_score = new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        };
        View.OnClickListener handler_exit = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Context context = activity.getApplicationContext();
                String text = activity.getString(R.string.goodbye);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        };
        button_newGame.setOnClickListener(handler_newGame);
        button_score.setOnClickListener(handler_score);
        button_exit.setOnClickListener(handler_exit);
    }
}
