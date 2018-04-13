package com.example.android.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragRequestsDetails extends AppCompatActivity {

    String id, title, desc, progress, target;
    TextView tvTitle, tvDesc, tvProgress, tvTarget;
    EditText edOfferAmount;
    Button btnSendOffer;
    SharedPreferences preferences;
    String currentURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_requests_details);

        intitalizations();

        id = getIntent().getStringExtra("ID");

        new dataFetch().execute();

        btnSendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edOfferAmount.getText().toString().trim().isEmpty()) {
                    int ofrAmount = Integer.parseInt(edOfferAmount.getText().toString());
                    if (ofrAmount > (Integer.parseInt(target) - Integer.parseInt(progress))) {
                        Toast.makeText(FragRequestsDetails.this, "we only need "
                                + (Integer.parseInt(target) - Integer.parseInt(progress)) + " more.", Toast.LENGTH_SHORT).show();
                    } else {
                        new dataSend().execute();
                    }
                } else {
                    Toast.makeText(FragRequestsDetails.this, "Enter Offering Amount..", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    class dataFetch extends AsyncTask<String,Void,String> {

        SharedPreferences preferences1 = getSharedPreferences("HiddenUrl", MODE_PRIVATE);
        String currentURL = preferences1.getString("URL", "");

        String requestUrl = currentURL+"DonateIt/getRequestDetails.php?reqId="+id;
        String JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(requestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int c = httpURLConnection.getResponseCode();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING).append("\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }



        @Override
        protected void onPostExecute(String result) {


            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                if (success.equalsIgnoreCase("true")) {
                    String title = jsonObject.getJSONArray("results").getJSONObject(0).getString("title");
                    String desc = jsonObject.getJSONArray("results").getJSONObject(0).getString("description");
                    String progress = jsonObject.getJSONArray("results").getJSONObject(0).getString("progress");
                    String target = jsonObject.getJSONArray("results").getJSONObject(0).getString("goal");

                    tvTitle.setText(title);
                    tvDesc.setText(desc);
                    tvProgress.setText(progress);
                    tvTarget.setText(target);

                } else {
                    edOfferAmount.setText("");
                    Toast.makeText(FragRequestsDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    class dataSend extends AsyncTask<String,Void,String> {

        SharedPreferences preferences1 = getSharedPreferences("HiddenUrl", MODE_PRIVATE);
        String currentURL = preferences1.getString("URL", "");
        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        String userId = preferences.getString("UserId", "");
        String requestUrl = currentURL+"DonateIt/requestOffer.php?requestId="+id+"&senderId="+userId+
                "&postName="+tvTitle.getText().toString()+"&offeredQuantity="+edOfferAmount.getText().toString();
        String JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(requestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int c = httpURLConnection.getResponseCode();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING).append("\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }



        @Override
        protected void onPostExecute(String result) {


            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                if (success.equalsIgnoreCase("true")) {
                    edOfferAmount.setText("");
                    Toast.makeText(FragRequestsDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    edOfferAmount.setText("");
                    Toast.makeText(FragRequestsDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void intitalizations() {
        tvTitle = findViewById(R.id.tvTitle_RequestDetails);
        tvDesc = findViewById(R.id.tvDesc_RequestDetails);
        tvProgress = findViewById(R.id.tvProgress_RequestDetails);
        tvTarget = findViewById(R.id.tvTarget_RequestDetails);

        edOfferAmount = findViewById(R.id.tvOfferAmount_RequestDetails);
        btnSendOffer = findViewById(R.id.btnSendOffer);
    }
}
