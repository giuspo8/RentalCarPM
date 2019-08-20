package com.example.rentalcar.Admin;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalcar.Adapters.ReservationAdapter;
import com.example.rentalcar.LateralMenu.Contacts;
import com.example.rentalcar.LateralMenu.EditReservation;
import com.example.rentalcar.LateralMenu.Problems;
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
import java.text.NumberFormat;
import java.util.Iterator;

public class StatisticsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView prima1;
    TextView prima2;
    TextView prima3;
    TextView prima4;
    TextView prima5;
    TextView prima6;
    TextView prima7;
    TextView prima8;
    TextView prima9;
    TextView seconda1;
    TextView seconda2;
    TextView terza1;
    TextView quarta1;

    int total=0;
    int totCompact=0;
    int totEconomy=0;
    int totStandard=0;
    int totIntermediate=0;
    int totMini=0;
    int totMiniElite=0;
    int totPremium=0;
    int totLuxury=0;
    int totLusso=0;
    int PayStation=0;
    double priceTotal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        prima1=findViewById(R.id.prima1);
        prima2=findViewById(R.id.prima2);
        prima3=findViewById(R.id.prima3);
        prima4=findViewById(R.id.prima4);
        prima5=findViewById(R.id.prima5);
        prima6=findViewById(R.id.prima6);
        prima7=findViewById(R.id.prima7);
        prima8=findViewById(R.id.prima8);
        prima9=findViewById(R.id.prima9);
        seconda1=findViewById(R.id.seconda1);
        seconda2=findViewById(R.id.seconda2);
        terza1=findViewById(R.id.terza1);
        quarta1=findViewById(R.id.quarta1);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);


        read_reservation();
        double online=((double)total-PayStation)/(double)total*100;
        double stazione=PayStation/(double)total*100;
        String average=nf.format(priceTotal/total);
        prima1.setText(String.valueOf(totCompact));
        prima2.setText(String.valueOf(totEconomy));
        prima3.setText(String.valueOf(totStandard));
        prima4.setText(String.valueOf(totIntermediate));
        prima5.setText(String.valueOf(totMini));
        prima6.setText(String.valueOf(totMiniElite));
        prima7.setText(String.valueOf(totPremium));
        prima8.setText(String.valueOf(totLuxury));
        prima9.setText(String.valueOf(totLusso));
        seconda1.setText(nf.format(online)+"%");
        seconda2.setText(nf.format(stazione)+"%");
        terza1.setText(average+" €");
        quarta1.setText(String.valueOf(total));


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
                //leggiamo tutti gli elementi tramite le loro etichette
                String car=value.getString("Macchina");
                switch (car) {
                    case "Fiat 500 X o similare":
                    case "Peugeot 308 SW o similare":
                    case "Alfa Romeo Giulietta o similare":
                        totCompact++;
                        break;
                    case "Fiat Panda o similare":
                    case "Volkswagen Golf o similare":
                    case "Renault Clio o similare":
                        totEconomy++;
                        break;
                    case "Fiat Ducato Panorama o similare":
                    case "Volkswagen Passat o similare":
                        totStandard++;
                        break;
                    case "Audi A3 o similare":
                        totIntermediate++;
                        break;
                    case "Smart for Four o similare":
                        totMini++;
                        break;
                    case "Fiat 500 o similare":
                        totMiniElite++;
                        break;
                    case "Audi Q5":
                    case "Renault Kadjar o similare":
                    case "Mercedes Classe C o similare":
                        totPremium++;
                        break;
                    case "Mercedes classe E o similare":
                        totLuxury++;
                        break;
                    case "Ferrari Testarossa":
                        totLusso++;
                }
                int payment=value.getInt("Pagamento");
                if (payment==0) PayStation++;
                double price=value.getDouble("Prezzo");
                priceTotal+=price;
                total++;
            } catch (JSONException e) {
                Toast.makeText(this,"ERRORE",Toast.LENGTH_LONG).show();
                // Something went wrong!
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
        getMenuInflater().inflate(R.menu.statistics, menu);
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
            Intent h=new Intent(StatisticsActivity.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(StatisticsActivity.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(StatisticsActivity.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(StatisticsActivity.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(StatisticsActivity.this, MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(StatisticsActivity.this, Admin_log.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
