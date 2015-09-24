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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jstorybook.common.contract.SystemKey;
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
		root.setPrefSize(500.0, 380.0);

		ImageView logoImageView = ResourceManager.getIconNode("splash.png");
		GUIUtil.bindFontStyle(logoImageView);
		Label informationLabel = new Label(SystemKey.SYSTEM_NAME.getValue() + " " + SystemKey.SYSTEM_VERSION.getValue());
		GUIUtil.bindFontStyle(informationLabel);
		Label licenceLabel = new Label("Designed by Martin(2008-2013), favdb(2014-), KMY(2015-)   GPL ver.2 later");
		GUIUtil.bindFontStyle(licenceLabel);
		VBox vBox = new VBox(logoImageView, informationLabel, licenceLabel);
		GUIUtil.setAnchor(vBox, 10.0, null, null, 15.0);

		Button closeButton = GUIUtil.createCommandButton(this.viewModelList, "close");
		closeButton.setText(ResourceManager.getMessage("msg.ok"));
		closeButton.setCancelButton(true);
		closeButton.setDefaultButton(true);
		closeButton.setPrefSize(140.0, 36.0);
		GUIUtil.setAnchor(closeButton, null, 20.0, 15.0, null);

		root.getChildren().addAll(vBox, closeButton);

		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle(ResourceManager.getMessage("msg.app.about"));
		this.setResizable(false);
		this.initModality(Modality.APPLICATION_MODAL);

		this.messenger.apply(CloseMessage.class, this, (ev) -> this.close());
		this.viewModelList.storeMessenger(this.messenger);
	}

}
