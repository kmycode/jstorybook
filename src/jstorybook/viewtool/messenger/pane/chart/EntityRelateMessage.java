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
package jstorybook.viewtool.messenger.pane.chart;

import jstorybook.viewtool.messenger.Message;

/**
 * 描画したエンティティを関連付けるメッセージ
 *
 * @author KMY
 */
public class EntityRelateMessage extends Message {

	private final int id1;
	private final int id2;

	public EntityRelateMessage (int id1, int id2) {
		this.id1 = id1;
		this.id2 = id2;
	}

	public int getId1 () {
		return this.id1;
	}

	public int getId2 () {
		return this.id2;
	}

}
