package com.example.rentalcar;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.TextView;

public class Contacts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    //in questi caso l'activity contacts ci funge da listener e quindi dobbiamo
    //fare override del metodo onclick
    @Override
    public void onClick(View v) {
        Uri uri;
        Intent i;
        //in base a cosa clicchiamo scateniamo un'azione
        switch(v.getId()) {
            case R.id.imageButtonFb:
            case R.id.textViewFb2:
                //ritorna un uri parsato sulla stringa che gli diamo
                uri=Uri.parse("https://www.facebook.com");
                //mostra l'uri all'utente. viene riconosciuto da android che si tratta di un indirizzo web
                i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
                break;
            case R.id.imageButtonInsta:
            case R.id.textViewInsta2:
                uri=Uri.parse("https://www.instagram.com/");
                i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
                break;
            case R.id.imageButtonPhone:
            case R.id.textViewPhone2:
                uri=Uri.parse("tel:07145765");
                //compone il numero dell'uri
                i=new Intent(Intent.ACTION_DIAL,uri);
                startActivity(i);
                break;
            case R.id.imageButtonEmail:
            case R.id.textViewEmail2:
                uri=Uri.parse("mailto:rentalcar@esempio.it");
                //manda un msg (in questo caso email) all'indirizzo dell'uri
                i=new Intent(Intent.ACTION_SENDTO,uri);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        findViewById(R.id.imageButtonFb).setOnClickListener(this);
        findViewById(R.id.imageButtonInsta).setOnClickListener(this);
        findViewById(R.id.imageButtonEmail).setOnClickListener(this);
        findViewById(R.id.imageButtonPhone).setOnClickListener(this);
        findViewById(R.id.textViewFb2).setOnClickListener(this);
        findViewById(R.id.textViewInsta2).setOnClickListener(this);
        findViewById(R.id.textViewPhone2).setOnClickListener(this);
        findViewById(R.id.textViewEmail2).setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.contacts, menu);
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
            Intent h=new Intent(Contacts.this,EditReservation.class);
            startActivity(h);
        } else if (id == R.id.nav_gallery) {
            Intent h=new Intent(Contacts.this,Contacts.class);
            startActivity(h);
        } else if (id == R.id.nav_slideshow) {
            Intent h=new Intent(Contacts.this,Problems.class);
            startActivity(h);
        } else if (id == R.id.nav_manage) {
            Intent h=new Intent(Contacts.this,faq.class);
            startActivity(h);
        }
        else if (id == R.id.ReturnHome) {
            Intent h1=new Intent(Contacts.this,MainActivity.class);
            startActivity(h1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
