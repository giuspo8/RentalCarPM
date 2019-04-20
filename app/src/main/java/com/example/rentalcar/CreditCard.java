package com.example.rentalcar;

public class CreditCard {
    private String number;//numero carta di credito
    private String expireDate;//data di scadenza
    private int secureCode;//codice di sicurezza
    private double credit;//saldo

    public CreditCard() {

    }

    public CreditCard(String number, String expireDate, int secureCode, double credit) {
        this.number = number;
        this.expireDate = expireDate;
        this.secureCode = secureCode;
        this.credit = credit;
    }

    public String getNumber() {
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

    public Object getCreditCard(String number){
        if (this.number==number) {
            return this;
        }
        else return null;
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
