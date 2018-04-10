package com.example.android.fyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


import java.net.URLEncoder;
import java.sql.Time;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static java.net.Proxy.Type.HTTP;

/**
 * Created by HP on 3/21/2018.
 */

public class FragPostRequestDetails extends Fragment {
    EditText item, quantity, keywords;
    DatePicker datePicker;
    Button post, cancel;
    View x;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *Inflate tab_layout and setup Views.
         */
        x = inflater.inflate(R.layout.post_request_details, null);

        item = (EditText) x.findViewById(R.id.etItem);
        quantity = (EditText) x.findViewById(R.id.etQuantity);
        keywords = (EditText) x.findViewById(R.id.etKeywords);

        datePicker = (DatePicker) x.findViewById(R.id.timePicker);

        post = (Button) x.findViewById(R.id.btnPost);
        cancel = (Button) x.findViewById(R.id.btnCancel);


        Bundle arguments = getArguments();
        final String title = arguments.getString("title");
        final String desc = arguments.getString("desc");


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), OrgNavigationDrawer.class);
                startActivity(i);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemS = item.getText().toString().trim();
                String quantityS = quantity.getText().toString().trim();
                String keywordsS  = keywords.getText().toString();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                JSONArray array=new JSONArray();
                JSONObject object = new JSONObject();
                try {
                    object.put("title",title);
                    object.put("desc",desc);
                    object.put("item",itemS);
                    object.put("quantity",quantityS);
                    object.put("keywords",keywordsS);
                    object.put("year",year);
                    object.put("month",month);
                    object.put("day",day);

                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new databaseProcess().execute(object);
            }
        });
        return x;
    }


    public class databaseProcess extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(JSONObject... data) {
            String response = "";

            Log.i("debug", "database execution started for post request");

            String link = "DonateIt/postRequest.php";
            JSONObject json = data[0];
            URL url = null;
            try {
                url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000 /* milliseconds */);
                httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String postData = json.toString();
                Log.i("debug", "string value for post data is" + " " + json);

                bufferedWriter.write(getPostDataString(json));
                bufferedWriter.flush();
                bufferedWriter.close();

                Log.i("debug", "input stream is not closed yet");
                InputStream is = httpURLConnection.getInputStream();



                String line = "";

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader((is), "iso-8859-1"));

                    StringBuilder sb = new StringBuilder();


                    while ((line = in.readLine()) != null) {

                        Log.i("debug", "string is not null");
                        sb.append(line);
                    }

                    in.close();
                     response =sb.toString().trim();
                    return response;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }




        @Override
        protected void onPostExecute(String result) {

            Log.i("debug", "result is a" + " "+result);

            if(result.equals("Request posted successfully"))
            {
                Toast.makeText(getContext(), "Request posted successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), OrgNavigationDrawer.class);
                startActivity(i);
            }else
            {
                Toast.makeText(getContext(), "Request posted Failed", Toast.LENGTH_SHORT).show();
            }

        }
        }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}

