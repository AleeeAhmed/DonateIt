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
import android.widget.TextView;

/**
 * Created by HP on 1/25/2018.
 */

public class FragOrgRequestDetails extends Fragment {
    public static TextView title;
    public static TextView desc;


    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;//total number of tabs
    View x;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        if (x == null) {
            try {


                x = inflater.inflate(R.layout.frag_full_request_details, null);
                title = (TextView) x.findViewById(R.id.title);
                desc = (TextView) x.findViewById(R.id.desc);
                tabLayout = (TabLayout) x.findViewById(R.id.tabs);
                viewPager = (ViewPager) x.findViewById(R.id.viewpager);
                title.setText("50 Blankets are needed");
                desc.setText("We need 50 blanket to be disctributed in the thar this winter as powerty level is very high and high number of poeple can not buy it");


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

            } catch (Exception ex) {
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
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragOrgSuggestions();
                case 1:
                    return new FragOrgInvitationsSent();
                case 2:
                    return new FragOrgOffers();
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
            switch (position) {
                case 0:
                    return "Suggestions";
                case 1:
                    return "Invitations Sent";
                case 2:
                    return "Offers";
            }
            return null;
        }

//        public class MyListAdapterRequestDetails extends ArrayAdapter<String> {
//
//            private final Activity context;
//            private final String[] title;
//            private final String[] desc;
//            private final ViewPager viewPager;
//            private final TabLayout tabLayout;
//
//            public MyListAdapterRequestDetails(Context context, String[] title, String[] desc, ViewPager viewPager, TabLayout tabLayout) {
//                super(context, R.layout.frag_full_request_details, title);
//                // TODO Auto-generated constructor stub
//
//                this.context = (Activity) context;
//                this.title = title;
//                this.desc = desc;
//                this.viewPager = viewPager;
//                this.tabLayout = tabLayout;
//
//            }
//
//            public View getView(int position, View view, ViewGroup parent) {
//                LayoutInflater inflater = context.getLayoutInflater();
//                View rowView = inflater.inflate(R.layout.frag_full_request_details, null, true);
//
//                TextView titleT = (TextView) rowView.findViewById(R.id.title);
//                ImageView image = (ImageView) rowView.findViewById(R.id.imageView);
//                TextView descT = (TextView) rowView.findViewById(R.id.desc);
//                ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
//
//                titleT.setText(title[position]);
//                descT.setText(desc[position]);
//
//                return rowView;
//
//            }
//
//            ;
//    }
        }
    }
