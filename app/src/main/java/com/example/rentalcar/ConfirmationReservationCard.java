package com.example.rentalcar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class ConfirmationReservationCard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button conferma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_reservation_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        conferma = (Button) findViewById(R.id.confpre);

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lettura di tutti i dati
                Bundle restitution=getIntent().getBundleExtra("dati_pre");
                String Stazione_ritiro=restitution.getString("stazione_ritir");
                Toast.makeText(getApplicationContext(), Stazione_ritiro, Toast.LENGTH_LONG).show();
                String Stazione_resti=restitution.getString("stazione_ric");
                Toast.makeText(getApplicationContext(), Stazione_resti, Toast.LENGTH_LONG).show();
                int anno_ritiro=restitution.getInt("anno_ritiro");
                int mese_ritiro=restitution.getInt("mese_ritiro");
                int giorno_ritiro=restitution.getInt("giorno_ritiro");
                int anno_restituzione=restitution.getInt("anno_ric");
                int mese_restituzione=restitution.getInt("mese_ric");
                int giorno_restituzione=restitution.getInt("giorno_ric");
                int ora_ritiro=restitution.getInt("ora_ritiro");
                int minuto_ritiro=restitution.getInt("minuto_ritiro");
                int ora_restituzione=restitution.getInt("ora_ric");
                int minuto_restituzione=restitution.getInt("minuto_ric");
                int res_image=restitution.getInt("res_image",R.drawable.alfaromeogiulietta);
                String model=restitution.getString("modl");
                String Class_car=restitution.getString("clas");
                double prezzo=restitution.getDouble("prezo",0.0);
                String shift=restitution.getString("shit");
                int num_pass=restitution.getInt("nuP",0);
                Toast.makeText(getApplicationContext(), "prenotazione effetuata paga ora", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirmation_reservation_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
