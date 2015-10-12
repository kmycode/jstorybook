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

import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.StringProperty;
import jstorybook.viewtool.messenger.Message;

/**
 * 登場人物の属性画面で、集団１個分の情報を追加するメッセージ
 *
 * @author KMY
 */
public class PersonAttributeColumnGroupAddMessage extends Message {

	private final String groupName;
	private final List<Column> columnList = new LinkedList<>();

	public PersonAttributeColumnGroupAddMessage (String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName () {
		return this.groupName;
	}

	public void addColumn (String name, StringProperty value, long order) {
		// 挿入位置を探索
		int index = this.columnList.size();
		for (int i = 0; i < this.columnList.size(); i++) {
			if (this.columnList.get(i).order > order) {
				index = i;
				break;
			}
		}
		this.columnList.add(index, new Column(name, value, order));
	}

	public List<Column> getColumnList () {
		return this.columnList;
	}

	public static class Column {

		private final String name;
		private final StringProperty value;
		private final long order;

		private Column (String name, StringProperty value, long order) {
			this.name = name;
			this.value = value;
			this.order = order;
		}

		public String getName () {
			return this.name;
		}

		public StringProperty valueProperty () {
			return this.value;
		}
	}

}
