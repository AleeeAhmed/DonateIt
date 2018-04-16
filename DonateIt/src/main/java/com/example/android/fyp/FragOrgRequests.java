package com.example.android.fyp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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

public class FragOrgRequests extends Fragment {

    String JSON_STRING;
    ListView list;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<FragOrgRequestsData> dataArrayList = new ArrayList<>();
    View view;
    MyAdapter adapter;
    String currentUrl,reqId;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_org_requests, container, false);

        list = (ListView) view.findViewById(R.id.lvOrgRequests);
        fab = (FloatingActionButton) view.findViewById(R.id.fabNewRequest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityRequestOverView.class);
                startActivity(intent);
            }
        });
        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentUrl = preferences.getString("URL", "");
        new datafetch().execute();
        return view;

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvOrgRequests) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            FragOrgRequestsData i = (FragOrgRequestsData) adapter.getItem(acmi.position);

            reqId = i.getId();

            menu.add(1,0,0,"Delete");

            //menu.add("Three");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId()==1){
            switch (item.getItemId()){

                case 0:{
                    new deleteRequest(reqId).execute();
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
        String object;
        JSONObject jsonObject;
        JSONArray jsonArray;
        SharedPreferences preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String user = preferences.getString("UserId", "");
        String requestUrl = currentUrl + "DonateIt/orgrequests.php?userId="+user;

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
            Log.i("debug", "org post execute started and result is"+""+result );


            try {
                Log.i("debug", "org post got into try" );
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");
                dataArrayList.clear();

                if (success.equalsIgnoreCase("true")) {
                    jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        String id = (jsonObject.getString("id"));
                        String title = (jsonObject.getString("title"));
                        String invitations = String.valueOf((jsonObject.getInt("invitations")));
                        String offers= String.valueOf((jsonObject.getInt("offers")));
                        String desc=(jsonObject.getString("description"));
                        String progress=(jsonObject.getString("progress"));
                        String target=(jsonObject.getString("target"));

                        FragOrgRequestsData data = new FragOrgRequestsData(id,title, invitations, offers, desc,progress,target);
                        dataArrayList.add(data);
                    }

                    adapter = new MyAdapter(getActivity(), dataArrayList);
                    list.setAdapter(adapter);
                    registerForContextMenu(list);
                } else {
                    Toast.makeText(getActivity(), "No requests found..", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class deleteRequest extends AsyncTask<String,Void,String> {
        String object, reqId;
        JSONObject jsonObject;
        JSONArray jsonArray;

        public deleteRequest(String reqId) {
            this.reqId = reqId;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String requestUrl = currentUrl + "DonateIt/deleteOrgRequests.php?id="+reqId;
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
            Log.i("debug", "org post execute started and result is"+""+result );
            try {
                Log.i("debug", "org post got into try" );
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");

                if (success.equalsIgnoreCase("true")) {
                    jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i<jsonArray.length(); i++) {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        new datafetch().execute();
                    }
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class MyAdapter extends BaseAdapter {

        ArrayList<FragOrgRequestsData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<FragOrgRequestsData> arrayList) {
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
                convertView = inflater.inflate(R.layout.full_org_requests, null);
                holder = new ViewHolder();
                holder.tvID = convertView.findViewById(R.id.idOrgReqList);
                holder.tvTitle = convertView.findViewById(R.id.titleOrgReqList);
                holder.tvDesc = convertView.findViewById(R.id.descOrgReqList);
                holder.tvTarget = convertView.findViewById(R.id.targetOrgReqList);
                holder.tvProgress = convertView.findViewById(R.id.progressOrgReqList);
                holder.tvInvitations = convertView.findViewById(R.id.invOrgReqList);
                holder.tvOffers = convertView.findViewById(R.id.offerOrgReqList);
                holder.progressBar = convertView.findViewById(R.id.pbProgressOrgReqList);
                //holder.imageView = convertView.findViewById(R.id.savedDonorRequestsList);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
            String user = preferences.getString("Username", "");
            preferences = getActivity().getSharedPreferences(user+"_SavedRequests", Context.MODE_PRIVATE);

/*            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor = preferences.edit();
                    if (preferences.getBoolean(arrayList.get(position).getID(), false)) {
                        editor.remove(arrayList.get(position).getID());
                        Toast.makeText(getActivity(), arrayList.get(position).getTitle()+" removed from saved requests", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.putBoolean(arrayList.get(position).getID(), true);
                        Toast.makeText(getActivity(), arrayList.get(position).getTitle()+" added to saved requests", Toast.LENGTH_SHORT).show();
                    }
                    editor.apply();
                    new FragRequests.dataFetch().execute();
                }
            });*/
            holder.progressBar.setMax(Integer.parseInt(arrayList.get(position).getTarget()));
            holder.progressBar.setProgress(Integer.parseInt(arrayList.get(position).getProgress()));
            holder.tvID.setText(arrayList.get(position).getId());
            holder.tvTitle.setText(arrayList.get(position).getTitle());
            holder.tvDesc.setText(arrayList.get(position).getDescription());
            holder.tvProgress.setText(arrayList.get(position).getProgress());
            holder.tvTarget.setText(arrayList.get(position).getTarget());
            holder.tvInvitations.setText(arrayList.get(position).getInvitations());
            holder.tvOffers.setText(arrayList.get(position).getOffers());

/*
            if (preferences.getBoolean(arrayList.get(position).getID(),false)) {
                holder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.filledheart));
            } else {
                holder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.emptyheart));
            }
*/

            return convertView;
        }

        class ViewHolder {
            TextView tvID,tvTitle, tvDesc, tvProgress, tvTarget, tvInvitations, tvOffers;
            ProgressBar progressBar;
            //ImageView imageView;
        }


    }

}
