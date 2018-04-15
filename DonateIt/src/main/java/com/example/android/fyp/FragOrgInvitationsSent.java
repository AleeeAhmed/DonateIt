package com.example.android.fyp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.List;
import java.util.Locale;

/**
 * Created by HP on 1/25/2018.
 */

public class FragOrgInvitationsSent extends Fragment {
    ListView list;
    String JSON_STRING;
    SharedPreferences preferences;
    String currentURL;
    MyAdapter adapter;
    ArrayList<FragOrgInvitationsSentData> dataArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_org_invitations_sent, container, false);
        list = (ListView)  view.findViewById(R.id.lvInvitations);

        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentURL = preferences.getString("URL", "");

        new dataFetch().execute();
        return view;
    }


    class dataFetch extends AsyncTask<String,Void,String>{
        JSONObject jsonObject;
        JSONArray jsonArray;
        SharedPreferences preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String userId = preferences.getString("UserId", "");
        String requestUrl = currentURL+"DonateIt/getInvitations.php?donor_id="+userId;

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


            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                dataArrayList.clear();

                if (success.equalsIgnoreCase("true")) {
                    int count = 0;
                    jsonArray = jsonObject.getJSONArray("results");
                    while (count<jsonArray.length()) {
                        jsonObject = jsonArray.getJSONObject(count);
                        String id = (jsonObject.getString("id"));
                        String title = (jsonObject.getString("title"));
                        int progress= (jsonObject.getInt("progress"));
                        int target = (jsonObject.getInt("goal"));
                        String orgId = (jsonObject.getString("orgId"));
                        String orgName = (jsonObject.getString("orgName"));

                        FragOrgInvitationsSentData data = new FragOrgInvitationsSentData(id, title,orgId,orgName, progress, target );
                        dataArrayList.add(data);

                        count++;
                    }
                    adapter = new MyAdapter(getActivity(), dataArrayList);
                    list.setAdapter(adapter);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String id_ = ((TextView) view.findViewById(R.id.id_InvitationsList)).getText().toString();
                            Intent intent = new Intent(getActivity(), FragRequestsDetails.class);
                            intent.putExtra("ID", id_);
                            startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class cancelRequest extends AsyncTask<String,Void,String>{
        JSONObject jsonObject;
        JSONArray jsonArray;
        String reqId  , orgID;

        public cancelRequest(String id, String orgId) {
            reqId = id;
            orgID = orgId;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String requestUrl = currentURL+"DonateIt/cancelRequest.php?request_id="+reqId+"&orgId="+orgID;
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


            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");


                if (success.equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    new dataFetch().execute();
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class MyAdapter extends BaseAdapter {

        ArrayList<FragOrgInvitationsSentData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<FragOrgInvitationsSentData> arrayList) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.frag_full_org_invitations_sent, null);
                holder = new ViewHolder();
                holder.tvID = convertView.findViewById(R.id.id_InvitationsList);
                holder.tvTitle = convertView.findViewById(R.id.postName_InvitationList);
                holder.tvOrgId = convertView.findViewById(R.id.orgId_InvitationsList);
                holder.tvOrgName = convertView.findViewById(R.id.orgName_InvitationList);
                holder.tvTarget = convertView.findViewById(R.id.target_InvitationList);
                holder.tvProgress = convertView.findViewById(R.id.progress_InvitationList);
                holder.progressBar = convertView.findViewById(R.id.pbProgress_InvitationList);
                holder.btnCancel = convertView.findViewById(R.id.btnCancel_InvitationList);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new cancelRequest(arrayList.get(position).getId(),arrayList.get(position).getOrgId()).execute();
                }
            });
            holder.progressBar.setMax(arrayList.get(position).getTarget());
            holder.progressBar.setProgress(arrayList.get(position).getProgress());
            holder.tvID.setText(arrayList.get(position).getId());
            holder.tvTitle.setText(arrayList.get(position).getTitle());
            holder.tvOrgName.setText(arrayList.get(position).getOrgName());
            holder.tvOrgId.setText(arrayList.get(position).getOrgId());
            holder.tvProgress.setText(""+arrayList.get(position).getProgress());
            holder.tvTarget.setText(""+arrayList.get(position).getTarget());

            return convertView;
        }

        class ViewHolder {
            TextView tvID,tvTitle,tvOrgId,tvOrgName, tvProgress, tvTarget;
            ProgressBar progressBar;
            Button btnCancel;
        }
    }


}

