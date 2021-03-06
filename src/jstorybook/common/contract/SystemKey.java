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

/**
 * システムの定数を設定
 *
 * @author KMY
 */
public enum SystemKey {

	SYSTEM_NAME("jStorybook"),
	SYSTEM_VERSION("6.0.0 alpha3"),
	FILE_VERSION(2),;

	private final Object value;


	private SystemKey (Object value) {
		this.value = value;
	}

	public Object getValue () {
		return this.value;
	}
}
