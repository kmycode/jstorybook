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

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.MyStage;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.dialog.PreferenceViewModel;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.CloseMessage;

/**
 * 環境設定ダイアログ
 *
 * @author KMY
 */
public class PreferenceDialog extends MyStage {

	ViewModelList viewModelList = new ViewModelList(new PreferenceViewModel());

	private final Messenger messenger = new Messenger();

	public PreferenceDialog (Stage parent) {
		super(parent);

		AnchorPane root = new AnchorPane();

		GridPane mainGrid = new GridPane();
		GUIUtil.setAnchor(mainGrid, 10.0, 10.0, null, 10.0);
		mainGrid.setVgap(16.0);
		mainGrid.setHgap(8.0);
		Label label;

		// フォント選択
		label = new Label(ResourceManager.getMessage("msg.preference.font"));
		GUIUtil.bindFontStyle(label);
		ComboBox<String> fontChoice = new ComboBox<>(FXCollections.observableArrayList(Font.getFamilies()));
		fontChoice.setEditable(true);
		GUIUtil.bindFontStyle(fontChoice);
		GridPane.setConstraints(label, 0, 0);
		GridPane.setConstraints(fontChoice, 1, 0);
		fontChoice.valueProperty().bindBidirectional(this.viewModelList.getProperty("fontFamily"));
		mainGrid.getChildren().addAll(label, fontChoice);

		// フォントサイズ
		label = new Label(ResourceManager.getMessage("msg.preference.font.size"));
		GUIUtil.bindFontStyle(label);
		TextField fontSize = new TextField();
		GUIUtil.bindFontStyle(fontSize);
		GridPane.setConstraints(label, 0, 1);
		GridPane.setConstraints(fontSize, 1, 1);
		fontSize.textProperty().bindBidirectional(this.viewModelList.getProperty("fontSize"));
		mainGrid.getChildren().addAll(label, fontSize);

		// メニューバー
		label = new Label("");
		GUIUtil.bindFontStyle(label);
		CheckBox isUseSystemMenu = new CheckBox(ResourceManager.getMessage("msg.preference.menubar.usesystem"));
		GUIUtil.bindFontStyle(isUseSystemMenu);
		GridPane.setConstraints(label, 0, 2);
		GridPane.setConstraints(isUseSystemMenu, 1, 2);
		isUseSystemMenu.selectedProperty().bindBidirectional(this.viewModelList.getProperty("isUseSystemMenu"));
		mainGrid.getChildren().addAll(label, isUseSystemMenu);

		// エンティティ検索の画面レイアウト
		label = new Label("");
		GUIUtil.bindFontStyle(label);
		CheckBox isSearchEntityOrientation = new CheckBox(ResourceManager.getMessage("msg.preference.search.entity.orientation"));
		GUIUtil.bindFontStyle(isSearchEntityOrientation);
		GridPane.setConstraints(label, 0, 3);
		GridPane.setConstraints(isSearchEntityOrientation, 1, 3);
		isSearchEntityOrientation.selectedProperty().bindBidirectional(this.viewModelList.getProperty("isSearchEntityOrientation"));
		mainGrid.getChildren().addAll(label, isSearchEntityOrientation);

		// 終了確認
		label = new Label("");
		GUIUtil.bindFontStyle(label);
		CheckBox isConfirmExit = new CheckBox(ResourceManager.getMessage("msg.preference.confirm.exit"));
		GUIUtil.bindFontStyle(isConfirmExit);
		GridPane.setConstraints(label, 0, 4);
		GridPane.setConstraints(isConfirmExit, 1, 4);
		isConfirmExit.selectedProperty().bindBidirectional(this.viewModelList.getProperty("isConfirmExit"));
		mainGrid.getChildren().addAll(label, isConfirmExit);

		// 起動時に最後のファイルを開く
		label = new Label("");
		GUIUtil.bindFontStyle(label);
		CheckBox isStartupOpenLastFile = new CheckBox(ResourceManager.getMessage("msg.preference.startup.open.last.story"));
		GUIUtil.bindFontStyle(isStartupOpenLastFile);
		GridPane.setConstraints(label, 0, 5);
		GridPane.setConstraints(isStartupOpenLastFile, 1, 5);
		isStartupOpenLastFile.selectedProperty().bindBidirectional(this.viewModelList.getProperty("isStartupOpenLastFile"));
		mainGrid.getChildren().addAll(label, isStartupOpenLastFile);

		HBox commandBox = new HBox();
		AnchorPane commandBoxSpacer = new AnchorPane();
		HBox.setHgrow(commandBoxSpacer, Priority.ALWAYS);
		Button okButton = GUIUtil.createCommandButton(this.viewModelList, "save");
		okButton.setText(ResourceManager.getMessage("msg.ok"));
		okButton.setGraphic(ResourceManager.getIconNode("ok.png"));
		okButton.setDefaultButton(true);
		okButton.setMinWidth(100);
		Button cancelButton = GUIUtil.createCommandButton(this.viewModelList, "cancel");
		cancelButton.setText(ResourceManager.getMessage("msg.cancel"));
		cancelButton.setGraphic(ResourceManager.getIconNode("cancel.png"));
		cancelButton.setCancelButton(true);
		cancelButton.setMinWidth(100);
		commandBox.getChildren().addAll(commandBoxSpacer, okButton, cancelButton);
		mainGrid.add(commandBox, 0, 6, 2, 1);

		root.getChildren().addAll(mainGrid);

		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle(ResourceManager.getMessage("msg.preference"));
		this.setResizable(false);
		this.initOwner(parent);
		this.initModality(Modality.WINDOW_MODAL);

		this.messenger.apply(CloseMessage.class, this, (ev) -> this.close());
		this.viewModelList.storeMessenger(this.messenger);
	}

}
