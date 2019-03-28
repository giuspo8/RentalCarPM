package com.example.rentalcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {

    CalendarView retireDate;//oggetto calendario
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        retireDate=findViewById(R.id.calendarView);
        retireDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //una volta cliccato su una data esegue queste azioni
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date=dayOfMonth+"/"+(month+1)+"/"+year;//salva su una stringa il risultato(al mese va sommato 1)
                Intent intent=new Intent(CalendarActivity.this,MainActivity.class);
                intent.putExtra("RetireDate",date);//lo rimandiamo indietro
                startActivity(intent);
            }
        });
    }
}
