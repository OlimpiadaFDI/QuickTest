package com.olimpiadafdi.quicktest.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.olimpiadafdi.quicktest.data.Question;
import com.olimpiadafdi.quicktest.data.Storage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsonRequest {

    private String uri;
    private String reason;
    private Runnable updateDataSuccess;
    private Runnable updateDataError;

    public JsonRequest(String uri, Context context, Runnable updateDataSuccess,
                       Runnable updateDataError, String reason) {
        this.uri = uri;
        this.updateDataSuccess = updateDataSuccess;
        this.updateDataError = updateDataError;
        this.reason = reason;
    }

    public void request() {
        String input[] = new String[2];
        input[0] = this.uri;
        input[1] = this.reason;
        new DownloadDataTask().execute(input);
    }

    private class DownloadDataTask extends AsyncTask<String, Integer, Boolean> {

        // JSON-Parser
        @Override
        protected Boolean doInBackground(String... input) {
            Log.e("QuickTest", "Do in background");
            String html = loadData(input[0]);
            if (html == null) {
                return false;
            }

            if (input[1].equalsIgnoreCase("getQuestion")) {
                Log.e("QuickTest", "parsing the question");
                Question q = HtmlParser.parseQuestion(html);
                Storage.getInstance().setQuestion(q);
            }

            return true;
        }

        private String loadData(String url) {
            HttpClient httpClient = new DefaultHttpClient();
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

                    String output = EntityUtils.toString(httpEntity);
                    return output;
                }
            } catch (ClientProtocolException e) {
                Log.e("QuickTest", "There was a protocol based error", e);
            } catch (IOException e) {
                Log.e("QuickTest", "There was an IO Stream related error", e);
            }
            publishProgress();
            return null;
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

    public JSONObject writeJSON_question() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", "Jack Hack");
            object.put("score", new Integer(200));
            object.put("current", new Double(152.32));
            object.put("nickname", "Hacker");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
        return object;
    }

    public JSONObject writeJSON_score() {
        JSONObject object = new JSONObject();

        return object;
    }
}
