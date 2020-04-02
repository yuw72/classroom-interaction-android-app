package com.horizon.myapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpSendRequest extends AsyncTask<String, Void, String> {
    static final int READ_TIMEOUT = 15000;
    static final int CONNECTION_TIMEOUT = 15000;
    static final String REUQEST_METHOD = "POST";
    ProgressDialog pregressDialog;
    private WeakReference<Context> contextRef;
    private ProgressDialog progressDialog;

    public HttpSendRequest(Context mContext){
        contextRef = new WeakReference<>(mContext);
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // display a progress dialog for good user experiance
        progressDialog = new ProgressDialog(contextRef.get());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params){
        String result="";
        String inputLine;
        String url = params[0];
        String data =params[1];
        JSONObject postData = new JSONObject();

        try{
            postData.put("firstname", "yuchao");
            URL myUrl = new URL(url);
            HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod(REUQEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(postData.toString());
            bufferedWriter.flush();

            // get the string from the input stream
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            result = stringBuilder.toString();
            bufferedWriter.close();
        }catch(Exception e){

        }
        Log.e("Post data: ",postData.toString());

        return result;
    }

    protected void onPostExecute(String result){
        // dismiss the progress dialog after receiving data from API
        progressDialog.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String val = jsonObject.getString("name");
//            MainActivity.showSendRequest.setText(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
