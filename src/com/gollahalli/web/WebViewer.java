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
import com.gollahalli.gui.PaymentsTable;
import javafx.scene.chart.XYChart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;


/**
 * This class generates HTML for printing and saving feature.
 */
public class WebViewer {

    double loanAmountText;
    double interestText;
    double yearsTextMonth;
    String loanAmount;
    String years;
    String months;
    String monthlyPayments;
    String totalInterest;
    String totalPayments;
    String name;
    String address1;

    public WebViewer() {
    }

    /**
     * @param loanAmountText    Loan amount
     * @param interestText  Interest rate
     * @param yearsTextMonth    Years in months
     * @param loanAmount    Loan amount
     * @param years         Years
     * @param months        Months
     * @param monthlyPayments   monthly payments
     * @param totalInterest     Total interests
     * @param totalPayments     Total payments
     * @param name  Name of the client
     * @param address1  Address 1 of the client
     */
    public WebViewer(double loanAmountText, double interestText, double yearsTextMonth, String loanAmount, String years, String months, String monthlyPayments, String totalInterest, String totalPayments, String name, String address1) {
        this.loanAmountText = loanAmountText;
        this.interestText = interestText;
        this.yearsTextMonth = yearsTextMonth;
        this.loanAmount = loanAmount;
        this.years = years;
        this.months = months;
        this.monthlyPayments = monthlyPayments;
        this.totalInterest = totalInterest;
        this.totalPayments = totalPayments;
        this.name = name;
        this.address1 = address1;
    }

    /**
     * This constructor is used to set the Name and address for "To" when printing.
     *
     * @param name  Name of the client
     * @param address1  Address 1 of the client
     */
    public WebViewer(String name, String address1) {
        this.name = name;
        this.address1 = address1;
    }

    /**
     * @param loanAmountText    Loan amount
     * @param interestText  Interest rate
     * @param yearsTextMonth    Years in months
     * @param loanAmount    Loan amount
     * @param years         Years
     * @param months        Months
     * @param monthlyPayments   monthly payments
     * @param totalInterest     Total interests
     * @param totalPayments     Total payments
     */
    public WebViewer(double loanAmountText, double interestText, double yearsTextMonth, String loanAmount, String years, String months, String monthlyPayments, String totalInterest, String totalPayments) {
        this.loanAmountText = loanAmountText;
        this.interestText = interestText;
        this.yearsTextMonth = yearsTextMonth;
        this.loanAmount = loanAmount;
        this.years = years;
        this.months = months;
        this.monthlyPayments = monthlyPayments;
        this.totalInterest = totalInterest;
        this.totalPayments = totalPayments;
    }

    public WebViewer(String loanAmount, String years, String months, String monthlyPayments, String totalInterest, String totalPayments) {
        this.loanAmount = loanAmount;
        this.years = years;
        this.months = months;
        this.monthlyPayments = monthlyPayments;
        this.totalInterest = totalInterest;
        this.totalPayments = totalPayments;
    }

    public WebViewer(double loanAmountText, double interestText, double yearsTextMonth) {
        this.loanAmountText = loanAmountText;
        this.interestText = interestText;
        this.yearsTextMonth = yearsTextMonth;
    }

    /**
     * @param aDouble   Input number to be converted
     * @param currentLocale Local builder
     * @return  String
     */
    static String displayCurrency(Double aDouble, Locale currentLocale) {
        NumberFormat currencyFormatter = NumberFormat
                .getCurrencyInstance(currentLocale);

        return currencyFormatter.format(aDouble);
    }

    /**
     * @param string Takes in a string number and converts it into Double
     * @return  String Returns string eg: $1,123,123.00
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

    /**
     *
     * To use this, do the following
     *<pre>
     * {@code
     *     WebViewer webViewer = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsText, loanAmountString, yearsTextString, monthsTextString, String.valueOf(monthlyOutput), String.valueOf(bd.doubleValue()), String.valueOf(bd.doubleValue() + loanAmountText));
     *     String test = webViewer.webReturnMonthly();
     * }
     * </pre>
     *
     * @return Monthly HTML code
     */
    public String webReturnMonthly(){
        Calculate calculate = new Calculate();

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
            html += "<tr><td>"+ String.valueOf(someNum++) + "<td class=\"text-center\">" + currencyMaker(String.valueOf(newYearly[0][i])) + "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(newYearly[1][i])) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(newYearly[2][i])) + "</td></tr>";
        }

        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title></title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://bootswatch.com/flatly/bootstrap.min.css\">\n" +
                "\n" +
                "<style>" +
                ".invoice-title h2, .invoice-title h3 {\n" +
                "    display: inline-block;\n" +
                "}\n" +
                "\n" +
                ".table > tbody > tr > .no-line {\n" +
                "    border-top: none;\n" +
                "}\n" +
                "\n" +
                ".table > thead > tr > .no-line {\n" +
                "    border-bottom: none;\n" +
                "}\n" +
                "\n" +
                ".table > tbody > tr > .thick-line {\n" +
                "    border-top: 2px solid;\n" +
                "}" +
                "</style>" +
                "\n" +
                "    <!-- Latest compiled and minified JavaScript -->\n" +
<<<<<<< HEAD
                "    <script src=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js\"></script>\n" +
=======
                "    <script src=\"local:bootstrap.min.js\"></script>\n" +
>>>>>>> origin/dev
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-xs-12\">\n" +
                "            <div class=\"invoice-title\">\n" +
                "                <h2>Invoice</h2>\n" +
                "\n" +
                "                <h3 class=\"pull-right\">Order # 12345</h3>\n" +
                "            </div>\n" +
                "            <hr>\n" +
                "            <div class=\"row\">\n" +
                "                <div class=\"col-xs-6\">\n" +
                "                    <address>\n" +
                "                        <strong>To:</strong><br>\n" +
                "                        "+ this.name+"<br>\n" +
                "                        "+ this.address1+"<br>\n" +
                "                    </address>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-6 text-right\">\n" +
                "                    <address>\n" +
                "                        <strong>Data:</strong><br>\n" +
                "                        October 12, 2015\n" +
                "                    </address>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-xs-6\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-heading\">\n" +
                "                    <h3 class=\"panel-title\"><strong>Summary</strong></h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\">\n" +
                "                    <div class=\"table-responsive\">\n" +
                "                        <table class=\"table table-condensed table-bordered\">\n" +
                "                            <!--<thead>-->\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <th><strong>Loan Amount</strong></th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(loanAmount) +"</td>\n" +
                "                            </tr>\n" +
                "                            <!--</thead>-->\n" +
                "                            <!--<tbody>-->\n" +
                "                            <!-- foreach ($order->lineItems as $line) or some such thing here -->\n" +
                "                            <tr>\n" +
                "                                <th>Years</th>\n" +
                "                                <td class=\"text-center\">"+ years +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Months</th>\n" +
                "                                <td class=\"text-center\">"+ months +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Monthly payments</th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(monthlyPayments) +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Total interest</th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(totalInterest) +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Total payments</th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(totalPayments) +"</td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-xs-6\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-heading\">\n" +
                "                    <h3 class=\"panel-title\"><strong>Pie chart</strong></h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\">\n" +
                "                    <img src=\"pie.png\">\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-md-12\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-heading\">\n" +
                "                    <h3 class=\"panel-title\"><strong>Order summary</strong></h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\">\n" +
                "                    <div class=\"table-responsive\">\n" +
                "                        <table class=\"table table-condensed table-bordered\">\n" +
                "                            <thead>\n" +
                "                            <tr>\n" +
                "                                <td><strong>Year</strong></td>\n" +
                "                                <td class=\"text-center\"><strong>Principal</strong></td>\n" +
                "                                <td class=\"text-center\"><strong>Interest</strong></td>\n" +
                "                                <td class=\"text-right\"><strong>Balance</strong></td>\n" +
                "                            </tr>\n" +
                "                            </thead>\n" +
                "                            <tbody>\n" +
                "                            <!-- foreach ($order->lineItems as $line) or some such thing here -->\n" +
                html +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-xs-4\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-body\">\n" +
                "                    Basic panel example\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-xs-4\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-body\">\n" +
                "                    Basic panel example\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-xs-4\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-body\">\n" +
                "                    Basic panel example\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
<<<<<<< HEAD
=======

        html1 = html1.replace("local:", getClass().getResource(".").toString());
        return html1;
>>>>>>> origin/dev
    }

    /**
     *
     * To use this, do the following
     * <pre>
     *  {@code
     *     WebViewer webViewer = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsText, loanAmountString, yearsTextString, monthsTextString, String.valueOf(monthlyOutput), String.valueOf(bd.doubleValue()), String.valueOf(bd.doubleValue() + loanAmountText));
     *     String test = webViewer.webReturnMonthly();
     * }
     * </pre>
     *
     * @return String of HTML
     */
    public String webReturnYearly(){
        Calculate calculate = new Calculate();

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
        int someNumber = 1;

        double yearlyPrincipal = 0.0;
        double yearlyInterest = 0.0;
        double yearlyBalance = 0.0;
        for (int i = 0; i < newYearly[0].length; i++) {
            if (i % 12 == 0 && i != 0) {
                html += "<tr><td>"+ String.valueOf(someNumber++) + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN))) +
                        "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN))) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(newYearly[2][i])) + "</td></tr>";
                yearlyPrincipal = 0;
                yearlyInterest = 0;
                yearlyBalance = 0;
            }
            yearlyPrincipal += newYearly[0][i];
            yearlyInterest += newYearly[1][i];
            newYearly[2][i] -= yearlyBalance;
            if (i == newYearly[0].length - 1 && i != 0) {
                html += "<tr><td>"+ String.valueOf(someNumber++) + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN))) +
                "</td>" + "<td class=\"text-center\">" + currencyMaker(String.valueOf(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN))) + "</td>" + "<td class=\"text-right\">" + currencyMaker(String.valueOf(newYearly[2][i])) + "</td></tr>";

            }

        }
        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title></title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://bootswatch.com/flatly/bootstrap.min.css\">\n" +
                "\n" +
                "<style>" +
                ".invoice-title h2, .invoice-title h3 {\n" +
                "    display: inline-block;\n" +
                "}\n" +
                "\n" +
                ".table > tbody > tr > .no-line {\n" +
                "    border-top: none;\n" +
                "}\n" +
                "\n" +
                ".table > thead > tr > .no-line {\n" +
                "    border-bottom: none;\n" +
                "}\n" +
                "\n" +
                ".table > tbody > tr > .thick-line {\n" +
                "    border-top: 2px solid;\n" +
                "}" +
                "</style>" +
                "\n" +
                "    <!-- Latest compiled and minified JavaScript -->\n" +
                "    <script src=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-xs-12\">\n" +
                "            <div class=\"invoice-title\">\n" +
                "                <h2>Invoice</h2>\n" +
                "\n" +
                "                <h3 class=\"pull-right\">Order # 12345</h3>\n" +
                "            </div>\n" +
                "            <hr>\n" +
                "            <div class=\"row\">\n" +
                "                <div class=\"col-xs-6\">\n" +
                "                    <address>\n" +
                "                        <strong>To:</strong><br>\n" +
                "                        "+ this.name+"<br>\n" +
                "                        "+ this.address1+"<br>\n" +
                "                    </address>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-6 text-right\">\n" +
                "                    <address>\n" +
                "                        <strong>Data:</strong><br>\n" +
                "                        October 12, 2015\n" +
                "                    </address>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-xs-6\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-heading\">\n" +
                "                    <h3 class=\"panel-title\"><strong>Summary</strong></h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\">\n" +
                "                    <div class=\"table-responsive\">\n" +
                "                        <table class=\"table table-condensed table-bordered\">\n" +
                "                            <!--<thead>-->\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <th><strong>Loan Amount</strong></th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(loanAmount) +"</td>\n" +
                "                            </tr>\n" +
                "                            <!--</thead>-->\n" +
                "                            <!--<tbody>-->\n" +
                "                            <!-- foreach ($order->lineItems as $line) or some such thing here -->\n" +
                "                            <tr>\n" +
                "                                <th>Years</th>\n" +
                "                                <td class=\"text-center\">"+ years +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Months</th>\n" +
                "                                <td class=\"text-center\">"+ months +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Monthly payments</th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(monthlyPayments) +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Total interest</th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(totalInterest) +"</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Total payments</th>\n" +
                "                                <td class=\"text-center\">"+ currencyMaker(totalPayments) +"</td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-xs-6\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-heading\">\n" +
                "                    <h3 class=\"panel-title\"><strong>Pie chart</strong></h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\">\n" +
                "                    <img src=\"/Users/akshayrajgollahalli/Box Sync/MyDrive/Projects/Gollahalli GitHub/JCal/src/resource/JCal-logo.png\">\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-md-12\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-heading\">\n" +
                "                    <h3 class=\"panel-title\"><strong>Order summary</strong></h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\">\n" +
                "                    <div class=\"table-responsive\">\n" +
                "                        <table class=\"table table-condensed table-bordered\">\n" +
                "                            <thead>\n" +
                "                            <tr>\n" +
                "                                <td><strong>Year</strong></td>\n" +
                "                                <td class=\"text-center\"><strong>Principal</strong></td>\n" +
                "                                <td class=\"text-center\"><strong>Interest</strong></td>\n" +
                "                                <td class=\"text-right\"><strong>Balance</strong></td>\n" +
                "                            </tr>\n" +
                "                            </thead>\n" +
                "                            <tbody>\n" +
                "                            <!-- foreach ($order->lineItems as $line) or some such thing here -->\n" +
                html +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-xs-4\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-body\">\n" +
                "                    Basic panel example\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-xs-4\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-body\">\n" +
                "                    Basic panel example\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-xs-4\">\n" +
                "            <div class=\"panel panel-default\">\n" +
                "                <div class=\"panel-body\">\n" +
                "                    Basic panel example\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }
}
