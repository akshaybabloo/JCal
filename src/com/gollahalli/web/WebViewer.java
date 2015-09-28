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

/**
 * Created by akshayrajgollahalli on 28/09/15.
 */
public class WebViewer {
    public static void main(String[] args) {
        String hi = "";
        String[] h = {"hi","hello","hey"};
        for (int i = 0; i < 3; i++) {
            hi += "<td>" + h[i] + "</td>";
        }
        System.out.println(hi);
    }

    double loanAmountText;
    double interestText;
    double yearsTextMonth;

    public WebViewer() {
    }

    public WebViewer(double loanAmountText, double interestText, double yearsTextMonth) {
        this.loanAmountText = loanAmountText;
        this.interestText = interestText;
        this.yearsTextMonth = yearsTextMonth;
    }

    public String webReturn(){
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

        String testme1 = "";
        int someNum = 1;

        String[][] whyNot = new String[1][(int)yearsTextMonth];

        for (int i = 0; i < yearsTextMonth; i++) {
            testme1 += "<tr><td>"+ String.valueOf(someNum++) + "<td>" + String.valueOf(newYearly[0][i]) + "</td>" + "<td>" + String.valueOf(newYearly[1][i]) + "</td>" + "<td>" + String.valueOf(newYearly[2][i]) + "</td></tr>";
        }

        String html = "<!DOCTYPE html>\n" +
                "<!--\n" +
                "  ~ Copyright (c) 2015 Akshay Raj Gollahalli\n" +
                "  ~\n" +
                "  ~  This program is free software; you can redistribute it and/or modify\n" +
                "  ~  it under the terms of the GNU General Public License as published by\n" +
                "  ~  the Free Software Foundation; either version 2 of the License, or\n" +
                "  ~  (at your option) any later version.\n" +
                "  ~\n" +
                "  ~  This program is distributed in the hope that it will be useful,\n" +
                "  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                "  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                "  ~  GNU General Public License for more details.\n" +
                "  ~\n" +
                "  ~  You should have received a copy of the GNU General Public License along\n" +
                "  ~  with this program; if not, write to the Free Software Foundation, Inc.,\n" +
                "  ~  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n" +
                "  -->\n" +
                "\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title></title>\n" +
                "    <!-- Latest compiled and minified CSS -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n" +
                "\n" +
                "    <!-- Optional theme -->\n" +
                "    <link rel=\"stylesheet\" href=\"temp.css\">\n" +
                "\n" +
                "    <!-- Latest compiled and minified JavaScript -->\n" +
                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n" +
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
                "                        John Smith<br>\n" +
                "                        1234 Main<br>\n" +
                "                        Apt. 4B<br>\n" +
                "                        Springfield, ST 54321\n" +
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
                "                                <td class=\"text-center\"><strong>Price</strong></td>\n" +
                "                            </tr>\n" +
                "                            <!--</thead>-->\n" +
                "                            <!--<tbody>-->\n" +
                "                            <!-- foreach ($order->lineItems as $line) or some such thing here -->\n" +
                "                            <tr>\n" +
                "                                <th>Years</th>\n" +
                "                                <td class=\"text-center\">$10.99</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Months</th>\n" +
                "                                <td class=\"text-center\">$20.00</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Monthly payments</th>\n" +
                "                                <td class=\"text-center\">$600.00</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Total interest</th>\n" +
                "                                <td class=\"text-center\">$600.00</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <th>Total payments</th>\n" +
                "                                <td class=\"text-center\">$600.00</td>\n" +
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
//                "                                <td>BS-200</td>\n" +
                                                    testme1 +
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
        return html;
    }

}
