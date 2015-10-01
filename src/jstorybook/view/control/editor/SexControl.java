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
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

/**
 * 性別を選択するコントロール
 *
  * @author KMY
 */
public class SexControl extends FlowPane {

	private final LongProperty value = new SimpleLongProperty(0);
	private boolean inListener = false;
	private final List<ToggleButton> buttons = new ArrayList<>();
	private final ToggleGroup group = new ToggleGroup();

	public SexControl () {
		this.value.addListener((obj) -> {
			if (!SexControl.this.inListener) {
				SexControl.this.inListener = true;
				SexControl.this.selectValue();
				SexControl.this.inListener = false;
			}
		});
	}

	private void selectValue () {
		for (ToggleButton button : this.buttons) {
			if ((Long) button.getUserData() == this.value.get()) {
				button.setSelected(true);
			}
		}
	}

	public void addButton (long id, String name, Color color) {
		ToggleButton button = new ToggleButton(name);
		button.setUserData(id);
		button.setTextFill(color);
		button.setToggleGroup(this.group);
		this.getChildren().add(button);
		this.buttons.add(button);
		button.selectedProperty().addListener((obj) -> {
			if (!SexControl.this.inListener) {
				SexControl.this.inListener = true;
				SexControl.this.value.set(id);
				SexControl.this.inListener = false;
			}
		});

		this.selectValue();
	}

	public void clear () {
		this.buttons.clear();
	}

	public LongProperty valueProperty () {
		return this.value;
	}

}
