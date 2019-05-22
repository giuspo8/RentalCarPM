package com.example.rentalcar.LateralMenu;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ExpandableListView;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.rentalcar.Admin.AdminActivity;
import com.example.rentalcar.MainPathReservation.MainActivity;
import com.example.rentalcar.R;
import com.example.rentalcar.Adapters.exlistview;

public class faq extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ExpandableListView listView;
    private exlistview listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ExpandableListView)findViewById(R.id.lvEx);
        //dichiarazione
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        //prendo le risposte e le metto in un vettore
        String risp1[] = getResources().getStringArray(R.array.l_o);
        //conversione da vettore a lista
        List<String> domanda1 = Arrays.asList(risp1);
        //aggiungo elementi alla lista
        listDataHeader.add("Dopo aver prenotato, è possibile cambiare la macchina ?");
        listDataHeader.add("Cosa succede se non si riconsegna la macchina in tempo ?");
        listDataHeader.add("Di cosa ho bisogno per noleggiare un' auto ?");
        listDataHeader.add("Cosa faccio se mi si rompe la macchina ?");
        listDataHeader.add("Posso prenotare un' auto per conto di qualcun altro ?");
        listDataHeader.add("E' tutto incluso nel prezzo del noleggio ?");
        //prendo le risposte e le metto in un vettore
        String risp2[] = getResources().getStringArray(R.array.l_i);
        //conversione da vettore a lista
        List<String> domanda2 = Arrays.asList(risp2);
        //prendo le risposte e le metto in un vettore
        String risp3[] = getResources().getStringArray(R.array.l_iop);
        //conversione da vettore a lista
        List<String> domanda3 = Arrays.asList(risp3);
        //prendo le risposte e le metto in un vettore
        String risp4[] = getResources().getStringArray(R.array.l_iok);
        //conversione da vettore a lista
        List<String> domanda4 = Arrays.asList(risp4);
        //prendo le risposte e le metto in un vettore
        String risp5[] = getResources().getStringArray(R.array.l_iopl);
        //conversione da vettore a lista
        List<String> domanda5 = Arrays.asList(risp5);
        //prendo le risposte e le metto in un vettore
        String risp6[] = getResources().getStringArray(R.array.l_ioplp);
        //conversione da vettore a lista
        List<String> domanda6 = Arrays.asList(risp6);
        //metto tutto dentro la lista
        listHash.put(listDataHeader.get(0),domanda1);
        listHash.put(listDataHeader.get(1),domanda2);
        listHash.put(listDataHeader.get(2),domanda3);
        listHash.put(listDataHeader.get(3),domanda4);
        listHash.put(listDataHeader.get(4),domanda5);
        listHash.put(listDataHeader.get(5),domanda6);
        listAdapter = new exlistview(this,listDataHeader,listHash);
        //inserisco tutto dentro la listview
        listView.setAdapter(listAdapter);
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
        getMenuInflater().inflate(R.menu.faq, menu);
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
            Intent h=new Intent(faq.this, EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(faq.this, Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(faq.this,faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(faq.this, MainActivity.class);
            startActivity(h1);
        }
        else if (id == R.id.nav_admin) {
            Intent i=new Intent(faq.this, AdminActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
