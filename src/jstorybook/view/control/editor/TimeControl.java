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
package jstorybook.view.control.editor;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;

/**
 * 時刻を入力するコントロール
 *
  * @author KMY
 */
public class TimeControl extends HBox {

	private final IntegerProperty hour = new SimpleIntegerProperty();
	private final IntegerProperty minute = new SimpleIntegerProperty();
	private final IntegerProperty second = new SimpleIntegerProperty();
	private boolean inListener = false;
	private final List<ToggleButton> buttons = new ArrayList<>();
	private final ToggleGroup group = new ToggleGroup();

	public TimeControl () {
		this.hour.addListener((obj) -> {
			if (!TimeControl.this.inListener) {
				TimeControl.this.inListener = true;
				TimeControl.this.selectValue();
				TimeControl.this.inListener = false;
			}
		});
		this.minute.addListener((obj) -> {
			if (!TimeControl.this.inListener) {
				TimeControl.this.inListener = true;
				TimeControl.this.selectValue();
				TimeControl.this.inListener = false;
			}
		});
		this.second.addListener((obj) -> {
			if (!TimeControl.this.inListener) {
				TimeControl.this.inListener = true;
				TimeControl.this.selectValue();
				TimeControl.this.inListener = false;
			}
		});

		Spinner<Number> input;
		Label label;

		label = new Label(ResourceManager.getMessage("msg.time.hour"));
		label.fontProperty().bind(FontManager.getInstance().fontProperty());
		input = new Spinner<>(0, 23, 0);
		input.setEditable(true);
		this.hour.bindBidirectional(input.getValueFactory().valueProperty());
		this.setSpinnerListener(input);
		this.getChildren().addAll(input, label);

		label = new Label(ResourceManager.getMessage("msg.time.minute"));
		label.fontProperty().bind(FontManager.getInstance().fontProperty());
		input = new Spinner<>(0, 59, 0);
		input.setEditable(true);
		this.minute.bindBidirectional(input.getValueFactory().valueProperty());
		this.setSpinnerListener(input);
		this.getChildren().addAll(input, label);

		label = new Label(ResourceManager.getMessage("msg.time.second"));
		label.fontProperty().bind(FontManager.getInstance().fontProperty());
		input = new Spinner<>(0, 59, 0);
		input.setEditable(true);
		this.second.bindBidirectional(input.getValueFactory().valueProperty());
		this.setSpinnerListener(input);
		this.getChildren().addAll(input, label);
	}

	private void setSpinnerListener (Spinner node) {
		node.getEditor().textProperty().addListener((obj) -> {
			try {
				int value = Integer.parseInt(node.getEditor().getText());
				node.getEditor().setStyle("-fx-text-fill:black");
				node.getValueFactory().setValue(value);
			} catch (NumberFormatException e) {
				node.getEditor().setStyle("-fx-text-fill:red");
			}
		});
	}

	private void selectValue () {
		int hour = this.hour.get();
		int minute = this.minute.get();
		int second = this.second.get();
		if (hour < 0) {
			hour = 0;
		}
		else if (hour > 23) {
			hour = 23;
		}
		if (minute < 0) {
			minute = 0;
		}
		else if (minute > 59) {
			minute = 59;
		}
		if (second < 0) {
			second = 0;
		}
		else if (second > 59) {
			second = 59;
		}
		this.hour.set(hour);
		this.minute.set(minute);
		this.second.set(second);
	}

	public IntegerProperty hourProperty () {
		return this.hour;
	}

	public IntegerProperty minuteProperty () {
		return this.minute;
	}

	public IntegerProperty secondProperty () {
		return this.second;
	}

}
