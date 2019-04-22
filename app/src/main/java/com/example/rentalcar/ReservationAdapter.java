package com.example.rentalcar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class ReservationAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    List<Reservation> data;

    public ReservationAdapter(Context context, int resource, List<Reservation> objects) {
        super(context, resource, objects);

        this.layoutResourceId=resource;
        this.context=context;
        this.data=objects;
    }
    //la classe dataHolder ha i seguenti attributi
    static class DataHolder{
        TextView tvRetireStation;
        TextView tvRestitutionStation;
        TextView tvCar;
        TextView tvEmail;
        TextView tvPayment;
        TextView tvRetireDateandHour;
        TextView tvRestitutionDateandHour;
        TextView tvPrice;

    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ReservationAdapter.DataHolder holder;
        //se la view non c'è dobbiamo crearla
        if (convertView==null) {
            LayoutInflater inflater=((Activity)context).getLayoutInflater();

            convertView=inflater.inflate(layoutResourceId,parent,false);

            holder=new DataHolder();
            holder.tvRetireStation=convertView.findViewById(R.id.RES_retirestationTextView);
            holder.tvRestitutionStation=convertView.findViewById(R.id.RES_restitutionStationTextView);
            holder.tvRetireDateandHour=convertView.findViewById(R.id.DateHourRetireTextView);
            holder.tvRestitutionDateandHour=convertView.findViewById(R.id.DateHourRestitutionTextView);
            holder.tvCar=convertView.findViewById(R.id.RES_car);
            holder.tvEmail=convertView.findViewById(R.id.RES_emailTextView);
            holder.tvPayment=convertView.findViewById(R.id.RES_paymentTextView);
            holder.tvPrice=convertView.findViewById(R.id.TvPrice);
            // Setta il tag associato con questa vista prendendo un oggetto che conterrà come attributi gli identificativi
            convertView.setTag(holder);

        }

        else {
            holder=(DataHolder)convertView.getTag();
        }
        //una volta presa la posizione nella list mettiamo ogni attributo nella corrispondente textview
        Reservation reservationItem=data.get(position);
        holder.tvRetireStation.setText(reservationItem.getRetStation().getStationName());
        holder.tvRestitutionStation.setText(reservationItem.getRecStation().getStationName());

        String RetireDateAndHour=reservationItem.getDateRetire();
        holder.tvRetireDateandHour.setText(RetireDateAndHour);

        String RestitutionDateAndHour=reservationItem.getDateRestitution();
        holder.tvRestitutionDateandHour.setText(RestitutionDateAndHour);

        holder.tvCar.setText(reservationItem.getCar().getCarName());
        holder.tvEmail.setText(reservationItem.getEmail());
        holder.tvPrice.setText(String.valueOf(reservationItem.getPrice()));
        if (reservationItem.getPayment()==1){
            holder.tvPayment.setText("Pagamento effettuato online");
        }
        else {
            holder.tvPayment.setText("Pagamento alla stazione");
        }
        return convertView;

    }
}
