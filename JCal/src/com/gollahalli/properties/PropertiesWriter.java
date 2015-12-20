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
    String copyRightYear;

    String version;

    public PropertiesWriter(String companyName, String name, String address, String contactNumber, String faxNumber, String version, String copyRightYear) {
        this.companyName = companyName;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.faxNumber = faxNumber;
        this.version = version;
        this.copyRightYear = copyRightYear;


        Properties properties = new Properties();

        properties.setProperty("CompanyName", this.companyName);
        properties.setProperty("Name", this.name);
        properties.setProperty("Address", this.address);
        properties.setProperty("ContactNumber", this.contactNumber);
        properties.setProperty("FaxNumber", this.faxNumber);
        properties.setProperty("Version", this.version);
        properties.setProperty("CopyRightYear", this.copyRightYear);


        try {
            File file = new File("JCal.properties");

            FileOutputStream fileOutputStream;
            fileOutputStream = new FileOutputStream(file);
            properties.store(fileOutputStream, "JCal properties");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
