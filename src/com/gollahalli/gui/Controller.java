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
import com.gollahalli.web.WebViewer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


/**
 * This class is the main controller for JCal-gui.
 */
public class Controller {

    public static final Logger logger = LoggerFactory.getLogger(Controller.class);
    ObservableList<PaymentsTable> tableData;
    ObservableList<PieChart.Data> pieChartData;
    @FXML
    private TextField loanAmount;
    @FXML
    private TextField years;
    @FXML
    private TextField months;
    @FXML
    private TextField interest;
    @FXML
    private ComboBox repaymentType;
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
    private Label loanAmountLabel;
    @FXML
    private Label yearsLabel;
    @FXML
    private Label monthsLabel;
    @FXML
    private Label monthlyPaymentsLabel;
    @FXML
    private Label totalInterestLabel;
    @FXML
    private Label totalPaymentsLabel;
    @FXML
    private GridPane summaryGrid;
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
    private MenuItem jcalPrint;
    @FXML
    private AnchorPane jcalAnchor;
    private double loanAmountText = 0;
    private double monthsText = 0;
    private double yearsText = 0;
    private double interestText = 0;
    private double yearsTextMonth = 0;
    private double monthsToWeeks = 0;
    private int someNum = 2;

    public void initialize() {
        logger.info("controller started");

        if (!new File("JCal.properties").exists()) {
            Platform.exit();
            logger.error("JCal.properties not found");
        }
        XYChart.Series<Number, Number> seriesMonthlyInterest = new XYChart.Series();
        XYChart.Series<Number, Number> seriesNewPrincipal = new XYChart.Series();
        XYChart.Series<Number, Number> seriesForBalance = new XYChart.Series();
        XYChart.Series<Number, Number> seriesForInterest = new XYChart.Series();
        graph1.getData().add(seriesMonthlyInterest);
        graph1.getData().add(seriesNewPrincipal);
        graph2.getData().add(seriesForBalance);
        graph3.getData().add(seriesForInterest);
        graph1.setAnimated(false);
        graph2.setAnimated(false);
        graph3.setAnimated(false);
        tableData = FXCollections.observableArrayList();
        pieChartData = FXCollections.observableArrayList();

        summaryGrid.setGridLinesVisible(true);

        primaryColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Year"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Interest"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Principal"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Balance"));

        paymentsAnnual.setItems(tableData);
        pieChart.setData(pieChartData);

        pieChart.setLabelLineLength(10);
        pieChart.setLegendSide(Side.RIGHT);


        repaymentType.getItems().addAll("Yearly", "Monthly", "Fortnightly", "Weekly");//, "Bi-Monthly", "Quarterly", "Daily");

        Calculate calculate = new Calculate();

        repaymentType.setOnAction(event -> {

            loanAmountText = 0;
            monthsText = 0;
            yearsText = 0;
            interestText = 0;
            yearsTextMonth = 0;
            someNum = 2;

            String repaymentTypeText = repaymentType.getValue().toString();
            String loanAmountString = loanAmount.getText();
            String monthsTextString = months.getText();
            String yearsTextString = years.getText();
            String interestTextString = interest.getText();

            if (loanAmountString.isEmpty() || monthsTextString.isEmpty() || yearsTextString.isEmpty() || interestTextString.isEmpty()) {
                logger.error("values were missing.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("There seems to be an error!");
                alert.setContentText("Please make sure there are no empty fields.");
                alert.showAndWait();
                return;
            }
            loanAmountText = Double.parseDouble(loanAmountString);
            monthsText = Double.parseDouble(monthsTextString);
            yearsText = Double.parseDouble(yearsTextString);
            interestText = Double.parseDouble(interestTextString);

            switch (repaymentTypeText) {
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

                    yearsTextMonth = yearsText * 12; // converting years to months
                    // monthly payments output
                    double monthlyOutput = calculate.fixedRateMortgageMonthly(loanAmountText, yearsTextMonth + monthsText, interestText);
                    // total interest paid
                    BigDecimal bd = new BigDecimal((monthlyOutput * yearsTextMonth + monthsText) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);

                    // summary
                    loanAmountLabel.setText(loanAmountString);
                    yearsLabel.setText(yearsTextString);
                    monthsLabel.setText(monthsTextString);
                    monthlyPaymentsLabel.setText(String.valueOf(monthlyOutput));
                    totalInterestLabel.setText(String.valueOf(bd.doubleValue()));
                    totalPaymentsLabel.setText(String.valueOf(bd.doubleValue() + loanAmountText));

                    // pie chart for principal and Interest
                    pieChartData.add(new PieChart.Data("Principal", loanAmountText));
                    pieChartData.add(new PieChart.Data("Interest", bd.doubleValue()));

                    PaymentsTable paymentsTablePrimary = new PaymentsTable();
                    double[][] monthlyChart = calculate.fixedRateMortgageMonthlyChart(loanAmountText, interestText, yearsTextMonth + monthsText);
                    paymentsTablePrimary.year.setValue(1);
                    paymentsTablePrimary.principal.setValue(monthlyChart[2][0]);
                    paymentsTablePrimary.interest.setValue(monthlyChart[1][0]);
                    seriesMonthlyInterest.getData().add(new XYChart.Data(0, monthlyChart[1][0]));
                    seriesNewPrincipal.getData().add(new XYChart.Data(0, monthlyChart[2][0]));
                    seriesForBalance.getData().add(new XYChart.Data(0, monthlyChart[3][0]));
                    seriesForInterest.getData().add(new XYChart.Data(0, interestText));
                    paymentsTablePrimary.balance.setValue(monthlyChart[3][0]);

                    tableData.add(paymentsTablePrimary);

                    for (int i = 1; i < yearsTextMonth + monthsText; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        monthlyChart = calculate.fixedRateMortgageMonthlyChart(monthlyChart[3][0], interestText, yearsTextMonth + monthsText - i);
                        paymentsTable.year.setValue(someNum++);
                        paymentsTable.principal.setValue(monthlyChart[2][0]);
                        paymentsTable.interest.setValue(monthlyChart[1][0]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, monthlyChart[1][0]));
                        seriesNewPrincipal.getData().add(new XYChart.Data(i, monthlyChart[2][0]));
                        seriesForBalance.getData().add(new XYChart.Data(i, monthlyChart[3][0]));
                        seriesForInterest.getData().add(new XYChart.Data(i, interestText));
                        paymentsTable.balance.setValue(monthlyChart[3][0]);
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

                    yearsTextMonth = yearsText * 12; // converting years to months
                    // monthly payments output
                    double monthlyOutputForYearly = calculate.fixedRateMortgageMonthly(loanAmountText, yearsTextMonth + monthsText, interestText);
                    // total interest paid
                    BigDecimal bd1 = new BigDecimal((monthlyOutputForYearly * yearsTextMonth + monthsText) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);

                    // summary
                    loanAmountLabel.setText(loanAmountString);
                    yearsLabel.setText(yearsTextString);
                    monthsLabel.setText(monthsTextString);
                    monthlyPaymentsLabel.setText(String.valueOf(monthlyOutputForYearly));
                    totalInterestLabel.setText(String.valueOf(bd1.doubleValue()));
                    totalPaymentsLabel.setText(String.valueOf(bd1.doubleValue() + loanAmountText));

                    // pie chart for principal and Interest
                    pieChartData.add(new PieChart.Data("Principal", loanAmountText));
                    pieChartData.add(new PieChart.Data("Interest", bd1.doubleValue()));

                    double[][] monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(loanAmountText, interestText, yearsTextMonth + monthsText);

                    double[][] newYearly = new double[5][(int) yearsTextMonth];
                    newYearly[0][0] = monthlyChartYearly[2][0];
                    newYearly[1][0] = monthlyChartYearly[1][0];
                    newYearly[2][0] = monthlyChartYearly[3][0];
                    for (int i = 1; i < yearsTextMonth; i++) {
                        monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(monthlyChartYearly[3][0], interestText, yearsTextMonth + monthsText - i);
                        newYearly[0][i] = monthlyChartYearly[2][0];
                        newYearly[1][i] = monthlyChartYearly[1][0];
                        newYearly[2][i] = monthlyChartYearly[3][0];
                    }

                    int someNumber = 1;

                    double yearlyPrincipal = 0.0;
                    double yearlyInterest = 0.0;
                    double yearlyBalance = loanAmountText;
                    for (int i = 0; i < newYearly[0].length; i++) {
                        PaymentsTable paymentsTableForYearly = new PaymentsTable();
                        paymentsTableForYearly.balance.setValue(newYearly[2][i]);
                        if (i % 12 == 0 && i != 0) {
                            paymentsTableForYearly.year.setValue(someNumber++);
                            paymentsTableForYearly.principal.setValue(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.interest.setValue(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.balance.setValue(new BigDecimal(yearlyBalance).setScale(2, RoundingMode.HALF_DOWN));
                            tableData.addAll(paymentsTableForYearly);
                            seriesMonthlyInterest.getData().add(new XYChart.Data(i - 12, yearlyInterest));
                            seriesNewPrincipal.getData().add(new XYChart.Data(i - 12, yearlyPrincipal));
                            seriesForBalance.getData().add(new XYChart.Data(i - 12, yearlyBalance));
                            seriesForInterest.getData().add(new XYChart.Data(i - 12, interestText));
                            yearlyPrincipal = 0;
                            yearlyInterest = 0;
                        }
                        yearlyPrincipal += newYearly[0][i];
                        yearlyInterest += newYearly[1][i];
                        yearlyBalance -= newYearly[0][i];
                        if (i == newYearly[0].length - 1 && i != 0) {
                            paymentsTableForYearly.year.setValue(someNumber);
                            paymentsTableForYearly.principal.setValue(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.interest.setValue(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.balance.setValue(new BigDecimal(yearlyBalance).setScale(2, RoundingMode.HALF_DOWN));
                            tableData.addAll(paymentsTableForYearly);
                            seriesMonthlyInterest.getData().add(new XYChart.Data(i - 12, yearlyInterest));
                            seriesNewPrincipal.getData().add(new XYChart.Data(i - 12, yearlyPrincipal));
                            seriesForBalance.getData().add(new XYChart.Data(i - 12, yearlyBalance));
                            seriesForInterest.getData().add(new XYChart.Data(i - 12, interestText));
                        }

                    }
                    break;

//                case "Quarterly":
//                    logger.info("Quarterly payments selected");
//                    paymentsAnnual.getItems().clear();
//                    seriesMonthlyInterest.getData().clear();
//                    double[][] quarterlyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 4.0);
//
//                    for (int i = 1; i < quarterlyOutput[0].length; i++) {
//                        PaymentsTable paymentsTable = new PaymentsTable();
//                        paymentsTable.year.setValue(quarterlyOutput[0][i]);
//                        paymentsTable.principal.setValue(quarterlyOutput[1][i]);
//                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, quarterlyOutput[1][i]));
//                        tableData.add(paymentsTable);
//                    }
//
//                    break;

                case "Weekly":
                    logger.info("Weekly payments selected");

                    pieChartData.clear();

                    primaryColumn.setText("Week");

                    paymentsAnnual.getItems().clear();

                    seriesForBalance.getData().clear(); // clearing balance seriesMonthlyInterest table
                    seriesForInterest.getData().clear(); // clearing interest seriesMonthlyInterest for table
                    seriesMonthlyInterest.getData().clear();
                    seriesNewPrincipal.getData().clear();
                    seriesMonthlyInterest.setName("Interest");
                    seriesNewPrincipal.setName("Principal");
                    seriesForBalance.setName("Balance");
                    seriesForInterest.setName("Annual interest");

                    yearsTextMonth = yearsText * 52; // converting years to weeks
                    monthsToWeeks = monthsText * 4; // converting months to weeks

                    // monthly payments output
                    double weeklyOutput = calculate.fixedRateMortgageWeekly(loanAmountText, yearsTextMonth + monthsToWeeks, interestText);
                    // total interest paid
                    BigDecimal bdWeekly = new BigDecimal((weeklyOutput * yearsTextMonth + monthsToWeeks) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);

                    // summary
                    loanAmountLabel.setText(loanAmountString);
                    yearsLabel.setText(yearsTextString);
                    monthsLabel.setText(monthsTextString);
                    monthlyPaymentsLabel.setText(String.valueOf(weeklyOutput));
                    totalInterestLabel.setText(String.valueOf(bdWeekly.doubleValue()));
                    totalPaymentsLabel.setText(String.valueOf(bdWeekly.doubleValue() + loanAmountText));

                    // pie chart for principal and Interest
                    pieChartData.add(new PieChart.Data("Principal", loanAmountText));
                    pieChartData.add(new PieChart.Data("Interest", bdWeekly.doubleValue()));

                    PaymentsTable paymentsTablePrimary1 = new PaymentsTable();
                    double[][] weeklyChart = calculate.fixedRateMortgageWeeklyChart(loanAmountText, interestText, yearsTextMonth + monthsToWeeks);
                    paymentsTablePrimary1.year.setValue(1);
                    paymentsTablePrimary1.principal.setValue(weeklyChart[2][0]);
                    paymentsTablePrimary1.interest.setValue(weeklyChart[1][0]);
                    seriesMonthlyInterest.getData().add(new XYChart.Data(0, weeklyChart[1][0]));
                    seriesNewPrincipal.getData().add(new XYChart.Data(0, weeklyChart[2][0]));
                    seriesForBalance.getData().add(new XYChart.Data(0, weeklyChart[3][0]));
                    seriesForInterest.getData().add(new XYChart.Data(0, interestText));
                    paymentsTablePrimary1.balance.setValue(weeklyChart[3][0]);

                    tableData.add(paymentsTablePrimary1);

                    for (int i = 1; i < yearsTextMonth + monthsText; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        weeklyChart = calculate.fixedRateMortgageWeeklyChart(weeklyChart[3][0], interestText, yearsTextMonth + monthsToWeeks - i);
                        paymentsTable.year.setValue(someNum++);
                        paymentsTable.principal.setValue(weeklyChart[2][0]);
                        paymentsTable.interest.setValue(weeklyChart[1][0]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, weeklyChart[1][0]));
                        seriesNewPrincipal.getData().add(new XYChart.Data(i, weeklyChart[2][0]));
                        seriesForBalance.getData().add(new XYChart.Data(i, weeklyChart[3][0]));
                        seriesForInterest.getData().add(new XYChart.Data(i, interestText));
                        paymentsTable.balance.setValue(weeklyChart[3][0]);
                        tableData.addAll(paymentsTable);
                    }
                    break;

                case "Fortnightly":

                    logger.info("Fortnightly payments selected");

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

                    yearsTextMonth = yearsText * 52; // converting years to weeks
                    monthsToWeeks = monthsText * 4; // converting months to weeks

                    double yearsTextMonth1 = yearsText * 26;
                    double monthsToWeeks1 = monthsText * 2;
                    // monthly payments output
                    double weeklyOutputForFortnightly = calculate.fixedRateMortgageFortnightly(loanAmountText, yearsTextMonth1 + monthsToWeeks1, interestText);
                    // total interest paid
                    BigDecimal bd2 = new BigDecimal((weeklyOutputForFortnightly * yearsTextMonth1 + monthsToWeeks1) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);

                    // summary
                    loanAmountLabel.setText(loanAmountString);
                    yearsLabel.setText(yearsTextString);
                    monthsLabel.setText(monthsTextString);
                    monthlyPaymentsLabel.setText(String.valueOf(weeklyOutputForFortnightly));
                    totalInterestLabel.setText(String.valueOf(bd2.doubleValue()));
                    totalPaymentsLabel.setText(String.valueOf(bd2.doubleValue() + loanAmountText));

                    // pie chart for principal and Interest
                    pieChartData.add(new PieChart.Data("Principal", loanAmountText));
                    pieChartData.add(new PieChart.Data("Interest", bd2.doubleValue()));

                    double[][] weeklyChartFortnightly = calculate.fixedRateMortgageWeeklyChart(loanAmountText, interestText, yearsTextMonth + monthsToWeeks);

                    double[][] newFortnightly = new double[5][(int) yearsTextMonth];
                    newFortnightly[0][0] = weeklyChartFortnightly[2][0];
                    newFortnightly[1][0] = weeklyChartFortnightly[1][0];
                    newFortnightly[2][0] = weeklyChartFortnightly[3][0];
                    for (int i = 1; i < yearsTextMonth; i++) {
                        weeklyChartFortnightly = calculate.fixedRateMortgageWeeklyChart(weeklyChartFortnightly[3][0], interestText, yearsTextMonth + monthsToWeeks - i);
                        newFortnightly[0][i] = weeklyChartFortnightly[2][0];
                        newFortnightly[1][i] = weeklyChartFortnightly[1][0];
                        newFortnightly[2][i] = weeklyChartFortnightly[3][0];
                    }

                    int someNumber1 = 1;

                    double yearlyPrincipal1 = 0.0;
                    double yearlyInterest1 = 0.0;
                    double yearlyBalance1 = loanAmountText;
                    for (int i = 0; i < newFortnightly[0].length; i++) {
                        PaymentsTable paymentsTableForYearly = new PaymentsTable();
                        if (i % 2 == 0 && i != 0) {
                            paymentsTableForYearly.year.setValue(someNumber1++);
                            paymentsTableForYearly.principal.setValue(new BigDecimal(yearlyPrincipal1).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.interest.setValue(new BigDecimal(yearlyInterest1).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.balance.setValue(new BigDecimal(yearlyBalance1).setScale(2, RoundingMode.HALF_DOWN));
                            tableData.addAll(paymentsTableForYearly);
                            seriesMonthlyInterest.getData().add(new XYChart.Data(i - 2, yearlyInterest1));
                            seriesNewPrincipal.getData().add(new XYChart.Data(i - 2, yearlyPrincipal1));
                            seriesForBalance.getData().add(new XYChart.Data(i - 2, yearlyBalance1));
                            seriesForInterest.getData().add(new XYChart.Data(i - 2, interestText));
                            yearlyPrincipal1 = 0;
                            yearlyInterest1 = 0;
                        }
                        yearlyPrincipal1 += newFortnightly[0][i];
                        yearlyInterest1 += newFortnightly[1][i];
                        yearlyBalance1 -= newFortnightly[0][i];
                        if (i == newFortnightly[0].length - 1 && i != 0) {
                            paymentsTableForYearly.year.setValue(someNumber1);
                            paymentsTableForYearly.principal.setValue(new BigDecimal(yearlyPrincipal1).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.interest.setValue(new BigDecimal(yearlyInterest1).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.balance.setValue(new BigDecimal(yearlyBalance1).setScale(2, RoundingMode.HALF_DOWN));
                            tableData.addAll(paymentsTableForYearly);
                            seriesMonthlyInterest.getData().add(new XYChart.Data(i - 2, yearlyInterest1));
                            seriesNewPrincipal.getData().add(new XYChart.Data(i - 2, yearlyPrincipal1));
                            seriesForBalance.getData().add(new XYChart.Data(i - 2, yearlyBalance1));
                            seriesForInterest.getData().add(new XYChart.Data(i - 2, interestText));
                        }

                    }
                    break;

//                case "Bi-Monthly":
//                    logger.info("Bi-Monthly payments selected");
//                    paymentsAnnual.getItems().clear();
//                    seriesMonthlyInterest.getData().clear();
//                    double[][] bimonthlyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 6.0);
//
//                    for (int i = 1; i < bimonthlyOutput[0].length; i++) {
//                        PaymentsTable paymentsTable = new PaymentsTable();
//                        paymentsTable.year.setValue(bimonthlyOutput[0][i]);
//                        paymentsTable.principal.setValue(bimonthlyOutput[1][i]);
//                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, bimonthlyOutput[1][i]));
//                        tableData.add(paymentsTable);
//                    }
//                    break;
//
//                case "Daily":
//                    logger.info("Bi-Monthly payments selected");
//                    paymentsAnnual.getItems().clear();
//                    seriesMonthlyInterest.getData().clear();
//                    double[][] dailyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 365.0);
//
//                    for (int i = 1; i < dailyOutput[0].length; i++) {
//                        PaymentsTable paymentsTable = new PaymentsTable();
//                        paymentsTable.year.setValue(dailyOutput[0][i]);
//                        paymentsTable.principal.setValue(dailyOutput[1][i]);
//                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, dailyOutput[1][i]));
//                        tableData.add(paymentsTable);
//                    }
//                    break;
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
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> jcalAnchor.setEffect(null));
        });

        jcalClose.setOnAction(event -> {
            logger.info("close button pressed from menu");
            Platform.exit();
        });

        jcalPrint.setOnAction(event -> {
            String switcher = repaymentType.getValue().toString();
            loanAmountText = 0;
            monthsText = 0;
            yearsText = 0;
            interestText = 0;
            yearsTextMonth = 0;

            String loanAmountString = loanAmount.getText();
            String monthsTextString = months.getText();
            String yearsTextString = years.getText();
            String interestTextString = interest.getText();

            if (loanAmountString.isEmpty() || monthsTextString.isEmpty() || yearsTextString.isEmpty() || interestTextString.isEmpty()) {
                logger.error("values were missing.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("There seems to be an error!");
                alert.setContentText("Please make sure there are no empty fields.");
                alert.showAndWait();
                return;
            }

            loanAmountText = Double.parseDouble(loanAmountString);
            monthsText = Double.parseDouble(monthsTextString);
            yearsText = Double.parseDouble(yearsTextString);
            interestText = Double.parseDouble(interestTextString);
            yearsTextMonth = yearsText * 12;

            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Customer details");
            dialog.setHeaderText("Please enter your customer details");

            // Set the button types.
            ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField custName = new TextField();
            custName.setPromptText("Full name");
            TextField custAddress = new TextField();
            custAddress.setPromptText("Address");

            grid.add(new Label("Full name:"), 0, 0);
            grid.add(custName, 1, 0);
            grid.add(new Label("Address:"), 0, 1);
            grid.add(custAddress, 1, 1);

            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(submitButtonType);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            custName.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> custName.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    return new Pair<>(custName.getText(), custAddress.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result1 = dialog.showAndWait();

            final String[] custNameString = {""};
            final String[] custAddressString = {""};

            result1.ifPresent(usernamePassword -> {
                custNameString[0] = usernamePassword.getKey();
                custAddressString[0] = usernamePassword.getValue();
            });

            String result = "";

            switch (switcher) {
                case "Yearly":
                    double monthlyOutput = calculate.fixedRateMortgageMonthly(loanAmountText, yearsTextMonth + monthsText, interestText);
                    // total interest paid
                    BigDecimal bd = new BigDecimal((monthlyOutput * yearsTextMonth) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);
                    WebViewer webViewer = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsText, loanAmountString, yearsTextString, monthsTextString, String.valueOf(monthlyOutput), String.valueOf(bd.doubleValue()), String.valueOf(bd.doubleValue() + loanAmountText), custNameString[0], custAddressString[0]);
                    logger.info("Yearly web view selected");
                    result = webViewer.webReturnYearly();
                    Task task1 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph1.snapshot(new SnapshotParameters(), null);
                                            File file = new File("InterestAndPrincipal.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th1 = new Thread(task1);
                    th1.start();
                    // -------------------------
                    Task task2 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph2.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Balance.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th2 = new Thread(task2);
                    th2.start();
                    // -------------------------
                    Task task3 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph3.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Interest.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th3 = new Thread(task3);
                    th3.start();
                    // -------------------------
                    Task task4 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = pieChart.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Pie.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th4 = new Thread(task4);
                    th4.start();
                    break;
                case "Monthly":
                    logger.info("Monthly web view selected");
                    double monthlyOutput2 = calculate.fixedRateMortgageMonthly(loanAmountText, yearsTextMonth + monthsText, interestText);
                    // total interest paid
                    BigDecimal bd2 = new BigDecimal((monthlyOutput2 * yearsTextMonth) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);
                    WebViewer webViewer2 = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsText, loanAmountString, yearsTextString, monthsTextString, String.valueOf(monthlyOutput2), String.valueOf(bd2.doubleValue()), String.valueOf(bd2.doubleValue() + loanAmountText), custNameString[0], custAddressString[0]);
                    result = webViewer2.webReturnMonthly();

                    Task task5 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph1.snapshot(new SnapshotParameters(), null);
                                            File file = new File("InterestAndPrincipal.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th5 = new Thread(task5);
                    th5.start();
                    // -------------------------
                    Task task6 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph2.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Balance.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th6 = new Thread(task6);
                    th6.start();
                    // -------------------------
                    Task task7 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph3.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Interest.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th7 = new Thread(task7);
                    th7.start();
                    // -------------------------
                    Task task8 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = pieChart.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Pie.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th8 = new Thread(task8);
                    th8.start();
                    break;
                case "Weekly":
                    logger.info("Weekly web view selected");
                    yearsTextMonth = yearsText * 52;
                    monthsToWeeks = monthsText * 4;
                    double monthlyOutput3 = calculate.fixedRateMortgageWeekly(loanAmountText, yearsTextMonth + monthsToWeeks, interestText);
                    // total interest paid
                    BigDecimal bd3 = new BigDecimal((monthlyOutput3 * yearsTextMonth) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);
                    WebViewer webViewer3 = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsToWeeks, loanAmountString, yearsTextString, monthsTextString, String.valueOf(monthlyOutput3), String.valueOf(bd3.doubleValue()), String.valueOf(bd3.doubleValue() + loanAmountText), custNameString[0], custAddressString[0]);

                    result = webViewer3.webReturnWeekly();

                    Task task9 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph1.snapshot(new SnapshotParameters(), null);
                                            File file = new File("InterestAndPrincipal.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th9 = new Thread(task9);
                    th9.start();
                    // -------------------------
                    Task task10 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph2.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Balance.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th10 = new Thread(task10);
                    th10.start();
                    // -------------------------
                    Task task11 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph3.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Interest.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th11 = new Thread(task11);
                    th11.start();
                    // -------------------------
                    Task task12 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = pieChart.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Pie.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th12 = new Thread(task12);
                    th12.start();
                    break;

                case "Fortnightly":
                    logger.info("Fortnightly web view selected");
                    yearsTextMonth = yearsText * 52;
                    monthsToWeeks = monthsText * 4;
                    double fortnightlyOutput4 = calculate.fixedRateMortgageWeekly(loanAmountText, yearsTextMonth + monthsToWeeks, interestText);
                    // total interest paid
                    BigDecimal bd4 = new BigDecimal((fortnightlyOutput4 * yearsTextMonth) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);
                    WebViewer webViewer4 = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsToWeeks, loanAmountString, yearsTextString, monthsTextString, String.valueOf(fortnightlyOutput4), String.valueOf(bd4.doubleValue()), String.valueOf(bd4.doubleValue() + loanAmountText), custNameString[0], custAddressString[0]);

                    result = webViewer4.webReturnFortnightly();

                    Task task13 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph1.snapshot(new SnapshotParameters(), null);
                                            File file = new File("InterestAndPrincipal.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th13 = new Thread(task13);
                    th13.start();
                    // -------------------------
                    Task task14 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph2.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Balance.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th14 = new Thread(task14);
                    th14.start();
                    // -------------------------
                    Task task15 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = graph3.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Interest.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th15 = new Thread(task15);
                    th15.start();
                    // -------------------------
                    Task task16 = new Task<Void>() {
                        @Override
                        public Void call() {
                            Platform.runLater(
                                    () -> {
                                        try {
                                            WritableImage wim = pieChart.snapshot(new SnapshotParameters(), null);
                                            File file = new File("Pie.png");

                                            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                                        } catch (Exception s) {
                                            s.printStackTrace();
                                        }
                                        System.out.println("finished");

                                    });

                            return null;
                        }
                    };
                    Thread th16 = new Thread(task16);
                    th16.start();
                    break;
            }

            Stage stage = new Stage();
            Parent root = null;
            try {
                GaussianBlur gb = new GaussianBlur();
                gb.setRadius(5.5);
                jcalAnchor.setEffect(gb);
                root = FXMLLoader.load(getClass().getResource("/resource/JCal_webview.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, 1024, 768);
            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            WebView browser = (WebView) scene.lookup("#web");
            browser.setPrefSize(800, 768);
            final WebEngine webEngine = browser.getEngine();
            File file = new File("temp.html");
            webEngine.load(file.toURI().toString());
            stage.setOnCloseRequest(event1 -> jcalAnchor.setEffect(null));
        });

    }
}
