package com.example.android.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HP on 1/20/2018.
 */

public class FragRequestTabs extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;//total number of tabs
    View x;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        if (x==null) {
            try {
                x = inflater.inflate(R.layout.frag_request_tabs, null);
                tabLayout = (TabLayout) x.findViewById(R.id.tabs);
                viewPager = (ViewPager) x.findViewById(R.id.viewpager);
                /**
                 *Set an Apater for the View Pager
                 */
                viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
                /**
                 * Now , this is a workaround ,
                 * The setupWithViewPager dose't works without the runnable .
                 * Maybe a Support Library Bug .
                 */

            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                }
            });

                tabLayout.setupWithViewPager(viewPager);
            }catch (Exception ex)
            {
                Log.d("Error", "onCreateView: ");
            }
        }
        return x;
    }
    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */
        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 :
                    return new FragRequests();
                case 1 :
                    return new FragSearches();
                case 2 :
                    return new FragSavedRequests();
            }
            return null;
        }
        @Override
        public int getCount() {
            return int_items;
        }
        /**
         * This method returns the title of the tab according to the position.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :
                    return "Requests";
                case 1 :
                    return "Search";
                case 2 :
                    return "Saved";
            }
            return null;
        }
    }
}