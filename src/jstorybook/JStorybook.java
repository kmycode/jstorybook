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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jstorybook.common.manager.LocaleManager;
import jstorybook.common.manager.ResourceManager;
import storybook.SbApp;
import jstorybook.view.dialog.ExceptionDialog;

/**
 *
 * @author KMY
 */
public class JStorybook extends Application {

	@Override
	public void start (Stage primaryStage) {
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle (ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();

		/*
		 		// 旧Storybookを起動
					String[] argsArray = new String[this.getParameters().getRaw().size()];
		int i = 0;
		for (String arg : this.getParameters().getRaw()) {
			argsArray[i ++] = arg;
		}
		SbApp.old_main(argsArray);
		 */
		ExceptionDialog ed = new ExceptionDialog(new NullPointerException());
		ed.showAndWait();

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main (String[] args) {
		launch(args);
	}

}
