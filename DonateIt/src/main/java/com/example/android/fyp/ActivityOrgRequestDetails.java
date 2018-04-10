package com.example.android.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by HP on 1/26/2018.
 */

public class ActivityOrgRequestDetails extends AppCompatActivity {
    public static TextView title;
    public static TextView desc;


    public void onCreate(Bundle savedInstanceState) {
        Log.i("debug", "request details activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_full_request_details);

        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);

        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        String titleS = bundle.getString("title");
        String descS = bundle.getString("desc");
        Log.i("debug", "request details view created and title is"+""+titleS);
        Log.i("debug", "request details view created and desc is"+""+descS);



            title.setText(titleS);
            desc.setText(descS);
            //desc.setText("We need 50 blanket to be disctributed in the thar this winter as powerty level is very high and high number of poeple can not buy it");

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.addTab(tabLayout.newTab().setText("Suggestions"));
            tabLayout.addTab(tabLayout.newTab().setText("Invitations Sent"));
            tabLayout.addTab(tabLayout.newTab().setText("Offers"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            final MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }


            class MyAdapter extends FragmentStatePagerAdapter {
                int tabNums;

                public MyAdapter(FragmentManager fm, int tabNums) {
                    super(fm);
                    this.tabNums = tabNums;

                }

                /**
                 * Return fragment with respect to Position .
                 */
                @Override
                public Fragment getItem(int position) {

                    switch (position) {
                        case 0:
                            return new FragOrgSuggestions();
                        case 1:
                            return new FragOrgInvitationsSent();
                        case 2:
                            return new FragOrgOffers();
                        default:
                            return null;
                    }
                }

                @Override
                public int getCount() {
                    return tabNums;
                }
            }











/*
package com.example.android.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

*/
/**
 * Created by HP on 1/26/2018.
 *//*


public class ActivityOrgRequestDetails extends AppCompatActivity {
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        Toast.makeText(this,""+title,Toast.LENGTH_SHORT).show();

        setFragment(new FragOrgRequestDetails());

    }
    public void setFragment(Fragment fragment ) {
        if (fragment != null) {

            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }
    }
}
*/
