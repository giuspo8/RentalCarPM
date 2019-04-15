package com.example.rentalcar;

public class CreditCard {
    private int number;
    private String expireDate;
    private int secureCode;
    private double credit;

    public CreditCard(int number, String expireDate, int secureCode, double credit) {
        this.number = number;
        this.expireDate = expireDate;
        this.secureCode = secureCode;
        this.credit = credit;
    }

    public int getNumber() {
        return number;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public int getSecureCode() {
        return secureCode;
    }

    public double getCredit() {
        return credit;
    }


    public void recharge(double money) {
        credit+=money;
    }

    public boolean getPayment(double price){
        if (price>=credit) return false;
        else credit-=price;
        return true;
    }
}
