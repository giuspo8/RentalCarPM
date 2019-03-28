package com.example.rentalcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

public class CalendarActivity2 extends AppCompatActivity {
    CalendarView restitutionDate;//oggetto calendario
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar2);
        restitutionDate=findViewById(R.id.calendarView2);
        restitutionDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //una volta cliccato su una data esegue le seguenti azioni
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date=dayOfMonth+"/"+(month+1)+"/"+year;//al mese va sommato 1
                Intent i=new Intent(CalendarActivity2.this,MainActivity.class);
                i.putExtra("RestitutionDate",date);
                startActivity(i);
            }
        });
    }
}
