package com.example.rentalcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import static android.widget.Toast.LENGTH_LONG;

public class EditReservation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listReservation;
    //ArrayList di oggetti Reservation
    ArrayList<Reservation> pArrayList=new ArrayList<>();
    private ReservationAdapter adapter;//usiamo un Adapter di una classe che abbiamo creato noi

    Button home;
    Button search;

    EditText emaileEt;
    EditText idEt;

    int id;
    int id2;//serve per la variabile da passare alla cancellazione
    String email;
    String email2;//serve per la variabile da passare alla cancellazione

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);
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

        emaileEt=findViewById(R.id.Email);//editText dell'email
        idEt=findViewById(R.id.prenot);//editText dell'id prenotazione
        listReservation=findViewById(R.id.editReservationList);//lista in cui carichiamo la prenotazione

        //torna alla home
        home = (Button) findViewById(R.id.indietro);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h1=new Intent(EditReservation.this,MainActivity.class);
                startActivity(h1);
            }
        });


        //nel caso di click a lungo sulla prenotazione
        listReservation.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //prendiamo email e id dall arraylist
                email2=pArrayList.get(position).getEmail();
                id2=pArrayList.get(position).getId();
                //e mostriamo una dialog chiamando il metodo showActionsDialog
                showActionsDialog(position);
                //ritorniamo true perchè callback consumed the long click
                return true;
            }
        });



        search=findViewById(R.id.buttonEditSearch);
        //bottone search
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se non tutti i campi sono stati compilati da errore
                if (emaileEt.getText().toString().equals("")||idEt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Per favore riempi tutti i campi!!!",Toast.LENGTH_LONG).show();
                }
                else{
                    //altrimenti salviamo i dati letti nelle editext e chiamiamo il metodo read_reservation che ci va a leggere dal server
                    //e a caricare sull arraylist
                    email=emaileEt.getText().toString();
                    id=Integer.parseInt(idEt.getText().toString());
                    read_reservation();
                }
            }
        });

    }

    private void showActionsDialog(final int position) {
        //creiamo un vettore costituito da CharSequence (sequenze di char)
        CharSequence choice[] = new CharSequence[]{"Torna indietro", "Elimina Prenotazione"};

        //creiamo un nuovo alert dialog builder passandogli semplicemente il contesto
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //settiamo il titolo del dialog
        builder.setTitle("Cosa vuoi fare?");
        //settiamo un insieme di items da far visualizzare nel dialog
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //non fa niente ergo torna indietro
                } else {
                    //chiama il metodo che cancella la prenotazione
                    delete_Reservation();
                }
            }
        });
        //visualizza il dialog
        builder.show();
    }

    public void delete_Reservation() {
        HttpURLConnection client = null;
        try {
            //stessa cosa di FindStationActivity
            URL url = new URL("http://rentalcar.altervista.org/elimina_prenotazioni.php?Email="+this.email
                    + "&ID=" + this.id);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                Toast.makeText(this, "La prenotazione è stata cancellata!", LENGTH_LONG).show();
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

    public void read_reservation() {
        HttpURLConnection client = null;
        try {
            //stessa cosa di FindStationActivity
            URL url = new URL("http://rentalcar.altervista.org/cerca_prenotazione.php?Email="+this.email
                    + "&ID=" + this.id);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();
            if (json_string.equals("[]")){
                Toast.makeText(getApplicationContext(),"Nessuna prenotazione corrisponde ai dati inseriti",Toast.LENGTH_LONG).show();
            }
            else {
                JSONObject json_data = convert2JSON(json_string);
                fill_listview(json_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (client!= null){
                client.disconnect();
            }
        }
    }

    private void fill_listview(JSONObject json_data){
        //il metodo keys() ci ritorna un iteratore di una collezione di oggetti
        Iterator<String> iter = json_data.keys();
        //metodo hasNext() ritorna true se ci sono ancora elementi
            while (iter.hasNext()) {
                //metodo next() ritorna il prossimo elemento nell'iteratore (la chiave)
                String key = iter.next();
                try {
                    //ritorna il valore corrispondente alla chiave key
                    JSONObject value = json_data.getJSONObject(key);
                    int id2=value.getInt("ID");
                    //leggiamo tutti gli elementi tramite le loro etichette
                    String RetStation=value.getString("StazioneRit");
                    StationNames sRet=new StationNames(RetStation);
                    String RestStation=value.getString("StazioneRic");
                    StationNames sRec=new StationNames(RestStation);
                    String dataRitiro=value.getString("DataRitiro");
                    String dataRiconsegna=value.getString("DataRestituzione");
                    String email=value.getString("Email");
                    String car=value.getString("Macchina");
                    CarItem c=new CarItem(car);
                    int payment=value.getInt("Pagamento");
                    double price=value.getDouble("Prezzo");

                    //creiamo un nuovo oggetto Reservation e lo istanziamo con tutti i valori ottenuti
                    Reservation r=new Reservation(id2,sRet,sRec,c,email,dataRitiro,dataRiconsegna,payment,price);
                    //lo aggiungiamo nell Arraylist
                    pArrayList.add(r);
                } catch (JSONException e) {
                    Toast.makeText(this,"ERRORE",Toast.LENGTH_LONG).show();
                    // Something went wrong!
                }
            }
        //utilizziamo un adapter di una classe fatta da noi
        adapter=new ReservationAdapter(this,R.layout.reservation_item_row,pArrayList);
        //settiamo la listview all'adapter
        listReservation.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.edit_reservation, menu);
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
            Intent h=new Intent(EditReservation.this,EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(EditReservation.this,Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(EditReservation.this,Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(EditReservation.this,faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(EditReservation.this,MainActivity.class);
            startActivity(h1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
