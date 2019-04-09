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

import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    List <CarItem> data;

    public CustomAdapter(Context context, int resource, List<CarItem> objects) {
        super(context, resource, objects);

        this.layoutResourceId=resource;
        this.context=context;
        this.data=objects;
    }
//la classe dataHolder ha i seguenti attributi
    static class DataHolder{
        ImageView ivImage;
        TextView tvCarName;
        TextView tvClassName;
        TextView tvCarShift;
        TextView tvNumberOfPassengers;
        TextView tvCarPrice;

    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataHolder holder;
        //se la view non c'è dobbiamo crearla
        if (convertView==null) {
            LayoutInflater inflater=((Activity)context).getLayoutInflater();

            convertView=inflater.inflate(layoutResourceId,parent,false);

            holder=new DataHolder();
            holder.ivImage=convertView.findViewById(R.id.imageViewCar);
            holder.tvCarName=convertView.findViewById(R.id.textViewCarName);
            holder.tvClassName=convertView.findViewById(R.id.textViewClassName);
            holder.tvCarShift=convertView.findViewById(R.id.textViewCarShift);
            holder.tvNumberOfPassengers=convertView.findViewById(R.id.textViewNumberPassengers);
            holder.tvCarPrice=convertView.findViewById(R.id.textViewCarPrice);
            // Setta il tag associato con questa vista prendendo un oggetto che conterrà come attributi gli identificativi
            convertView.setTag(holder);

        }

        else {
            holder=(DataHolder)convertView.getTag();
        }
        //una volta presa a posizione nella list mettiamo ogni attributo nella corrispondente textview,image etc
        CarItem carItem=data.get(position);
        holder.tvCarName.setText(carItem.carName);
        holder.ivImage.setImageResource(carItem.resIdImage);
        holder.tvClassName.setText(carItem.classCar);
        holder.tvCarShift.setText(carItem.carShift);
        holder.tvNumberOfPassengers.setText(String.valueOf(carItem.numberOfPassengers));
        holder.tvCarPrice.setText(String.valueOf(carItem.priceGg));

        return convertView;

    }
}
