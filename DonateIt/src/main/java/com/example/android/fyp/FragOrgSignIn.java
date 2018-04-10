package com.example.android.fyp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
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
 * Created by HP on 3/14/2018.
 */


public class FragOrgSignIn extends Fragment {
    View view;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Button signIn,signInDonor;
    EditText edtEmail, edtPassword;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_org_signin, null);

        signIn = view.findViewById(R.id.btnSignIn);
        signInDonor = view.findViewById(R.id.btnSingInDonor);
        edtEmail = (EditText) view.findViewById(R.id.etEmail);
        edtPassword = (EditText) view.findViewById(R.id.etPass);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                new databaseProcess().execute(email,password);

            }
        });
        signInDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager=getActivity().getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout,new FragLogIn()).commit();

            }
        });

        return view;
    }

    public class databaseProcess extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            String email = params[0];
            String password = params[1];
            String phpurl = "http://10.0.2.2:81/DonateIt/orgSignIn.php";



            try {
                URL url = new URL(phpurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter((outputStream), "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream), "iso-8859-1"));


                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                    Log.i("debug", "response is" + " "+response+ "null is not the value");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.i("debug", "response is" + " "+response+ "null is not the value");

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return response.trim();

        }


        @Override
        protected void onPostExecute(String result) {

            Log.i("debug", "result is" + " "+result +""+"it should be Success");


            if(result.equals("Success"))
            {
                Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), OrgNavigationDrawer.class);
                startActivity(intent);
            }else if(result.matches("Failed"))
            {
                Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"Email or Password is wrong",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getContext(),"got out",Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),"got out",Toast.LENGTH_SHORT).show();

        }
    }

}
