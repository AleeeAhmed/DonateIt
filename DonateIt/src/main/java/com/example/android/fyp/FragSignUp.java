package com.example.android.fyp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                email = emailE.getText().toString();
                userName = userNameE.getText().toString();
                mobileNo =  mobileNoE.getText().toString();
                country = countryE.getText().toString();
                city = cityE.getText().toString();
                confirmPassword = cPasswordE.getText().toString();
                password = passwordE.getText().toString();

                if (!email.isEmpty() || !userName.isEmpty() || !mobileNo.isEmpty() ||
                        !country.isEmpty() || !city.isEmpty() || !confirmPassword.isEmpty() || !password.isEmpty()) {
                    if (password.equals(confirmPassword)) {
                        new databaseProcessForDonor().execute(email, userName, mobileNo, country, city, password);
                    } else {
                        Toast.makeText(getContext(), "Passwords does not matches", Toast.LENGTH_SHORT).show();
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

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String email = params[0];
            String username = params[1];
            String mobileNo = params[2];
            String country = params[3];
            String city = params[4];
            String password = params[5];

            String link = currentUrl+"DonateIt/donorSignup.php?email="+email+"&username="+username+
                    "&mobileNo="+mobileNo+"&country="+country+"&city="+city+"&password="+password;

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
                Intent i = new Intent(getActivity(), ActivityValidationReg.class);
                startActivity(i);
                getActivity().finish();

            }else
            {
                Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
