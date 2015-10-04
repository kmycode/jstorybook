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
package jstorybook.common.contract;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;

/**
 * 設定のキー
 *
 * @author KMY
 */
public enum PreferenceKey {

	WINDOW_WIDTH(1366),
	WINDOW_HEIGHT(768),
	FONT_FAMILY("Meiryo UI"),
	FONT_SIZE(14.0),
	MENUBAR_USESYSTEM(false),
	SEARCH_ENTITY_ORIENTATION(Orientation.HORIZONTAL);

	private final Object defaultValue;
	private ObjectProperty value = new SimpleObjectProperty();

	private PreferenceKey (Object defaultValue) {
		this.defaultValue = defaultValue;
		this.value.set(defaultValue);
	}

	public Object getDefaultValue () {
		return this.defaultValue;
	}

	public Object getValue () {
		return this.value.get();
	}

	public Double getDouble () {
		return (Double) this.getValue();
	}

	public Boolean getBoolean () {
		return (Boolean) this.getValue();
	}

	public Integer getInteger () {
		return (Integer) this.getValue();
	}

	public String getString () {
		return (String) this.getValue();
	}

	public ReadOnlyObjectProperty getProperty () {
		return this.value;
	}

	public void setValue (Object val) {
		this.value.set(val);
	}
}
