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
package jstorybook.viewtool.messenger.operatemodel;

import jstorybook.viewtool.messenger.Message;

/**
 * 新規ストーリー作成メッセージ
 *
 * @author KMY
 */
public class NewStoryCreateMessage extends Message {

	private final String newStoryName;

	public NewStoryCreateMessage (String name) {
		this.newStoryName = name;
	}

	public String getNewStoryName () {
		return this.newStoryName;
	}

}
