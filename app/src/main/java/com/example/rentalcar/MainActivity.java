package com.example.rentalcar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener, SearchView.OnQueryTextListener {
    private static final int REQUEST_CODE_CALENDAR=10;
    private static final int REQUEST_CODE_CALENDAR2=11;
    private ImageButton btnCalendarRetire;
    private ImageButton btnCalendarRestitution;
    private ImageButton btnTimeRetire;
    private ImageButton btnTimeRestitution;
    private TextView textCalendarRetire;
    private TextView textCalendarRestitution;
    private TextView textHourRetire;
    private TextView textHourRestitution;
    private Button searchBtn;
    private ListView listRetire;
    private ListViewAdapter adapter;
    private String[] stationList;
    private SearchView stationSearch;
    ArrayList<StationNames> arraylist = new ArrayList<StationNames>();
    Bundle search=new Bundle();
    boolean flagTime;//variabile che mi serve per controllare se ho chiamato il primo o il secondo bottone per l'ora

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCalendarRetire = findViewById(R.id.CalendarButton);//bottone calendario ritiro
        btnCalendarRestitution = findViewById(R.id.CalendarButton2);//bottone calenario riconsegna
        textCalendarRetire = findViewById(R.id.TextViewCalendarRitiro);//textview calendario ritiro
        textCalendarRestitution = findViewById(R.id.TextViewCalendarRiconsegna);//textview calendario riconsegna
        btnTimeRetire = findViewById(R.id.TimeButton);//bottone orario ritiro
        btnTimeRestitution = findViewById(R.id.TimeButton2);//bottone orario riconsegna
        textHourRetire = findViewById(R.id.TextViewOraRitiro);//textview orario ritiro
        textHourRestitution = findViewById(R.id.TextViewOraRiconsegna);//textview orario riconsegna
        searchBtn = findViewById(R.id.search_button1);//bottone Cerca
        listRetire=findViewById(R.id.ListViewRetire);//lista delle stazioni Ritiro
        stationSearch=findViewById(R.id.SearchViewStazioneRitiro);//search view stazioni ritiro

        stationList = new String[]{"Milano Centrale","Milano Aeroporto","Roma","Ancona","Pescara"};//qui vanno messi i nomi delle stazioni

        for (int i = 0; i < stationList.length; i++) {
            StationNames stationNames = new StationNames(stationList[i]);
            arraylist.add(stationNames);//mette tutte le stazioni in un array
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        listRetire.setAdapter(adapter);

        stationSearch.setOnQueryTextListener(this);

        listRetire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stationSearch.setQuery(arraylist.get(position).getStationName(),true);
            }
        });

        btnCalendarRetire.setOnClickListener(new View.OnClickListener() {
            //gestisce il click sull'icona del calendario del ritiro
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,CalendarActivity.class);//andiamo all'activity del calendario di ritiro
                startActivityForResult(i,REQUEST_CODE_CALENDAR);
            }
        });
        btnCalendarRestitution.setOnClickListener(new View.OnClickListener() {
            //gestisce il click sull'icona del calendario della riconsegna
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,CalendarActivity.class);
                startActivityForResult(i,REQUEST_CODE_CALENDAR2);//gli diamo un request code diverso per discriminarlo dall'altro
            }
        });
        btnTimeRetire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagTime=true;//settiamo la variabile di controllo
                DialogFragment timePicker=new TimePickerFragment();//creiamo un nuovo oggetto di tipo TimePickerFragment
                timePicker.show(getSupportFragmentManager(),"time picker");//chiamiamo il metodo show che ce lo fa visualizzare
            }
        });
        btnTimeRestitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagTime=false;
                DialogFragment timePicker2=new TimePickerFragment();//creiamo un nuovo oggetto di tipo TimePickerFragment
                timePicker2.show(getSupportFragmentManager(),"time picker 2");//chiamiamo il metodo show che ce lo fa visualizzare
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textCalendarRestitution.getText().toString().equals("")||textCalendarRetire.getText().toString().equals("")||
                        textHourRestitution.getText().toString().equals("")||textHourRetire.getText().toString().equals(""))
                {
                    show("Per Favore Riempi Tutti i Campi!!!");//richiama il metodo creato da me che fa il toast
                }
                else {
                    Intent i = new Intent(MainActivity.this, CarChoosing.class);
                    i.putExtra("search data", search);//metto i dati della prenotazione del bundle nell'intent
                    startActivity(i);
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void show(String message){
        Toast toast=Toast.makeText(this,message,Toast.LENGTH_LONG);//crea il toast
        toast.setGravity(Gravity.CENTER,0,0);//lo posiziona al centro
        toast.show();//lo mostra

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        listRetire.setVisibility(View.VISIBLE);
        adapter.filter(text);
        return false;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (flagTime==true){//fatto il controllo, se flagTime==true vuol dire che abbiamo schiacciato sul bottone del ritiro
            String hour=new DecimalFormat("00").format(hourOfDay);//serve per far si che 00 non lo scriva come 0
            String min=new DecimalFormat("00").format(minute);
            textHourRetire.setText(hour+":"+min);
            search.putInt("ora ritiro",hourOfDay);//metto l'int dell'ora e dei minuti nel bundle search che manderò all activity CarChoosing
            search.putInt("minuto ritiro",minute);
        }
        else{
            String hour=new DecimalFormat("00").format(hourOfDay);//serve per far si che 00 non lo scriva come 0
            String min=new DecimalFormat("00").format(minute);
            textHourRestitution.setText(hour+":"+min);
            search.putInt("ora restituzione",hourOfDay);//metto l'int dell'ora e dei minuti nel bundle search che manderò all activity CarChoosing
            search.putInt("minuto restituzione",minute);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //controlliamo quale delle due richieste sono state mandate indietro e agiamo di conseguenza
        if ((requestCode==REQUEST_CODE_CALENDAR)&&(resultCode== Activity.RESULT_OK)) {
            Bundle retire=data.getBundleExtra("Date");//creo un Bundle in cui mettere i dati provenienti dal calendario
            int yearRetire=retire.getInt("anno");//assegno a tre interi i valori rispettivamente di anno mese e giorno
            int monthRetire=retire.getInt("mese");
            int dayRetire=retire.getInt("giorno");
            textCalendarRetire.setText(String.valueOf(dayRetire)+"/"+String.valueOf(monthRetire)+"/"+String.valueOf(yearRetire));//li metto nella textview e quindi devo trasformarli in stringa
            search.putInt("anno ritiro",yearRetire);//mando i tre valori nel bundle search che poi verrà mandato in CarChoosing
            search.putInt("mese ritiro",monthRetire);
            search.putInt("giorno ritiro",dayRetire);
        }
        else if ((requestCode==REQUEST_CODE_CALENDAR2)&&(resultCode== Activity.RESULT_OK)) {
            Bundle restitution=data.getBundleExtra("Date");//creo un Bundle in cui mettere i dati provenienti dal calendario
            int yearRestitution=restitution.getInt("anno");//assegno a tre interi i valori rispettivamente di anno mese e giorno
            int monthRestitution=restitution.getInt("mese");
            int dayRestitution=restitution.getInt("giorno");
            textCalendarRestitution.setText(String.valueOf(dayRestitution)+"/"+String.valueOf(monthRestitution)+"/"+String.valueOf(yearRestitution));//li metto nella textview e quindi devo trasformarli in stringa
            search.putInt("anno restituzione",yearRestitution);//mando i tre valori nel bundle search che poi verrà mandato in CarChoosing
            search.putInt("mese restituzione",monthRestitution);
            search.putInt("giorno restituzione",dayRestitution);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
