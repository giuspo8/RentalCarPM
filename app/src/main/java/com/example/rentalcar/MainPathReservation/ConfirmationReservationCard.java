package com.example.rentalcar.MainPathReservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.rentalcar.Admin.Admin_log;
import com.example.rentalcar.LateralMenu.Contacts;
import com.example.rentalcar.LinkedReservationClasses.CreditCard;
import com.example.rentalcar.LateralMenu.EditReservation;
import com.example.rentalcar.LateralMenu.Problems;
import com.example.rentalcar.R;
import com.example.rentalcar.LinkedReservationClasses.ReadResponse;
import com.example.rentalcar.LateralMenu.faq;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

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
    //stringhe di righe per email
    String message1,message2,message3,message4,message5,message6,message7,message8;

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

    int id;

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
                    Toast.makeText(getApplicationContext(),"Per favore inserisci tutti i dati obbligatori.",LENGTH_LONG).show();
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
                                Toast.makeText(getApplicationContext(), "Il credito disponibile sulla carta non è sufficiente.", LENGTH_LONG).show();
                            } else {
                                //paghiamo e inseriamo nel database prenotazione e utente
                                cd1.getPayment(totalPrice);
                                insert_reservation();
                                insert_user();
                                //andiamo all activity finale
                                //leggiamo l'id che inseriremo anche nella mail
                                read_id();
                                message1="Caro "+name+" "+surname;
                                message2="La ringraziamo per averci scelto,";
                                message3="ecco il riepilogo della sua prenotazione:";
                                message4=" Id Prenotazione: "+id;
                                message5="Ritiro: "+stazione_ritiro+" "+ritiro;
                                message6="Restituzione: "+stazione_resti+" "+restituzione;
                                message7=model+"   "+totalPrice+" euro";
                                message8="Le auguriamo buon viaggio";
                                //metodo per mandare email
                                send_mail();
                                Intent i=new Intent(ConfirmationReservationCard.this, EmailFinalActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                    else {
                        //inseriamo nel database prenotazione e utente
                        insert_reservation();
                        insert_user();
                        //leggiamo l'id che inseriremo anche nella mail
                        read_id();
                        message1="Caro "+name+" "+surname;
                        message2="La ringraziamo per averci scelto,";
                        message3="ecco il riepilogo della sua prenotazione:";
                        message4=" Id Prenotazione: "+id;
                        message5="Ritiro: "+stazione_ritiro+" "+ritiro;
                        message6="Restituzione: "+stazione_resti+" "+restituzione;
                        message7=model+"   "+totalPrice+" euro";
                        message8="Le auguriamo buon viaggio";
                            //metodo per mandare email
                            send_mail();

                        //andiamo all activity finale
                        Intent i=new Intent(ConfirmationReservationCard.this,EmailFinalActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
    }

    public void read_id() {
        HttpURLConnection client = null;
        try {
            //stessa cosa di FindStationActivity
            URL url = new URL("http://rentalcar.altervista.org/leggi_id.php?Email=" + this.email
                    + "&DataRitiro=" + URLEncoder.encode(this.ritiro,"UTF-8")+
                    "&DataRestituzione=" + URLEncoder.encode(this.restituzione,"UTF-8") );
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();
            if (json_string.equals("[]")) {
                Toast.makeText(getApplicationContext(), "Nessuna prenotazione corrisponde ai dati inseriti", Toast.LENGTH_LONG).show();
            } else {
                JSONObject json_data = convert2JSON(json_string);
                Iterator<String> iter = json_data.keys();
                //metodo hasNext() ritorna true se ci sono ancora elementi
                while (iter.hasNext()) {
                    //metodo next() ritorna il prossimo elemento nell'iteratore (la chiave)
                    String key = iter.next();
                    try {
                        //ritorna il valore corrispondente alla chiave key
                        JSONObject value = json_data.getJSONObject(key);
                        id = value.getInt("ID");

                    } catch (JSONException e) {
                        Toast.makeText(this, "ERRORE", Toast.LENGTH_LONG).show();
                        // Something went wrong!
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (client != null) {
                client.disconnect();
            }
        }
    }

    private void insert_reservation(){
        HttpURLConnection client = null;
        URL url;
        try {
            String encodedURL=encode_url();//assegno alla stringa il risultato del metodo per fare l'encode dell'url(per gli spazi)
            url = new URL(encodedURL);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                //Toast.makeText(this, "Inserimento effettuato", LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Errore nell'inserimento", LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

    private void send_mail(){
        HttpURLConnection client = null;
        URL url;
        try {
            url = new URL("http://rentalcar.altervista.org/invio_email.php?Email=" + this.email +
                    "&msg1=" + URLEncoder.encode(this.message1,"UTF-8")+
                    "&msg2=" +URLEncoder.encode(this.message2,"UTF-8")+
                    "&msg3=" +URLEncoder.encode(this.message3,"UTF-8")+
                    "&msg4=" +URLEncoder.encode(this.message4,"UTF-8")+
                    "&msg5=" +URLEncoder.encode(this.message5,"UTF-8")+
                    "&msg6=" +URLEncoder.encode(this.message6,"UTF-8")+
                    "&msg7=" +URLEncoder.encode(this.message7,"UTF-8")+
                    "&msg8=" +URLEncoder.encode(this.message8,"UTF-8")
            );
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("")) {
                //Toast.makeText(this, "invio effettuato", LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Errore nell'invio", LENGTH_LONG).show();
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
            url = new URL("http://rentalcar.altervista.org/inserisci_utenti.php?Nome=" +
                    URLEncoder.encode(this.name,"UTF-8") +
                    "&Cognome=" + URLEncoder.encode(this.surname,"UTF-8") +
                    "&Telefono=" + this.telephone +
                    "&Email=" + URLEncoder.encode(this.email,"UTF-8"));
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                //Toast.makeText(this, "Inserimento effettuato", LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Errore nell'inserimento", LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

    String encode_url(){
        String encoded_url;
        try{
            //concateno le stringhe con le parti che mi interessano codificate
            encoded_url="http://rentalcar.altervista.org/inserisci_prenotazione.php?StazioneRit="+
                    URLEncoder.encode(this.stazione_ritiro,"UTF-8");
            encoded_url+="&StazioneRic=" +URLEncoder.encode(this.stazione_resti,"UTF-8");
            encoded_url+= "&Macchina=" + URLEncoder.encode(this.model,"UTF-8");
            encoded_url+= "&Email=" + this.email+ "&Pagamento=" + this.payment;
            encoded_url+= "&DataRitiro="+URLEncoder.encode(this.ritiro,"UTF-8");
            encoded_url+="&DataRestituzione="+URLEncoder.encode(this.restituzione,"UTF-8")+
                    "&Prezzo="+this.totalPrice;
            return encoded_url;
        }
        catch (Exception e){
        }
        return "";
    }


    private JSONObject convert2JSON(String json_data){
        JSONObject obj = null;
        //stessa cosa di FindStationActivity
        try {
            obj = new JSONObject(json_data);
            Log.d("My App", obj.toString());
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json_data + "\"");
        }
        return obj;
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent h=new Intent(ConfirmationReservationCard.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(ConfirmationReservationCard.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(ConfirmationReservationCard.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(ConfirmationReservationCard.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(ConfirmationReservationCard.this, MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(ConfirmationReservationCard.this, Admin_log.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
