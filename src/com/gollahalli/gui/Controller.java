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

package com.gollahalli.gui;

import com.gollahalli.api.Calculate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * This class is the main controller for JCal-gui.
 */
public class Controller {

    public static final Logger logger = LoggerFactory.getLogger(Controller.class);
    ObservableList<PaymentsTable> tableData;
    ObservableList<PieChart.Data> pieChartData;
    @FXML
    private TextField loan_amount;
    @FXML
    private TextField years;
    @FXML
    private TextField months;
    @FXML
    private TextField interest;
    @FXML
    private ComboBox repayment_type;
    @FXML
    private TableView<PaymentsTable> paymentsAnnual;
    @FXML
    private TableColumn primaryColumn;
    @FXML
    private TableColumn interestColumn;
    @FXML
    private TableColumn principalColumn;
    @FXML
    private TableColumn balanceColumn;
    @FXML
    private Label loan_amount_label;
    @FXML
    private Label years_label;
    @FXML
    private Label months_label;
    @FXML
    private Label monthly_payments_label;
    @FXML
    private Label total_interest_label;
    @FXML
    private Label total_payments_label;
    @FXML
    private GridPane summary_grid;
    @FXML
    private AreaChart<Number, Number> graph1;
    @FXML
    private AreaChart<Number, Number> graph2;
    @FXML
    private AreaChart<Number, Number> graph3;
    @FXML
    private PieChart pieChart;
    @FXML
    private MenuItem jcalAbout;
    @FXML
    private MenuItem jcalClose;
    @FXML
    private AnchorPane jcalAnchor;
    private double loan_amount_text = 0;
    private double months_text = 0;
    private double years_text = 0;
    private double interest_text = 0;
    private double years_text_month = 0;
    private int someNum = 2;

    public void initialize() {
        logger.info("controller started");
        XYChart.Series<Number, Number> seriesMonthlyInterest = new XYChart.Series();
        XYChart.Series<Number, Number> seriesNewPrincipal = new XYChart.Series();
        XYChart.Series<Number, Number> seriesForBalance = new XYChart.Series();
        XYChart.Series<Number, Number> seriesForInterest = new XYChart.Series();
        graph1.getData().add(seriesMonthlyInterest);
        graph1.getData().add(seriesNewPrincipal);
        graph2.getData().add(seriesForBalance);
        graph3.getData().add(seriesForInterest);
        tableData = FXCollections.observableArrayList();
        pieChartData = FXCollections.observableArrayList();

        summary_grid.setGridLinesVisible(true);

        primaryColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Year"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Interest"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Principal"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Balance"));

        paymentsAnnual.setItems(tableData);
        pieChart.setData(pieChartData);

        pieChart.setLabelLineLength(10);
        pieChart.setLegendSide(Side.RIGHT);


        repayment_type.getItems().addAll("Yearly", "Monthly", "Bi-Monthly", "Fortnightly", "Quarterly", "Weekly", "Daily");

        Calculate calculate = new Calculate();

        repayment_type.setOnAction(event -> {

            loan_amount_text = 0;
            months_text = 0;
            years_text = 0;
            interest_text = 0;
            years_text_month = 0;
            someNum = 2;

            String repayment_type_text = repayment_type.getValue().toString();
            String loan_amount_string = loan_amount.getText();
            String months_text_string = months.getText();
            String years_text_string = years.getText();
            String interest_text_string = interest.getText();

            if (loan_amount_string.isEmpty() || months_text_string.isEmpty() || years_text_string.isEmpty() || interest_text_string.isEmpty()) {
                logger.error("values were missing.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("There seems to be an error!");
                alert.setContentText("Please make sure there are no empty fields.");
                alert.showAndWait();
                return;
            }
            loan_amount_text = Double.parseDouble(loan_amount_string);
            months_text = Double.parseDouble(months_text_string);
            years_text = Double.parseDouble(years_text_string);
            interest_text = Double.parseDouble(interest_text_string);

            switch (repayment_type_text) {
                case "Monthly":
                    logger.info("Monthly payments selected");

                    pieChartData.clear();

                    primaryColumn.setText("Month");

                    paymentsAnnual.getItems().clear();

                    seriesForBalance.getData().clear(); // clearing balance seriesMonthlyInterest table
                    seriesForInterest.getData().clear(); // clearing interest seriesMonthlyInterest for table
                    seriesMonthlyInterest.getData().clear();
                    seriesNewPrincipal.getData().clear();
                    seriesMonthlyInterest.setName("Interest");
                    seriesNewPrincipal.setName("Principal");
                    seriesForBalance.setName("Balance");
                    seriesForInterest.setName("Annual interest");

                    years_text_month = years_text * 12; // converting years to months
                    // monthly payments output
                    double monthly_output = calculate.fixedRateMortgageMonthly(loan_amount_text, years_text_month + months_text, interest_text);
                    // total interest paid
                    BigDecimal bd = new BigDecimal((monthly_output * years_text_month) - loan_amount_text).setScale(2, RoundingMode.HALF_DOWN);

                    // summary
                    loan_amount_label.setText(loan_amount_string);
                    years_label.setText(years_text_string);
                    months_label.setText(months_text_string);
                    monthly_payments_label.setText(String.valueOf(monthly_output));
                    total_interest_label.setText(String.valueOf(bd.doubleValue()));
                    total_payments_label.setText(String.valueOf(bd.doubleValue() + loan_amount_text));

                    // pie chart for principal and Interest
                    pieChartData.add(new PieChart.Data("Principal", loan_amount_text));
                    pieChartData.add(new PieChart.Data("Interest", bd.doubleValue()));

                    PaymentsTable paymentsTablePrimary = new PaymentsTable();
                    double[][] monthly_chart = calculate.fixedRateMortgageMonthlyChart(loan_amount_text, interest_text, years_text_month + months_text);
                    paymentsTablePrimary.year.setValue(1);
                    paymentsTablePrimary.principal.setValue(monthly_chart[2][0]);
                    paymentsTablePrimary.interest.setValue(monthly_chart[1][0]);
                    seriesMonthlyInterest.getData().add(new XYChart.Data(0, monthly_chart[1][0]));
                    seriesNewPrincipal.getData().add(new XYChart.Data(0, monthly_chart[2][0]));
                    seriesForBalance.getData().add(new XYChart.Data(0, monthly_chart[3][0]));
                    seriesForInterest.getData().add(new XYChart.Data(0, interest_text));
                    paymentsTablePrimary.balance.setValue(monthly_chart[3][0]);

                    tableData.add(paymentsTablePrimary);

                    for (int i = 1; i < years_text_month + months_text; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        monthly_chart = calculate.fixedRateMortgageMonthlyChart(monthly_chart[3][0], interest_text, years_text_month + months_text - i);
                        paymentsTable.year.setValue(someNum++);
                        paymentsTable.principal.setValue(monthly_chart[2][0]);
                        paymentsTable.interest.setValue(monthly_chart[1][0]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, monthly_chart[1][0]));
                        seriesNewPrincipal.getData().add(new XYChart.Data(i, monthly_chart[2][0]));
                        seriesForBalance.getData().add(new XYChart.Data(i, monthly_chart[3][0]));
                        seriesForInterest.getData().add(new XYChart.Data(i, interest_text));
                        paymentsTable.balance.setValue(monthly_chart[3][0]);
                        tableData.addAll(paymentsTable);
                    }
                    break;

                case "Yearly":
                    logger.info("Yearly payments selected");

                    primaryColumn.setText("Year");

                    pieChartData.clear();

                    paymentsAnnual.getItems().clear();

                    seriesForBalance.getData().clear(); // clearing balance seriesMonthlyInterest table
                    seriesForInterest.getData().clear(); // clearing interest seriesMonthlyInterest for table
                    seriesMonthlyInterest.getData().clear();
                    seriesNewPrincipal.getData().clear();
                    seriesMonthlyInterest.setName("Interest");
                    seriesNewPrincipal.setName("Principal");
                    seriesForBalance.setName("Balance");
                    seriesForInterest.setName("Annual interest");

                    years_text_month = years_text * 12; // converting years to months
                    // monthly payments output
                    double monthlyOutputForYearly = calculate.fixedRateMortgageMonthly(loan_amount_text, years_text_month + months_text, interest_text);
                    // total interest paid
                    BigDecimal bd1 = new BigDecimal((monthlyOutputForYearly * years_text_month) - loan_amount_text).setScale(2, RoundingMode.HALF_DOWN);

                    // summary
                    loan_amount_label.setText(loan_amount_string);
                    years_label.setText(years_text_string);
                    months_label.setText(months_text_string);
                    monthly_payments_label.setText(String.valueOf(monthlyOutputForYearly));
                    total_interest_label.setText(String.valueOf(bd1.doubleValue()));
                    total_payments_label.setText(String.valueOf(bd1.doubleValue() + loan_amount_text));

                    // pie chart for principal and Interest
                    pieChartData.add(new PieChart.Data("Principal", loan_amount_text));
                    pieChartData.add(new PieChart.Data("Interest", bd1.doubleValue()));

                    double[][] monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(loan_amount_text, interest_text, years_text_month + months_text);

                    double[][] newYearly = new double[5][(int) years_text_month];
                    newYearly[0][0] = monthlyChartYearly[2][0];
                    newYearly[1][0] = monthlyChartYearly[1][0];
                    newYearly[2][0] = monthlyChartYearly[3][0];
                    for (int i = 1; i < years_text_month; i++) {
                        monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(monthlyChartYearly[3][0], interest_text, years_text_month - i);
                        newYearly[0][i] = monthlyChartYearly[2][0];
                        newYearly[1][i] = monthlyChartYearly[1][0];
                        newYearly[2][i] = monthlyChartYearly[3][0];
                    }

                    int someNumber = 1;

                    double yearlyPrincipal = 0.0;
                    double yearlyInterest = 0.0;
                    double yearlyBalance = 0.0;
                    System.out.println(newYearly[0].length);
                    for (int i = 0; i < newYearly[0].length; i++) {
                        PaymentsTable paymentsTableForYearly = new PaymentsTable();
                        if (i % 12 == 0 && i != 0) {
                            paymentsTableForYearly.year.setValue(someNumber++);
                            paymentsTableForYearly.principal.setValue(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.interest.setValue(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.balance.setValue(newYearly[2][i]);
                            tableData.addAll(paymentsTableForYearly);
                            seriesMonthlyInterest.getData().add(new XYChart.Data(i - 12, yearlyInterest));
                            seriesNewPrincipal.getData().add(new XYChart.Data(i-12, yearlyPrincipal));
                            seriesForBalance.getData().add(new XYChart.Data(i-12, newYearly[2][i]));
                            seriesForInterest.getData().add(new XYChart.Data(i-12, interest_text));
                            yearlyPrincipal = 0;
                            yearlyInterest = 0;
                            yearlyBalance = 0;
                        }
                        yearlyPrincipal += newYearly[0][i];
                        yearlyInterest += newYearly[1][i];
                        newYearly[2][i] -= yearlyBalance;
                        if (i == newYearly[0].length - 1 && i != 0) {
                            paymentsTableForYearly.year.setValue(someNumber);
                            paymentsTableForYearly.principal.setValue(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.interest.setValue(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.balance.setValue(newYearly[2][i]);
                            tableData.addAll(paymentsTableForYearly);
                            seriesMonthlyInterest.getData().add(new XYChart.Data(i - 12, yearlyInterest));
                            System.out.println(yearlyInterest);
                            seriesNewPrincipal.getData().add(new XYChart.Data(i-12, yearlyPrincipal));
                            seriesForBalance.getData().add(new XYChart.Data(i-12, newYearly[2][i]));
                            seriesForInterest.getData().add(new XYChart.Data(i-12, interest_text));
                        }

                    }
                    break;

                case "Quarterly":
                    logger.info("Quarterly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] quarterly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 4.0);

                    for (int i = 1; i < quarterly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(quarterly_output[0][i]);
                        paymentsTable.principal.setValue(quarterly_output[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, quarterly_output[1][i]));
                        tableData.add(paymentsTable);
                    }

                    break;

                case "Weekly":
                    logger.info("Weekly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] weekly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 52.0);

                    for (int i = 1; i < weekly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(weekly_output[0][i]);
                        paymentsTable.principal.setValue(weekly_output[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, weekly_output[1][i]));
                        tableData.add(paymentsTable);
                    }
                    break;

                case "Fortnightly":
                    logger.info("Fortnightly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] fortnightly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 26.0);

                    for (int i = 1; i < fortnightly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(fortnightly_output[0][i]);
                        paymentsTable.principal.setValue(fortnightly_output[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, fortnightly_output[1][i]));
                        tableData.add(paymentsTable);
                    }

                    break;

                case "Bi-Monthly":
                    logger.info("Bi-Monthly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] bimonthly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 6.0);

                    for (int i = 1; i < bimonthly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(bimonthly_output[0][i]);
                        paymentsTable.principal.setValue(bimonthly_output[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, bimonthly_output[1][i]));
                        tableData.add(paymentsTable);
                    }
                    break;

                case "Daily":
                    logger.info("Bi-Monthly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] daily_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 365.0);

                    for (int i = 1; i < daily_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(daily_output[0][i]);
                        paymentsTable.principal.setValue(daily_output[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, daily_output[1][i]));
                        tableData.add(paymentsTable);
                    }
                    break;
            }
        });

        jcalAbout.setOnAction(event -> {
            Stage stage = new Stage();
            Parent root = null;
            try {
                GaussianBlur gb = new GaussianBlur();
                gb.setRadius(5.5);
                jcalAnchor.setEffect(gb);
                root = FXMLLoader.load(getClass().getResource("/resource/JCal-about.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> jcalAnchor.setEffect(null));
        });

        jcalClose.setOnAction(event -> {
            logger.info("close button pressed from menu");
            Platform.exit();
        });

    }


}
