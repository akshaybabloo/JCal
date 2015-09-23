package com.gollahalli.gui;

import com.gollahalli.api.Calculate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Created by akshayrajgollahalli on 15/09/15.
 */
public class Controller {

    public static final Logger logger = LoggerFactory.getLogger(Controller.class);
    ObservableList<PaymentsTable> data;
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
    private double loan_amount_text = 0;
    private double months_text = 0;
    private double years_text = 0;
    private double interest_text = 0;
    private double years_text_month = 0;
    private int someNum = 2;

    public void initialize() {
        logger.info("controller started");
        XYChart.Series<Number, Number> series = new XYChart.Series();
        XYChart.Series<Number, Number> series1 = new XYChart.Series();
        graph1.getData().add(series);
        graph1.getData().add(series1);
        data = FXCollections.observableArrayList();

        summary_grid.setGridLinesVisible(true);

        primaryColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Year"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Interest"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Principal"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Balance"));

        paymentsAnnual.setItems(data);


        repayment_type.getItems().addAll("Monthly", "Bi-Monthly", "Fortnightly", "Yearly", "Quarterly", "Weekly", "Daily");

        Calculate calculate = new Calculate();

        repayment_type.setOnAction(event -> {
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
                    paymentsAnnual.getItems().clear();
                    series.getData().clear();
                    series1.getData().clear();
                    series.setName("Interest");
                    series1.setName("Principal");

                    years_text_month = years_text * 12;
                    double monthly_output = calculate.fixedRateMortgageMonthly(loan_amount_text, years_text_month, interest_text);
                    BigDecimal bd = new BigDecimal((monthly_output * years_text_month) - loan_amount_text).setScale(2, RoundingMode.HALF_DOWN);

                    loan_amount_label.setText(loan_amount_string);
                    years_label.setText(years_text_string);
                    months_label.setText(months_text_string);
                    monthly_payments_label.setText(String.valueOf(monthly_output));
                    total_interest_label.setText(String.valueOf(bd.doubleValue()));
                    total_payments_label.setText(String.valueOf(bd.doubleValue() + loan_amount_text));

                    PaymentsTable paymentsTablePrimary = new PaymentsTable();
                    double[][] monthly_chart = calculate.fixedRateMortgageMonthlyChart(loan_amount_text, interest_text, years_text_month);
                    paymentsTablePrimary.year.setValue(1);
                    paymentsTablePrimary.principal.setValue(monthly_chart[2][0]);
                    paymentsTablePrimary.interest.setValue(monthly_chart[1][0]);
                    series.getData().add(new XYChart.Data(0, monthly_chart[1][0]));
                    series1.getData().add(new XYChart.Data(0, monthly_chart[2][0]));
                    paymentsTablePrimary.balance.setValue(monthly_chart[3][0]);

                    data.add(paymentsTablePrimary);

                    for (int i = 1; i < years_text_month; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        monthly_chart = calculate.fixedRateMortgageMonthlyChart(monthly_chart[3][0], interest_text, years_text_month - i);
                        paymentsTable.year.setValue(someNum++);
                        paymentsTable.principal.setValue(monthly_chart[2][0]);
                        paymentsTable.interest.setValue(monthly_chart[1][0]);
                        series.getData().add(new XYChart.Data(i, monthly_chart[1][0]));
                        series1.getData().add(new XYChart.Data(i, monthly_chart[2][0]));
                        paymentsTable.balance.setValue(monthly_chart[3][0]);
                        data.addAll(paymentsTable);
                    }
                    break;

                case "Yearly":
                    logger.info("Yearly payments selected");
                    paymentsAnnual.getItems().clear();
                    series.getData().clear();
                    double[][] yearly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 1.0);

                    for (int i = 1; i < yearly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(yearly_output[0][i]);
                        paymentsTable.principal.setValue(yearly_output[1][i]);
                        series.getData().add(new XYChart.Data(i, yearly_output[1][i]));
                        data.add(paymentsTable);
                    }
                    break;

                case "Quarterly":
                    logger.info("Quarterly payments selected");
                    paymentsAnnual.getItems().clear();
                    series.getData().clear();
                    double[][] quarterly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 4.0);

                    for (int i = 1; i < quarterly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(quarterly_output[0][i]);
                        paymentsTable.principal.setValue(quarterly_output[1][i]);
                        series.getData().add(new XYChart.Data(i, quarterly_output[1][i]));
                        data.add(paymentsTable);
                    }

                    break;

                case "Weekly":
                    logger.info("Weekly payments selected");
                    paymentsAnnual.getItems().clear();
                    series.getData().clear();
                    double[][] weekly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 52.0);

                    for (int i = 1; i < weekly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(weekly_output[0][i]);
                        paymentsTable.principal.setValue(weekly_output[1][i]);
                        series.getData().add(new XYChart.Data(i, weekly_output[1][i]));
                        data.add(paymentsTable);
                    }
                    break;

                case "Fortnightly":
                    logger.info("Fortnightly payments selected");
                    paymentsAnnual.getItems().clear();
                    series.getData().clear();
                    double[][] fortnightly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 26.0);

                    for (int i = 1; i < fortnightly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(fortnightly_output[0][i]);
                        paymentsTable.principal.setValue(fortnightly_output[1][i]);
                        series.getData().add(new XYChart.Data(i, fortnightly_output[1][i]));
                        data.add(paymentsTable);
                    }

                    break;

                case "Bi-Monthly":
                    logger.info("Bi-Monthly payments selected");
                    paymentsAnnual.getItems().clear();
                    series.getData().clear();
                    double[][] bimonthly_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 6.0);

                    for (int i = 1; i < bimonthly_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(bimonthly_output[0][i]);
                        paymentsTable.principal.setValue(bimonthly_output[1][i]);
                        series.getData().add(new XYChart.Data(i, bimonthly_output[1][i]));
                        data.add(paymentsTable);
                    }
                    break;

                case "Daily":
                    logger.info("Bi-Monthly payments selected");
                    paymentsAnnual.getItems().clear();
                    series.getData().clear();
                    double[][] daily_output = calculate.compoundInterest(loan_amount_text, interest_text, years_text, 365.0);

                    for (int i = 1; i < daily_output[0].length; i++) {
                        PaymentsTable paymentsTable = new PaymentsTable();
                        paymentsTable.year.setValue(daily_output[0][i]);
                        paymentsTable.principal.setValue(daily_output[1][i]);
                        series.getData().add(new XYChart.Data(i, daily_output[1][i]));
                        data.add(paymentsTable);
                    }
                    break;
            }
        });

    }


}
