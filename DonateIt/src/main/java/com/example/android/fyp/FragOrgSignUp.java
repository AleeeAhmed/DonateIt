package com.example.android.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by HP on 1/19/2018.
 */

public class FragOrgSignUp extends Fragment {
    View view;
    FragmentTransaction transaction;
    String name, registrationNo, email, password, confirmPassword, currentUrl;
    EditText orgName, orgRegNo, orgEmail, orgPassowrd, orgConfirmPassword;
    Button signUp, signIn;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_org_sign_up_screen, container, false);

        intializations();
        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentUrl = preferences.getString("URL", "");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = orgName.getText().toString();
                registrationNo = orgRegNo.getText().toString();
                email = orgEmail.getText().toString();
                password = orgPassowrd.getText().toString().trim();
                confirmPassword = orgConfirmPassword.getText().toString().trim();

                if (!email.isEmpty() && !name.isEmpty() && !registrationNo.isEmpty() &&
                         !confirmPassword.isEmpty() && !password.isEmpty()) {
                    if (password.equals(confirmPassword)) {
                        Log.i("Debug", "password matched");
                        new databaseProcessForOrg().execute(name, registrationNo, email, password);

                    } else {
                        Toast.makeText(getActivity(), "passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Form incomplete.. fill form..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_validation, new FragValidationRegistrationPage()).commit();
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Intent intent = new Intent(getActivity(), ActivityValidationReg.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    private void intializations() {

        orgName = (EditText) view.findViewById(R.id.orgName);
        orgRegNo = (EditText) view.findViewById(R.id.orgRegNo);
        orgEmail = (EditText) view.findViewById(R.id.email);
        orgPassowrd = (EditText) view.findViewById(R.id.password);
        orgConfirmPassword = (EditText) view.findViewById(R.id.cPassword);
        signUp = (Button) view.findViewById(R.id.btnSignUp);
        signIn = (Button) view.findViewById(R.id.btnSignIn);


    }

    private class databaseProcessForOrg extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String name = params[0];
            String registrationNo = params[1];
            String email = params[2];
            String password = params[3];

            String link = currentUrl+"DonateIt/orgSignup.php?email="+email+"&name="+name+
                    "&registrationNo="+registrationNo+"&password="+password;

            Log.i("Debug", "values are"+""+name+""+registrationNo+""+email+""+password);

            try {
                Log.i("Debug", "regestration started");
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream), "iso-8859-1"));


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
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), ActivityValidationReg.class);
                    startActivity(i);
                }else
                {
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }
}


