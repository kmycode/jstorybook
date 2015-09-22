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
package jstorybook.model.entity;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ストーリー個別の設定モデル
 *
 * @author KMY
 */
public class StorySetting extends Entity {

	private final StringProperty key = new SimpleStringProperty();
	private final StringProperty value = new SimpleStringProperty("");
	private final LongProperty intValue = new SimpleLongProperty();

	public StringProperty keyProperty () {
		return this.key;
	}

	public StringProperty valueProperty () {
		return this.value;
	}

	public LongProperty intValueProperty () {
		return this.intValue;
	}

	@Override
	public String toString () {
		return this.key.get() + "=" + this.intValue.get() + "/" + this.value.get();
	}

	@Override
	public Entity entityClone () {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
