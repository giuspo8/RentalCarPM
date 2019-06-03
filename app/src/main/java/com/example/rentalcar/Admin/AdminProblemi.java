package com.example.rentalcar.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rentalcar.Adapters.ListViewAdapter;
import com.example.rentalcar.Adapters.ProblemsAdapter;
import com.example.rentalcar.LateralMenu.Contacts;
import com.example.rentalcar.LateralMenu.EditReservation;
import com.example.rentalcar.LateralMenu.Problems;
import com.example.rentalcar.LateralMenu.faq;
import com.example.rentalcar.LinkedReservationClasses.ReadResponse;
import com.example.rentalcar.LinkedReservationClasses.Segnalations;
import com.example.rentalcar.LinkedReservationClasses.StationNames;
import com.example.rentalcar.MainPathReservation.MainActivity;
import com.example.rentalcar.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class AdminProblemi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listRetire;
    ArrayList<Segnalations> ProblemsArray = new ArrayList<>();
    private ProblemsAdapter listAdapter;//usiamo un Adapter di una classe che abbiamo creato noi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_problemi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listRetire=findViewById(R.id.ListViewProblemi);

        read_problems();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void read_problems(){
        HttpURLConnection client = null;
        try {
            //creiamo un nuovo oggetto URL che fa riferimento al nostro sito con il file php per leggere le segnalazioni
            URL url = new URL("http://rentalcar.altervista.org/leggi_problemi.php");
            //apriamo la connessione
            client = (HttpURLConnection) url.openConnection();
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
                Segnalations s=new Segnalations(value.getString("Problema"));
                //lo aggiungiamo nell Arraylist
                ProblemsArray.add(s);
            } catch (JSONException e) {
                Toast.makeText(this,"ERRORE",Toast.LENGTH_LONG).show();
                // Something went wrong!
            }
        }
        //utilizziamo un adapter di una classe fatta da noi
        listAdapter = new ProblemsAdapter(this,ProblemsArray );
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
        getMenuInflater().inflate(R.menu.admin_problemi, menu);
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
            Intent h=new Intent(AdminProblemi.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(AdminProblemi.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(AdminProblemi.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(AdminProblemi.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(AdminProblemi.this,MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(AdminProblemi.this, Admin_log.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
