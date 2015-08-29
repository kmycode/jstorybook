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

import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * 地域・言語のマネージャ
 *
 * @author KMY
 */
public class LocaleManager {

	private static LocaleManager defaultInstance = new LocaleManager(Locale.getDefault());
	private ObjectProperty<Locale> locale;

	private LocaleManager () {
		this.locale = new SimpleObjectProperty<>();
	}

	private LocaleManager (Locale locale) {
		this();
		this.locale.set(locale);
	}

	public ObjectProperty<Locale> localeProperty () {
		return this.locale;
	}

	public static LocaleManager getInstance () {
		return LocaleManager.defaultInstance;
	}
}
