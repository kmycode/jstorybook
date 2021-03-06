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
 * ストーリー設定名の一蘭
 *
 * @author KMY
 */
public enum StorySettingName {

	// 設定追加したら、StorySettingDAOも忘れずに変更してねっ☆

	FILE_VERSION("fileversion", true),
	ENTITY_COUNT("entitycount", true),
	STORY_NAME("storyname", false),;

	private final String key;
	private final boolean isInt;

	private StorySettingName (String key, boolean isInt) {
		this.key = key;
		this.isInt = isInt;
	}

	public String getKey () {
		return this.key;
	}

	public boolean isInt () {
		return this.isInt;
	}

	public boolean isString () {
		return !this.isInt;
	}

}
