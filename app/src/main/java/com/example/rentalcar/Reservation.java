package com.example.rentalcar;

public class Reservation {
    private int id;
    private StationNames retStation;
    private StationNames recStation;
    private CarItem car;
    private String email;
    private int yearRet;
    private int monthRet;
    private int dayRet;
    private int hourRet;
    private int minRet;
    private int yearRic;
    private int monthRic;
    private int dayRic;
    private int hourRic;
    private int minRic;
    private int payment;

    public Reservation(int id, StationNames retStation, StationNames recStation, CarItem car, String email, int yearRet, int monthRet, int dayRet, int hourRet, int minRet, int yearRic, int monthRic, int dayRic, int hourRic, int minRic, int payment) {
        this.id = id;
        this.retStation = retStation;
        this.recStation = recStation;
        this.car = car;
        this.email = email;
        this.yearRet = yearRet;
        this.monthRet = monthRet;
        this.dayRet = dayRet;
        this.hourRet = hourRet;
        this.minRet = minRet;
        this.yearRic = yearRic;
        this.monthRic = monthRic;
        this.dayRic = dayRic;
        this.hourRic = hourRic;
        this.minRic = minRic;
        this.payment = payment;
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

    public int getYearRet() {
        return yearRet;
    }

    public int getMonthRet() {
        return monthRet;
    }

    public int getDayRet() {
        return dayRet;
    }

    public int getHourRet() {
        return hourRet;
    }

    public int getMinRet() {
        return minRet;
    }

    public int getYearRic() {
        return yearRic;
    }

    public int getMonthRic() {
        return monthRic;
    }

    public int getDayRic() {
        return dayRic;
    }

    public int getHourRic() {
        return hourRic;
    }

    public int getMinRic() {
        return minRic;
    }

    public int getPayment() {
        return payment;
    }
}
