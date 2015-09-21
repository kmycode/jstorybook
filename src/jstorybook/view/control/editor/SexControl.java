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

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import jstorybook.common.manager.ResourceManager;

/**
 * 性別を選択するコントロール
 *
  * @author KMY
 */
public class SexControl extends HBox {

	private final LongProperty value = new SimpleLongProperty(0);
	private boolean inListener = false;

	public SexControl () {
		ToggleButton male = new ToggleButton(ResourceManager.getMessage("msg.person.sex.male"));
		male.setSelected(true);
		ToggleButton female = new ToggleButton(ResourceManager.getMessage("msg.person.sex.female"));
		ToggleGroup group = new ToggleGroup();
		male.setToggleGroup(group);
		female.setToggleGroup(group);

		this.getChildren().addAll(male, female);

		male.selectedProperty().addListener((obj) -> {
			if (!SexControl.this.inListener) {
				SexControl.this.inListener = true;
				SexControl.this.value.set(0);
				SexControl.this.inListener = false;
			}
		});
		female.selectedProperty().addListener((obj) -> {
			if (!SexControl.this.inListener) {
				SexControl.this.inListener = true;
				SexControl.this.value.set(1);
				SexControl.this.inListener = false;
			}
		});
		value.addListener((obj) -> {
			if (!SexControl.this.inListener) {
				SexControl.this.inListener = true;
				if (value.get() == 0L) {
					male.setSelected(true);
				}
				else {
					female.setSelected(true);
				}
				SexControl.this.inListener = false;
			}
		});
	}

	public LongProperty valueProperty () {
		return this.value;
	}

}
