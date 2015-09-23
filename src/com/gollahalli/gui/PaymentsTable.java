package com.gollahalli.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class is used to set data for the table
 */
public class PaymentsTable {

    public SimpleIntegerProperty year = new SimpleIntegerProperty();
    public SimpleDoubleProperty interest = new SimpleDoubleProperty();
    public SimpleDoubleProperty principal = new SimpleDoubleProperty();
    public SimpleDoubleProperty balance = new SimpleDoubleProperty();


    /**
     * @return SimpleIntegerProperty An SimpleIntegerProperty is returned
     */
    public int getYear() {
        return year.get();
    }


    /**
     * @return SimpleDoubleProperty An SimpleDoubleProperty is returned
     */
    public double getInterest() {
        return interest.get();
    }


    /**
     * @return SimpleDoubleProperty An SimpleDoubleProperty is returned
     */
    public double getPrincipal() {
        return principal.get();
    }


    /**
     * @return SimpleDoubleProperty An SimpleDoubleProperty is returned
     */
    public double getBalance() {
        return balance.get();
    }

}
