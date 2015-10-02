package com.gollahalli.properties;

/**
 * Created by akshayrajgollahalli on 2/10/15.
 */
public class Company {
    String companyName;
    String name;
    String address;
    String contactNumber;
    String contactFax;

    public Company(String companyName, String name, String address, String contactNumber, String contactFax) {
        this.companyName = companyName;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.contactFax = contactFax;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public String getContactFax() {
        return contactFax;
    }
}
