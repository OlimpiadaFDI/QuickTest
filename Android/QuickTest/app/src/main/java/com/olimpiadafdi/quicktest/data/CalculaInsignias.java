package com.olimpiadafdi.quicktest.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.olimpiadafdi.quicktest.connection.JsonRequest;

import java.util.ArrayList;

public class CalculaInsignias {

    private static final int NUM_TIPOS = 6;
    private static String UNLOCKBADGE = "unlockBadge";

    private Question question;
    private int resp;

    /*
    Quicktest.
    1) REY --> Todas acertadas (esta creo que era de cajón)
    2) EL 7 MÁGICO -->Mínimo de 7 acertadas (por poner..)
    3) RAPIDO Y EFICAZ --> 5 aciertos y acabado en menos de 30 segundos
        (el tiempo total será de 60 y 10 preguntas, pues es un QUICK test..)
    4) GENIO EN PROGRAMACION --> Acertar 3 preguntas del tipo 1.
    5,6,7,8,9) genio en x! (las otras categorias, lo mismo que la 4, las
        categorías que yo he marcado son base de datos, ingeniería del software,
        redes, computadores y sistemas operativos ATENCION! ESTO SI ES MODIFICABLE,
        PERO AVISADLO CON TIEMPO
     */
    Context context;
    private int cont;
    private ArrayList<Integer> tipos;
    private ArrayList<Long> timeStamp;
    String text;

    public CalculaInsignias(Context context){
        this.context = context;
        this.cont = 0;

        // Nota: El tipo de pregunta es un entero. Comienzan desde el 1
        tipos = new ArrayList<Integer>();
        for (int i=1; i<=NUM_TIPOS; i++)
            tipos.add(0);

        timeStamp = new ArrayList<Long>();
        for (int i=1; i<=10; i++)
            timeStamp.add(Long.valueOf("-1"));
    }

    public void nuevaPregunta(Question q, int r, long ts) {
        this.question = q;
        this.resp = r;

        int tipo = question.getTipo();
        tipo -= 1;

        if (resp == question.getCorrect()){
            tipos.set(tipo, tipos.get(tipo) + 1);
            timeStamp.set(cont, ts);
            Log.d("QuickTest - Timestamp", "Timestamp: "+ ts);
        }
        cont++;

        calculaInsignias(tipo);
    }

    public int getCorrectas(){
        int correctas = 0;
        for (int i = 0; i < NUM_TIPOS; i++) {
            correctas += tipos.get(i);
        }
        return correctas;
    }
    public long getTiempoTotal(){
        long t = 0;
        for (int i = 0; i <10; i++) {
            if (timeStamp.get(i)>0) {
                t += timeStamp.get(i);
            }
        }
        t = t/1000;
        return t;
    }

    public void calculaInsignias(int tipo){
        int correctas = getCorrectas();
        if (correctas == 10){
            triggerInsignia_01();   // REY: Todas acertadas
        }
        if (correctas == 7){
            triggerInsignia_02();   // EL 7 MAGICO: 7 aciertos
        }
        if (correctas == 5){        // RAPIDO Y EFICAZ: 5 aciertos en 30 segundos
            for (int i = 0; i < 10-5; i++) {
                long ts1 = timeStamp.get(i);
                long ts2 = timeStamp.get(i+1);
                long ts3 = timeStamp.get(i+2);
                long ts4 = timeStamp.get(i+3);
                long ts5 = timeStamp.get(i+4);

                if (ts1>0 && ts2>0 && ts3>0 && ts4>0 && ts5>0) {
                    if (ts1 + ts2 + ts3 + ts4 + ts5 <= 30000) {
                        triggerInsignia_03();
                    }
                }
            }
        }
        int num = tipos.get(tipo);     // Acertar 3 preguntas de un mismo tipo
        if (num==3){
            triggerInsignia_04(tipo);
        }
    }

    public void triggerInsignia_01(){
        // Insignia 01:
        // REY: Todas acertadas

        String s[] = {Storage.getInstance().getNick(), Integer.toString(1)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, context, updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'REY'!";
    }

    public void triggerInsignia_02(){
        // Insignia 02:
        // EL 7 MAGICO: 7 aciertos

        String s[] = {Storage.getInstance().getNick(), Integer.toString(2)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, context, updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'EL 7 MAGICO'!";
    }

    public void triggerInsignia_03(){
        // Insignia 03:
        // RAPIDO Y EFICAZ: 5 aciertos en 30 segundos

        String s[] = {Storage.getInstance().getNick(), Integer.toString(3)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, context, updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'RAPIDO Y EFICAZ'!";
    }

    public void triggerInsignia_04(int tipo){
        // Insignia 04-09:
        // Accertar 3 preguntas de un tipo

        tipo += 1;
        String tipoGenio = null;
        int cod = 0;
        switch (tipo) {
            case 1:  tipoGenio = "Programación";
                cod =4;
                break;
            case 2:  tipoGenio = "IS";
                cod =5;
                break;
            case 3:  tipoGenio = "Base de Datos";
                cod =6;
                break;
            case 4:  tipoGenio = "Redes";
                cod =7;
                break;
            case 5:  tipoGenio = "Sistemas Operativos";
                cod =8;
                break;
            case 6:  tipoGenio = "Computadores";
                cod =9;
                break;
        }

        String s[] = {Storage.getInstance().getNick(), Integer.toString(cod)};
        JsonRequest jsonRequest = new JsonRequest(UNLOCKBADGE, context, updateDataSuccess, updateDataError, s);
        jsonRequest.request();

        text = "¡Desbloqueada la Insignia 'GENIO en "+tipoGenio+"'!";
    }

    private Runnable updateDataSuccess = new Runnable() {
        public void run() {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Log.d("QuickTest", "Badge unlocked" + text);
        }
    };

    private Runnable updateDataError = new Runnable() {
        public void run() {
            Log.d("QuickTest", "Error unlocking Badge");
        }
    };
}
