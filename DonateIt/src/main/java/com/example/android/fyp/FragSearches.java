package com.example.android.fyp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HP on 1/20/2018.
 */

public class FragSearches extends Fragment {

    String JSON_STRING;
    ArrayList<String> savedSearches = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_searches, container, false);
        ListView searchesList = (ListView) view.findViewById(R.id.saved_searches_l);
        ListView categoriesList = (ListView) view.findViewById(R.id.categories_l);
        Log.i("Debug", "starting");
                /*

        String[] savedSearches = {"Warm Clothes", "Water Cleaning", "Shelters", "Education", "Food"};

        String[] categories = {"Shelter", "Wearings", "Foods","Education", "Drinks"};

*/

        ArrayAdapter<String> savedSearchesAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1 ,savedSearches );
        searchesList.setAdapter(savedSearchesAdapter);

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1 ,categories );
        categoriesList.setAdapter(categoriesAdapter);
        Log.i("Debug", "datagetch exeuting");
        new datafetch().execute();

        return view;
    }

    class datafetch extends AsyncTask<String,Void,String> {

        String object;

        JSONObject jsonObject;
        JSONArray jsonArray;



        String requestUrl = "http://10.0.2.2:81/DonateIt/searches.php";

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(requestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                object =  stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("Debug", "returned");
            return object;

        }



        @Override
        protected void onPostExecute(String result) {
            String json_string = result;
            Log.i("Debug", "post execution");
            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;


                Log.i("Debug", ""+jsonArray.length());

                while (count<jsonArray.length())
                {

                    JSONObject jo = jsonArray.getJSONObject(count);
                    String category = (jo.getString("category"));
                    if(category.equals("Saved"))
                    {
                        savedSearches.add(jo.getString("keywords"));
                        Log.i("Debug",jo.getString("keywords"));
                    }else if(category.equals("category"))
                    {
                        categories.add(jo.getString("keywords"));
                        Log.i("Debug",jo.getString("keywords"));
                    }


                    //Log.i("Debug", savedSearches.get(count) + "   " + categories.get(count)+" " );
                    //adapter.notifyDataSetChanged();
                    count++;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


}
