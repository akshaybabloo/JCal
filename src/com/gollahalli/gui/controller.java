package com.gollahalli.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Created by akshayrajgollahalli on 15/09/15.
 */
public class controller {

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

    public void initialize(){

        loan_amount.setOnAction(event -> {
            String test = loan_amount.getText();
            System.out.println(test);
        });
    }
}
