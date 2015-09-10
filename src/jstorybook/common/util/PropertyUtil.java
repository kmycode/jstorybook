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
package jstorybook.common.util;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;

/**
 * プロパティを扱う上でのユーティリティクラス
 *
  * @author KMY
 */
public class PropertyUtil {

	private PropertyUtil () {
	}

	// 指定されたすべてのプロパティに、共通のリスナーを設定する
	public static void addAllListener (ChangeListener<?> listener, Property<?>... args) {
		for (Property p : args) {
			p.addListener(listener);
		}
	}

	// 指定されたすべてのプロパティに、共通のリスナーを設定する
	public static void addAllListener (InvalidationListener listener, Property<?>... args) {
		for (Property p : args) {
			p.addListener(listener);
		}
	}

}
