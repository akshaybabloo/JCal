package com.gollahalli.gui;

import com.gollahalli.api.Calculate;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

    public static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public void initialize(){
        logger.info("controller started");

        loan_amount.setOnAction(event -> {
            String test = loan_amount.getText();
            System.out.println(test);
        });

        repayment_type.getItems().addAll("Monthly", "Bi-Monthly", "Fortnightly", "Yearly", "Quarterly", "Weekly", "Daily");

        Calculate calculate = new Calculate();

        repayment_type.setOnAction(event -> {
            summary.getChildren().clear();
            String repayment_type_text = repayment_type.getValue().toString();
            double loan_amount_text = Double.parseDouble(loan_amount.getText());
            double weeks_text = Double.parseDouble(weeks.getText());
            double years_text = Double.parseDouble(years.getText());
            double interest_text = Double.parseDouble(interest.getText());
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
