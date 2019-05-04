package com.example.rentalcar.MainPathReservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.rentalcar.Admin.AdminActivity;
import com.example.rentalcar.LateralMenu.Contacts;
import com.example.rentalcar.LateralMenu.EditReservation;
import com.example.rentalcar.Adapters.ListViewAdapter;
import com.example.rentalcar.LateralMenu.Problems;
import com.example.rentalcar.R;
import com.example.rentalcar.LinkedReservationClasses.ReadResponse;
import com.example.rentalcar.LinkedReservationClasses.StationNames;
import com.example.rentalcar.LateralMenu.faq;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class FindStationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {

    private ListView listRetire;
    private SearchView stationSearch;
    ArrayList<StationNames> StationArray = new ArrayList<>();//Creiamo un oggetto ArrayList,cioè un Array a cui possiamo aggiungere oggetti di tipo StationNames tramite il metodo .add
    private ListViewAdapter listAdapter;//usiamo un Adapter di una classe che abbiamo creato noi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //serve per darci il permesso di stabilire la connessione nel Thread principale
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        stationSearch=findViewById(R.id.searchStation);//search view stazioni ritiro
        listRetire=findViewById(R.id.listviewStation);//list view che conterrà la lista delle stazioni

        //chiamiamo il metodo che andrà a leggere la lista delle stazioni sul server
        read_station();

        //qui semplicemente stiamo settando il listener della searchview in attesa di azioni dell'utente
        stationSearch.setOnQueryTextListener(this);

        listRetire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //qui stiamo dicendo che quando clicchiamo su un oggetto della lista quello viene trascritto sulla searchview,
                // con il metodo get ritorniamo l'elemento nella posizione position e con getStationName ritorniamo il valore dell'attributo
                stationSearch.setQuery(StationArray.get(position).getStationName(),true);
                Intent i=new Intent(FindStationActivity.this, MainActivity.class);
                String station= String.valueOf(stationSearch.getQuery());
                i.putExtra("station",station);
                setResult(Activity.RESULT_OK,i);
                finish();
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

    private void read_station(){
        HttpURLConnection client = null;
        try {
            //creiamo un nuovo oggetto URL che fa riferimento al nostro sito con il file php per leggere le stazioni
            URL url = new URL("http://rentalcar.altervista.org/leggi_stazioni.php");
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
                //creiamo un nuovo oggetto stazione e gli assegniamo il valore che abbiamo preso (mappato dal nome Stazione)
                StationNames s=new StationNames(value.getString("Stazione"));
                //lo aggiungiamo nell Arraylist
                StationArray.add(s);
            } catch (JSONException e) {
                Toast.makeText(this,"ERRORE",Toast.LENGTH_LONG).show();
                // Something went wrong!
            }
        }
        //utilizziamo un adapter di una classe fatta da noi
        listAdapter = new ListViewAdapter(this,StationArray );
        //settiamo la listview all'adapter
        listRetire.setAdapter(listAdapter);
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
    //facciamo ritornare falso per far si che la SearchView si gestisca l'azione di default
    //con true la query veniva gestita dal listener
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        listRetire.setVisibility(View.VISIBLE);//una volta che si inizia a scrivere sulla searchview rendiamo visibile la lista
        listAdapter.filter(text);//chiamiamo il metodo filter della classe ListViewAdapter e li passiamo il testo che stiamo scrivendo ogni volta che cambia
        //ritorniamo false perchè l'azione non è gestita dal listener
        return false;
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
        getMenuInflater().inflate(R.menu.find_station, menu);
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
            Intent h=new Intent(FindStationActivity.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(FindStationActivity.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(FindStationActivity.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(FindStationActivity.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(FindStationActivity.this,MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(FindStationActivity.this, AdminActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
