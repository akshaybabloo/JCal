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

package com.gollahalli.main;

import com.gollahalli.properties.Company;
import com.gollahalli.properties.PropertiesWriter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * This class is the main class which starts the software.
 *
 * @author Akshay Raj Gollahalli
 */
public class App extends Application {

    public static final Logger logger = LoggerFactory.getLogger(App.class);
    public static double JAVA_VERSION = getVersion();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This function returns a function number eg: 1.8
     *
     * @return double version number
     */
    static double getVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
    }

    /**
     * This function first check the Java version to be equal to 1.8 or more, if not an error is give.
     * If no error is found, FXML file is loaded.
     *
     * @param primaryStage Primary stage.
     */
    @Override
    public void start(Stage primaryStage) {
        if (JAVA_VERSION < 1.8) {
            logger.error("Java version error. version " + JAVA_VERSION + " installed");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("There seems to be an error!");
            alert.setContentText("You are using Java version " + JAVA_VERSION + " to run this application you " +
                    "you need to download the latest version of Java. please go to http://www.java.com");
            alert.showAndWait();
        }

        if (!new File("JCal.properties").exists()) {
            // Custom dialog
            Dialog<Company> dialog = new Dialog<>();
            dialog.setTitle("sup");
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
            TextField nameField = new TextField();
            TextField addressField = new TextField();
            TextField contactNumberField = new TextField();
            TextField faxNumberField = new TextField();

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

            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(buttonTypeOk);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            companyNameField.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            // Result converter for dialog
            dialog.setResultConverter(b -> {

                if (b == buttonTypeOk) {

                    return new Company(companyNameField.getText(), nameField.getText(), addressField.getText(), contactNumberField.getText(), faxNumberField.getText());
                }
                if(b == ButtonType.CANCEL){
                    Platform.exit();
                    primaryStage.close();
                    logger.info("Cancel button pressed. JCal has been closed");
                }
                Platform.exit();
                return null;
            });

            // Show dialog
            Optional<Company> result = dialog.showAndWait();

            result.ifPresent(usernamePassword -> new PropertiesWriter(usernamePassword.getCompanyName(), usernamePassword.getName(), usernamePassword.getAddress(), usernamePassword.getContactNumber(), usernamePassword.getContactFax(),"1.0.2"));
        }

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/resource/JCal-gui.fxml"));
            logger.info("JCal-gui loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("couldn't load JCal-gui. Ended with exception " + e);
        }

        Scene scene = new Scene(root, 800, 700);
        primaryStage.getIcons().add(new Image("/resource/JCal-logo.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JCal - Loan calculator");
        primaryStage.show();


    }
}
