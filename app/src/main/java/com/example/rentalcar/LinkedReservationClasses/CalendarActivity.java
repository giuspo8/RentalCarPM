package com.example.rentalcar.LinkedReservationClasses;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.rentalcar.MainPathReservation.MainActivity;
import com.example.rentalcar.R;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    CalendarView retireDate;//oggetto calendario
    //CalendarView restitutionDate;//oggetto calendario restituzione
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        retireDate=findViewById(R.id.calendarView);

        //setto la data minima visualizzabile al giorno di domani(giorno di oggi più il numero i millisecondi in un giorno)
        retireDate.setMinDate(Calendar.getInstance().getTimeInMillis()+86400000);
        retireDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //una volta cliccato su una data esegue queste azioni
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent=new Intent(CalendarActivity.this, MainActivity.class);
                Bundle b=new Bundle();
                b.putInt("anno",year);
                b.putInt("mese",month+1);
                b.putInt("giorno",dayOfMonth);
                intent.putExtra("Date",b);//lo rimandiamo indietro
                setResult(Activity.RESULT_OK,intent);//diciamo che è andato tutto bene e mandiamo indietro l'intent
                finish();
            }
        });
    }
}
