package com.gollahalli.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by akshayrajgollahalli on 2/10/15.
 */
public class PropertiesWriter {
    String companyName;
    String name;
    String address;
    String contactNumber;
    String faxNumber;

    public PropertiesWriter(String companyName, String name, String address, String contactNumber, String faxNumber) {
        this.companyName = companyName;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.faxNumber = faxNumber;


        Properties properties = new Properties();

        properties.setProperty("CompanyName", this.companyName);
        properties.setProperty("Name", this.name);
        properties.setProperty("Address", this.address);
        properties.setProperty("ContactNumber", this.contactNumber);
        properties.setProperty("FaxNumber", this.faxNumber);


        try {
            File file = new File("JCal.properties");

            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(file);
            properties.store(fileOutputStream, "Your JCal properties");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
