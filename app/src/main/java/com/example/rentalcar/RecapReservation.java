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
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.text.*;

public class RecapReservation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<CarItem> carData=new ArrayList<>();//arraylist che conterrà la lista delle auto
    private ListView listViewCar;//oggetto listview
    private CustomAdapter adapter;//adapter fatto da noi
    Button paga_ora;
    Button paga_in_stazione;
    Bundle dati_pre=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewCar=findViewById(R.id.recap_car_list);//oggetto list view
        adapter=new CustomAdapter(this,R.layout.car_item_row,carData);
        int res_image=getIntent().getIntExtra("image",R.drawable.alfaromeogiulietta);
        dati_pre.putInt("res_image",res_image);
        String model=getIntent().getStringExtra("model");
        dati_pre.putString("modl",model);
        String Class_car=getIntent().getStringExtra("class");
        dati_pre.putString("clas",Class_car);
        double prezzo=getIntent().getDoubleExtra("prezzo",0.0);
        dati_pre.putDouble("prezo",prezzo);
        String shift=getIntent().getStringExtra("shift");
        dati_pre.putString("shit",shift);
        int num_pass=getIntent().getIntExtra("numP",0);
        dati_pre.putInt("nuP",num_pass);
        carData.add(new CarItem(res_image,model,Class_car,prezzo,shift,num_pass));
        listViewCar.setAdapter(adapter);
        //inserire il prezzo nel bottone
        String prezzo_convertito = Double.toString(prezzo);
        StringBuilder sb=new StringBuilder("Paga ora(");
        sb.append(prezzo_convertito);
        sb.append(")");
        StringBuilder sb1=new StringBuilder("Paga alla stazione(");
        sb1.append(prezzo_convertito);
        sb1.append(" + 5€)");
        Button buttonPO = (Button) findViewById(R.id.pagao);
        buttonPO.setText(sb.toString());
        Button buttonPS = (Button) findViewById(R.id.pagas);
        buttonPS.setText(sb1.toString());
        //lettura e visualizzazione della data e stazione e inserimento nel bundle
        String stazione_ritiro=getIntent().getStringExtra("st_ri");
        dati_pre.putString("stazione_ritir",stazione_ritiro);
        TextView dr = (TextView) findViewById(R.id.stazione_ritiro);
        dr.setText(stazione_ritiro);
        String stazione_ric=getIntent().getStringExtra("st_ric");
        dati_pre.putString("stazione_ric",stazione_ric);
        TextView dr1 = (TextView) findViewById(R.id.stazione_riconsegna);
        dr1.setText(stazione_ric);

        int anno_ritiro=getIntent().getIntExtra("a_ri",0);
        dati_pre.putInt("anno_ritiro",anno_ritiro);
        int mese_ritiro=getIntent().getIntExtra("m_ri",0);
        dati_pre.putInt("mese_ritiro",mese_ritiro);
        int giorno_ritiro=getIntent().getIntExtra("g_ri",0);
        dati_pre.putInt("giorno_ritiro",giorno_ritiro);
        int ora_ritiro=getIntent().getIntExtra("o_ri",0);
        dati_pre.putInt("ora_ritiro",ora_ritiro);
        int minuto_ritiro=getIntent().getIntExtra("m_ri",0);
        dati_pre.putInt("minuto_ritiro",minuto_ritiro);
        TextView dr2 = (TextView) findViewById(R.id.data_ritiro);
        dr2.setText(String.valueOf(giorno_ritiro)+"/"+String.valueOf(mese_ritiro)+"/"+String.valueOf(anno_ritiro)+" - "+String.valueOf(ora_ritiro)+":"+String.valueOf(minuto_ritiro));
        int anno_ric=getIntent().getIntExtra("a_r",0);
        dati_pre.putInt("anno_ric",anno_ric);
        int mese_ric=getIntent().getIntExtra("m_r",0);
        dati_pre.putInt("mese_ric",mese_ric);
        int giorno_ric=getIntent().getIntExtra("g_r",0);
        dati_pre.putInt("giorno_ric",giorno_ric);
        int ora_ric=getIntent().getIntExtra("o_rr",0);
        dati_pre.putInt("ora_ric",ora_ric);
        int minuto_ric=getIntent().getIntExtra("m_rr",0);
        dati_pre.putInt("minuto_ric",minuto_ric);
        TextView dr3 = (TextView) findViewById(R.id.data_riconsegna);
        dr3.setText(String.valueOf(giorno_ric)+"/"+String.valueOf(mese_ric)+"/"+String.valueOf(anno_ric)+" - "+String.valueOf(ora_ric)+":"+String.valueOf(minuto_ric));

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
                Intent h1=new Intent(RecapReservation.this,ConfirmationReservationCard.class);
                h1.putExtra("dati_pre",dati_pre);
                startActivity(h1);
            }
        });
        paga_in_stazione = (Button) findViewById(R.id.pagas);
        paga_in_stazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h1=new Intent(RecapReservation.this,ConfirmationReservation.class);
                h1.putExtra("dati_pre",dati_pre);
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
