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

import java.util.ArrayList;
import java.util.List;
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

	CONFIRM_EXIT("confirm_exit", PreferenceType.BOOLEAN, true),
	WINDOW_WIDTH("window_width", PreferenceType.INTEGER, 1366),
	WINDOW_HEIGHT("window_height", PreferenceType.INTEGER, 768),
	FONT_FAMILY("font_family", PreferenceType.STRING, "Meiryo UI"),
	FONT_SIZE("font_size", PreferenceType.DOUBLE, 14.0),
	MENUBAR_USESYSTEM("menubar_usesystem", PreferenceType.BOOLEAN, false),
	SEARCH_ENTITY_ORIENTATION("search_entity_orientation", PreferenceType.ORIENTATION, Orientation.HORIZONTAL);

	public static List<PreferenceKey> getList () {
		List<PreferenceKey> list = new ArrayList<>();
		list.add(CONFIRM_EXIT);
		list.add(WINDOW_WIDTH);
		list.add(WINDOW_HEIGHT);
		list.add(FONT_FAMILY);
		list.add(FONT_SIZE);
		list.add(MENUBAR_USESYSTEM);
		list.add(SEARCH_ENTITY_ORIENTATION);
		return list;
	}

	public enum PreferenceType {
		INTEGER,
		STRING,
		DOUBLE,
		BOOLEAN,
		ORIENTATION,;
	}

	private final String name;
	private final Object defaultValue;
	private final PreferenceType type;
	private ObjectProperty value = new SimpleObjectProperty();

	private PreferenceKey (String name, PreferenceType type, Object defaultValue) {
		this.defaultValue = defaultValue;
		this.value.set(defaultValue);
		this.name = name;
		this.type = type;
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

	public String getName () {
		return this.name;
	}

	public PreferenceType getType () {
		return this.type;
	}

	public ReadOnlyObjectProperty getProperty () {
		return this.value;
	}

	public void setValue (Object val) {
		this.value.set(val);
	}
}
