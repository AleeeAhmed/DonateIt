package com.example.android.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerifyOrg extends AppCompatActivity {

    EditText edVerify,edEmail;
    Button btnVerify;
    SharedPreferences preferences;
    String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_org);


        btnVerify = findViewById(R.id.btnVerify);
        edVerify = findViewById(R.id.edVerficationNumber);
        edEmail = findViewById(R.id.edEmail_verify);

        preferences = getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentUrl = preferences.getString("URL", "");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifyCode = edVerify.getText().toString();
                String email = edEmail.getText().toString();

                if (email.isEmpty() || verifyCode.isEmpty()) {
                    Toast.makeText(VerifyOrg.this, "Fill all fields..", Toast.LENGTH_SHORT).show();
                } else {
                    new databaseProcessForOrg(verifyCode,email).execute();
                }
            }
        });

    }



    private class databaseProcessForOrg extends AsyncTask<String, Void, String> {

        String verify  , email;
        public databaseProcessForOrg(String verifyCode, String email) {
            this.verify = verifyCode;
            this.email = email;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String link = currentUrl+"DonateIt/verify_org.php?email="+email+"&code="+verify;


            try {
                Log.i("Debug", "regestration started");
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream), "UTF-8"));


                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                    Log.i("debug", "response line is" + " "+response+ " null is not the value");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.i("debug", "response after disconnection is" + " "+response);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("debug", "response returned is" + " "+response);

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                if(success.equalsIgnoreCase("true")) {
                    Toast.makeText(VerifyOrg.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerifyOrg.this, ActivityValidationReg.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Toast.makeText(VerifyOrg.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VerifyOrg.this, ActivityValidationReg.class);
        startActivity(intent);
        finish();
    }
}
