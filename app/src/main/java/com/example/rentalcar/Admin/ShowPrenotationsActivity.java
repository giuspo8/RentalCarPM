package com.example.rentalcar.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rentalcar.Adapters.ReservationAdapter;
import com.example.rentalcar.LateralMenu.Contacts;
import com.example.rentalcar.LateralMenu.EditReservation;
import com.example.rentalcar.LateralMenu.faq;
import com.example.rentalcar.LinkedReservationClasses.CarItem;
import com.example.rentalcar.LinkedReservationClasses.ReadResponse;
import com.example.rentalcar.LinkedReservationClasses.Reservation;
import com.example.rentalcar.LinkedReservationClasses.StationNames;
import com.example.rentalcar.MainPathReservation.MainActivity;
import com.example.rentalcar.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import static android.widget.Toast.LENGTH_LONG;

public class ShowPrenotationsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView listReservation;
    //ArrayList di oggetti Reservation
    ArrayList<Reservation> pArrayList=new ArrayList<>();
    private ReservationAdapter adapter;//usiamo un Adapter di una classe che abbiamo creato noi

    String email2;
    int id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prenotations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //serve per darci il permesso di stabilire la connessione nel Thread principale
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listReservation=findViewById(R.id.prenotation_listview);

        //chiamiamo il metodo read_reservation
        read_reservation();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            URL url = new URL("http://rentalcar.altervista.org/elimina_prenotazioni.php?Email="+ URLEncoder.encode(this.email2,"UTF-8")
                    + "&ID=" + this.id2);
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


    private void read_reservation(){
        HttpURLConnection client = null;
        try {
            //creiamo un nuovo oggetto URL che fa riferimento al nostro sito con il file php per leggere le prenotazioni
            URL url = new URL("http://rentalcar.altervista.org/leggi_prenotazioni.php");
            //apriamo la connessione e settiamo il metodo come Post(facoltativo)
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            //prendiamo lo stream in ingresso
            InputStream in = client.getInputStream();
            //creiamo una nuova stringa e la mettiamo in una certa forma (vedi readresponse)
            String json_string = ReadResponse.readStream(in);
            //convertiamo la stringa in un Json object
            JSONObject json_data = convert2JSON(json_string);
            //popoliamo la lista
            fill_listview(json_data);
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
                int id=value.getInt("ID");
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
                Reservation r=new Reservation(id,sRet,sRec,c,email,dataRitiro,dataRiconsegna,payment,price);
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
        try {
            //converte la stringa in un Json object
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
        getMenuInflater().inflate(R.menu.show_prenotations, menu);
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
            Intent h=new Intent(ShowPrenotationsActivity.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(ShowPrenotationsActivity.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(ShowPrenotationsActivity.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(ShowPrenotationsActivity.this, MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(ShowPrenotationsActivity.this, AdminActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
