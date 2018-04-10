package com.example.android.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by HP on 3/21/2018.
 */

public class FragOrgPostRequestOverview extends Fragment {
    Button next;
    EditText title, description;
    View x;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *Inflate tab_layout and setup Views.
         */
        x = inflater.inflate(R.layout.request_overview, null);
        title = (EditText) x.findViewById(R.id.etTitle);
        description = (EditText) x.findViewById(R.id.etDescription);
        next = (Button) x.findViewById(R.id.btnNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleS = title.getText().toString();
                String descriptionS = description.getText().toString();

                Bundle arguments = new Bundle();
                arguments.putString("title",titleS);
                arguments.putString("desc",descriptionS);
                FragPostRequestDetails fragPostRequestDetails = new FragPostRequestDetails();
                fragPostRequestDetails.setArguments(arguments);
                setFragment(fragPostRequestDetails);
            }
        });



        return x;
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
