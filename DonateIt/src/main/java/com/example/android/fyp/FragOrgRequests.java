package com.example.android.fyp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
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

public class FragOrgRequests extends Fragment {

    ////this is not used anywhere... can be deleted

    String JSON_STRING;
    ArrayList<String> title = new ArrayList<>(), desc = new ArrayList<>();
    ArrayList<Integer> invitations = new ArrayList<>(), offers = new ArrayList<>();
    ArrayAdapter<String> adapter;

    FragmentManager fragmentManager;
    ListView list;
    TextView progress, targetAmount;
    View view;
    Button post;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("debug", "org data class created" );
        view = inflater.inflate(R.layout.frag_org_requests, container, false);
        list = (ListView) view.findViewById(R.id.list);
        post = (Button) view.findViewById(R.id.btnNewRequest);
        Log.i("debug", "org data loading started" );


        /*String[] title = {"50 Blankets are needed", "2 million $ are needed", "Volenteers are needed"};
        String[] invitations = {"3", "30", "10"};
        String[] offers = {"10", "15", "17"};
        int[] progress = {10, 90, 50};*/

        MyListAdapterOrgRequests adapterOrgRequests = new MyListAdapterOrgRequests(getContext(), title, invitations, offers);
        Log.i("debug", "setadapter called" );
        list.setAdapter(adapterOrgRequests);
        new datafetch().execute();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getContext(), ActivityOrgRequestDetails.class);
                Log.i("debug", "org title passed is"+""+title.get(position) );
                Bundle bundle = new Bundle();
                bundle.putString("title",title.get(position));
                bundle.putString("desc",desc.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new FragOrgPostRequestOverview());
            }
        });
        return view;

    }

    class datafetch extends AsyncTask<String,Void,String> {


        String object;

        JSONObject jsonObject;
        JSONArray jsonArray;

        String requestUrl = "DonateIt/orgrequests.php";

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
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                object =  stringBuilder.toString().trim();
                Log.i("debug", "org object is"+""+object );

            } catch (MalformedURLException e) {
                e.printStackTrace();
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
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;

                Log.i("debug", "org post end of try" );
                while (count<jsonArray.length())
                {
                    Log.i("debug", "org post while loop started" );
                    JSONObject jo = jsonArray.getJSONObject(count);
                    title.add(jo.getString("title"));
                    invitations.add(jo.getInt("invitations"));
                    offers.add(jo.getInt("offers"));
                    desc.add(jo.getString("description"));

                    Log.i("debug", "org data" + ""+jo.getString("title")+""+jo.getString("invitations")+""+jo.getString("offers"));

                    //adapter.notifyDataSetChanged();
                    count++;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }



    public class MyListAdapterOrgRequests extends ArrayAdapter<String> {

        private final Activity context;
        private ArrayList<String> title;
        private  ArrayList<Integer> invitations;
        private  ArrayList<Integer> offers;

        public MyListAdapterOrgRequests(Context context, ArrayList<String> title, ArrayList<Integer> invitations,
                                        ArrayList<Integer> offers )
        {
            super(context, R.layout.full_org_requests, title);
            // TODO Auto-generated constructor stub

            this.context= (Activity) context;
            this.title=title;
            this.invitations=invitations;
            this.offers= offers;
        }

        public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.full_org_requests, null,true);

            ;

            TextView titleT = (TextView) rowView.findViewById(R.id.title);
            TextView invitationT = (TextView) rowView.findViewById(R.id.invitations_sent);
            TextView offerT = (TextView) rowView.findViewById(R.id.offers);
            progress = (TextView) rowView.findViewById(R.id.progress);
            targetAmount = (TextView) rowView.findViewById(R.id.targetAmount);


            titleT.setText(title.get(position));
            invitationT.setText(invitations.get(position) + " Invitation sent");
            offerT.setText(offers.get(position) + " Offers");

            return rowView;

        };
    }
    public void setFragment(Fragment fragment ){
        if(fragment!=null){

            fragmentManager=getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        }
        //DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
    }
}
