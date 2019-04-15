package com.example.rentalcar;

//creiamo una classe CarItem con attributi caratteristici delle auto che vogliamo far visualizzare
public class CarItem {
    private int resIdImage;//id dell'immagine della macchina
    private String carName;//nome dell'auto
    private String classCar;//classe dell'auto
    private double priceGg;//prezzo giornaliero
    private String carShift;//cambio
    private int numberOfPassengers;//numero di posti

    public CarItem(int resIdImage, String carName, String classCar, double priceGg, String carShift, int numberOfPassengers) {
        this.resIdImage = resIdImage;
        this.carName = carName;
        this.classCar = classCar;
        this.priceGg = priceGg;
        this.carShift = carShift;
        this.numberOfPassengers = numberOfPassengers;
    }

    public int getResIdImage() {
        return resIdImage;
    }

    public String getCarName() {
        return carName;
    }

    public String getClassCar() {
        return classCar;
    }

    public double getPriceGg() {
        return priceGg;
    }

    public String getCarShift() {
        return carShift;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }
}
