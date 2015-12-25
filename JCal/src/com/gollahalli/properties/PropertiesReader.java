package com.gollahalli.properties;

import com.gollahalli.api.General;
import java.io.*;
import java.util.Properties;

public class PropertiesReader {
    public String[] reader(){
        String[] result = new String[6];
        Properties properties = new Properties();
        InputStream is;
        
        General g = new General();
        
        try {
            File file = new File(g.getRoot() + "/.JCal/JCal.properties");
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            is = null;
        }

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result[0] = properties.getProperty("CompanyName");
        result[1] = properties.getProperty("Name");
        result[2] = properties.getProperty("Address");
        result[3] = properties.getProperty("ContactNumber");
        result[4] = properties.getProperty("FaxNumber");
        result[5] = properties.getProperty("CopyRightYear");

        return result;
    }
}
