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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jstorybook.model.entity.Entity;
import jstorybook.viewtool.messenger.Message;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * エンティティの編集画面を開くよう要求するメッセージ
 *
 * @author KMY
 */
public abstract class EntityEditorShowMessage<E extends Entity> extends Message {

	// クローン
	private final ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>(new EditorColumnList());

	// 基本となるエンティティ本体
	private final ObjectProperty<EditorColumnList> baseColumnList = new SimpleObjectProperty<>(new EditorColumnList());

	protected EntityEditorShowMessage (EditorColumnList columns, EditorColumnList baseColumns) {
		this.columnList.set(columns);
		this.baseColumnList.set(baseColumns);
	}

	public ReadOnlyObjectProperty<EditorColumnList> columnListProperty () {
		return this.columnList;
	}

	public ReadOnlyObjectProperty<EditorColumnList> baseColumnListProperty () {
		return this.baseColumnList;
	}

}
