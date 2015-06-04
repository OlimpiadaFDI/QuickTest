package com.olimpiadafdi.quicktest.data;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class CalculaInsignias {

    private static final int NUM_TIPOS = 10;

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

    public CalculaInsignias(Context context){
        this.context = context;
        this.cont = 0;
    }

    public void nuevaPregunta(Question q, int r) {
        this.question = q;
        this.resp = r;

        // Nota: El tipo de pregunta es un entero. Comienzan desde el 1
        tipos = new ArrayList<Integer>();
        for (int i=1; i<=NUM_TIPOS; i++)
            tipos.add(0);
        calculaInsignias();
    }

    public void calculaInsignias(){
        int tipo = question.getTipo();
        if (resp == question.getCorrect()){
            cont++;
            tipos.set(tipo, tipos.get(tipo) + 1);
        }
        if (cont == 10){
            triggerInsignia_01();   // REY: Todas acertadas
        }
        if (cont == 7){
            triggerInsignia_02();   // EL 7 MAGICO: 7 aciertos
        }

        for (int i=1; i<=NUM_TIPOS; i++){    // Acertar 3 preguntas de un mismo tipo
            int num = tipos.get(tipo);
            if (num==3){
                triggerInsignia_03(tipo);
            }
        }
    }

    public void triggerInsignia_01(){
        // Insignia 01:
        // REY: Todas acertadas
        String text = "¡Desbloqueada la Insignia 'REY'!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void triggerInsignia_02(){
        // Insignia 02:
        // EL 7 MAGICO: 7 aciertos
        String text = "¡Desbloqueada la Insignia 'EL 7 MAGICO'!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void triggerInsignia_03(int tipo){
        // Insignia 04-09:
        // Accertar 3 preguntas de un tipo
        String text = "¡Desbloqueada la Insignia 'GENIO en "+tipo+"'!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
}
