/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storybook.toolkit.swing;

import java.awt.Font;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author KMY
 */
public class FontManager {

	private static FontManager defaultInstance = new FontManager("Arial", FontType.PLAIN, 11);
	private static FontManager boldInstance = new FontManager("Arial", FontType.BOLD, 11);
	private static FontManager italicInstance = new FontManager("Arial", FontType.ITALIC, 11);

	private ObjectProperty<Font> font = new SimpleObjectProperty<Font>();

	// 現在、フォント変更時のリスナーが動いている状態であるか
	private static boolean isListening = false;

	// フォントの種類
	public enum FontType {

		PLAIN(Font.PLAIN),
		BOLD(Font.BOLD),
		ITALIC(Font.ITALIC);

		private int type;

		private FontType (int type) {
			this.type = type;
		}

		public int getValue () {
			return this.type;
		}
	}

	private FontManager (String name, FontType type, int size) {
		this.font.set(new Font(name, type.getValue(), size));
	}

	// デフォルトのフォントを取得する
	public static FontManager getInstance () {
		return getInstance(FontType.PLAIN);
	}

	// お好みの種類のフォントを取得する
	public static FontManager getInstance (FontType type) {
		if (type == FontType.PLAIN) {
			return defaultInstance;
		}
		if (type == FontType.BOLD) {
			return boldInstance;
		}
		if (type == FontType.ITALIC) {
			return italicInstance;
		}
		return defaultInstance;
	}

	// フォントを変更する
	public void setFont (String name, int size) {
		defaultInstance.fontProperty().set(new Font(name, FontType.PLAIN.getValue(), size));
		boldInstance.fontProperty().set(new Font(name, FontType.BOLD.getValue(), size));
		italicInstance.fontProperty().set(new Font(name, FontType.ITALIC.getValue(), size));
	}

	public ObjectProperty<Font> fontProperty () {
		return this.font;
	}

}
