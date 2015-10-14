package com.gollahalli.gui;

import com.gollahalli.api.General;
import com.gollahalli.properties.Company;
import com.gollahalli.properties.PropertiesReader;
import com.gollahalli.properties.PropertiesWriter;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Optional;

public class ControllerAbout {

    @FXML
    private Button checkForUpdates;
    @FXML
    private Button changeDetails;
    @FXML
    private Text registeredTo;
    @FXML
    private Text version_about;

    public void initialize() {

        checkForUpdates.setDisable(true);

        String[] propertySetter = propertyReader();

        registeredTo.setText("Registered to: " + propertySetter[0]);

        changeDetails.setOnAction(event -> {

            String[] property = propertyReader();

            // Custom dialog
            Dialog<Company> dialog = new Dialog<>();
            dialog.setTitle("Company details");
            dialog.setHeaderText("Personalize JCal to suit your company \n" +
                    "press Okay (or click title bar 'X' for cancel).");
            dialog.setResizable(true);

            // Widgets
            Label companyNameLabel = new Label("Company Name: ");
            Label nameLabel = new Label("Name: ");
            Label addressLabel = new Label("Address: ");
            Label contactNumberLabel = new Label("Contact Number: ");
            Label faxNumberLabel = new Label("Fax Number: ");
            TextField companyNameField = new TextField();
            companyNameField.setText(property[0]);
            TextField nameField = new TextField();
            nameField.setText(property[1]);
            TextField addressField = new TextField();
            addressField.setText(property[2]);
            TextField contactNumberField = new TextField();
            contactNumberField.setText(property[3]);
            TextField faxNumberField = new TextField();
            faxNumberField.setText(property[4]);

            // Create layout and add to dialog
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 35, 20, 35));
            grid.add(companyNameLabel, 1, 1); // col=1, row=1
            grid.add(companyNameField, 2, 1);
            grid.add(nameLabel, 1, 2); // col=1, row=2
            grid.add(nameField, 2, 2);
            grid.add(addressLabel, 1, 3); // col=1, row=2
            grid.add(addressField, 2, 3);
            grid.add(contactNumberLabel, 1, 4); // col=1, row=2
            grid.add(contactNumberField, 2, 4);
            grid.add(faxNumberLabel, 1, 5); // col=1, row=2
            grid.add(faxNumberField, 2, 5);
            dialog.getDialogPane().setContent(grid);

            // Add button to dialog
            ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

            // Result converter for dialog
            dialog.setResultConverter(b -> {

                if (b == buttonTypeOk) {

                    return new Company(companyNameField.getText(), nameField.getText(), addressField.getText(), contactNumberField.getText(), faxNumberField.getText());
                }
                return null;
            });

            // Show dialog
            Optional<Company> result = dialog.showAndWait();

            General general = new General();

            result.ifPresent(usernamePassword -> new PropertiesWriter(usernamePassword.getCompanyName(), usernamePassword.getName(), usernamePassword.getAddress(), usernamePassword.getContactNumber(), usernamePassword.getContactFax(), general.getVersion()));
        });

        General general = new General();
        version_about.setText("Version: " + general.getVersion());
    }

    static String[] propertyReader(){
        String[] result = new String[5];
        result[0] = "";
        result[1] = "";
        result[2] = "";
        result[3] = "";
        result[4] = "";

        if (new File("JCal.properties").exists()) {
            PropertiesReader propertiesReader = new PropertiesReader();
            result = propertiesReader.reader();
        }

        return result;
    }
}
