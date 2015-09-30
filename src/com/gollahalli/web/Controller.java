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

package com.gollahalli.web;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.PrinterResolution;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by akshayrajgollahalli on 29/09/15.
 */
public class Controller {
    @FXML
    private Button toolBarPrint;
    @FXML
    private Button toolBarSave;
    @FXML
    private WebView web;

    public void initialize() {

        toolBarPrint.setOnAction(event -> {
            final WebEngine webEngine = web.getEngine();
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            PrinterJob job = PrinterJob.createPrinterJob(printer);
            job.getJobSettings().setPrintQuality(PrintQuality.HIGH);
//            job.getJobSettings().setPrintResolution(new PrintResolution().getFeedResolution());
            job.getJobSettings().setPageLayout(pageLayout);
            final boolean print = job.showPrintDialog(null);
            if (print) {
                webEngine.print(job);
                job.endJob();
            }
        });
    }


}
