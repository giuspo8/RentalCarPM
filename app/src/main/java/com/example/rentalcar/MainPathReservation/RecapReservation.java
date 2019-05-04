package com.example.rentalcar.MainPathReservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;

import com.example.rentalcar.Admin.AdminActivity;
import com.example.rentalcar.LinkedReservationClasses.CarItem;
import com.example.rentalcar.LateralMenu.Contacts;
import com.example.rentalcar.Adapters.CustomAdapter;
import com.example.rentalcar.LateralMenu.EditReservation;
import com.example.rentalcar.LateralMenu.Problems;
import com.example.rentalcar.R;
import com.example.rentalcar.LateralMenu.faq;

import java.util.ArrayList;
import java.util.Date;
import java.text.*;
import java.util.concurrent.TimeUnit;

public class RecapReservation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<CarItem> carData=new ArrayList<>();//arraylist che conterrà la lista delle auto
    private ListView listViewCar;//oggetto listview
    private CustomAdapter adapter;//adapter fatto da noi
    Button paga_ora;
    Button paga_in_stazione;
    //Bundle da inviare alla successiva activity
    Bundle dati_pre=new Bundle();
    Date dateRetire;
    Date dateRestitution;
    double totalPrice;

    boolean payNow=false;//variabile che ci serve per indicare se il cliente paga ora o in stazione

    String ritiro;
    String restituzione;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewCar=findViewById(R.id.recap_car_list);//oggetto list view
        adapter=new CustomAdapter(this,R.layout.car_item_row,carData);
        int res_image=getIntent().getIntExtra("image",R.drawable.alfaromeogiulietta);

        String model=getIntent().getStringExtra("model");
        dati_pre.putString("modl",model);
        String Class_car=getIntent().getStringExtra("class");

        double prezzo=getIntent().getDoubleExtra("prezzo",0.0);

        String shift=getIntent().getStringExtra("shift");

        int num_pass=getIntent().getIntExtra("numP",0);

        carData.add(new CarItem(res_image,model,Class_car,prezzo,shift,num_pass));
        listViewCar.setAdapter(adapter);


        //inserire il prezzo nel bottone
        StringBuilder sb=new StringBuilder("Paga ora(");

        StringBuilder sb1=new StringBuilder("Paga alla stazione(");


        //lettura e visualizzazione della data e stazione e inserimento nel bundle
        String stazione_ritiro=getIntent().getStringExtra("st_ri");
        dati_pre.putString("stazione_ritir",stazione_ritiro);

        TextView dr = (TextView) findViewById(R.id.stazione_ritiro);
        dr.setText(stazione_ritiro);

        String stazione_ric=getIntent().getStringExtra("st_ric");
        dati_pre.putString("stazione_ric",stazione_ric);

        TextView dr1 = (TextView) findViewById(R.id.stazione_riconsegna);
        dr1.setText(stazione_ric);

        TextView dr2 = findViewById(R.id.data_ritiro);
        TextView dr3 = findViewById(R.id.data_riconsegna);

        ritiro=getIntent().getStringExtra("ritiro");
        restituzione=getIntent().getStringExtra("restituzione");

        dati_pre.putString("ritiro",ritiro);
        dati_pre.putString("restituzione",restituzione);


        //formattiamo le stringhe delle date di ritiro e restituzione in modo da avere due variabili di tipo data
        try {
            dateRetire = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(ritiro);
            dateRestitution = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(restituzione);
        }
        catch (Exception e){

        }

        //con le due variabili di tipo data mi vado a calcolare il prezzo totale andando a moltiplicare il prezzo giornaliero per il numero di giorni
        long millisDiff =dateRestitution.getTime()- dateRetire.getTime() ;
        long days = TimeUnit.DAYS.convert(millisDiff, TimeUnit.MILLISECONDS);
        totalPrice=(prezzo*days)+prezzo;//l'approssimazione avviene per eccesso

        //metto il prezzo nel Bundle da inviare alla successiva activity
        dati_pre.putDouble("prezo",totalPrice);


        dr2.setText(String.valueOf(dateRetire));
        dr3.setText(String.valueOf(dateRestitution));

        sb.append(Double.toString(totalPrice)+" €");
        sb.append(")");

        sb1.append(Double.toString(totalPrice+25)+" €");//maggiorazione di prezzo dovuta al pagamento in stazione
        sb1.append(")");


        Button buttonPO = (Button) findViewById(R.id.pagao);
        buttonPO.setText(sb.toString());
        Button buttonPS = (Button) findViewById(R.id.pagas);
        buttonPS.setText(sb1.toString());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        paga_ora = (Button) findViewById(R.id.pagao);
        paga_ora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //settiamo la variabile booleana payNow a true (quindi paghiamo ora) e la mettiamo nell'intent
                boolean payNow=true;
                Intent h1=new Intent(RecapReservation.this, ConfirmationReservationCard.class);
                h1.putExtra("dati_pre",dati_pre);
                h1.putExtra("now",payNow);
                startActivity(h1);
            }
        });
        paga_in_stazione = (Button) findViewById(R.id.pagas);
        paga_in_stazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //la variabile payNow resta settata a false
                Intent h1=new Intent(RecapReservation.this,ConfirmationReservationCard.class);
                h1.putExtra("dati_pre",dati_pre);
                h1.putExtra("now",payNow);
                startActivity(h1);
            }
        });


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
        getMenuInflater().inflate(R.menu.recap_reservation, menu);
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
            Intent h=new Intent(RecapReservation.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(RecapReservation.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(RecapReservation.this, Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(RecapReservation.this, faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(RecapReservation.this, MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(RecapReservation.this, AdminActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
