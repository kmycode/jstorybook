/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jstorybook;

import javafx.application.Application;
import javafx.stage.Stage;
import jstorybook.view.MainWindow;

/**
 * jStorybookのエントリーポイント
 *
 * @author KMY
 */
public class JStorybook extends Application {

	@Override
	public void start (Stage primaryStage) {

		MainWindow mainWindow = new MainWindow(primaryStage);
		mainWindow.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main (String[] args) {
		launch(args);
	}

}
