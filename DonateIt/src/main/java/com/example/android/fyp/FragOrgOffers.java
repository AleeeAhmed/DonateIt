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
import android.widget.ListView;
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

public class FragOrgOffers extends Fragment {
    ListView list;
    View view;
    String JSON_STRING;
    SharedPreferences preferences;
    MyAdapter adapter;
    ArrayList<FragOrgOffersData> dataArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_org_offers, container, false);
        list = (ListView)  view.findViewById(R.id.lvOrgOffer);
        new datafetch().execute();
        return view;
    }

    class datafetch extends AsyncTask<String,Void,String> {

        String object;
        SharedPreferences preferences = getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE);
        String userId = preferences.getString("UserId", "");
        SharedPreferences preferences1 = getActivity().getSharedPreferences("HiddenUrl",Context.MODE_PRIVATE);
        String currentUrl = preferences1.getString("URL", "");
        JSONObject jsonObject;
        JSONArray jsonArray;

        String requestUrl = currentUrl+"DonateIt/getOffers.php?userId="+userId;

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
                    for (int i = 0 ; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(0);
                        String offerID = jsonObject.getString("offer_id");
                        String reqId = jsonObject.getString("request_id");
                        String senderId = jsonObject.getString("sender_id");
                        String offeredQuantity = jsonObject.getString("offered_quantity");
                        String senderRating = jsonObject.getString("sender_rating");
                        String userId = jsonObject.getString("user_id");
                        String postName = jsonObject.getString("title");

                        FragOrgOffersData data = new FragOrgOffersData(offerID, reqId, senderId, userId,senderRating, offeredQuantity, postName);
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

    class MyAdapter extends BaseAdapter {
        ArrayList<FragOrgOffersData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        MyAdapter(Context context, ArrayList<FragOrgOffersData> arrayList) {
            this.arrayList = arrayList;
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
                convertView = inflater.inflate(R.layout.frag_full_org_offers, null);
                holder = new ViewHolder();
                holder.tvOfferId = convertView.findViewById(R.id.offerId_OrgOffer);
                holder.tvReqId = convertView.findViewById(R.id.reqId_OrgOffer);
                holder.tvSenderId = convertView.findViewById(R.id.senderId_OrgOffer);
                holder.tvOrgID = convertView.findViewById(R.id.orgId_OrgOffer);
                holder.tvOfferAmount = convertView.findViewById(R.id.amount_OrgOffers);
                holder.tvPostName = convertView.findViewById(R.id.postName_OrgOffers);
                holder.rbSender = convertView.findViewById(R.id.ratingBar_OrgOffers);
                holder.btnAccept = convertView.findViewById(R.id.accept_OrgOffer);
                holder.btnRject = convertView.findViewById(R.id.reject_OrgOffer);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.btnRject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.tvOfferId.setText(arrayList.get(position).getOfferID());
            holder.tvPostName.setText(arrayList.get(position).getPostName());
            holder.tvOfferAmount.setText(arrayList.get(position).getOfferedQuantity());
            holder.tvReqId.setText(arrayList.get(position).getReqID());
            holder.tvOrgID.setText(arrayList.get(position).getOrgID());
            holder.tvSenderId.setText(arrayList.get(position).getSenderID());
            holder.rbSender.setNumStars(Integer.parseInt(arrayList.get(position).getSenderRating()));

            return convertView;
        }

        class ViewHolder {
            TextView tvOfferId, tvPostName, tvOfferAmount, tvReqId, tvSenderId, tvOrgID;
            RatingBar rbSender;
            Button btnAccept, btnRject;

        }

    }

}

