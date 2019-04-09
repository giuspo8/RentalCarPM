package com.example.rentalcar;

public class CarItem {
    int resIdImage;//id dell'immagine della macchina
    String carName;//nome dell'auto
    String classCar;//classe dell'auto
    double priceGg;//prezzo giornaliero
    String carShift;//cambio
    int numberOfPassengers;//numero di posti

    public CarItem(int resIdImage, String carName, String classCar, double priceGg, String carShift, int numberOfPassengers) {
        this.resIdImage = resIdImage;
        this.carName = carName;
        this.classCar = classCar;
        this.priceGg = priceGg;
        this.carShift = carShift;
        this.numberOfPassengers = numberOfPassengers;
    }
}
