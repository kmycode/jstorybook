/*
 * jStorybook: すべての小説家・作者のためのオープンソース・ソフトウェア
 * Copyright (C) 2008 - 2012 Martin Mustun
 *   (このソースの製作者) KMY
 * 
 * このプログラムはフリーソフトです。
 * あなたは、自由に修正し、再配布することが出来ます。
 * あなたの権利は、the Free Software Foundationによって発表されたGPL ver.2以降によって保護されています。
 * 
 * このプログラムは、小説・ストーリーの制作がよりよくなるようにという願いを込めて再配布されています。
 * あなたがこのプログラムを再配布するときは、GPLライセンスに同意しなければいけません。
 *  <http://www.gnu.org/licenses/>.
 */
package jstorybook.view.dialog;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.MyStage;

/**
 * 進捗状況をあらわすダイアログ
 *
 * @author KMY
 */
public class ProgressDialog extends MyStage {

	private ProgressBar bar = new ProgressBar();

	public ProgressDialog (Stage parent) {
		super(parent);

		AnchorPane root = new AnchorPane();
		root.setPrefSize(400.0, 50.0);

		this.bar.setProgress(0);
		GUIUtil.setAnchor(this.bar, 15.0, 20.0, 15.0, 20.0);
		this.bar.progressProperty().addListener((obj) -> {
			if (this.bar.getProgress() == 1) {
				Platform.runLater(() -> this.hide());
			}
		});
		root.getChildren().add(this.bar);

		Scene scene = new Scene(root);
		this.setTitle(ResourceManager.getMessage("msg.progress"));
		this.setScene(scene);
		this.initStyle(StageStyle.DECORATED);
		this.initModality(Modality.APPLICATION_MODAL);	// showAndWaitではだめ
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setOnCloseRequest((ev) -> {
			ev.consume();
		});
	}

	public DoubleProperty progressProperty () {
		return this.bar.progressProperty();
	}

}
