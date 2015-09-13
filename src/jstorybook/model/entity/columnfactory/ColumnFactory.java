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
package jstorybook.model.entity.columnfactory;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * カラムファクトリを扱う抽象クラス
 *
 * @author KMY
 */
public abstract class ColumnFactory<E> {

	protected ObjectProperty<EditorColumnList> editorColumnList = new SimpleObjectProperty<>(
			new EditorColumnList());
	private boolean editorColumnCreated = false;

	// columnListProperty を推奨
	@Deprecated
	public EditorColumnList getColumnList () {
		if (!this.editorColumnCreated) {
			this.editorColumnList.get().clear();
			this.createColumnList();
			this.editorColumnCreated = true;
		}
		return this.editorColumnList.get();
	}

	public ObjectProperty<EditorColumnList> columnListProperty () {
		this.getColumnList();
		return this.editorColumnList;
	}

	public abstract EditorColumnList createColumnList ();

	public abstract EditorColumnList createColumnList (E model);

}
