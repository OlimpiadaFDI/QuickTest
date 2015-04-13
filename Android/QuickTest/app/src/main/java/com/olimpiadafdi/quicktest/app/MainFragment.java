package com.olimpiadafdi.quicktest.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.olimpiadafdi.quicktest.R;
import com.olimpiadafdi.quicktest.connection.JsonRequest;
import com.olimpiadafdi.quicktest.data.Answer;
import com.olimpiadafdi.quicktest.data.CalculaInsignias;
import com.olimpiadafdi.quicktest.data.Question;
import com.olimpiadafdi.quicktest.data.Storage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static String GETQUESTION = "getQuestion";

    private CalculaInsignias calculaInsignias;

    private Activity activity;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private TextView textView_question;
    private TextView textView_questionCount;
    private TextView textView_countDown;
    private CountDownTimer countDown;
    private static long timestamp;

    public MainFragment(){}

    public interface mainInterface{
        public void backToMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Asociamos los widgets del layout a variables java
        this.textView_question = (TextView) rootView.findViewById(R.id.textView_question);
        this.textView_questionCount = (TextView) rootView.findViewById(R.id.textView_questionCount);
        this.button1 = (Button) rootView.findViewById(R.id.button1);
        this.button2 = (Button) rootView.findViewById(R.id.button2);
        this.button3 = (Button) rootView.findViewById(R.id.button3);
        this.button4 = (Button) rootView.findViewById(R.id.button4);
        this.textView_countDown = (TextView) rootView.findViewById(R.id.textView_countDown);

        Context context = activity.getApplicationContext();
        String countDownString = activity.getString(R.string.countDown, "30");
        textView_countDown.setText(countDownString);

        countDown = new CountDownTimer(30000, 1) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {}
        }.start();

        calculaInsignias = new CalculaInsignias(activity.getApplicationContext());

        this.handleButtons();
        getQuestion();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void handleButtons() {
        View.OnClickListener handler1 = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(1);
            }
        };
        View.OnClickListener handler2 = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(2);
            }
        };
        View.OnClickListener handler3 = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(3);
            }
        };
        View.OnClickListener handler4 = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(4);
            }
        };
        button1.setOnClickListener(handler1);
        button2.setOnClickListener(handler2);
        button3.setOnClickListener(handler3);
        button4.setOnClickListener(handler4);
    }

    public void checkAnswer(int answer){
        Question q = Storage.getInstance().getQuestion();
        Context context = activity.getApplicationContext();

        //Calculamos las insignias:
        calculaInsignias.nuevaPregunta(q, answer,timestamp);

        String text;
        if (answer == q.getCorrect()){
            text = context.getString(R.string.correct_answer);
        }
        else{
            text = context.getString(R.string.incorrect_answer);
        }
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        if (Storage.getInstance().getQuestionsAlreadyAsked().size()<10){
            JsonRequest jsonRequest = new JsonRequest(GETQUESTION, context, updateDataSuccess, updateDataError, null);
            jsonRequest.request();
        }
        else{
            try{
                ((mainInterface) activity).backToMenu();
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }
    }

    public void getQuestion(){
        // Checking Internet connection
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(activity.getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Context context = activity.getApplicationContext();

        boolean conectados = false;
        String text = "";
        int duration = Toast.LENGTH_SHORT;

        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            conectados = true;
            text = context.getString(R.string.connected);
        } else {
            // display error
            text = context.getString(R.string.not_connected);
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //Retrieving the next question

        JsonRequest jsonRequest = new JsonRequest(GETQUESTION, context, updateDataSuccess, updateDataError, null);
        jsonRequest.request();
    }

    private Runnable updateDataSuccess = new Runnable() {
        public void run() {
            Log.d("QuickTest", "success");
            Question q = Storage.getInstance().getQuestion();
            Storage.getInstance().getQuestionsAlreadyAsked().add(q.getId());

            int num = Storage.getInstance().getQuestionsAlreadyAsked().size();
            String questionCount = activity.getString(R.string.questionsCount, num, 10);
            textView_questionCount.setText(questionCount);

            //Visualizar la pregunta
            if (q != null){
                textView_question.setText(q.getQuestion());

                Answer a1 = q.getAnswers().get(0);
                button1.setText(a1.getAnswer());
                if (a1.getId() == q.getCorrect())
                    q.setCorrect(1);

                Answer a2 = q.getAnswers().get(1);
                button2.setText(a2.getAnswer());
                if (a2.getId() == q.getCorrect())
                    q.setCorrect(2);

                Answer a3 = q.getAnswers().get(2);
                button3.setText(a3.getAnswer());
                if (a3.getId() == q.getCorrect())
                    q.setCorrect(3);

                Answer a4 = q.getAnswers().get(3);
                button4.setText(a4.getAnswer());
                if (a4.getId() == q.getCorrect())
                    q.setCorrect(4);

                Storage.getInstance().setQuestion(q);

                countDown.cancel();
                countDown = new CountDownTimer(30000, 1) {
                    public void onTick(long millisUntilFinished) {
                        timestamp = 30000 - millisUntilFinished;
                        Context context = activity.getApplicationContext();
                        String countdown = activity.getString(R.string.countDown, Long.toString(millisUntilFinished / 1000));
                        textView_countDown.setText(countdown);
                    }
                    public void onFinish() {
                        Context context = activity.getApplicationContext();
                        String text = activity.getString(R.string.timesUp);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        try{
                            ((mainInterface) activity).backToMenu();
                        }catch (ClassCastException e){
                            e.printStackTrace();
                        }
                    }
                }.start();

                //AlertDialogManager d = new AlertDialogManager();
                //d.showAlertDialog(getApplicationContext(), "Correcto", "Muy bien",true);
            }
            else{
                Log.e("QuickTest", "Question is null");
            }
        }
    };
    private Runnable updateDataError = new Runnable() {
        public void run() {
            Log.d("QuickTest", "updateDataError");
        }
    };
}
