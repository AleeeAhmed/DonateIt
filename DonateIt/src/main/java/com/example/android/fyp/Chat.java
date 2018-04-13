package com.example.android.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Chat extends AppCompatActivity {

    ListView listView;
    ArrayList<ChatData> dataArrayList = new ArrayList<>();
    SharedPreferences preferences;
    JSONArray jsonArray;
    JSONObject jsonObject;
    MyAdapter adapter;
    String currentURL, msgFrom, msgId,currentDate,message;
    EditText edNewMsg;
    Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listView = (ListView) findViewById(R.id.lvChat);
        edNewMsg = (EditText) findViewById(R.id.edNewMsg);
        btnSend = (Button) findViewById(R.id.btnSendMessage);

        preferences = getSharedPreferences("HiddenUrl", Context.MODE_PRIVATE);
        currentURL = preferences.getString("URL", "");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            msgId = getIntent().getStringExtra("MessageId");
            msgFrom = bundle.getString("MessageFrom");
        }

        getCurrentDate();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = edNewMsg.getText().toString().trim();
                if (!message.isEmpty()) {
                    new dataFetchForNewMessage(msgId, msgFrom,currentDate,message).execute();
                } else {
                    Toast.makeText(Chat.this, "Message cannot be sent empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new dataFetch(msgId).execute();

    }
    private void getCurrentDate() {
        currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    private void scrollToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    class dataFetch extends AsyncTask<String,Void,String> {

        String JSON_STRING, msgId;


        dataFetch(String messageId) {
            msgId = messageId;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String requestUrl = currentURL+"DonateIt/getMessages.php?messageId="+msgId;
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

                        String msg = (jsonObject.getString("message"));
                        String date = (jsonObject.getString("date"));
                        String sender = (jsonObject.getString("messageFrom"));

                        ChatData data = new ChatData(msg,date,sender);
                        dataArrayList.add(data);
                    }
                }

                adapter = new MyAdapter(Chat.this, dataArrayList);
                listView.setAdapter(adapter);
                scrollToBottom();

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
    class dataFetchForNewMessage extends AsyncTask<String,Void,String> {

        String JSON_STRING, msgId,sender, date, msg;


        dataFetchForNewMessage(String messageId, String msgFrom, String currentDate, String message) {
            msgId = messageId;
            sender = msgFrom;
            date = currentDate;
            msg = message;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String requestUrl = currentURL+"DonateIt/newMessage.php?" +
                        "messageId="+msgId+"&message="+msg+"&date="+date+"&messageFrom="+sender;
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
            try {
                jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");

                if (success.equalsIgnoreCase("true")) {
                    edNewMsg.setText("");
                    Toast.makeText(Chat.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    new dataFetch(msgId).execute();
                } else {
                    edNewMsg.setText("");
                    Toast.makeText(Chat.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    class MyAdapter extends BaseAdapter {

        ArrayList<ChatData> arrayList = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<ChatData> arrayList) {
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
            ChatData data = (ChatData) getItem(position);
            String user = data.getSender();
            if (convertView == null||
                    !((ViewHolder) convertView.getTag()).tvSender.equals(data.getSender())) {
                if (user.equalsIgnoreCase(msgFrom)) {
                    convertView = inflater.inflate(R.layout.right_chat, null);
                } else {
                    convertView = inflater.inflate(R.layout.left_chat, null);
                }

                holder = new ViewHolder();
                holder.tvMessage = convertView.findViewById(R.id.tvMessageChat);
                holder.tvDate = convertView.findViewById(R.id.tvDateChat);
                holder.tvSender = data.getSender();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvMessage.setText(arrayList.get(position).getMessage());
            holder.tvDate.setText(arrayList.get(position).getDate());

            return convertView;
        }

        class ViewHolder {
            TextView tvMessage, tvDate;
            String tvSender;
        }


    }

}
