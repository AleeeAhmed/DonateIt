package com.example.android.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AliAhmed on 15-Apr-18.
 */

public class ActivityPostRequest extends AppCompatActivity {

    String title, desc, item, quantity, keywords;
    EditText edItem, edQuantity, edKeywords;
    DatePicker datePicker;
    Button btnPost, btnCancel;
    SharedPreferences preferences;
    JSONObject jsonObject;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_request_details);

        intializzations();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = edItem.getText().toString().trim();
                quantity = edQuantity.getText().toString().trim();
                keywords = edKeywords.getText().toString().trim();
                title = getIntent().getStringExtra("Title");
                desc = getIntent().getStringExtra("Desc");
                if (item.isEmpty() || quantity.isEmpty() || keywords.isEmpty()) {
                    Toast.makeText(ActivityPostRequest.this, "Fill above fields..", Toast.LENGTH_SHORT).show();
                } else {
                    new databaseProcess().execute();
                }
            }
        });
    }

    private void intializzations() {
        edItem = findViewById(R.id.etItem_PostRequest);
        edKeywords = findViewById(R.id.etKeywords_PostRequest);
        edQuantity = findViewById(R.id.etQuantity_PostRequest);
        datePicker = findViewById(R.id.timePicker_PostRequest);
        btnCancel = findViewById(R.id.btnCancel_PostRequest);
        btnPost = findViewById(R.id.btnPost_PostRequest);
    }

    public class databaseProcess extends AsyncTask<JSONObject, Void, String> {
        SharedPreferences preferences = getSharedPreferences("HiddenUrl", MODE_PRIVATE);
        String currentUrl = preferences.getString("URL", "");
        SharedPreferences preferences1 = getSharedPreferences("Login", MODE_PRIVATE);
        String userId = preferences1.getString("UserId", "");
        int day = datePicker.getDayOfMonth(), month = datePicker.getMonth(), year = datePicker.getYear();
        String date = year + "-" + month + "-" + day;
        @Override
        protected String doInBackground(JSONObject... data) {
            String response = "";

            String link = currentUrl + "DonateIt/postRequest.php?" +
                    "title=" + title + "&desc=" + desc + "&itemName=" + item + "&itemQuantity=" + quantity +
                    "&keywords="+keywords+"&endDate="+date+"userId="+userId;

            URL url = null;
            try {
                url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                InputStream is = httpURLConnection.getInputStream();
                String line = "";

                BufferedReader in = new BufferedReader(new InputStreamReader((is), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                while ((line = in.readLine()) != null) {

                    sb.append(line).append("\n");
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

            try {

                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");

                if(success.equalsIgnoreCase("true"))
                {
                    Toast.makeText(ActivityPostRequest.this, "Request posted successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ActivityPostRequest.this, OrganizationDashboard.class);
                    startActivity(i);
                }else
                {
                    Toast.makeText(ActivityPostRequest.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
