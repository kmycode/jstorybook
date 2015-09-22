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
package jstorybook.viewtool.messenger.pane;

import jstorybook.model.entity.Group;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * 集団の編集エディタを表示するメッセージ
 *
 * @author KMY
 */
public class GroupEditorShowMessage extends EntityEditorShowMessage<Group> {

	private static GroupEditorShowMessage defaultInstance = new GroupEditorShowMessage(null, null);

	public GroupEditorShowMessage (EditorColumnList columns, EditorColumnList baseColumns) {
		super(columns, baseColumns);
	}

	@Override
	public GroupEditorShowMessage newMessage (EditorColumnList columns, EditorColumnList baseColumns) {
		if (this == defaultInstance) {
			return new GroupEditorShowMessage(columns, baseColumns);
		}
		throw new UnsupportedOperationException("This is not defaultInstance!");
	}

	public static GroupEditorShowMessage getInstance () {
		return defaultInstance;
	}

}
