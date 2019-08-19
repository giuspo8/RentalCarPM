package com.example.rentalcar.MainPathReservation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import com.example.rentalcar.Admin.Admin_log;
import com.example.rentalcar.R;

public class StartActivity extends AppCompatActivity {

    Button loginbtn;
    Button registerbtn;
    Button adminbtn;
    Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //salviamo in una variabile booleana true se c'è connessione false altrimenti
        isConnected = check_connection();
        //se non siamo connessi mostriamo la snackbar
        if (!isConnected) {
            show_snackbar();
        }

       loginbtn=findViewById(R.id.logininitbutton);
       registerbtn=findViewById(R.id.registerinitbutton);
       adminbtn=findViewById(R.id.admingobutton);

       loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected = check_connection();
                //se non siamo connessi mostriamo la snackbar
                if (!isConnected) {
                    show_snackbar();
                }
                else {
                    Intent i = new Intent(StartActivity.this, UserLoginActivity.class);
                    startActivity(i);
                }
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected = check_connection();
                //se non siamo connessi mostriamo la snackbar
                if (!isConnected) {
                    show_snackbar();
                }
                else {
                    Intent i = new Intent(StartActivity.this, RegistrationActivity.class);
                    startActivity(i);
                }
            }
        });

        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected = check_connection();
                //se non siamo connessi mostriamo la snackbar
                if (!isConnected) {
                    show_snackbar();
                }
                else {
                    Intent i = new Intent(StartActivity.this, Admin_log.class);
                    startActivity(i);
                }
            }
        });

    }

    public boolean check_connection(){
        //questo oggetto connectivitymanager ci serve per valutare lo stato della connessione
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork!= null) && (activeNetwork.isConnectedOrConnecting());
    }

    public void show(String message){
        Toast toast=Toast.makeText(this,message,Toast.LENGTH_LONG);//crea il toast
        toast.setGravity(Gravity.CENTER,0,0);//lo posiziona al centro
        toast.show();//lo mostra

    }

    private void show_snackbar(){
        //testo della snackbar
        Snackbar.make(findViewById(R.id.drawer_layout), "Devi attivare la tua connessione!!!", Snackbar.LENGTH_LONG)
                //opzione da selezionare che automaticamente va alle impostazioni del cellulare per attivare la connessione
                .setAction("attiva Internet!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(i);
                    }
                })
                .show();
    }





}
