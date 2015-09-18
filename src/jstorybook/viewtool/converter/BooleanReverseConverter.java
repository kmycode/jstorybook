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
package jstorybook.viewtool.converter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 論理値を反転する
 *
 * @author KMY
 */
public class BooleanReverseConverter {

	private final BooleanProperty value = new SimpleBooleanProperty(false);
	private final BooleanProperty result = new SimpleBooleanProperty(true);

	public BooleanReverseConverter () {
		this.value.addListener((obj) -> {
			this.result.set(!this.value.get());
		});
	}

	public BooleanProperty valueProperty () {
		return this.value;
	}

	public ReadOnlyBooleanProperty resultProperty () {
		return this.result;
	}

}
