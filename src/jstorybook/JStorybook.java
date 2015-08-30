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
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle (ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		StackPane root = new StackPane();

		/*
		 		// 旧Storybookを起動
					String[] argsArray = new String[this.getParameters().getRaw().size()];
		int i = 0;
		for (String arg : this.getParameters().getRaw()) {
			argsArray[i ++] = arg;
		}
		SbApp.old_main(argsArray);
		 */
		DockablePane pane = new DockablePane(primaryStage);
		DockableAreaGroupPane group = pane.rootGroupProperty().get();

		DockableTabPane tabPane = group.add(0);
		tabPane.getTabs().add(new DockableTab("おはよう"));
		tabPane.getTabs().add(new DockableTab("こんにちは"));
		tabPane.getTabs().add(new DockableTab("こんばんは"));
		tabPane.getTabs().add(new DockableTab("やまだくん"));
		tabPane.getTabs().add(new DockableTab("かわむらさん"));
		tabPane.getTabs().add(new DockableTab("さいとうくん"));

		AnchorPane.setTopAnchor(pane, 10.0);
		AnchorPane.setLeftAnchor(pane, 10.0);
		AnchorPane.setRightAnchor(pane, 10.0);
		AnchorPane.setBottomAnchor(pane, 10.0);
		root.getChildren().add(pane);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();

		ExceptionDialog.showAndWait(new NullPointerException());
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main (String[] args) {
		launch(args);
	}

}
