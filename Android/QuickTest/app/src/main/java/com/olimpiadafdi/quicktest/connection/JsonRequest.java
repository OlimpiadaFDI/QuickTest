package com.olimpiadafdi.quicktest.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.olimpiadafdi.quicktest.data.Badge;
import com.olimpiadafdi.quicktest.data.Question;
import com.olimpiadafdi.quicktest.data.Storage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class JsonRequest {

    private static String GETQUESTION = "getQuestion";
    private static String url_getQuestion = "http://ssii2014.e-ucm.es:80/OlimpiadaFDIServices/rest/quickTest/preguntaAleatoria";

    private static String LOGIN = "login";
    private static String url_login = "http://ssii2014.e-ucm.es:80//OlimpiadaFDIServices/rest/insignias/logginUsuario";

    private static String UNLOCKBADGE = "unlockBadge";
    private static String url_unlockBadge = "http://ssii2014.e-ucm.es:80//OlimpiadaFDIServices/rest/insignias/asignarInsigniaAUsuario";

    private static String SHOWBADGES = "showBadges";
    private static String url_showBadges = "http://ssii2014.e-ucm.es//OlimpiadaFDIServices/rest/insignias/insigniasUsuario/";

    private String uri;
    private String purpose;
    private String[] data;
    private Runnable updateDataSuccess;
    private Runnable updateDataError;

    public JsonRequest(String purpose, Context context, Runnable updateDataSuccess,
                       Runnable updateDataError, String[] data) {

        if (purpose.equalsIgnoreCase(GETQUESTION))
            this.uri = url_getQuestion;

        else if (purpose.equalsIgnoreCase(LOGIN))
            this.uri = url_login;

        else if (purpose.equalsIgnoreCase(UNLOCKBADGE))
            this.uri = url_unlockBadge;

        else if (purpose.equalsIgnoreCase(SHOWBADGES))
            this.uri = url_showBadges;

        this.purpose = purpose;
        this.data = data;
        this.updateDataSuccess = updateDataSuccess;
        this.updateDataError = updateDataError;

    }

    public void request() {
        String input[] = new String[2];
        input[0] = this.uri;
        input[1] = this.purpose;
        new DownloadDataTask().execute(input);
    }

    private class DownloadDataTask extends AsyncTask<String, Integer, Boolean> {

        // JSON-Parser
        @Override
        protected Boolean doInBackground(String... input) {
            String uri = input[0];
            String purpose = input[1];
            boolean result = true;

            // Login
            if (purpose.equalsIgnoreCase(LOGIN)){
                Log.i("QuickTest", "Do in background - " + LOGIN);
                JSONObject json = writeJSON_login();
                String response = POST(uri, json.toString());
                if (response == null) {
                    result = false;
                }
                Log.i("QuickTest", "Parsing - " + LOGIN);
                result = HtmlParser.checkLogin(response);
            }

            // Get Question
            else if (purpose.equalsIgnoreCase(GETQUESTION)) {
                Log.i("QuickTest", "Do in background - " + GETQUESTION);
                String response = GET(uri);
                if (response == null) {
                    result = false;
                }
                Log.i("QuickTest", "Parsing - " + GETQUESTION);
                Question q = HtmlParser.parseQuestion(response);
                Storage.getInstance().setQuestion(q);
            }

            if (purpose.equalsIgnoreCase(UNLOCKBADGE)){
                Log.i("QuickTest", "Do in background - " + UNLOCKBADGE);
                JSONObject json = writeJSON_unlockBadge();
                String response = POST(uri, json.toString());
                if (response == null) {
                    result = false;
                }
                Log.i("QuickTest", "Parsing - " + UNLOCKBADGE);
                //result = HtmlParser.checkLogin(response);
            }

            if (purpose.equalsIgnoreCase(SHOWBADGES)){
                Log.i("QuickTest", "Do in background - " + SHOWBADGES);
                uri += Storage.getInstance().getNick();
                String response = GET(uri);
                if (response == null) {
                    result = false;
                }
                result = HtmlParser.parseListBadges(response);
                Log.i("QuickTest", "Parsing - " + SHOWBADGES);
            }
            return result;
        }

        private String GET(String url) {
            String output = null;
            // Create a new HttpClient and Post Header
            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 10000);
            HttpConnectionParams.setSoTimeout(myParams, 10000);
            HttpClient httpClient = new DefaultHttpClient(myParams);

            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse;

            try {
                httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null) {

                    /*InputStream instream = httpEntity.getContent();

                    BufferedInputStream bis = new BufferedInputStream(instream);
                    ByteArrayBuffer baf = new ByteArrayBuffer(50);

                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    // publishProgress(1);

                    // Convert the Bytes read to a String.
                    String html = new String(baf.toByteArray());
                    return html;
                    */

                    output = EntityUtils.toString(httpEntity);
                }
            } catch (ClientProtocolException e) {
                Log.e("QuickTest", "There was a protocol based error", e);
            } catch (IOException e) {
                Log.e("QuickTest", "There was an IO Stream related error", e);
            }
            publishProgress();
            return output;
        }

        private String POST(String url, String json) {
            String output = null;

            // Create a new HttpClient and Post Header
            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 10000);
            HttpConnectionParams.setSoTimeout(myParams, 10000);
            HttpClient httpClient = new DefaultHttpClient(myParams);

            try {

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-type", "application/json");

                StringEntity se = new StringEntity(json);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    output = EntityUtils.toString(httpEntity);
                }
            } catch (ClientProtocolException e) {
                Log.e("QuickTest", "There was a protocol based error", e);
            } catch (IOException e) {
                Log.e("QuickTest", "There was an IO Stream related error", e);
            }
            publishProgress();
            return output;
        }

        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
            // progress[0]
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                updateDataSuccess.run();
            } else {
                updateDataError.run();
            }
        }
    }

    public JSONObject writeJSON_login() {
        JSONObject object = new JSONObject();
        try {
            object.put("nombre", data[0]);
            object.put("pass", data[1]);

            /*object.put("nombre", "Jack Hack");
            object.put("score", new Integer(200));
            object.put("current", new Double(152.32));
            object.put("nickname", "Hacker");*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject writeJSON_unlockBadge() {
        JSONObject object = new JSONObject();
        try {
            object.put("nombreUsuario", data[0]);
            object.put("idInsignia", data[1]);

            /*object.put("nombre", "Jack Hack");
            object.put("score", new Integer(200));
            object.put("current", new Double(152.32));
            object.put("nickname", "Hacker");*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
