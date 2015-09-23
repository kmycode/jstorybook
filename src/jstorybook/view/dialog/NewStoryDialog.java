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

import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.MyStage;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.dialog.NewStoryDialogViewModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;
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

	public NewStoryDialog (Stage parent, Messenger messenger) {
		super(parent);

		this.parentMessenger = messenger;

		AnchorPane root = new AnchorPane();
		root.setPrefSize(500.0, 150.0);

		Label newLabel = new Label(ResourceManager.getMessage("msg.new.story.name.input"));
		GUIUtil.setAnchor(newLabel, 20.0, null, null, 20.0);

		GUIUtil.setAnchor(this.storyTitleText, null, 20.0, 60.0, 20.0);
		this.viewModelList.getProperty("storyName").bind(this.storyTitleText.textProperty());

		HBox commandBox = new HBox();
		Button okButton = GUIUtil.createCommandButton(this.viewModelList, "create");
		okButton.setText(ResourceManager.getMessage("msg.ok"));
		Button cancelButton = GUIUtil.createCommandButton(this.viewModelList, "cancel");
		cancelButton.setText(ResourceManager.getMessage("msg.cancel"));
		cancelButton.fontProperty().bind(FontManager.getInstance().fontProperty());
		commandBox.getChildren().addAll(okButton, cancelButton);
		GUIUtil.setAnchor(commandBox, null, 30.0, 20.0, null);

		root.getChildren().addAll(newLabel, this.storyTitleText, commandBox);

		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle(ResourceManager.getMessage("msg.new.story"));
		this.setResizable(false);
		this.initModality(Modality.APPLICATION_MODAL);

		scene.setOnKeyPressed((ev) -> {
			if (ev.getCode() == KeyCode.ESCAPE) {
				this.viewModelList.executeCommand("cancel");
			}
		});

		this.viewModelList.storeMessenger(this.messenger);
		this.messenger.apply(CloseMessage.class, this, (ev) -> this.close());
		this.messenger.relay(CurrentStoryModelGetMessage.class, this, this.parentMessenger);
	}

	public StringProperty storyNameProperty () {
		return this.storyTitleText.textProperty();
	}

}
