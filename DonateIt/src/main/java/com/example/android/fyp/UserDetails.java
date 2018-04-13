package com.example.android.fyp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class UserDetails extends AppCompatActivity {

    EditText edName, edEmail, edCity, edCountry, edItemName, edItemAmount;
    Button btnSave, btnSaveItem, btnCancelItem;
    FloatingActionButton fab;
    SharedPreferences preferences;
    String currentURL, name, email,city, country, id, itemName, itemAmount;
    LinearLayout LLUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        initializations();

        preferences = getSharedPreferences("Login", MODE_PRIVATE);
        id = preferences.getString("UserId", "");
        preferences = getSharedPreferences("HiddenUrl", MODE_PRIVATE);
        currentURL = preferences.getString("URL","");

        new datafetch().execute();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edName.getText().toString().trim();
                email = edEmail.getText().toString().trim();
                city = edCity.getText().toString().trim();
                country = edCountry.getText().toString().trim();
                if (!name.isEmpty() || !email.isEmpty() || !city.isEmpty() || !country.isEmpty()) {
                    new dataSaving().execute();
                } else {
                    Toast.makeText(UserDetails.this, "Fill all fields properly..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.GONE);
                LLUserDetails.setVisibility(View.VISIBLE);
            }
        });

        btnCancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLUserDetails.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                edItemAmount.setText("");
                edItemName.setText("");
            }
        });

        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAmount = edItemAmount.getText().toString();
                itemName = edItemName.getText().toString();

                if (!itemName.isEmpty() || !itemAmount.isEmpty()) {
                    new dataItemSaving().execute();
                } else {
                    Toast.makeText(UserDetails.this, "item  name or amount cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializations() {
        edName = findViewById(R.id.edName_UserDetails);
        edCity = findViewById(R.id.edCity_UserDetails);
        edCountry = findViewById(R.id.edCountry_UserDetails);
        edEmail = findViewById(R.id.edEmail_UserDetails);
        edItemAmount = findViewById(R.id.edItemAmount_UserDetails);
        edItemName = findViewById(R.id.edItemName_UserDetails);
        btnSave = findViewById(R.id.btnSave_UserDetails);
        btnSaveItem = findViewById(R.id.btnSaveItem_UserDetails);
        btnCancelItem = findViewById(R.id.btnCancelItem_UserDetails);
        fab = findViewById(R.id.fab_AddItems);
        LLUserDetails = findViewById(R.id.LLUserDetails);
    }

    class datafetch extends AsyncTask<String,Void,String> {
        String object,JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
        String requestUrl = currentURL + "DonateIt/getDonor.php?id="+id;

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i("debug", "org data doinbackground started" );
                URL url = new URL(requestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
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

                object =  stringBuilder.toString().trim();
                Log.i("debug", "org object is"+""+object );

            } catch (IOException e) {
                e.printStackTrace();
            }
            return object;
        }



        @Override
        protected void onPostExecute(String result) {
            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                if (success.equalsIgnoreCase("true")) {

                    edCity.setText(jsonObject.getString("city"));
                    edCountry.setText(jsonObject.getString("country"));
                    edEmail.setText(jsonObject.getString("email"));
                    edName.setText(jsonObject.getString("name"));

                } else {
                    Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class dataSaving extends AsyncTask<String,Void,String> {
        String object,JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
        String requestUrl = currentURL + "DonateIt/updateDonor.php?id="+id+
                "&name="+name+"&email="+email+"&city="+city+"&country="+country;

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i("debug", "org data doinbackground started" );
                URL url = new URL(requestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
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

                object =  stringBuilder.toString().trim();
                Log.i("debug", "org object is"+""+object );

            } catch (IOException e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                if (success.equalsIgnoreCase("true")) {

                    Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class dataItemSaving extends AsyncTask<String,Void,String> {
        String object,JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
        String requestUrl = currentURL + "DonateIt/donorItems.php?donor_id="+id+
                "&item_name="+itemName+"&item_amount="+itemAmount;

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i("debug", "org data doinbackground started" );
                URL url = new URL(requestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
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

                object =  stringBuilder.toString().trim();
                Log.i("debug", "org object is"+""+object );

            } catch (IOException e) {
                e.printStackTrace();
            }
            return object;
        }



        @Override
        protected void onPostExecute(String result) {
            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                if (success.equalsIgnoreCase("true")) {
                    edItemAmount.setText("");
                    edItemName.setText("");
                    Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else {
                    edItemAmount.setText("");
                    edItemName.setText("");
                    Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
