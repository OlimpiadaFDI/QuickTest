package com.olimpiadafdi.quicktest.connection;

import android.util.Log;

import com.olimpiadafdi.quicktest.data.Answer;
import com.olimpiadafdi.quicktest.data.Question;
import com.olimpiadafdi.quicktest.data.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HtmlParser {

    public static String CODE = "code";
    public static String MESSAGE = "message";
    public static String RESULT = "result";

    public static boolean checkLogin(String html){
        Boolean b = false;
        try{
            JSONObject jObj = new JSONObject(html);
            int code = jObj.getInt(CODE);
            String message = jObj.getString(MESSAGE);
            String result = jObj.getString(RESULT);

            if (code==0){
                b = true;
            }
            else{
                Storage.getInstance().setResultErrorLogin(result);
            }
            Log.i("QuickTest", result);
        } catch (JSONException e) {
            Log.e("QuickTest", "There was an error parsing the JSON", e);
        }
    return b;
    }

    public static Question parseQuestion(String html){
        Question q = null;
        try {
            JSONObject jObj = new JSONObject(html);
            int code = jObj.getInt(CODE);
            String message = jObj.getString(MESSAGE);
            JSONObject result = jObj.getJSONObject(RESULT);

            if (code==0){
                JSONObject pregunta = result.getJSONObject("pregunta");
                String p = pregunta.getString("pregunta");
                int idPregunta = pregunta.getInt("idPregunta");
                int respuestaCorrecta = pregunta.getInt("respuestaCorrecta");
                int tipo = pregunta.getInt("tipo");

                q = new Question(p, idPregunta, respuestaCorrecta, tipo);

                JSONArray respuestas = result.getJSONArray("respuestas");
                for (int i=0; i<respuestas.length(); i++){
                    JSONObject r = respuestas.getJSONObject(i);
                    String respuesta = r.getString("respuesta");
                    int idRespuesta = r.getInt("idRespuesta");
                    q.getAnswers().add(new Answer(respuesta, idRespuesta));
                }
            }
            else{
                Log.e("QuickTest", message);
            }
            return q;
        } catch (JSONException e) {
            Log.e("QuickTest", "There was an error parsing the JSON", e);
        }
        return null;
    }
}
