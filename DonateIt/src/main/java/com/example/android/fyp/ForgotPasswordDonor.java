package com.example.android.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class ForgotPasswordDonor extends Fragment {

    View view;
    EditText edEmail;
    Button btnForgot;
    String currentUrl;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_forgot_password_donor, container, false);


        intializations();

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    new databaseProcessForOrg(email).execute();
                }
            }
        });





        return view;
    }


    private void intializations() {


        edEmail = view.findViewById(R.id.edEmail_forgotPassword);
        btnForgot = view.findViewById(R.id.btnForgotPassword);



    }

    private class databaseProcessForOrg extends AsyncTask<String, Void, String> {

        SharedPreferences preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        String currentUrl = preferences.getString("URL", "");
        String verify  , email;
        public databaseProcessForOrg(String email) {
            this.email = email;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String link = currentUrl+"DonateIt/forgotPasswordDonor.php?email="+email;


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
                if(success.equalsIgnoreCase("true"))
                {
                    final String password = jsonObject.getString("password");


                    BackgroundMail.newBuilder(getActivity())
                            .withUsername("waqarfyp@gmail.com")
                            .withPassword("PAKISTAN@123")
                            .withMailto(email)
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withSubject("Forgot Password DonateIt")
                            .withBody("Your Login Details are below..\nPassword: " + password)
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getActivity(), "Forgot Password is sent..", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                @Override
                                public void onFail() {
                                    Toast.makeText(getActivity(), "Something went wrong.. Couldn't send you email now..", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .send();

                    Intent i = new Intent(getActivity(), ActivityValidationReg.class);
                    startActivity(i);
                }else
                {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }

}
