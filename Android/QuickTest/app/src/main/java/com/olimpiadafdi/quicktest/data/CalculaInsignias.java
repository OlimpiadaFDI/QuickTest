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
    private int timeLeft;
    String text;

    public CalculaInsignias(Context context){
        this.context = context;
        this.cont = 0;

        // Nota: El tipo de pregunta es un entero. Comienzan desde el 1
        tipos = new ArrayList<Integer>();
        for (int i=1; i<=NUM_TIPOS; i++)
            tipos.add(0);
    }

    public void nuevaPregunta(Question q, int r) {
        this.question = q;
        this.resp = r;
        calculaInsignias();
    }

    public void calculaInsignias(){
        int tipo = question.getTipo();
        tipo -= 1;
        if (resp == question.getCorrect()){
            cont++;
            tipos.set(tipo, tipos.get(tipo) + 1);
        }

        int correctas = 0;
        for (int i = 0; i < NUM_TIPOS; i++) {
            correctas += tipos.get(i);
        }
        if (correctas == 10){
            triggerInsignia_01();   // REY: Todas acertadas
        }
        if (correctas == 7){
            triggerInsignia_02();   // EL 7 MAGICO: 7 aciertos
        }
        int num = tipos.get(tipo);     // Acertar 3 preguntas de un mismo tipo
        if (num==3){
            triggerInsignia_03(tipo);
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

    public void triggerInsignia_03(int tipo){
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

        text = "¡Desbloqueada la Insignia 'GENIO tipo "+tipoGenio+"'!";
    }

    private Runnable updateDataSuccess = new Runnable() {
        public void run() {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Log.d("QuickTest", "Badge unlocked");
        }
    };

    private Runnable updateDataError = new Runnable() {
        public void run() {
            Log.d("QuickTest", "Error unlocking Badge");
        }
    };
}
