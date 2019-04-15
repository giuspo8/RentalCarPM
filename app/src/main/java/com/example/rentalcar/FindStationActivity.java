package com.example.rentalcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        stationSearch=findViewById(R.id.searchStation);//search view stazioni ritiro
        listRetire=findViewById(R.id.listviewStation);


        read_station();

        //qui semplicemente stiamo settando il listener della searchview in attesa di azioni dell'utente
        stationSearch.setOnQueryTextListener(this);

        listRetire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //qui stiamo dicendo che quando clicchiamo su un oggetto della lista quello viene trascritto sulla searchview,
                // con il metodo get ritorniamo l'elemento nella posizione position e con getStationName ritorniamo il valore dell'attributo
                stationSearch.setQuery(StationArray.get(position).getStationName(),true);
                Intent i=new Intent(FindStationActivity.this,MainActivity.class);
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
            URL url = new URL("http://rentalcar.altervista.org/leggi_stazioni.php");
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in);
            JSONObject json_data = convert2JSON(json_string);
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
        Iterator<String> iter = json_data.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                JSONObject value = json_data.getJSONObject(key);
                StationNames s=new StationNames(value.getString("Stazione"));
                StationArray.add(s);
            } catch (JSONException e) {
                Toast.makeText(this,"ERRORE",Toast.LENGTH_LONG).show();
                // Something went wrong!
            }
        }
        listAdapter = new ListViewAdapter(this,StationArray );
        listRetire.setAdapter(listAdapter);
    }

    private JSONObject convert2JSON(String json_data){
        JSONObject obj = null;
        try {
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
