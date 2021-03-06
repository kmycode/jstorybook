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
 * エンティティの種類
 *
 * @author KMY
 */
public enum EntityType {
	NONE(""),
	PERSON("person"),
	GROUP("group"),
	PLACE("place"),
	KEYWORD("keyword"),
	EVENT("event"),
	SCENE("scene"),
	CHAPTER("chapter"),
	PART("part"),
	STORYLINE("storyline"),
	SEX("sex"),
	ATTRIBUTE("attribute"),
	PERSON_ATTRIBUTE("person_attribute"),
	TAG("tag"),;

	private final String iconName;

	private EntityType (String iconName) {
		this.iconName = iconName;
	}

	public String getIconName () {
		return this.iconName;
	}

}
