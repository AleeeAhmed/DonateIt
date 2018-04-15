package com.example.android.fyp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by HP on 1/27/2018.
 */

public class ActivityValidationReg extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_registration);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_Validation);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_Validation);
        tabs.setupWithViewPager(viewPager);

    }
    private class Adapter extends FragmentPagerAdapter {


        Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return  new FragLogIn();
                case 1: return  new FragSignUp();
                default:{}

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "Sign In";
                case 1:
                    return "Sign Up";
                default:
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new AlertDialog.Builder(ActivityValidationReg.this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setTitle("Closing DonateIt")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
