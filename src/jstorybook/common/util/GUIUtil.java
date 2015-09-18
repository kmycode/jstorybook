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
package jstorybook.common.util;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.converter.BooleanReverseConverter;

/**
 * GUIのユーティリティクラス
 *
  * @author KMY
 */
public class GUIUtil {

	private GUIUtil () {
	}

	public static final double UNSET = -3.14159265358979;

	public static Button createCommandButton (ViewModelList vmlist, String commandName) {
		Button button = new Button();

		BooleanReverseConverter converter = new BooleanReverseConverter();
		converter.valueProperty().bind(vmlist.canExecuteCommandProperty(commandName));
		button.disableProperty().bind(converter.resultProperty());

		button.setOnAction((ev) -> {
			vmlist.executeCommand(commandName);
		});

		return button;
	}

	public static void setAnchor (Node node, Double top, Double right, Double bottom, Double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setLeftAnchor(node, left);
		AnchorPane.setRightAnchor(node, right);
		AnchorPane.setBottomAnchor(node, bottom);
	}

	public static void setAnchor (Node node, double value) {
		GUIUtil.setAnchor(node, value, value, value, value);
	}

}
