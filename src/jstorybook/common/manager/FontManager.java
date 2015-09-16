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
package jstorybook.common.manager;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * フォントのマネージャ
 *
 * @author KMY
 */
public class FontManager {

	private static FontManager defaultInstance = new FontManager();

	private StringProperty fontName = new SimpleStringProperty();
	private DoubleProperty fontSize = new SimpleDoubleProperty();

	private ObjectProperty<Font> normalFont = new SimpleObjectProperty<>();
	private ObjectProperty<Font> boldFont = new SimpleObjectProperty<>();
	private ObjectProperty<Font> titleFont = new SimpleObjectProperty<>();

	private FontManager () {
		this.fontName.addListener((obj) -> {
			FontManager.this.reloadFont();
		});
		this.fontSize.addListener((obj) -> {
			FontManager.this.reloadFont();
		});

		this.fontName.set("Meiryo UI");
		this.fontSize.set(12.0);
	}

	public static FontManager getInstance () {
		return FontManager.defaultInstance;
	}

	private void reloadFont () {
		this.normalFont.set(Font.font(this.fontName.get(), this.fontSize.get()));
		this.boldFont.set(Font.font(this.fontName.get(), FontWeight.BOLD, this.fontSize.get()));
		this.titleFont.set(Font.font(this.fontName.get(), FontWeight.BOLD, 18));
	}

	public StringProperty fontNameProperty () {
		return this.fontName;
	}

	public DoubleProperty fontSizeProperty () {
		return this.fontSize;
	}

	public ReadOnlyObjectProperty<Font> fontProperty () {
		return this.normalFont;
	}

	public ReadOnlyObjectProperty<Font> boldFontProperty () {
		return this.boldFont;
	}

	public ReadOnlyObjectProperty<Font> titleFontProperty () {
		return this.titleFont;
	}

	public Font orderSizeFont (double size) {
		return Font.font(this.fontName.get(), size);
	}

	public Font orderSizeBoldFont (double size) {
		return Font.font(this.fontName.get(), FontWeight.BOLD, size);
	}

}
