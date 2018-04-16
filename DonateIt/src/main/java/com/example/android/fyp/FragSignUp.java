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
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

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

public class FragSignUp extends Fragment {
    View view;
    Button signUp,signUpOrg;
    EditText emailE, userNameE, mobileNoE,countryE, cityE, cPasswordE ,passwordE;
    String email, userName, mobileNo, country, city, password, confirmPassword, currentUrl;
    SharedPreferences preferences;
    int randomPIN;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){

        view = inflater.inflate(R.layout.frag_signup, container, false);

        intializations();

        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentUrl = preferences.getString("URL", "");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailE.getText().toString().trim();
                userName = userNameE.getText().toString().trim();
                mobileNo =  mobileNoE.getText().toString().trim();
                country = countryE.getText().toString().trim();
                city = cityE.getText().toString().trim();
                confirmPassword = cPasswordE.getText().toString().trim();
                password = passwordE.getText().toString().trim();

                if (!email.isEmpty() && !userName.isEmpty() && !mobileNo.isEmpty() &&
                        !country.isEmpty() && !city.isEmpty() && !confirmPassword.isEmpty() && !password.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (password.equals(confirmPassword)) {
                            new databaseProcessForDonor().execute(email, userName, mobileNo, country, city, password);
                        } else {
                            Toast.makeText(getContext(), "Passwords does not matches", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                            Toast.makeText(getActivity(), "Email format is not correct", Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(getActivity(), "Form is incomplete.. All Fields are mandatory..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_validation, new FragOrgSignUp()).addToBackStack(null).commit();
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                            .setIcon(R.drawable.ic_launcher_foreground)
                            .setTitle("Closing DonateIt")
                            .setMessage("Are you sure you want to exit?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void intializations() {
        emailE = (EditText) view.findViewById(R.id.etEmail);
        userNameE = (EditText) view.findViewById(R.id.etUserName);
        mobileNoE = (EditText) view.findViewById(R.id.etMobileNumber);
        countryE = (EditText) view.findViewById(R.id.etCountry);
        cityE = (EditText) view.findViewById(R.id.etCity);
        cPasswordE= (EditText) view.findViewById(R.id.etCPass);
        passwordE = (EditText) view.findViewById(R.id.etPass);
        signUp = (Button) view.findViewById(R.id.btnSignUp);
        signUpOrg = (Button) view.findViewById(R.id.btnSignup_org);
    }

    private class databaseProcessForDonor extends AsyncTask<String, Void, String> {
        SharedPreferences preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        String currentUrl = preferences.getString("URL", "");
        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String email = params[0];
            String username = params[1];
            String mobileNo = params[2];
            String country = params[3];
            String city = params[4];
            String password = params[5];

            randomPIN = (int)(Math.random()*9000)+1000;

            String link = currentUrl+"DonateIt/donorSignup.php?email="+email+"&username="+username+
                    "&mobileNo="+mobileNo+"&country="+country+"&city="+city+"&password="+password+"&verificationNum="+randomPIN;

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

            if(result.contains("Successful"))
            {
                Toast.makeText(getActivity(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();

                BackgroundMail.newBuilder(getActivity())
                        .withUsername("waqarfyp@gmail.com")
                        .withPassword("PAKISTAN@123")
                        .withMailto(email)
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Email Verification DonateIt")
                        .withBody("Please verify your app using  following data..\n\nEnter this number to verify it..\n\n"+randomPIN)
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(), "Verification code is sent to your email..", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                Toast.makeText(getActivity(), "Something went wrong.. Couldn't send you verification email now..", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .send();


                Intent i = new Intent(getActivity(), ActivityValidationReg.class);
                startActivity(i);

            }else
            {
                Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
