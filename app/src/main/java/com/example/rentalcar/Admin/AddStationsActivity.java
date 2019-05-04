package com.example.rentalcar.Admin;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalcar.LateralMenu.Contacts;
import com.example.rentalcar.LateralMenu.EditReservation;
import com.example.rentalcar.LateralMenu.Problems;
import com.example.rentalcar.LateralMenu.faq;
import com.example.rentalcar.LinkedReservationClasses.ReadResponse;
import com.example.rentalcar.MainPathReservation.MainActivity;
import com.example.rentalcar.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.LENGTH_LONG;

public class AddStationsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button addStation;
    Button removeStation;
    TextView stationEt;
    String stationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addStation=findViewById(R.id.add_station_button);
        removeStation=findViewById(R.id.remove_station_button);
        stationEt=findViewById(R.id.add_station_et);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se il campo è vuoto parte un toast
                if (stationEt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Per favore inserisci il nome della stazione da aggiungere",Toast.LENGTH_LONG).show();
                }
                else{
                    //altrimenti prende il nome della stazione e chiama il metodo add_station
                    stationName=stationEt.getText().toString();
                    add_station();
                }
            }
        });

        removeStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se il campo è vuoto parte un toast
                if (stationEt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Per favore inserisci il nome della stazione da rimuovere",Toast.LENGTH_LONG).show();
                }
                else{
                    //altrimenti prende il nome della stazione e chiama il metodo remove_station
                    stationName=stationEt.getText().toString();
                    remove_station();
                }

            }
        });
    }

    public void add_station(){
        HttpURLConnection client = null;
        URL url;
        try {
            //sempre solita cosa
            url = new URL("http://rentalcar.altervista.org/aggiungi_stazioni.php?NomeStazione=" + URLEncoder.encode(this.stationName,"UTF-8"));
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                Toast.makeText(this, "Inserimento effettuato", LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Errore nell'inserimento", LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }

    }

    public void remove_station(){
        HttpURLConnection client = null;
        try {
            //passiamo soltanto il nome della stazione
            URL url = new URL("http://rentalcar.altervista.org/elimina_stazioni.php?NomeStazione="+this.stationName);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                Toast.makeText(this, "La stazione è stata cancellata!", LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Errore nell'eliminazione", LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
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
        getMenuInflater().inflate(R.menu.add_stations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent h=new Intent(AddStationsActivity.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(AddStationsActivity.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(AddStationsActivity.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(AddStationsActivity.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(AddStationsActivity.this, MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(AddStationsActivity.this, AdminActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
