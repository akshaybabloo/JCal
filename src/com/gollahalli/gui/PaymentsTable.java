package com.gollahalli.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PaymentsTable {

    public SimpleIntegerProperty year = new SimpleIntegerProperty();
    public SimpleDoubleProperty interest = new SimpleDoubleProperty();
    public SimpleDoubleProperty principal = new SimpleDoubleProperty();
    public SimpleDoubleProperty balance = new SimpleDoubleProperty();

    public int getYear() {
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
