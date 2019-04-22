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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;

public class ConfirmationReservationCard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String stazione_ritiro;
    String stazione_resti;

    String model;
    int payment=0;//vale 0 se pago in stazione, 1 se pago ora
    String email;
    String name;
    String surname;
    String telephone;

    TextView emailtv;
    TextView nametv;
    TextView surnametv;
    TextView telephonetv;

    TextView cardNumbertv;
    TextView expireDatetv;
    TextView secureCodetv;

    String creditCardNumber;
    String creditCardExpireDate;
    int creditCardSecureCode;
    double creditCardLeft=500;//soldi che metto sulla carta di credito fittizia

    double totalPrice;


    String ritiro;
    String restituzione;

    boolean payNow;




    Button conferma;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_reservation_card);
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

        cardNumbertv=findViewById(R.id.numerocarta);
        expireDatetv=findViewById(R.id.datascadenza);
        secureCodetv=findViewById(R.id.codicesicurezza);

        payNow=getIntent().getBooleanExtra("now",false);

        if (payNow){
            //se il cliente paga ora rendiamo visibili le textview delle carte di credito
            cardNumbertv.setVisibility(View.VISIBLE);
            expireDatetv.setVisibility(View.VISIBLE);
            secureCodetv.setVisibility(View.VISIBLE);
        }

        //prendiamo i dati dall'activity precedente
        Bundle restitution=getIntent().getBundleExtra("dati_pre");
        stazione_ritiro=restitution.getString("stazione_ritir");
        stazione_resti=restitution.getString("stazione_ric");

        totalPrice=restitution.getDouble("prezo",0.0);
        model=restitution.getString("modl");

        ritiro=restitution.getString("ritiro");
        restituzione=restitution.getString("restituzione");


        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //lettura di tutti i dati
                email= emailtv.getText().toString();
                name= nametv.getText().toString();
                surname= surnametv.getText().toString();
                telephone= telephonetv.getText().toString();

                //se non sono stati riempiti i campi relativi all'anagrafica da errore
                if (emailtv.getText().toString().equals("")||nametv.getText().toString().equals("")||
                        surnametv.getText().toString().equals("")||telephonetv.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Per favore inserisci tutti i dati obbligatori!!",LENGTH_LONG).show();
                }
                else {
                    //se pago ora leggo i dati della carta di credito e controllo che abbia credito sufficiente
                    if (payNow) {
                        //se non sono stati riempiti i campi relativi alla carta di credito da messaggio di errore
                        if (cardNumbertv.getText().toString().equals("")||expireDatetv.getText().toString().equals("")||
                                secureCodetv.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),"Per favore inserisci tutti i dati obbligatori!!",LENGTH_LONG).show();
                        }
                        else{
                            payment=1;//setto payment a 1
                            creditCardNumber = cardNumbertv.getText().toString();
                            creditCardExpireDate = expireDatetv.getText().toString();
                            creditCardSecureCode = Integer.parseInt(secureCodetv.getText().toString());
                            //creo un nuovo oggetto carta di credito con i dati ottenuti
                            CreditCard cd1 = new CreditCard(creditCardNumber, creditCardExpireDate, creditCardSecureCode, creditCardLeft);
                            //chiama un metodo che controlla se il credito è sufficiente
                            if (!confirm_credit_card(cd1, totalPrice)) {
                                Toast.makeText(getApplicationContext(), "il credito disponibile sulla carta non è sufficiente", LENGTH_LONG).show();
                            } else {
                                //paghiamo e inseriamo nel database prenotazione e utente
                                cd1.getPayment(totalPrice);
                                insert_reservation();
                                insert_user();
                                //andiamo all activity finale
                                //qui inserire metodo per mandare email
                                Intent i=new Intent(ConfirmationReservationCard.this,EmailFinalActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                    else {
                        //inseriamo nel database prenotazione e utente
                        insert_reservation();
                        insert_user();
                        //andiamo all activity finale
                        //qui inserire metodo per mandare email
                        Intent i=new Intent(ConfirmationReservationCard.this,EmailFinalActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
    }

    private void insert_reservation(){
        HttpURLConnection client = null;
        URL url;
        try {
            url = new URL("http://rentalcar.altervista.org/inserisci_prenotazione.php?StazioneRit=" + this.stazione_ritiro
                    + "&StazioneRic=" + this.stazione_resti + "&Macchina=" + this.model
                    + "&Email=" + this.email+ "&Pagamento=" + this.payment
                    + "&DataRitiro="+this.ritiro+"&DataRestituzione="+this.restituzione
                    +"&Prezzo="+this.totalPrice
            );
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

    private void insert_user(){
        HttpURLConnection client = null;
        URL url;
        try {
            //metodo get quindi scriviamo i dati da inviare direttamente nell'Url
            url = new URL("http://rentalcar.altervista.org/inserisci_utenti.php?Nome=" + this.name
                    + "&Cognome=" + this.surname + "&Telefono=" + this.telephone
                    + "&Email=" + this.email);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                //Toast.makeText(this, "Inserimento effettuato", LENGTH_LONG).show();
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

    private boolean confirm_credit_card(CreditCard cd,double price){
        if (cd.getPayment(price))
        return true;
        else return false;
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
