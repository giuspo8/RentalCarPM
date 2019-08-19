package com.example.rentalcar.MainPathReservation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentalcar.Adapters.CustomAdapter;
import com.example.rentalcar.Admin.Admin_button;
import com.example.rentalcar.Admin.Admin_log;
import com.example.rentalcar.LinkedReservationClasses.CarItem;
import com.example.rentalcar.LinkedReservationClasses.ReadResponse;
import com.example.rentalcar.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import static android.widget.Toast.LENGTH_LONG;

public class UserLoginActivity extends AppCompatActivity {

    EditText editTextPassword;
    EditText editTextEmail;
    Button loginButton;
    String email;
    String password;
    String nome;
    String cognome;
    String telefono;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editTextPassword=findViewById(R.id.password_useredittext);
        editTextEmail=findViewById(R.id.email_useredittext);
        loginButton=findViewById(R.id.login_user);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (email.contains("@") && email.contains(".") && !email.contains("\"")  && !email.contains(" "))
                {
                    if (password.contains("\"") || password.contains(" ")) {
                        Toast.makeText(getApplicationContext(),"Formato password non valido.",LENGTH_LONG).show();
                    }
                    else
                    {
                        check_authentication();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Formato e-mail non valido.",LENGTH_LONG).show();
                }
            }
        });
    }

    private void check_authentication() {
        HttpURLConnection client = null;
        URL url;
        sp=getSharedPreferences("datiUtente",Context.MODE_PRIVATE);
        editor=sp.edit();
        try {
            // se la richiesta è GET gli passiamo email e password letti sulle edittext
            url = new URL("http://rentalcar.altervista.org/leggi_utenti.php?email=" + URLEncoder.encode(this.email,"UTF-8")+
                    "&password=" + URLEncoder.encode(this.password,"UTF-8"));
            //apriamo la connessione e settiamo il metodo come GET(facoltativo)
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setDoInput(true);
            //prendiamo lo stream in ingresso
            InputStream in = client.getInputStream();
            //creiamo una nuova stringa e la mettiamo in una certa forma (vedi readresponse)
            String json_string = ReadResponse.readStream(in).trim();
            //se il risultato è [] vuol dire che non è stato trovato nessun risultato corrispondente
            if (json_string.equals("[]")) {
                Toast.makeText(this,"I dati inseriti sono errati! Prego Riprovare",Toast.LENGTH_LONG).show();
            }
            //se invece abbiamo trovato qualcosa allora possiamo garantire l'ingresso alla successiva activity
            //in base al valore della variabile flag
            else {
                read_user();
                editor.putString("email",email);
                editor.apply();
                Intent i = new Intent(UserLoginActivity.this, MainActivity.class);
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

    private void read_user(){
        HttpURLConnection client = null;
        try {
            //stessa cosa di FindStationActivity
            URL url = new URL("http://rentalcar.altervista.org/leggi_utenti.php?email=" + URLEncoder.encode(this.email,"UTF-8")+
                    "&password=" + URLEncoder.encode(this.password,"UTF-8"));
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
                nome=value.getString("nome");
                cognome=value.getString("cognome");
                telefono=value.getString("telefono");
                //salviamo tutto in shared preferences
                editor.putString("nome",nome);
                editor.putString("cognome",cognome);
                editor.putString("telefono",telefono);
                editor.apply();

            } catch (JSONException e) {
                Toast.makeText(this,"ERRORE",Toast.LENGTH_LONG).show();
                // Something went wrong!
            }
        }

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
}
