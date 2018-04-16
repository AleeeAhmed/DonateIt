package com.example.android.fyp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragDonorOffers extends Fragment {
    ListView list;
    ArrayList<FragDonorOffersData> dataArrayList = new ArrayList<>();
    MyAdapter adapter;
    SharedPreferences preferences;
    String currentURL;
    View view;

    public FragDonorOffers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_donor_offers, container, false);

        list = (ListView) view.findViewById(R.id.lvDonorOffers);

        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentURL = preferences.getString("URL", "");

        new dataFetch().execute();


        return view;
    }


    class dataFetch extends AsyncTask<String,Void,String> {
        SharedPreferences preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String userId = preferences.getString("UserId","");
        String JSON_STRING;
        String requestUrl = currentURL+"DonateIt/getDonorOffers.php?userId="+userId;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(requestUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int c = httpURLConnection.getResponseCode();
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

                return stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }



        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            JSONArray jsonArray;
            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                dataArrayList.clear();

                if (success.equalsIgnoreCase("true")) {
                    jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String offer_id = (jsonObject.getString("offer_id"));
                        String title = (jsonObject.getString("title"));
                        String quantity = (jsonObject.getString("quantity"));
                        String status = (jsonObject.getString("status"));

                        FragDonorOffersData data = new FragDonorOffersData(offer_id,title, quantity, status );
                        dataArrayList.add(data);
                    }
                }

                adapter = new MyAdapter(getActivity(), dataArrayList);
                list.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }


    private class MyAdapter extends BaseAdapter {

        ArrayList<FragDonorOffersData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<FragDonorOffersData> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return dataArrayList.size();
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
                convertView = inflater.inflate(R.layout.donor_offerss_list, null);
                holder = new ViewHolder();
                holder.tvId = convertView.findViewById(R.id.idDonorOffers);
                holder.tvOrgName = convertView.findViewById(R.id.tvPostNameDonorOffers);
                holder.tvPost = convertView.findViewById(R.id.tvOfferAmountDonorOffers);
                holder.tvLastMessage = convertView.findViewById(R.id.tvOfferStatusDonorOffers);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvId.setText(arrayList.get(position).getOfferID());
            holder.tvOrgName.setText(arrayList.get(position).getTitle());
            holder.tvPost.setText(arrayList.get(position).getQuantity());
            holder.tvLastMessage.setText(arrayList.get(position).getStatus());

            return convertView;
        }

        class ViewHolder {
            TextView tvId,tvOrgName, tvPost, tvLastMessage;
        }


    }




}
