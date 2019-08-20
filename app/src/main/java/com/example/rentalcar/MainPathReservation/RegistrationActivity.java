package com.example.rentalcar.MainPathReservation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalcar.LinkedReservationClasses.CreditCard;
import com.example.rentalcar.LinkedReservationClasses.ReadResponse;
import com.example.rentalcar.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;

public class RegistrationActivity extends AppCompatActivity {

    String email;
    String name;
    String surname;
    String telephone;
    String password;

    String message1;
    String message2;
    String message3;
    String message4;
    String message5;
    String message6;




    TextView emailtv;
    TextView nametv;
    TextView surnametv;
    TextView telephonetv;
    TextView passwordtv;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Button conferma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //serve per darci il permesso di stabilire la connessione nel Thread principale
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conferma = findViewById(R.id.confUser);

        emailtv = findViewById(R.id.emailUser);
        nametv = findViewById(R.id.NomeUser);
        surnametv = findViewById(R.id.CognomeUser);
        telephonetv = findViewById(R.id.telefonoUser);
        passwordtv=findViewById(R.id.passwordUser);

conferma.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        //lettura di tutti i dati
        email = emailtv.getText().toString();
        name = nametv.getText().toString();
        surname = surnametv.getText().toString();
        telephone = telephonetv.getText().toString();
        password =passwordtv.getText().toString();


        //se non sono stati riempiti i campi relativi all'anagrafica da errore
        if (emailtv.getText().toString().equals("") || nametv.getText().toString().equals("") ||
                surnametv.getText().toString().equals("") || telephonetv.getText().toString().equals("")
                || passwordtv.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Per favore inserisci tutti i dati obbligatori.", LENGTH_LONG).show();
        } else {
            if (!(email.contains("@") && email.contains("."))) {
                Toast.makeText(getApplicationContext(), "Formato email non valido.", LENGTH_LONG).show();
            } else {
                if (password.contains("\"") || password.contains(" ")) {
                    Toast.makeText(getApplicationContext(), "Formato Password non valido", LENGTH_LONG).show();
                } else {
                    if (password.length() < 6 || !Pattern.compile("[0-9]").matcher(password).find()) {
                        Toast.makeText(getApplicationContext(), "La Password deve essere composta da almeno 6 caratteri di cui almeno un numero", LENGTH_LONG).show();
                    } else {
                        //salviamo tutto in shared preferences
                        sp = getSharedPreferences("datiUtente", Context.MODE_PRIVATE);
                        editor=sp.edit();
                        editor.putString("email", email);
                        editor.putString("nome", name);
                        editor.putString("cognome", surname);
                        editor.putString("telefono", telephone);
                        editor.apply();
                        insert_user();
                    }
                }
            }
        }
    }
});
    }

    private void insert_user(){
        HttpURLConnection client = null;
        URL url;
        try {
            //metodo get quindi scriviamo i dati da inviare direttamente nell'Url
            url = new URL("http://rentalcar.altervista.org/inserisci_utenti.php?Nome=" +
                    URLEncoder.encode(this.name,"UTF-8") +
                    "&Cognome=" + URLEncoder.encode(this.surname,"UTF-8") +
                    "&Telefono=" + this.telephone +
                    "&Email=" + URLEncoder.encode(this.email,"UTF-8") +
                    "&Password=" + URLEncoder.encode(this.password,"UTF-8"));
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("1")) {
                send_mail();
                Intent i = new Intent(RegistrationActivity.this, EmailRegistrationActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "L'email inserita è già presente. Prego riprovare", LENGTH_LONG).show();
                Intent i = new Intent(RegistrationActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

    private void send_mail(){
        HttpURLConnection client = null;
        URL url;
        message1="Caro "+name+" "+surname;
        message2="Le confermiamo la Sua registrazione,";
        message3="ecco i Suoi dati di accesso:";
        message4=" Email: "+email;
        message5="Password: "+password;
        message6="Grazie per averci scelto";
        try {
            url = new URL("http://rentalcar.altervista.org/inviocredenziali.php?Email=" + this.email +
                    "&msg1=" + URLEncoder.encode(this.message1,"UTF-8")+
                    "&msg2=" +URLEncoder.encode(this.message2,"UTF-8")+
                    "&msg3=" +URLEncoder.encode(this.message3,"UTF-8")+
                    "&msg4=" +URLEncoder.encode(this.message4,"UTF-8")+
                    "&msg5=" +URLEncoder.encode(this.message5,"UTF-8")+
                    "&msg6=" +URLEncoder.encode(this.message6,"UTF-8")
            );
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            InputStream in = client.getInputStream();
            String json_string = ReadResponse.readStream(in).trim();

            if (json_string.equals("")) {
                //Toast.makeText(this, "invio effettuato", LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Errore nell'invio", LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

}

