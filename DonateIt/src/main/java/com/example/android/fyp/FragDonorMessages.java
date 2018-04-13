package com.example.android.fyp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
 * Created by HP on 1/24/2018.
 */

public class FragDonorMessages extends Fragment {
    ListView list;
    ArrayList<FragDonorMessagesData> dataArrayList = new ArrayList<>();
    MyAdapter adapter;
    SharedPreferences preferences;
    String currentURL;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_donor_messages, container, false);

        list = (ListView) view.findViewById(R.id.listDonorMessages);

        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentURL = preferences.getString("URL", "");

        new dataFetch().execute();


        return view;
    }


    class dataFetch extends AsyncTask<String,Void,String> {
        SharedPreferences preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String userId = preferences.getString("UserId","");
        String JSON_STRING;
        String requestUrl = currentURL+"DonateIt/getChatSender.php?id="+userId;

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
                        String msgId = (jsonObject.getString("messageId"));
                        String postName = (jsonObject.getString("postName"));
                        String orgName = (jsonObject.getString("orgName"));
                        String lastMessage = (jsonObject.getString("lastMessage"));

                        FragDonorMessagesData data = new FragDonorMessagesData(msgId,orgName, postName, lastMessage );
                        dataArrayList.add(data);
                    }
                }

                adapter = new MyAdapter(getActivity(), dataArrayList);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String messageId = ((TextView) view.findViewById(R.id.idMessagesDonorList)).getText().toString();

                        Intent intent = new Intent(getActivity(), Chat.class);
                        intent.putExtra("MessageId", messageId);
                        intent.putExtra("MessageFrom", "donor");
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }


    private class MyAdapter extends BaseAdapter {

        ArrayList<FragDonorMessagesData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<FragDonorMessagesData> arrayList) {
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
                    convertView = inflater.inflate(R.layout.full_frag_messages, null);
                    holder = new ViewHolder();
                    holder.tvId = convertView.findViewById(R.id.idMessagesDonorList);
                    holder.tvOrgName = convertView.findViewById(R.id.orgNameMessagesDonorList);
                    holder.tvPost = convertView.findViewById(R.id.postMessagesDonorList);
                    holder.tvLastMessage = convertView.findViewById(R.id.lastMessagesDonorList);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.tvId.setText(arrayList.get(position).getMsgId());
                holder.tvOrgName.setText(arrayList.get(position).getOrgName());
                holder.tvPost.setText(arrayList.get(position).getPost());
                holder.tvLastMessage.setText(arrayList.get(position).getLastMessage());

            return convertView;
        }

        class ViewHolder {
            TextView tvId,tvOrgName, tvPost, tvLastMessage;
        }


    }



}
