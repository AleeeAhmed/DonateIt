package com.example.android.fyp;

import android.app.Activity;
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
import android.widget.RatingBar;
import android.widget.TextView;

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
 * Created by HP on 1/25/2018.
 */

public class FragOrgInvitationsSent extends Fragment {
    ListView list;
    String JSON_STRING;
    ArrayList<String> names = new ArrayList<>(), goods = new ArrayList<>();
    ArrayList<Integer> rating = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_org_invitations_sent, container, false);
        list = (ListView)  view.findViewById(R.id.list);

        /*String[] names = {"Saleem", "Sajid", "Abid", "Mubasher", "Danish"};
        String[] itemNumber = {"100 Blankets", "1 Blankets", "4 Blankets", "25 Blankets", "10 Blankets"};
        Integer[] numStars = {5,4,5,5,4}*/;


        adapter=new MyListAdapterSaved(getContext(), names, goods , rating);
        list.setAdapter(adapter);

        new datafetch().execute();
        return view;
    }

    class datafetch extends AsyncTask<String,Void,String> {

        String object;

        JSONObject jsonObject;
        JSONArray jsonArray;

        String requestUrl = "http://10.0.2.2:81/DonateIt/invitationssent.php";

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
            return object;

        }



        @Override
        protected void onPostExecute(String result) {


            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;




                while (count<jsonArray.length())
                {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    names.add(jo.getString("name"));
                    goods.add(jo.getString("goods"));
                    rating.add(jo.getInt("rating"));

                    Log.i("debug", "suggestions details are"+""+jo.getString("name" )+""+jo.getString("goods")+""+jo.getString("rating"));

                    adapter.notifyDataSetChanged();
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public class MyListAdapterSaved extends ArrayAdapter<String> {

        private final Activity context;
        private ArrayList<String> name;
        private  ArrayList<String> goods;
        private  ArrayList<Integer> rating;

        public MyListAdapterSaved(Context context, ArrayList<String> name, ArrayList<String> goods, ArrayList<Integer> rating ) {
            super(context, R.layout.frag_full_org_invitations_sent, name);
            // TODO Auto-generated constructor stub

            this.context= (Activity) context;
            this.name=name;
            this.goods=goods;
            this.rating = rating;

        }

        public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.frag_full_org_invitations_sent, null,true);

            TextView nameT = (TextView) rowView.findViewById(R.id.name);
            TextView goodsT = (TextView) rowView.findViewById(R.id.amount_item);
            RatingBar ratingBarT = (RatingBar) rowView.findViewById(R.id.ratingBar);

            nameT.setText(name.get(position));
            goodsT.setText(goods.get(position));
            ratingBarT.setNumStars(rating.get(position));

            return rowView;

        };
    }

}

