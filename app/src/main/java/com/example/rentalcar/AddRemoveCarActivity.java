package com.example.rentalcar;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.widget.Toast.LENGTH_LONG;

public class AddRemoveCarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

Button addCarButton;
Button removeCarButton;
EditText modelEt;
EditText classEt;
EditText shiftEt;
EditText passengersEt;
EditText priceEt;

String model;
String classCar;
String shift;
int passengers;
double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addCarButton=findViewById(R.id.add_car_button);
        removeCarButton=findViewById(R.id.remove_car_button);
        modelEt=findViewById(R.id.add_model_car_et);
        classEt=findViewById(R.id.add_class_car_et);
        shiftEt=findViewById(R.id.add_shift_car_et);
        passengersEt=findViewById(R.id.add_passengers_car_et);
        priceEt=findViewById(R.id.add_price_car_et);

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se nessun campo è vuoto va avanti altrimenti parte un toast
                if (modelEt.getText().toString().equals("")||classEt.getText().toString().equals("")||
                        shiftEt.getText().toString().equals("")||passengersEt.getText().toString().equals("") ||
                        priceEt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Per favore riempi tutti i campi!!",Toast.LENGTH_LONG).show();
                }
                else {
                    //prende i valori e chiama il metodo add_car()
                    model=modelEt.getText().toString();
                    classCar=classEt.getText().toString();
                    shift=shiftEt.getText().toString();
                    passengers=Integer.parseInt(passengersEt.getText().toString());
                    price=Double.parseDouble(priceEt.getText().toString());
                    add_car();
                }
            }
        });

        removeCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelEt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Per favore inserisci il nome del modello da cancellare!!",Toast.LENGTH_LONG).show();
                }
                else {
                    //prende il valore del modello da cancellare e chiama il metodo remove_car()
                    model=modelEt.getText().toString();
                    remove_car();
                }
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

    private void add_car(){
        HttpURLConnection client = null;
        URL url;
        try {
            //sempre solita cosa
            url = new URL("http://rentalcar.altervista.org/aggiungi_auto.php?Modello=" + this.model+"&Classe="+this.classCar
            +"&PrezzoGg="+this.price+"&Cambio="+this.shift+"&NrPasseggeri="+this.passengers);
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

    private void remove_car(){
        HttpURLConnection client = null;
        try {
            //sempre solita cosa
            URL url = new URL("http://rentalcar.altervista.org/elimina_auto.php?Modello="+this.model);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                Toast.makeText(this, "L'auto è stata eliminata!", LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.add_remove_car, menu);
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
            Intent h=new Intent(AddRemoveCarActivity.this,EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(AddRemoveCarActivity.this,Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(AddRemoveCarActivity.this,Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(AddRemoveCarActivity.this,faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(AddRemoveCarActivity.this,MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(AddRemoveCarActivity.this,AdminActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
