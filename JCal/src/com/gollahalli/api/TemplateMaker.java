package com.gollahalli.api;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TemplateMaker {

    String companyName;
    String custFullName;
    String custAddress;
    String currentDate;
    String loanAmount;
    String years;
    String months;
    String typeOfPayments;
    String payments;
    String totalInterest;
    String totalPayments;
    String typeOfTime;
    String data;
    String year;
    String contactName;
    String contactAddress;
    String contactNumber;
    String faxNumber;

    public TemplateMaker(String companyName, String custFullName, String custAddress, String currentDate, String loanAmount, String years, String months, String typeOfPayments, String payments, String totalInterest, String totalPayments, String typeOfTime, String data, String year, String contactName, String contactAddress, String contactNumber, String faxNumber) {
        this.companyName = companyName;
        this.custFullName = custFullName;
        this.custAddress = custAddress;
        this.currentDate = currentDate;
        this.loanAmount = loanAmount;
        this.years = years;
        this.months = months;
        this.typeOfPayments = typeOfPayments;
        this.payments = payments;
        this.totalInterest = totalInterest;
        this.totalPayments = totalPayments;
        this.typeOfTime = typeOfTime;
        this.data = data;
        this.year = year;
        this.contactName = contactName;
        this.contactAddress = contactAddress;
        this.contactNumber = contactNumber;
        this.faxNumber = faxNumber;

        Configuration cfg = new Configuration();

        try {
            Template template = cfg.getTemplate("/resource/Template.ftl");

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

            Writer file = new FileWriter(new File("temp.html"));
            template.process(data1,file);
            file.flush();
            file.close();

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
