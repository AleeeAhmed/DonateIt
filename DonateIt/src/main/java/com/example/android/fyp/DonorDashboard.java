package com.example.android.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DonorDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_donor_dashboard);
        setSupportActionBar(toolbar);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutDonorDashboard, new FragRequests()).addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_donor_dashboard);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_donor_dashboard);
        navigationView.setNavigationItemSelectedListener(this);

        preferences = getSharedPreferences("Login", MODE_PRIVATE);

        View header = navigationView.getHeaderView(0);
        ImageView img = header.findViewById(R.id.imageViewDonorDashboard);
        TextView tvName = header.findViewById(R.id.headerNameDonorDashboard);
        tvName.setText(preferences.getString("Username",""));
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorDashboard.this, UserDetails.class);
                startActivity(intent);
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorDashboard.this, UserDetails.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_donor_dashboard);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(DonorDashboard.this, R.style.Theme_AppCompat_Light_Dialog_Alert)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.donor_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_requests_donor_dashboard) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutDonorDashboard, new FragRequests()).addToBackStack(null).commit();
        } else if (id == R.id.nav_saved_donor_dashboard) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutDonorDashboard, new FragSavedRequests()).addToBackStack(null).commit();
        } else if (id == R.id.nav_messages_donor_dashboard) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutDonorDashboard, new FragDonorMessages()).addToBackStack(null).commit();
        } else if (id == R.id.nav_invitations_donor_dashboard) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutDonorDashboard, new FragOrgInvitationsSent()).addToBackStack(null).commit();
        } else if (id == R.id.nav_donationHistory_donor_dashboard) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutDonorDashboard, new FragDonationHistory()).addToBackStack(null).commit();
        } else if (id == R.id.nav_settings_donor_dashboard) {
            Toast.makeText(this, "Nothing here..", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout_donor_dashboard) {
            preferences = getSharedPreferences("Login", MODE_PRIVATE);
            editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(DonorDashboard.this, ActivityValidationReg.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_donor_dashboard);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
