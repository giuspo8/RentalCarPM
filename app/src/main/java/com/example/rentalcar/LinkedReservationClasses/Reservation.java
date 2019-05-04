package com.example.rentalcar.LinkedReservationClasses;

import com.example.rentalcar.LinkedReservationClasses.CarItem;
import com.example.rentalcar.LinkedReservationClasses.StationNames;

public class Reservation {
    private int id;
    private StationNames retStation;
    private StationNames recStation;
    private CarItem car;
    private String email;
    private String dateRetire;
    private String dateRestitution;
    private int payment;
    private double price;

    public Reservation(int id, StationNames retStation, StationNames recStation, CarItem car, String email, String dateRetire, String dateRestitution, int payment, double price) {
        this.id = id;
        this.retStation = retStation;
        this.recStation = recStation;
        this.car = car;
        this.email = email;
        this.dateRetire = dateRetire;
        this.dateRestitution = dateRestitution;
        this.payment = payment;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public StationNames getRetStation() {
        return retStation;
    }

    public StationNames getRecStation() {
        return recStation;
    }

    public CarItem getCar() {
        return car;
    }

    public String getEmail() {
        return email;
    }

    public String getDateRetire() {
        return dateRetire;
    }

    public String getDateRestitution() {
        return dateRestitution;
    }

    public int getPayment() {
        return payment;
    }

    public double getPrice() {
        return price;
    }
}
