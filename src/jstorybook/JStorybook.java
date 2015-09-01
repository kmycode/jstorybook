/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jstorybook;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jstorybook.common.manager.LocaleManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.view.MainWindow;
import jstorybook.view.control.DockableAreaGroupPane;
import jstorybook.view.control.DockablePane;
import jstorybook.view.control.DockableTab;
import jstorybook.view.control.DockableTabPane;
import jstorybook.view.control.DockingBorder;
import storybook.SbApp;
import jstorybook.view.dialog.ExceptionDialog;

/**
 *
 * @author KMY
 */
public class JStorybook extends Application {

	@Override
	public void start (Stage primaryStage) {

		MainWindow mainWindow = new MainWindow(primaryStage);
		mainWindow.show();

		//ExceptionDialog.showAndWait(new NullPointerException());
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main (String[] args) {
		launch(args);
	}

}
