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
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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

        summaryGrid.setGridLinesVisible(true);

        primaryColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Year"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Interest"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Principal"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Balance"));

        paymentsAnnual.setItems(tableData);
        pieChart.setData(pieChartData);

        pieChart.setLabelLineLength(10);
        pieChart.setLegendSide(Side.RIGHT);


        repaymentType.getItems().addAll("Yearly", "Monthly", "Bi-Monthly", "Fortnightly", "Quarterly", "Weekly", "Daily");

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
                    BigDecimal bd = new BigDecimal((monthlyOutput * yearsTextMonth) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);

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
                    BigDecimal bd1 = new BigDecimal((monthlyOutputForYearly * yearsTextMonth) - loanAmountText).setScale(2, RoundingMode.HALF_DOWN);

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
                        monthlyChartYearly = calculate.fixedRateMortgageMonthlyChart(monthlyChartYearly[3][0], interestText, yearsTextMonth - i);
                        newYearly[0][i] = monthlyChartYearly[2][0];
                        newYearly[1][i] = monthlyChartYearly[1][0];
                        newYearly[2][i] = monthlyChartYearly[3][0];
                    }

                    int someNumber = 1;

                    double yearlyPrincipal = 0.0;
                    double yearlyInterest = 0.0;
                    double yearlyBalance = 0.0;
                    for (int i = 0; i < newYearly[0].length; i++) {
                        PaymentsTable paymentsTableForYearly = new PaymentsTable();
                        if (i % 12 == 0 && i != 0) {
                            paymentsTableForYearly.year.setValue(someNumber++);
                            paymentsTableForYearly.principal.setValue(new BigDecimal(yearlyPrincipal).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.interest.setValue(new BigDecimal(yearlyInterest).setScale(2, RoundingMode.HALF_DOWN));
                            paymentsTableForYearly.balance.setValue(newYearly[2][i]);
                            tableData.addAll(paymentsTableForYearly);
                            seriesMonthlyInterest.getData().add(new XYChart.Data(i - 12, yearlyInterest));
                            seriesNewPrincipal.getData().add(new XYChart.Data(i - 12, yearlyPrincipal));
                            seriesForBalance.getData().add(new XYChart.Data(i - 12, newYearly[2][i]));
                            seriesForInterest.getData().add(new XYChart.Data(i - 12, interestText));
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
                            seriesNewPrincipal.getData().add(new XYChart.Data(i - 12, yearlyPrincipal));
                            seriesForBalance.getData().add(new XYChart.Data(i - 12, newYearly[2][i]));
                            seriesForInterest.getData().add(new XYChart.Data(i - 12, interestText));
                        }

                    }
                    break;

                case "Quarterly":
                    logger.info("Quarterly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] quarterlyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 4.0);

                    for (int i = 1; i < quarterlyOutput[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(quarterlyOutput[0][i]);
                        paymentsTable.principal.setValue(quarterlyOutput[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, quarterlyOutput[1][i]));
                        tableData.add(paymentsTable);
                    }

                    break;

                case "Weekly":
                    logger.info("Weekly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] weeklyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 52.0);

                    for (int i = 1; i < weeklyOutput[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(weeklyOutput[0][i]);
                        paymentsTable.principal.setValue(weeklyOutput[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, weeklyOutput[1][i]));
                        tableData.add(paymentsTable);
                    }
                    break;

                case "Fortnightly":
                    logger.info("Fortnightly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] fortnightlyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 26.0);

                    for (int i = 1; i < fortnightlyOutput[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(fortnightlyOutput[0][i]);
                        paymentsTable.principal.setValue(fortnightlyOutput[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, fortnightlyOutput[1][i]));
                        tableData.add(paymentsTable);
                    }

                    break;

                case "Bi-Monthly":
                    logger.info("Bi-Monthly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] bimonthlyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 6.0);

                    for (int i = 1; i < bimonthlyOutput[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(bimonthlyOutput[0][i]);
                        paymentsTable.principal.setValue(bimonthlyOutput[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, bimonthlyOutput[1][i]));
                        tableData.add(paymentsTable);
                    }
                    break;

                case "Daily":
                    logger.info("Bi-Monthly payments selected");
                    paymentsAnnual.getItems().clear();
                    seriesMonthlyInterest.getData().clear();
                    double[][] dailyOutput = calculate.compoundInterest(loanAmountText, interestText, yearsText, 365.0);

                    for (int i = 1; i < dailyOutput[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(dailyOutput[0][i]);
                        paymentsTable.principal.setValue(dailyOutput[1][i]);
                        seriesMonthlyInterest.getData().add(new XYChart.Data(i, dailyOutput[1][i]));
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

        jcalPrint.setOnAction(event -> {
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

            double yearsTextMonth = yearsText * 12;
            System.out.println(loanAmountText);
            System.out.println(monthsText);
            System.out.println(yearsText);
            System.out.println(interestText);

            WebViewer webViewer = new WebViewer(loanAmountText, interestText, yearsTextMonth + monthsText);
            String result = webViewer.webReturn();

            Stage stage = new Stage();
            Parent root = null;
            try {
                BoxBlur bb = new BoxBlur();
                GaussianBlur gb = new GaussianBlur();
                gb.setRadius(5.5);
                jcalAnchor.setEffect(gb);
                root = FXMLLoader.load(getClass().getResource("/resource/JCal_webview.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, 1024, 768);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();

            final WebView browser = new WebView();
            browser.setPrefSize(1024, 768);
            final WebEngine webEngine = browser.getEngine();

            ScrollPane scrollPane = (ScrollPane) scene.lookup("#scroll");
            scrollPane.setPannable(true);
            scrollPane.setContent(browser);

            webEngine.loadContent(result);
            stage.setOnCloseRequest(event1 -> jcalAnchor.setEffect(null));


        });

    }


}
