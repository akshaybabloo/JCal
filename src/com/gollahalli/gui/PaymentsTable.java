package com.gollahalli.gui;

/**
 * Created by akshayrajgollahalli on 16/09/15.
 */
public class PaymentsTable {

    private String year;
    private String interest;
    private String principal;
    private String balance;

    public PaymentsTable(String year, String interest, String principal, String balance) {
        this.year = year;
        this.interest = interest;
        this.principal = principal;
        this.balance = balance;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
