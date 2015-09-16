package com.gollahalli.gui;

import com.gollahalli.api.Calculate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by akshayrajgollahalli on 15/09/15.
 */
public class Controller {

    @FXML
    private TextField loan_amount;
    @FXML
    private TextField years;
    @FXML
    private TextField weeks;
    @FXML
    private TextField interest;

    @FXML
    private ComboBox repayment_type;

    @FXML
    private TextFlow summary;

    @FXML
    private TableView<PaymentsTable> paymentsAnnual;
    @FXML
    private TableColumn yearColumn;
    @FXML
    private TableColumn interestColumn;
    @FXML
    private TableColumn principalColumn;
    @FXML
    private TableColumn balanceColumn;


    @FXML
    private StackedAreaChart test;

    ObservableList<PaymentsTable> data;

    private double loan_amount_text = 0;
    private double weeks_text = 0;
    private double years_text = 0;
    private double interest_text = 0;


    public static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public void initialize(){
        logger.info("controller started");

        data = FXCollections.observableArrayList(
//                new PaymentsTable("Jacob", "Smith", "jacob.smith@example.com", "Smith"),
//                new PaymentsTable("Isabella", "Johnson", "isabella.johnson@example.com", "Smith"),
//                new PaymentsTable("Ethan", "Williams", "ethan.williams@example.com", "Smith"),
//                new PaymentsTable("Emma", "Jones", "emma.jones@example.com", "Smith"),
//                new PaymentsTable("Michael", "Brown", "michael.brown@example.com", "Smith")

//                new PaymentsTable("Michael", "Brown", "michael.brown@example.com", "Smith")
        );

        yearColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Year"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Interest"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Principal"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<PaymentsTable, String>("Balance"));

        paymentsAnnual.setItems(data);

        repayment_type.getItems().addAll("Monthly", "Bi-Monthly", "Fortnightly", "Yearly", "Quarterly", "Weekly", "Daily");

        Calculate calculate = new Calculate();

        repayment_type.setOnAction(event -> {
            summary.getChildren().clear();
            String repayment_type_text = repayment_type.getValue().toString();
            String loan_amount_string = loan_amount.getText();
            String weeks_text_string = weeks.getText();
            String years_text_string = years.getText();
            String interest_text_string = interest.getText();

            if (loan_amount_string.isEmpty() || weeks_text_string.isEmpty() || years_text_string.isEmpty() || interest_text_string.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("There seems to be an error!");
                alert.setContentText("Please make sure there are no empty fields.");
                alert.showAndWait();
                return;
            }
            loan_amount_text = Double.parseDouble(loan_amount_string);
            weeks_text = Double.parseDouble(weeks_text_string);
            years_text = Double.parseDouble(years_text_string);
            interest_text = Double.parseDouble(interest_text_string);

            Text final_text = new Text();

            switch (repayment_type_text){
                case "Monthly":
                    logger.info("Monthly payments selected");
                    double[] monthly_output = calculate.compound_interest(loan_amount_text,interest_text,years_text,12.0);
                    final_text.setText("For the loan amount of " + loan_amount_text + ", at time interval of " + years_text +
                            " years and " + weeks_text + " weeks at an interest rate of " + interest_text + " % the interest you would have to pay is $" + monthly_output[1] +
                            ". The total amount you wold have to pay would be $" + monthly_output[0]);
                    summary.getChildren().add(final_text);
                    break;

                case "Yearly":
                    logger.info("Yearly payments selected");
                    double[] yearly_output = calculate.compound_interest(loan_amount_text,interest_text,years_text,1.0);
                    final_text.setText("For the loan amount of " + loan_amount_text + ", at time interval of " + years_text +
                            " years and " + weeks_text + " weeks at an interest rate of " + interest_text + " % the interest you would have to pay is $" + yearly_output[1] +
                            ". The total amount you wold have to pay would be $" + yearly_output[0]);
                    summary.getChildren().add(final_text);
                    break;

                case "Quarterly":
                    logger.info("Quarterly payments selected");
                    double[] quarterly_output = calculate.compound_interest(loan_amount_text,interest_text,years_text,4.0);
                    final_text.setText("For the loan amount of " + loan_amount_text + ", at time interval of " + years_text +
                            " years and " + weeks_text + " weeks at an interest rate of " + interest_text + " % the interest you would have to pay is $" + quarterly_output[1] +
                            ". The total amount you wold have to pay would be $" + quarterly_output[0]);
                    summary.getChildren().add(final_text);
                    break;

                case "Weekly":
                    logger.info("Weekly payments selected");
                    double[] weekly_output = calculate.compound_interest(loan_amount_text,interest_text,years_text,52.0);
                    Text weekly_text = new Text();
                    weekly_text.setText("For the loan amount of " + loan_amount_text + ", at time interval of " + years_text +
                            " years and " + weeks_text + " weeks at an interest rate of " + interest_text + " % the interest you would have to pay is $" + weekly_output[1] +
                            ". The total amount you wold have to pay would be $" + weekly_output[0]);
                    summary.getChildren().add(weekly_text);
                    break;

                case "Fortnightly":
                    logger.info("Fortnightly payments selected");
                    double[] fortnightly_output = calculate.compound_interest(loan_amount_text,interest_text,years_text,26.0);
                    final_text.setText("For the loan amount of " + loan_amount_text + ", at time interval of " + years_text +
                            " years and " + weeks_text + " at an interest rate of " + interest_text + " % the interest you would have to pay is $" + fortnightly_output[1] +
                            ". The total amount you wold have to pay would be $" + fortnightly_output[0]);
                    summary.getChildren().add(final_text);
                    break;

                case "Bi-Monthly":
                    logger.info("Bi-Monthly payments selected");
                    double[] bimonthly_output = calculate.compound_interest(loan_amount_text,interest_text,years_text,6.0);
                    final_text.setText("For the loan amount of " + loan_amount_text + ", at time interval of " + years_text +
                            " years and " + weeks_text + " at an interest rate of " + interest_text + " % the interest you would have to pay is $" + bimonthly_output[1] +
                            ". The total amount you wold have to pay would be $" + bimonthly_output[0]);
                    summary.getChildren().add(final_text);
                    break;

                case "Daily":
                    logger.info("Bi-Monthly payments selected");
                    double[] daily_output = calculate.compound_interest(loan_amount_text,interest_text,years_text,365.0);
                    final_text.setText("For the loan amount of " + loan_amount_text + ", at time interval of " + years_text +
                            " years and " + weeks_text + " at an interest rate of " + interest_text + " % the interest you would have to pay is $" + daily_output[1] +
                            ". The total amount you wold have to pay would be $" + daily_output[0]);
                    summary.getChildren().add(final_text);
                    break;
            }
        });
    }


}
