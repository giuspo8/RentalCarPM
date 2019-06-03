package com.example.rentalcar.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;

public class Admin_log extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText editTextPassword;
    EditText editTextEmail;
    Button loginButton;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editTextPassword=findViewById(R.id.password_adminedittext);
        editTextEmail=findViewById(R.id.email_adminedittext);
        loginButton=findViewById(R.id.login_admin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (email.contains("@") && email.contains(".") && !email.contains("\""))
                {
                    check_authentication();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"E-mail non corretta.",LENGTH_LONG).show();
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void check_authentication() {
        HttpURLConnection client = null;
        URL url;
        try {
            // se la richiesta è GET gli passiamo email e password letti sulle edittext
            url = new URL("http://rentalcar.altervista.org/leggiAdmin.php?email=" + URLEncoder.encode(this.email,"UTF-8")+
                    "&password=" + URLEncoder.encode(this.password,"UTF-8"));
            //apriamo la connessione e settiamo il metodo come GET(facoltativo)
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            //prendiamo lo stream in ingresso
            InputStream in = client.getInputStream();
            //creiamo una nuova stringa e la mettiamo in una certa forma (vedi readresponse)
            String json_string = ReadResponse.readStream(in).trim();
            //se il risultato è [] vuol dire che non è stato trovato nessun risultato corrispondente
            if (json_string.equals("[]")) {
                Toast.makeText(this,"I dati inseriti sono errati! Prego Riprovare",Toast.LENGTH_LONG).show();
            }
            //se invece abbiamo trovato qualcosa allora possiamo garantire l'ingresso alla successiva activity
            //in base al valore della variabile flag
            else {
                Intent i = new Intent(Admin_log.this, Admin_button.class);
                startActivity(i);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_log, menu);
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
            Intent h=new Intent(Admin_log.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(Admin_log.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(Admin_log.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(Admin_log.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(Admin_log.this, MainActivity.class);
            startActivity(h1);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
