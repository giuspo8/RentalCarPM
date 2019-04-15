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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.rentalcar.R.drawable.cinquecento;

public class CarChoosing extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<CarItem> carData=new ArrayList<>();
    private ListView listViewCar;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_choosing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listViewCar=findViewById(R.id.ListViewCar);//oggetto list view

        read_car();

        listViewCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //qui passiamo l'oggetto cliccato all activity finale
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
                    default:{
                        resId=R.drawable.panda;
                    }
                }


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
        try {
            obj = new JSONObject(json_data);
            Log.d("My App", obj.toString());
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json_data + "\"");
        }
        return obj;
    }
    public void show(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();//metodo per fare i toast
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
