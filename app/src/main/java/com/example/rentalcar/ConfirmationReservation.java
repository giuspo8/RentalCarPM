package com.example.rentalcar;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConfirmationReservation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String stazione_ritiro;
    String stazione_resti;
    int anno_ritiro;
    int mese_ritiro;
    int giorno_ritiro;
    int anno_restituzione;
    int mese_restituzione;
    int giorno_restituzione;
    int ora_ritiro;
    int minuto_ritiro;
    int ora_restituzione;
    int minuto_restituzione;
    int res_image;
    String model;
    String Class_car;
    double prezzo;
    String shift;
    int num_pass;
    int payment=0;
    String email;
    String name;
    String surname;
    String telephone;

    TextView emailtv;
    TextView nametv;
    TextView surnametv;
    TextView telephonetv;

    Button conferma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        conferma = (Button) findViewById(R.id.confpre);
        emailtv=findViewById(R.id.email);
        nametv=findViewById(R.id.Nome);
        surnametv=findViewById(R.id.Cognome);
        telephonetv=findViewById(R.id.telefono);

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lettura di tutti i dati
                email= emailtv.getText().toString();
                name= nametv.getText().toString();
                surname= surnametv.getText().toString();
                telephone= telephonetv.getText().toString();
                Bundle restitution=getIntent().getBundleExtra("dati_pre");
                stazione_ritiro=restitution.getString("stazione_ritir");
                Toast.makeText(getApplicationContext(), stazione_ritiro, Toast.LENGTH_LONG).show();
                stazione_resti=restitution.getString("stazione_ric");
                Toast.makeText(getApplicationContext(), stazione_resti, Toast.LENGTH_LONG).show();
                anno_ritiro=restitution.getInt("anno_ritiro");
                mese_ritiro=restitution.getInt("mese_ritiro");
                giorno_ritiro=restitution.getInt("giorno_ritiro");
                anno_restituzione=restitution.getInt("anno_ric");
                mese_restituzione=restitution.getInt("mese_ric");
                giorno_restituzione=restitution.getInt("giorno_ric");
                ora_ritiro=restitution.getInt("ora_ritiro");
                minuto_ritiro=restitution.getInt("minuto_ritiro");
                ora_restituzione=restitution.getInt("ora_ric");
                minuto_restituzione=restitution.getInt("minuto_ric");
                res_image=restitution.getInt("res_image",R.drawable.alfaromeogiulietta);
                model=restitution.getString("modl");
                Class_car=restitution.getString("clas");
                prezzo=restitution.getDouble("prezo",0.0);
                shift=restitution.getString("shit");
                num_pass=restitution.getInt("nuP",0);
                Toast.makeText(getApplicationContext(), "prenotazione effetuata paga in stazione", Toast.LENGTH_LONG).show();
                insert_reservation();
                insert_user();
            }
        });
    }

    private void insert_reservation(){
        HttpURLConnection client = null;
        URL url;
        try {
                url = new URL("http://rentalcar.altervista.org/inserisci_prenotazione.php?StazioneRit=" + this.stazione_ritiro
                        + "&StazioneRic=" + this.stazione_resti + "&Macchina=" + this.model
                        + "&Email=" + this.email+ "&AnnoRit=" + this.anno_ritiro
                        + "&AnnoRic=" + this.anno_restituzione+ "&MeseRit=" + this.mese_ritiro
                        + "&MeseRic=" + this.mese_restituzione+ "&GiornoRit=" + this.giorno_ritiro
                        + "&GiornoRic=" + this.giorno_restituzione+ "&OraRit=" + this.ora_ritiro
                        + "&OraRic=" + this.ora_restituzione+ "&MinutoRit=" + this.minuto_ritiro
                        + "&MinutoRic=" + this.minuto_restituzione+ "&Pagamento=" + this.payment);
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");
                client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

    private void insert_user(){
        HttpURLConnection client = null;
        URL url;
        try {
            url = new URL("http://rentalcar.altervista.org/inserisci_utenti.php?Nome=" + this.name
                    + "&Cognome=" + this.surname + "&Telefono=" + this.telephone
                    + "&Email=" + this.email);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.confirmation_reservation, menu);
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
            Intent h=new Intent(ConfirmationReservation.this,EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(ConfirmationReservation.this,Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(ConfirmationReservation.this,Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(ConfirmationReservation.this,faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(ConfirmationReservation.this,MainActivity.class);
            startActivity(h1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
