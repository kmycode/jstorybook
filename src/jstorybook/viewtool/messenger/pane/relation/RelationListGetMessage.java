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
package jstorybook.viewtool.messenger.pane.relation;

import java.util.List;
import jstorybook.viewtool.messenger.Message;

/**
 * 他のエンティティとの関連付けリストを取得するメッセージ
 *
 * @author KMY
 */
public abstract class RelationListGetMessage extends Message {

	private List<Long> relationList;

	public List<Long> getRelationList () {
		return this.relationList;
	}

	public void setRelationList (List<Long> list) {
		this.relationList = list;
	}

}
