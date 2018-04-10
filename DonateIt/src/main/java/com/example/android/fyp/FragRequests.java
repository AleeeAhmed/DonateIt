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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.security.auth.Subject;

/**
 * Created by HP on 1/20/2018.
 */

public class FragRequests extends Fragment {
    ListView list;
    View view;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String currentURL;
    String JSON_STRING;
    ArrayList<FragRequestsData> dataArrayList = new ArrayList<>();
    MyAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.requests, container, false);

        list = (ListView)  view.findViewById(R.id.listDonorRequests);
        preferences = getActivity().getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentURL = preferences.getString("URL", "");

        new dataFetch().execute();
        return view;
    }


    class dataFetch extends AsyncTask<String,Void,String>{

        String requestUrl = currentURL+"DonateIt/requests.php";

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
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                dataArrayList.clear();

                while (count<jsonArray.length())
                {
                    jsonObject = jsonArray.getJSONObject(count);
                    String id = (jsonObject.getString("id"));
                    String title = (jsonObject.getString("title"));
                    String desc= (jsonObject.getString("description"));
                    int progress= (jsonObject.getInt("progress"));
                    int target = (jsonObject.getInt("goal"));

                    FragRequestsData data = new FragRequestsData(id, title, desc, progress, target );
                    dataArrayList.add(data);

                    count++;
                }

                adapter = new MyAdapter(getActivity(), dataArrayList);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        String id_ = ((TextView) view.findViewById(R.id.idDonorRequestsList)).getText().toString();
                        String title = ((TextView) view.findViewById(R.id.titleDonorRequestsList)).getText().toString();
                        String desc= ((TextView) view.findViewById(R.id.descDonorRequestsList)).getText().toString();
                        String progress= ((TextView) view.findViewById(R.id.progressDonorRequestsList)).getText().toString();
                        String target = ((TextView) view.findViewById(R.id.targetAmountDonorRequestsList)).getText().toString();

                        Intent intent = new Intent(getActivity(), FragRequestsDetails.class);
                        intent.putExtra("ID", id_);
                        intent.putExtra("Title", title);
                        intent.putExtra("Desc", desc);
                        intent.putExtra("Progress", progress);
                        intent.putExtra("Target", target);

                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class MyAdapter extends BaseAdapter {

        ArrayList<FragRequestsData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<FragRequestsData> arrayList) {
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
                convertView = inflater.inflate(R.layout.frag_full_requests, null);
                holder = new ViewHolder();
                holder.tvID = convertView.findViewById(R.id.idDonorRequestsList);
                holder.tvTitle = convertView.findViewById(R.id.titleDonorRequestsList);
                holder.tvDesc = convertView.findViewById(R.id.descDonorRequestsList);
                holder.tvTarget = convertView.findViewById(R.id.targetAmountDonorRequestsList);
                holder.tvProgress = convertView.findViewById(R.id.progressDonorRequestsList);
                holder.progressBar = convertView.findViewById(R.id.pbProgressDonorRequestsList);
                holder.imageView = convertView.findViewById(R.id.savedDonorRequestsList);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            preferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
            String user = preferences.getString("Username", "");
            preferences = getActivity().getSharedPreferences(user+"_SavedRequests", Context.MODE_PRIVATE);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
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
                    new dataFetch().execute();
                }
            });
            holder.progressBar.setMax(arrayList.get(position).getTarget());
            holder.progressBar.setProgress(arrayList.get(position).getProgress());
            holder.tvID.setText(arrayList.get(position).getID());
            holder.tvTitle.setText(arrayList.get(position).getTitle());
            holder.tvDesc.setText(arrayList.get(position).getDescription());
            holder.tvProgress.setText(""+arrayList.get(position).getProgress());
            holder.tvTarget.setText(""+arrayList.get(position).getTarget());

            if (preferences.getBoolean(arrayList.get(position).getID(),false)) {
                holder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.filledheart));
            } else {
                holder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.emptyheart));
            }

            return convertView;
        }

        class ViewHolder {
            TextView tvID,tvTitle, tvDesc, tvProgress, tvTarget;
            ProgressBar progressBar;
            ImageView imageView;
        }


    }


}
