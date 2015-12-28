package com.gollahalli.api;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akshayrajgollahalli on 2/10/15.
 */
public class General {
    public static final Logger logger = LoggerFactory.getLogger(General.class);

    public String getDate() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
        return format.format(date);
    }

    public String getVersion() {
        return "1.2.0";
    }
    
    public String getRoot(){
        return System.getProperty("user.home");
    }
    
    public boolean isMacOS(){
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public void templateMaker(String companyName, String custFullName, String custAddress, String currentDate, String loanAmount, String years, String months, String typeOfPayments, String payments, String totalInterest, String totalPayments, String typeOfTime, String data, String year, String contactName, String contactAddress, String contactNumber, String faxNumber){

        Configuration cfg = new Configuration();

        try {
            Template template = cfg.getTemplate("/Template.ftl");

            Map<String, Object> data1 = new HashMap<>();
            data1.put("companyName", companyName);
            data1.put("custFullName", custFullName);
            data1.put("custAddress", custAddress);
            data1.put("currentDate", currentDate);
            data1.put("loanAmount", loanAmount);
            data1.put("years", years);
            data1.put("months", months);
            data1.put("typeOfPayments", typeOfPayments);
            data1.put("payments", payments);
            data1.put("totalInterest", totalInterest);
            data1.put("totalPayments", totalPayments);
            data1.put("typeOfTime", typeOfTime);
            data1.put("data", data);
            data1.put("year", year);
            data1.put("contactName", contactName);
            data1.put("contactAddress", contactAddress);
            data1.put("contactNumber", contactNumber);
            data1.put("faxNumber", faxNumber);
            General g = new General();
            Writer file = new FileWriter(new File(g.getRoot() + "/.JCal/temp.html"));
            template.process(data1,file);
            file.flush();
            file.close();

        } catch (IOException | TemplateException e) {
            logger.error("error occured" + e.getMessage());
            e.printStackTrace();
        }
    }
}
