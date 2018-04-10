package com.example.android.fyp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by HP on 1/25/2018.
 */

public class FragOrgMessages extends Fragment {
    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.org_messages, container, false);
        list = (ListView) view.findViewById(R.id.list);

        String[] names = {"Abid", "Sajid", "Saleem", "Shahid Ullah", "Ismat Ullah","Awais","Akhtar Zaman"};

        String[] post = {"50M Dollars", "Water Purifying Scheme", "Shelters program", "Primary education", "Cancer Hospital","Help needed","World Food Campaign"};

        String[] lastMessage = {"Okay", "I will be waiting", "No problem","I appreciate it", "Thank you","Let's see","why not"};

        Integer[] img = {R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo,R.drawable.logo};


        MyListAdapterOrgMessages adapter=new MyListAdapterOrgMessages(getContext(), names,post,lastMessage, img);
        list.setAdapter(adapter);


        return view;
    }

    public class MyListAdapterOrgMessages extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] name ;
        private final String[] lastMessage;
        private final String[] post ;
        private final Integer[] img;

        public MyListAdapterOrgMessages(Context context, String[] name,String[] post, String[] lastMessage, Integer[] img) {
            super(context, R.layout.full_frag_messages, name);
            // TODO Auto-generated constructor stub

            this.context= (Activity) context;
            this.name=name;
            this.lastMessage=lastMessage;
            this.post = post;
            this.img = img;

        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.full_frag_messages, null,true);

            TextView nameT = (TextView) rowView.findViewById(R.id.name);
            TextView messageT = (TextView) rowView.findViewById(R.id.last_message);
            //TextView postT = (TextView) rowView.findViewById(R.id.post);
            ImageView imaget = (ImageView) rowView.findViewById(R.id.image);

            nameT.setText(name[position]);
            messageT.setText(lastMessage[position]);
            //postT.setText(post[position]);
            imaget.setImageResource(img[position]);

            return rowView;

        };
    }
}
