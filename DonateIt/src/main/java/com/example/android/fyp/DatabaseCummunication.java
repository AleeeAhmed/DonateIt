/*
package com.example.android.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

*/
/**
 * Created by HP on 2/13/2018.
 *//*


public class DatabaseCummunication extends AsyncTask<String,Void,String> {


    Context ctx;
    String role;
    String JSON_STRING;
    AlertDialog alertDialog;


    public DatabaseCummunication(Context ctx) {
        this.ctx = ctx ;
    }

    @Override
    protected String doInBackground(String... params) {
        String response= "";

        String registerUrl = "http://10.0.2.2:81/DonateIt/donorSignup.php";
        String loginUrl = "http://10.0.2.2:81/DonateIt/login.php";
        String method = params[0];
        if(method.equals("register")){
            String email = params[1];
            String username = params[2];
            String mobileNo = params[3];
            String country = params[4];
            String city = params[5];
            String password = params[6];


            try {
                Log.i("Debug", "regestration");
                URL url = new URL(registerUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data = URLEncoder.encode("email","UTF-8") + "=" +URLEncoder.encode(email,"UTF-8")+ "&" +
                        URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+ "&" +
                        URLEncoder.encode("mobileNo","UTF-8") + "=" + URLEncoder.encode(mobileNo, "UTF-8")+ "&" +
                        URLEncoder.encode("country","UTF-8") + "=" +URLEncoder.encode(country,"UTF-8")+ "&" +
                        URLEncoder.encode("city","UTF-8") + "=" + URLEncoder.encode(city, "UTF-8")+ "&" +
                        URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


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


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //response = "Registration Successful";
            Log.i("debug", "response is" + " "+response);
            return response;
        }
        else if(method.equals("login")){

            Log.i("Debug", "login");
            String email = params[1];
            String password = params[2];

            try {
                URL url = new URL(loginUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();

               // InputStream inputStream = httpURLConnection.getInputStream();
               // BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
               // StringBuilder stringBuilder = new StringBuilder();

                */
/*while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                    role =  stringBuilder.toString().trim();
                }*//*


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
                return response;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        Log.i("debug", "response is" + " "+response + "null is the value");
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Info");
    }

    @Override
    protected void onPostExecute(String result) {

        JSONObject jsonObject;
        JSONArray jsonArray;
        String user;

        Log.i("debug", "post execution started and result is" + " "+ result );

        if(result.equals("Registration Successful"))
        {
            Toast.makeText(ctx, result,Toast.LENGTH_SHORT).show();
        }
        else if(result.contains("Failed"))
        {
            alertDialog.setMessage(result);
            alertDialog.show();

//            alertDialog.setMessage(result);
//            alertDialog.show();
        }else if(result.matches("Login Successful"))
        {


                Intent intent = new Intent(ctx, DonorNavigationDrawer.class);
                ctx.startActivity(intent);

                */
/*
            try {
                jsonObject = new JSONObject(result);
                JSONObject jsonObjects = jsonObject.getJSONObject("userinfo");
                Log.i("debug", "jsonobjects is" + " "+jsonObjects);
                String role = jsonObjects.getString("role");
                Log.i("debug", "object role is" + " "+role);



                if(role.equals("Donor"))
                {
                    Intent intent = new Intent(ctx, DonorNavigationDrawer.class);
                    ctx.startActivity(intent);

                }else if(role.equals("Org"))
                {
                    Intent intent = new Intent(ctx, OrgNavigationDrawer.class);
                    ctx.startActivity(intent);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*//*


            Log.i("debug", "post execution ends");
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

*/
