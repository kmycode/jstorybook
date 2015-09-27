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

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jstorybook.common.contract.SystemKey;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.MyStage;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.dialog.AboutViewModel;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.CloseMessage;

/**
 * jStorybookの情報を表示するダイアログ
 *
 * @author KMY
 */
public class AboutDialog extends MyStage {

	ViewModelList viewModelList = new ViewModelList(new AboutViewModel());

	private final Messenger messenger = new Messenger();

	public AboutDialog (Stage parent) {
		super(parent);

		AnchorPane root = new AnchorPane();
		root.setMinWidth(500.0);

		ImageView logoImageView = ResourceManager.getIconNode("splash.png");
		Label informationLabel = new Label(SystemKey.SYSTEM_NAME.getValue() + " " + SystemKey.SYSTEM_VERSION.getValue());
		informationLabel.fontProperty().bind(FontManager.getInstance().boldFontProperty());
		Label licenceLabel = new Label(
				"Designed by\n		Martin(2008-2013) : Storybook,\n		favdb(2014-) : oStorybook,\n		KMY(2015-) : jStorybook\n	GPL ver.2 later");
		GUIUtil.bindFontStyle(licenceLabel);

		VBox vBox = new VBox(logoImageView, informationLabel, licenceLabel);
		vBox.setSpacing(10.0);
		GUIUtil.setAnchor(vBox, 10.0, null, null, 15.0);

		HBox commandBox = new HBox();
		AnchorPane commandBoxSpacer = new AnchorPane();
		HBox.setHgrow(commandBoxSpacer, Priority.ALWAYS);
		Button closeButton = GUIUtil.createCommandButton(this.viewModelList, "close");
		closeButton.setText(ResourceManager.getMessage("msg.ok"));
		closeButton.setGraphic(ResourceManager.getIconNode("ok.png"));
		closeButton.setCancelButton(true);
		closeButton.setDefaultButton(true);
		closeButton.setMinSize(140.0, 36.0);
		commandBox.getChildren().addAll(commandBoxSpacer, closeButton);
		vBox.getChildren().add(commandBox);

		root.getChildren().addAll(vBox);

		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle(ResourceManager.getMessage("msg.app.about"));
		this.initOwner(parent);
		this.setResizable(false);
		this.initModality(Modality.WINDOW_MODAL);

		this.messenger.apply(CloseMessage.class, this, (ev) -> this.close());
		this.viewModelList.storeMessenger(this.messenger);
	}

}
