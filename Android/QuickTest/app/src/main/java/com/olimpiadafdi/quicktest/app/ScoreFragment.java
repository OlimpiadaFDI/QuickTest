package com.olimpiadafdi.quicktest.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.olimpiadafdi.quicktest.R;
import com.olimpiadafdi.quicktest.connection.JsonRequest;
import com.olimpiadafdi.quicktest.data.Badge;
import com.olimpiadafdi.quicktest.data.SharedPrefInfo;
import com.olimpiadafdi.quicktest.data.Storage;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class ScoreFragment extends Fragment {

    private static String SHOWBADGES = "showBadges";

    private Activity activity;
    private TextView textView_lastGameData;
    private ListView lv;
    private Button button_backToMenu;
    private ArrayList<Badge> list;
    private ArrayAdapter<Badge> adapter;

    public ScoreFragment(){}

    public interface scoreInterface{
        public void backToMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_score, container, false);

        //Asociamos los widgets del layout a variables java
        this.textView_lastGameData = (TextView) rootView.findViewById(R.id.textView_lastGameData);
        this.button_backToMenu = (Button) rootView.findViewById(R.id.button_backToMenu);
        this.lv = (ListView) rootView.findViewById(R.id.listView_badges);

        JsonRequest jsonRequest = new JsonRequest(SHOWBADGES, activity.getApplicationContext(), updateDataSuccess, updateDataError, null);
        jsonRequest.request();

        list = new ArrayList<Badge>();
        adapter = new ArrayAdapter<Badge>(activity.getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, list){
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Badge getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return list.get(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                Badge data = list.get(position);
                text1.setTextColor(Color.DKGRAY);
                text2.setTextColor(Color.GRAY);
                text1.setText(data.getId() +" - "+ data.getDescCorta());
                text2.setText(data.getDescLarga() +" ("+data.getPuntuacion() +").");
                return view;
            }
        };
        lv.setAdapter(adapter);

        SharedPrefInfo info = new SharedPrefInfo();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences(info.PREF_NAME, info.PRIVATE_MODE);
        if (pref.contains(info.KEY_ANSWERS) && pref.contains(info.KEY_TOTALTIME)){
            String answers = pref.getString(info.KEY_ANSWERS, null);
            String time = pref.getString(info.KEY_TOTALTIME, null);

            // Escribimos el textView que muestra la versión de la app
            Context context = activity.getApplicationContext();
            String text = activity.getString(R.string.lastGameData, answers, time);
            textView_lastGameData.setText(text);
        }
        adapter.notifyDataSetChanged();

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
        View.OnClickListener handler_backToMenu = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    ((scoreInterface) activity).backToMenu();
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        };
        button_backToMenu.setOnClickListener(handler_backToMenu);
    }

    private Runnable updateDataSuccess = new Runnable() {
        public void run() {
            list = Storage.getInstance().getListBadges();
            adapter.notifyDataSetChanged();
            Log.e("QuickTest", "Retrieving Score Success");
        }
    };

    private Runnable updateDataError = new Runnable() {
        public void run() {
            Log.d("QuickTest", "updateDataError Score");
        }
    };
}
