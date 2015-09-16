package com.gollahalli.gui;

import javafx.beans.property.SimpleDoubleProperty;

public class PaymentsTable {

    public SimpleDoubleProperty year = new SimpleDoubleProperty();
    public SimpleDoubleProperty interest = new SimpleDoubleProperty();
    public SimpleDoubleProperty principal = new SimpleDoubleProperty();
    public SimpleDoubleProperty balance = new SimpleDoubleProperty();

    public double getYear() {
        return year.get();
    }


    public double getInterest() {
        return interest.get();
    }


    public double getPrincipal() {
        return principal.get();
    }


    public double getBalance() {
        return balance.get();
    }

}
