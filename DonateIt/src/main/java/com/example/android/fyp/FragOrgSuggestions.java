package com.example.android.fyp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HP on 1/25/2018.
 */

public class FragOrgSuggestions extends Fragment {
    ListView list;
    ArrayList<FragOrgSuggestionsData> dataArrayList = new ArrayList<>();
    MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_org_suggestion, container, false);
        list = (ListView)  view.findViewById(R.id.lvOrgSuggestions);
        new datafetch().execute();
        return view;
    }

    class datafetch extends AsyncTask<String,Void,String> {

        String object,JSON_STRING;
        SharedPreferences preferences= getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE),
                preferences1 = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        String currentURL = preferences1.getString("URL",""),
                UserId = preferences.getString("UserId", "");
        JSONObject jsonObject;
        JSONArray jsonArray;

        String requestUrl = currentURL+"DonateIt/getSuggestions.php?user="+UserId;

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
                    stringBuilder.append(JSON_STRING).append("\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                object =  stringBuilder.toString().trim();

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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String request_id = (jsonObject.getString("request_id"));
                        String itemNames = (jsonObject.getString("itemName"));
                        String itemAmount = (jsonObject.getString("itemAmount"));
                        String donorID = (jsonObject.getString("donorId"));
                        String donorName = (jsonObject.getString("donorName"));

                        FragOrgSuggestionsData data = new FragOrgSuggestionsData(request_id,donorID, donorName, itemNames, itemAmount);
                        dataArrayList.add(data);
                    }
                    adapter = new MyAdapter(getActivity(), dataArrayList);
                    list.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class dataRequest extends AsyncTask<String,Void,String> {

        String object,JSON_STRING,donor,request;
        SharedPreferences preferences= getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE),
                preferences1 = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        String currentURL = preferences1.getString("URL",""),
                UserId = preferences.getString("UserId", "");
        JSONObject jsonObject;
        JSONArray jsonArray;

        dataRequest(String donorId, String reqId) {
            donor = donorId ;
            request = reqId;
        }

        String requestUrl ;

        @Override
        protected String doInBackground(String... params) {
            try {
                requestUrl = currentURL+"DonateIt/sendInvite.php?requestId="+request+"&donorId="+donor+"&orgId="+UserId;
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
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyAdapter extends BaseAdapter {

        ArrayList<FragOrgSuggestionsData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<FragOrgSuggestionsData> arrayList) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.frag_full_org_suggestions, null);
                holder = new ViewHolder();
                holder.tvRequestID = convertView.findViewById(R.id.requestID_OrgSuggestions);
                holder.tvDonorID = convertView.findViewById(R.id.donorID_OrgSuggestions);
                holder.tvDonorName = convertView.findViewById(R.id.donorName_OrgSuggestions);
                holder.tvItemName = convertView.findViewById(R.id.itemName_OrgSuggestions);
                holder.tvItemAmount = convertView.findViewById(R.id.itemAmount_OrgSuggestions);
                holder.btnInvite = convertView.findViewById(R.id.btnInvite_OrgSuggestions);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String donorID = arrayList.get(position).getDonorID();
                    String reqID = arrayList.get(position).getRequestID();
                    new dataRequest(donorID,reqID).execute();
                }
            });
            holder.tvRequestID.setText(arrayList.get(position).getRequestID());
            holder.tvDonorID.setText(arrayList.get(position).getDonorID());
            holder.tvDonorName.setText(arrayList.get(position).getDonorName());
            holder.tvItemName.setText(arrayList.get(position).getItemName());
            holder.tvItemAmount.setText(arrayList.get(position).getItemAmount());
            return convertView;
        }

        class ViewHolder {
            TextView tvRequestID,tvDonorID,tvDonorName, tvItemName, tvItemAmount;
            Button btnInvite;
        }


    }

}

