package com.example.rentalcar.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText editTextPassword;
    EditText editTextEmail;
    Button buttonReservationAdmin;
    Button modifyStationButton;
    Button modifyCarButton;
    String email;
    String password;

    int flag;//vale 0 se clicchiamo sul bottone per visualizzare le prenotazioni, 1 se clicchiamo quello per le stazioni
    //2 se clicchiamo quello per le auto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editTextPassword=findViewById(R.id.password_adminedittext);
        editTextEmail=findViewById(R.id.email_adminedittext);
        buttonReservationAdmin=findViewById(R.id.reservation_admin);
        modifyStationButton=findViewById(R.id.station_admin_button);
        modifyCarButton=findViewById(R.id.car_Admin_button);

        //al click del bottone conferma prende ciò che è scritto sulle due edittext le salva in due stringhe e chiama il metodo check_authentication()
        buttonReservationAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //a seconda del bottone che clicchiamo andiamo a modificare il valore della variabile flag
                flag=0;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                check_authentication(flag);
            }
        });

        modifyStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                check_authentication(flag);
            }
        });

        modifyCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=2;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                check_authentication(flag);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    private void check_authentication(int flag) {
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
                if (flag==0) {
                    Intent i=new Intent(AdminActivity.this, ShowPrenotationsActivity.class);
                    startActivity(i);
                }
                else if (flag==1) {
                    Intent i=new Intent(AdminActivity.this, AddStationsActivity.class);
                    startActivity(i);
                }
                else if(flag==2) {
                    Intent i=new Intent(AdminActivity.this, AddRemoveCarActivity.class);
                    startActivity(i);
                }
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
        getMenuInflater().inflate(R.menu.admin, menu);
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
            Intent h=new Intent(AdminActivity.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(AdminActivity.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(AdminActivity.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(AdminActivity.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(AdminActivity.this, MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(AdminActivity.this,AdminActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
