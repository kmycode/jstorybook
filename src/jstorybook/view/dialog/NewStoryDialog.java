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

import java.io.File;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.MyStage;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.dialog.NewStoryDialogViewModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.SaveFileChooserMessage;
import jstorybook.viewtool.messenger.general.CloseMessage;

/**
 * 新しいストーリーを作成するダイアログ
 *
 * @author KMY
 */
public class NewStoryDialog extends MyStage {

	ViewModelList viewModelList = new ViewModelList(new NewStoryDialogViewModel());

	private final Messenger messenger = new Messenger();
	private final Messenger parentMessenger;

	TextField storyTitleText = new TextField();
	TextField fileNameText = new TextField();

	public NewStoryDialog (Stage parent, Messenger messenger) {
		super(parent);

		this.parentMessenger = messenger;

		AnchorPane root = new AnchorPane();

		VBox layoutVBox = new VBox();
		GUIUtil.setAnchor(layoutVBox, 10.0, 20.0, 10.0, 20.0);
		layoutVBox.setSpacing(10.0);
		layoutVBox.setMinWidth(500.0);
		root.getChildren().add(layoutVBox);

		Label newLabel = new Label(ResourceManager.getMessage("msg.new.story.name.input"));
		GUIUtil.setAnchor(newLabel, 20.0, null, null, 20.0);
		GUIUtil.bindFontStyle(newLabel);

		GUIUtil.setAnchor(this.storyTitleText, 50.0, 20.0, null, 20.0);
		this.viewModelList.getProperty("storyName").bind(this.storyTitleText.textProperty());
		GUIUtil.bindFontStyle(this.storyTitleText);

		Label selectLabel = new Label(ResourceManager.getMessage("msg.new.story.file.select"));
		GUIUtil.setAnchor(selectLabel, 100.0, 20.0, null, 20.0);
		GUIUtil.bindFontStyle(selectLabel);

		Button selectButton = GUIUtil.createCommandButton(this.viewModelList, "fileSelect");
		selectButton.setText(ResourceManager.getMessage("msg.select"));

		GUIUtil.setAnchor(this.fileNameText, 130.0, 120.0, null, 20.0);
		GUIUtil.bindFontStyle(this.fileNameText);
		this.fileNameText.setEditable(false);
		this.viewModelList.getProperty("fileName").bindBidirectional(this.fileNameText.textProperty());

		HBox fileSelectBox = new HBox(this.fileNameText, selectButton);
		GUIUtil.setAnchor(fileSelectBox, 128.0, 20.0, null, 20.0);
		HBox.setHgrow(this.fileNameText, Priority.ALWAYS);

		HBox commandBox = new HBox();
		AnchorPane commandBoxSpacer = new AnchorPane();
		HBox.setHgrow(commandBoxSpacer, Priority.ALWAYS);
		Button okButton = GUIUtil.createCommandButton(this.viewModelList, "create");
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
		GUIUtil.setAnchor(commandBox, null, 30.0, 20.0, null);

		layoutVBox.getChildren().addAll(newLabel, this.storyTitleText, selectLabel, fileSelectBox, commandBox);

		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle(ResourceManager.getMessage("msg.new.story"));
		this.setResizable(false);
		this.initOwner(parent);
		this.initModality(Modality.WINDOW_MODAL);

		this.viewModelList.storeMessenger(this.messenger);
		this.messenger.apply(CloseMessage.class, this, (ev) -> this.close());
		this.messenger.apply(SaveFileChooserMessage.class, this, (ev) -> this.saveFileChooser((SaveFileChooserMessage) ev));
		this.messenger.relay(CurrentStoryModelGetMessage.class, this, this.parentMessenger);
	}

	private void saveFileChooser (SaveFileChooserMessage mes) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jStorybook files (*.db)", "*.db"));
		File file = fileChooser.showSaveDialog(this);
		if (file != null) {
			mes.fileNameProperty().set(file.getPath());
		}
	}

	public StringProperty storyNameProperty () {
		return this.storyTitleText.textProperty();
	}

}
