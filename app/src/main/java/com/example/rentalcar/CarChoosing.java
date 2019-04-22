package com.example.rentalcar;

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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.example.rentalcar.R.drawable.cinquecento;

public class CarChoosing extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<CarItem> carData=new ArrayList<>();//arraylist che conterrà la lista delle auto
    private ListView listViewCar;//oggetto listview
    private CustomAdapter adapter;//adapter fatto da noi

    String ritiro;
    String restituzione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_choosing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        listViewCar=findViewById(R.id.ListViewCar);//oggetto list view

        //chiamiamo il metodo che ci legge le auto dal server
        read_car();

        listViewCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //passiamo i dati alla successiva activity
                Intent i=new Intent(CarChoosing.this,RecapReservation.class);
                i.putExtra("model",carData.get(position).getCarName());
                i.putExtra("image",carData.get(position).getResIdImage());
                i.putExtra("class",carData.get(position).getClassCar());
                i.putExtra("prezzo",carData.get(position).getPriceGg());
                i.putExtra("shift",carData.get(position).getCarShift());
                i.putExtra("numP",carData.get(position).getNumberOfPassengers());
                Bundle restitution=getIntent().getBundleExtra("search");
                i.putExtra("st_ri",restitution.getString("Stazione_ritiro"));
                i.putExtra("st_ric",restitution.getString("Stazione_restituzione"));

                //lettura dei dati dall' activity precedente
                ritiro=restitution.getString("data ritiro");
                restituzione=restitution.getString("data restituzione");

                i.putExtra("ritiro",ritiro);
                i.putExtra("restituzione",restituzione);
                startActivity(i);
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

    private void read_car(){
        HttpURLConnection client = null;
        try {
            //stessa cosa di FindStationActivity
            URL url = new URL("http://rentalcar.altervista.org/leggi_auto.php");
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
                //simile a FindStationActivity
                String model=value.getString("Model");
                String classCar=value.getString("ClassCar");
                Double priceGg=value.getDouble("Pricegg");
                String carShift= value.getString("Shift");
                int numberPassengers=value.getInt("Number");
                int resId;
                switch (model){
                    case "Fiat 500 o similare": {
                        resId = R.drawable.cinquecento;
                        break;
                    }
                    case "Mercedes Classe C o similare": {
                        resId = R.drawable.mercedes;
                        break;
                    }
                    case "Fiat 500 X o similare": {
                        resId = R.drawable.cinquecentox;
                        break;
                    }
                    case "Mercedes classe E o similare": {
                        resId = R.drawable.mercedesclassee;
                        break;
                    }case "Smart for Four o similare": {
                        resId = R.drawable.smart;
                        break;
                    }case "Audi Q5": {
                        resId = R.drawable.q5;
                        break;
                    }case "Alfa Romeo Giulietta o similare": {
                        resId = R.drawable.alfaromeogiulietta;
                        break;
                    }case "Fiat Ducato Panorama o similare": {
                        resId = R.drawable.fiatducatopanorama;
                        break;
                    }case "Volkswagen Passat o similare": {
                        resId = R.drawable.passat;
                        break;
                    }case "Audi A3 o similare": {
                        resId = R.drawable.audia3;
                        break;
                    }case "Peugeot 308 SW o similare": {
                        resId = R.drawable.peugstationwagon;
                        break;
                    }case "Renault Clio o similare": {
                        resId = R.drawable.clio;
                        break;
                    }case "Volkswagen Golf o similare": {
                        resId = R.drawable.golf;
                        break;
                    }case "Renault Kadjar o similare": {
                        resId = R.drawable.kadjar;
                        break;
                    }case "Fiat Panda o similare": {
                        resId = R.drawable.panda;
                        break;
                    }
                    default:{
                        resId=R.drawable.panda;
                    }
                }

                //creiamo un nuovo oggetto carItem con tutti i valori che abbiamo ottenuto
                CarItem c=new CarItem(resId,model,classCar,priceGg,carShift,numberPassengers);
                carData.add(c);
            } catch (JSONException e) {
                Toast.makeText(this,"ERRORE",Toast.LENGTH_LONG).show();
                // Something went wrong!
            }
        }
        adapter=new CustomAdapter(this,R.layout.car_item_row,carData);
        listViewCar.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.car_choosing, menu);
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
            Intent h=new Intent(CarChoosing.this,EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(CarChoosing.this,Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(CarChoosing.this,Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(CarChoosing.this,faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(CarChoosing.this,MainActivity.class);
            startActivity(h1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
