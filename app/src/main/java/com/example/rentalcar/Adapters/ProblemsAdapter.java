package com.example.rentalcar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rentalcar.LinkedReservationClasses.Reservation;
import com.example.rentalcar.LinkedReservationClasses.Segnalations;
import com.example.rentalcar.LinkedReservationClasses.StationNames;
import com.example.rentalcar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProblemsAdapter extends BaseAdapter {

    Context myContext;
    LayoutInflater inflater;
    private List<Segnalations> listProblems = null;
    private ArrayList<Segnalations> arraylist;
    //il costruttore prende il contesto e la lista che contiene gli elementi di tipo StationNames
    public ProblemsAdapter(Context context, List<Segnalations> listProblems) {
        myContext = context;
        this.listProblems = listProblems;
        inflater = LayoutInflater.from(myContext);
        this.arraylist = new ArrayList<Segnalations>();
        this.arraylist.addAll(listProblems);
    }

    //classe che ha come unico attributo una textview
    public class ViewHolder {
        TextView name;
    }

    @Override
    //metodo che ci ritorna il numero di elementi presenti nella lista
    public int getCount() {
        return listProblems.size();
    }

    @Override
    //ci ritorna l'oggetto StationNames che si trova in posizione position
    public Segnalations getItem(int position) {
        return listProblems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //viewgroup=view che può contenere altre view
    //metodo che ci ridà una view e a cui passiamo la posizione e la vista attuale
    public View getView(final int position, View view, ViewGroup parent) {
        final ProblemsAdapter.ViewHolder holder;
        //se la vista non esiste ancora dobbiamo  crearla
        if (view == null) {
            holder = new ProblemsAdapter.ViewHolder();
            view = inflater.inflate(R.layout.list_view_stations, null);
            // Localizza la TextView in listview_item.xml e la mettiamo nell'attributo name di holder
            holder.name = (TextView) view.findViewById(R.id.name);
            // Setta il tag associato con questa vista prendendo un oggetto che conterrà come attributo l'identificativo
            view.setTag(holder);
        } else {
            //ritorna l'oggetto Tag associato con questa vista che ha come attributo solo la chiave (int) identificativa
            // (in questo caso castomizzata come ViewHolder)
            holder = (ProblemsAdapter.ViewHolder) view.getTag();
        }
        //scriviamo nell'attributo name di tipo textview di holder il nome della stazione corrispondente alla posizione position dell'arraylist
        holder.name.setText(listProblems.get(position).getProblem());
        return view;
    }

    // Filter prende in ingresso la stringa che stiamo scrivendo in questo momento sulla searchview
    public void filter(String charText) {
        //serve per non fare distinzioni tra minuscole e maiuscole
        //Locale.getdefault ci dice che stiamo utilizzando la scrittura locale(es. italiana)
        charText = charText.toLowerCase(Locale.getDefault());
        //rimuove tutti gli elementi dall'ArrayList
        listProblems.clear();
        //se non abbiamo ancora scritto nulla aggiungiamo tutti i valori nell arraylist perchè dobbiamo visualizzarli tutti senza filtri
        if (charText.length() == 0) {
            listProblems.addAll(arraylist);
        }
        //altrimenti per ogni(for each) oggetto (di tipo StationNames) dell'arraylist andiamo a vedere se il suo attributo StationName contiene quella stringa
        //e se la contiene la aggiungiamo nell'arraylist(che inizialmente è vuoto perchè l'abbiamo svuotato con clear)
        else {
            for (Segnalations wp : arraylist) {
                if (wp.getProblem().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listProblems.add(wp);
                }
            }
        }
        //Notifica l'osservatore che il dato è cambiato
        // e che qualsiasi vista che sta mostrando il dato si deve aggiornare
        notifyDataSetChanged();
    }

}
