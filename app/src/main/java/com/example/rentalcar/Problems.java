package com.example.rentalcar;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.LENGTH_LONG;

public class Problems extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String text;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button button= (Button)findViewById(R.id.button1);
        editText=findViewById(R.id.editTextProblems);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                text=editText.getText().toString();
                insert_problem();
            }
        });
    }

    public void insert_problem(){
        HttpURLConnection client = null;
        URL url;
        try {
            url = new URL("http://rentalcar.altervista.org/inserisci_problemi.php?Problema=" +
                    URLEncoder.encode(this.text,"UTF-8")
            );
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                Toast.makeText(this, "segnalazione inviata", LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.problems, menu);
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
            Intent h=new Intent(Problems.this,EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(Problems.this,Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(Problems.this,Problems.class);
             startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(Problems.this,faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(Problems.this,MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(Problems.this,AdminActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
