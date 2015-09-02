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
package jstorybook.viewtool.action;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

/**
 * アクションをあつかう抽象クラス
 *
 * @author KMY
 */
public abstract class Action {

	protected StringProperty name = new SimpleStringProperty();
	protected ObjectProperty<Image> image = new SimpleObjectProperty<>();
	protected Canvas canvas;

	public Action () {
		// ツールボタンのアイコンを設定する
		this.canvas = new Canvas();
		this.image.addListener((obj) -> {
			Action.this.canvas.getGraphicsContext2D().drawImage(this.image.get(), 0, 0);
			this.canvas.widthProperty().bind(this.image.get().widthProperty());
			this.canvas.heightProperty().bind(this.image.get().heightProperty());
		});
	}

	public ReadOnlyStringProperty nameProperty () {
		return this.name;
	}

	public ReadOnlyObjectProperty<Image> imageProperty () {
		return this.image;
	}

	// -------------------------------------------------------
	public abstract void onAction ();

	public MenuItem createMenuItem () {
		MenuItem item = new MenuItem();
		item.textProperty().bind(this.name);
		item.setOnAction((obj) -> this.onAction());
		return item;
	}

	public Button createToolBarButton () {
		Button button = new Button();
		button.setOnAction((obj) -> this.onAction());
		button.setGraphic(this.canvas);
		return button;
	}

	public Button createButton () {
		Button button = this.createToolBarButton();
		button.textProperty().bind(this.name);
		button.setGraphic(null);
		return button;
	}

	public Button createButtonWithIcon () {
		Button button = this.createToolBarButton();
		button.textProperty().bind(this.name);
		return button;
	}

}
