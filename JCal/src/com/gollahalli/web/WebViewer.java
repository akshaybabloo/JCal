/*
 * Copyright (c) 2015 Akshay Raj Gollahalli
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.gollahalli.web;

import com.gollahalli.api.Calculate;
import com.gollahalli.api.General;
import com.gollahalli.properties.PropertiesReader;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * This class generates HTML for printing and saving feature.
 */
public class WebViewer {

    public static final Logger logger = LoggerFactory.getLogger(WebViewer.class);


    double loanAmountText;
    double interestText;
    double yearsTextMonth;
    String loanAmount;
    String years;
    String months;
    String monthlyPayments;
    String totalInterest;
    String totalPayments;
    String custName;
    String custAddress;

    General general = new General();

    public WebViewer() {
    }

    public WebViewer(double loanAmountText, double interestText, double yearsTextMonth, String loanAmount, String years, String months, String monthlyPayments, String totalInterest, String totalPayments, String custName, String custAddress) {
        this.loanAmountText = loanAmountText;
        this.interestText = interestText;
        this.yearsTextMonth = yearsTextMonth;
        this.loanAmount = loanAmount;
        this.years = years;
        this.months = months;
        this.monthlyPayments = monthlyPayments;
        this.totalInterest = totalInterest;
        this.totalPayments = totalPayments;
        this.custName = custName;
        this.custAddress = custAddress;
        logger.info("reached webviewer");

    }

    /**
     * @param aDouble       Input number to be converted
     * @param currentLocale Local builder
     * @return String
     */
    static String displayCurrency(Double aDouble, Locale currentLocale) {
        NumberFormat currencyFormatter = NumberFormat
                .getCurrencyInstance(currentLocale);

        return currencyFormatter.format(aDouble);
    }

    /**
     * @param string Takes in a string number and converts it into Double
     * @return String Returns string eg: $1,123,123.00
     */
    static String currencyMaker(String string) {
        double convertMe = Double.parseDouble(string);
        ArrayList<Locale> locales = new ArrayList<>();
        locales.add(0, new Locale.Builder().setLanguage("en").setRegion("US")
                .build());
        String temp = "";
        for (Locale locale : locales) {
            temp = displayCurrency(convertMe, locale);
        }
        return temp;
    }

    static String[] propertyReader() {
        String[] result = new String[6];
        result[0] = "";
        result[1] = "";
        result[2] = "";
        result[3] = "";
        result[4] = "";
        result[5] = "";
        
        General g = new General();
        
        if (new File(g.getRoot() + "/.JCal/JCal.properties").exists()) {
            PropertiesReader propertiesReader = new PropertiesReader();
            result = propertiesReader.reader();
        }

        return result;
    }

    /**
     * To use this, do the following
     * <pre>
     * {@code
     *     WebViewer webViewer = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsText, loanAmountString, yearsTextString, monthsTextString, String.valueOf(monthlyOutput), String.valueOf(bd.doubleValue()), String.valueOf(bd.doubleValue() + loanAmountText));
     *     String test = webViewer.webReturnMonthly();
     * }
     * </pre>
     *
     */
    public void webReturnMonthly() {
        Calculate calculate = new Calculate();

        String[] property = propertyReader();

        double[][] monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(loanAmountText, interestText, yearsTextMonth);

        double[][] newYearly = new double[5][(int) yearsTextMonth];
        newYearly[0][0] = monthlyChartYearly[2][0];
        newYearly[1][0] = monthlyChartYearly[1][0];
        newYearly[2][0] = monthlyChartYearly[3][0];
        for (int i = 1; i < yearsTextMonth; i++) {
            monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(monthlyChartYearly[3][0], interestText, yearsTextMonth - i);
            newYearly[0][i] = monthlyChartYearly[2][0];
            newYearly[1][i] = monthlyChartYearly[1][0];
            newYearly[2][i] = monthlyChartYearly[3][0];
        }

        String html = "";
        int someNum = 1;

        for (int i = 0; i < yearsTextMonth; i++) {
            html += "<tr><td>" + String.valueOf(someNum++)+ "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(newYearly[0][i])) + "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(newYearly[1][i])) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(newYearly[2][i])) + "</td></tr>";
        }

        general.templateMaker(WordUtils.capitalize(property[0]), WordUtils.capitalize(this.custName), WordUtils.capitalize(this.custAddress),
                general.getDate(), currencyMaker(loanAmount), years, months, " type of payment", currencyMaker(monthlyPayments),
                currencyMaker(totalInterest), currencyMaker(totalPayments), "type of time", html, property[5],
                WordUtils.capitalize(property[1]), WordUtils.capitalize(property[2]), property[3], property[4]);

    }

    /**
     * To use this, do the following
     * <pre>
     *  {@code
     *     WebViewer webViewer = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsText, loanAmountString, yearsTextString, monthsTextString, String.valueOf(monthlyOutput), String.valueOf(bd.doubleValue()), String.valueOf(bd.doubleValue() + loanAmountText));
     *     String test = webViewer.webReturnMonthly();
     * }
     * </pre>
     *
     */
    public void webReturnYearly() {
        logger.info("yearly called");
        Calculate calculate = new Calculate();

        double[][] monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(loanAmountText, interestText, yearsTextMonth);
        logger.info("monthly chart yearly called");
        String[] property = propertyReader();

        double[][] newYearly = new double[5][(int) yearsTextMonth];
        newYearly[0][0] = monthlyChartYearly[2][0];
        newYearly[1][0] = monthlyChartYearly[1][0];
        newYearly[2][0] = monthlyChartYearly[3][0];
        for (int i = 1; i < yearsTextMonth; i++) {
            monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(monthlyChartYearly[3][0], interestText, yearsTextMonth - i);
            newYearly[0][i] = monthlyChartYearly[2][0];
            newYearly[1][i] = monthlyChartYearly[1][0];
            newYearly[2][i] = monthlyChartYearly[3][0];
        }

        String html = "";
        int someNumber = 1;


        double yearlyPrincipal = 0.0;
        double yearlyInterest = 0.0;
        double yearlyBalance = loanAmountText;
        for (int i = 0; i < newYearly[0].length; i++) {
            if (i % 12 == 0 && i != 0) {
                html += "<tr><td>" + String.valueOf(someNumber++)+ "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN))) +
                        "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN))) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyBalance).setScale(2, RoundingMode.HALF_DOWN))) + "</td></tr>";
                yearlyPrincipal = 0;
                yearlyInterest = 0;
            }
            yearlyPrincipal += newYearly[0][i];
            yearlyInterest += newYearly[1][i];
            yearlyBalance -= newYearly[0][i];
            if (i == newYearly[0].length - 1 && i != 0) {
                html += "<tr><td>" + String.valueOf(someNumber++)+ "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN))) +
                        "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN))) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyBalance).setScale(2, RoundingMode.HALF_DOWN))) + "</td></tr>";

            }

        }
        
        general.templateMaker(WordUtils.capitalize(property[0]), WordUtils.capitalize(this.custName), WordUtils.capitalize(this.custAddress),
                general.getDate(), currencyMaker(loanAmount), years, months, " type of payment", currencyMaker(monthlyPayments),
                currencyMaker(totalInterest), currencyMaker(totalPayments), "type of time", html, property[5],
                WordUtils.capitalize(property[1]), WordUtils.capitalize(property[2]), property[3], property[4]);

    }

    public void webReturnWeekly() {
        Calculate calculate = new Calculate();

        String[] property = propertyReader();

        double[][] monthlyChartYearly = calculate.fixedRateMortgageWeeklyChart(loanAmountText, interestText, yearsTextMonth);

        double[][] newYearly = new double[5][(int) yearsTextMonth];
        newYearly[0][0] = monthlyChartYearly[2][0];
        newYearly[1][0] = monthlyChartYearly[1][0];
        newYearly[2][0] = monthlyChartYearly[3][0];
        for (int i = 1; i < yearsTextMonth; i++) {
            monthlyChartYearly = calculate.fixedRateMortgageWeeklyChart(monthlyChartYearly[3][0], interestText, (yearsTextMonth) - i);
            newYearly[0][i] = monthlyChartYearly[2][0];
            newYearly[1][i] = monthlyChartYearly[1][0];
            newYearly[2][i] = monthlyChartYearly[3][0];
        }

        String html = "";
        int someNum = 1;

        for (int i = 0; i < yearsTextMonth; i++) {
            html += "<tr><td>" + String.valueOf(someNum++)+ "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(newYearly[0][i])) + "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(newYearly[1][i])) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(newYearly[2][i])) + "</td></tr>";
        }

        general.templateMaker(WordUtils.capitalize(property[0]), WordUtils.capitalize(this.custName), WordUtils.capitalize(this.custAddress),
                general.getDate(), currencyMaker(loanAmount), years, months, " type of payment", currencyMaker(monthlyPayments),
                currencyMaker(totalInterest), currencyMaker(totalPayments), "type of time", html, property[5],
                WordUtils.capitalize(property[1]), WordUtils.capitalize(property[2]), property[3], property[4]);
    }

    public void webReturnFortnightly() {
        Calculate calculate = new Calculate();

        double yearsTextMonth1 = yearsTextMonth / 2;
        // monthly payments output
        double weeklyOutputForFortnightly = calculate.fixedRateMortgageFortnightly(loanAmountText, yearsTextMonth1, interestText);
        // total interest paid
        BigDecimal bd2 = new BigDecimal((weeklyOutputForFortnightly * yearsTextMonth1) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);
        System.out.println(weeklyOutputForFortnightly);
        System.out.println(bd2);


        double[][] monthlyChartYearly = calculate.fixedRateMortgageWeeklyChart(loanAmountText, interestText, yearsTextMonth);

        String[] property = propertyReader();

        double[][] newFortnightly = new double[5][(int) yearsTextMonth];
        newFortnightly[0][0] = monthlyChartYearly[2][0];
        newFortnightly[1][0] = monthlyChartYearly[1][0];
        newFortnightly[2][0] = monthlyChartYearly[3][0];
        for (int i = 1; i < yearsTextMonth; i++) {
            monthlyChartYearly = calculate.fixedRateMortgageWeeklyChart(monthlyChartYearly[3][0], interestText, yearsTextMonth - i);
            newFortnightly[0][i] = monthlyChartYearly[2][0];
            newFortnightly[1][i] = monthlyChartYearly[1][0];
            newFortnightly[2][i] = monthlyChartYearly[3][0];
        }

        String html = "";
        int someNumber = 1;

        double yearlyPrincipal = 0.0;
        double yearlyInterest = 0.0;
        double yearlyBalance = loanAmountText;
        for (int i = 0; i < newFortnightly[0].length; i++) {
            if (i % 2 == 0 && i != 0) {
                html += "<tr><td>" + String.valueOf(someNumber++)+ "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN))) +
                        "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN))) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyBalance).setScale(2, RoundingMode.HALF_DOWN))) + "</td></tr>";
                yearlyPrincipal = 0;
                yearlyInterest = 0;
            }
            yearlyPrincipal += newFortnightly[0][i];
            yearlyInterest += newFortnightly[1][i];
            yearlyBalance -= newFortnightly[0][i];
            if (i == newFortnightly[0].length - 1 && i != 0) {
                html += "<tr><td>" + String.valueOf(someNumber++)+ "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN))) +
                        "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN))) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyBalance).setScale(2, RoundingMode.HALF_DOWN))) + "</td></tr>";

            }

        }

        general.templateMaker(WordUtils.capitalize(property[0]), WordUtils.capitalize(this.custName), WordUtils.capitalize(this.custAddress),
                general.getDate(), currencyMaker(loanAmount), years, months, " type of payment", currencyMaker(String.valueOf(weeklyOutputForFortnightly)),
                currencyMaker(String.valueOf(bd2.doubleValue())), currencyMaker(String.valueOf(bd2.doubleValue() + loanAmountText)), "type of time", html, property[5],
                WordUtils.capitalize(property[1]), WordUtils.capitalize(property[2]), property[3], property[4]);

    }
}
