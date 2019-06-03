package com.example.rentalcar.LinkedReservationClasses;

public class Segnalations {
    private int id;
    private String problem;
    //semplice classe che contiene come unico attributo il nome della Stazione
    public Segnalations(String problem) {
        this.problem = problem;
    }

    public String getProblem() {
        return this.problem;
    }

}
