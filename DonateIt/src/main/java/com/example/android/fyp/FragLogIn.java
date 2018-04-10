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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by HP on 1/27/2018.
 */

public class FragLogIn extends Fragment {
    View view;
    Button signIn;
    Button signInFb;
    Button signInOrg;
    EditText emailE,passwordE;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String currentUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_sign_in_screen, null);

        intializations();

        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentUrl = preferences.getString("URL","");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailE.getText().toString().trim();
                String password = passwordE.getText().toString().trim();

                new databaseProcessForDonor().execute(email, password);
            }
        });

        signInOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailE.getText().toString();
                String password = passwordE.getText().toString();
                new databaseProcessForOrg().execute(email, password);
            }
        });
        return view;
    }

    private void intializations() {
        emailE = (EditText) view.findViewById(R.id.etEmail);
        passwordE = (EditText) view.findViewById(R.id.etPass);
        signIn = (Button) view.findViewById(R.id.btnSingIn);
        signInOrg = (Button) view.findViewById(R.id.btnSingInOrg) ;
    }


    private class databaseProcessForDonor extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String email = params[0];
            String password = params[1];
            String phpurl = currentUrl+"Donateit/donorLogin.php?email="+email+"&password="+password;

            try {
                URL url = new URL(phpurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.connect();
                int c = httpURLConnection.getResponseCode();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream), "UTF-8"));


                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                    Log.i("debug", "response is" + " "+response);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.i("debug", "response is" + " "+response);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return response.trim();

        }


        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");

                if(success.equalsIgnoreCase("true"))
                {
                    preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.clear();
                    editor.putString("Username", jsonObject.getString("email"));
                    editor.putString("UserId", jsonObject.getString("id"));
                    editor.apply();
                    Toast.makeText(getActivity(),"Logged in Successfully..",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), DonorDashboard.class);
                    startActivity(intent);
                    getActivity().finish();
                }else
                {
                    Toast.makeText(getActivity(),"Email or Password is wrong",Toast.LENGTH_SHORT).show();
                    passwordE.setText("");
                    emailE.setText("");
                }
            } catch (Exception ignored) {

            }
        }
    }

    private class databaseProcessForOrg extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String email = params[0];
            String password = params[1];

            String phpurl = currentUrl+"DonateIt/orgSignIn.php?email="+email+"&password="+password;

            try {
                URL url = new URL(phpurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream), "UTF-8"));


                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                    Log.i("debug", "response is" + " "+response+ "null is not the value");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.i("debug", "response is" + " "+response+ "null is not the value");

            } catch (IOException e) {
                e.printStackTrace();
            }


            return response.trim();

        }


        @Override
        protected void onPostExecute(String result) {

            Log.i("debug", "result is" + " "+result +""+"it should be Success");

            if(result.equalsIgnoreCase("Success"))
            {
                Toast.makeText(getContext(),"Logged in Successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrgNavigationDrawer.class);
                startActivity(intent);
            }else if(result.contains("Failed"))
            {
                Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"Email or Password is wrong",Toast.LENGTH_SHORT).show();
                passwordE.setText("");
                emailE.setText("");
            }
        }
    }

}
