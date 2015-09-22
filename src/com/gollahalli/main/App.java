/* JCal
 Copyright (C) 2015  Akshay Raj Gollahalli

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along
 with this program; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package com.gollahalli.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App extends Application {

    public static final Logger logger = LoggerFactory.getLogger(App.class);
    public static double JAVA_VERSION = getVersion();

    public static void main(String[] args) {
        launch(args);
    }

    static double getVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
    }

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
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/resource/JCal-gui.fxml"));
            logger.info("JCal-gui loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
//            logger.error("couldn't load JCal-gui. Ended with exception " + e);
        }

        Scene scene = new Scene(root, 800, 800);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JCal");
        primaryStage.show();


    }
}
