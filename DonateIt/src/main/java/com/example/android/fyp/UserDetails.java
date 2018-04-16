package com.example.android.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.ArrayList;

public class UserDetails extends AppCompatActivity {

    EditText edName, edEmail, edCity, edCountry, edItemName, edItemAmount;
    Button btnSave, btnSaveItem, btnCancelItem;
    TextView tvItemLabel;
    FloatingActionButton fab;
    SharedPreferences preferences;
    String currentURL, name, email,city, country, id, itemAmount;
    String itemId, itemName, getItemAmount;
    LinearLayout LLUserDetails;
    ListView listView;
    MyAdapter adapter;
    ArrayList<UserDetailsData> dataArrayList = new ArrayList<>();


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
        new datafetchDonorItems().execute();

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
                tvItemLabel.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                LLUserDetails.setVisibility(View.VISIBLE);
            }
        });

        btnCancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLUserDetails.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                tvItemLabel.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
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
        listView = findViewById(R.id.lvUserDetails);
        LLUserDetails = findViewById(R.id.LLUserDetails);
        tvItemLabel = findViewById(R.id.tvUserItemsLabel);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvUserDetails) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            UserDetailsData i = (UserDetailsData) adapter.getItem(acmi.position);

            itemId = i.getItemId();

            menu.add(0,0,0,"Delete");

            //menu.add("Three");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId()==0){
            switch (item.getItemId()){

                case 0:{
                    new dataItemDeletion(itemId).execute();
                    break;
                }
                default: {}
            }
            return true;
        }else {
            return false;
        }
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
    class datafetchDonorItems extends AsyncTask<String,Void,String> {
        String object,JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
        String requestUrl = currentURL + "DonateIt/getDonorItems.php?userId="+id;

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
                dataArrayList.clear();
                if (success.equalsIgnoreCase("true")) {
                    jsonArray = jsonObject.getJSONArray("results");
                    for (int i =0; i<jsonArray.length();i++) {
                        UserDetailsData data = new UserDetailsData(
                                jsonArray.getJSONObject(i).getString("item_id"),
                                jsonArray.getJSONObject(i).getString("item_name"),
                                jsonArray.getJSONObject(i).getString("item_amount")
                                );
                        dataArrayList.add(data);
                    }
                    adapter = new MyAdapter(UserDetails.this, dataArrayList);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
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
                    fab.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    tvItemLabel.setVisibility(View.VISIBLE);
                    LLUserDetails.setVisibility(View.GONE);
                    new datafetchDonorItems().execute();
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
    class dataItemDeletion extends AsyncTask<String,Void,String> {
        String object,JSON_STRING,item_id;
        JSONObject jsonObject;
        JSONArray jsonArray;

        public dataItemDeletion(String itemId) {
            item_id = itemId;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String requestUrl = currentURL + "DonateIt/deleteDonorItems.php?itemId="+item_id;
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
                    new datafetchDonorItems().execute();
                } else {
                    Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class MyAdapter extends BaseAdapter {
        ArrayList<UserDetailsData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        MyAdapter(Context context, ArrayList<UserDetailsData> list) {
            arrayList = list;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.donor_items_list, null);
                holder = new ViewHolder();
                holder.tvItemAmount = convertView.findViewById(R.id.tvAmountDonorItems);
                holder.tvItemId = convertView.findViewById(R.id.idDonorItems);
                holder.tvItemName = convertView.findViewById(R.id.tvNameDonorItems);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvItemName.setText(arrayList.get(position).getItmName());
            holder.tvItemId.setText(arrayList.get(position).getItemId());
            holder.tvItemAmount.setText(arrayList.get(position).getItemAmount());
            return convertView;
        }

        class ViewHolder {

            TextView tvItemId, tvItemName, tvItemAmount;

        }

    }

}
