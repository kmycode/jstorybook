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

import java.util.ArrayList;
import java.util.List;
import jstorybook.model.entity.column.EditorColumn;

/**
 * カラムファクトリを扱う抽象クラス
 *
 * @author KMY
 */
public abstract class ColumnFactory {

	protected List<EditorColumn> editorColumnList = new ArrayList<EditorColumn>();
	private boolean editorColumnCreated = false;

	public List<EditorColumn> getColumnList () {
		if (!this.editorColumnCreated) {
			this.editorColumnList.clear();
			this.createColumnList();
			this.editorColumnCreated = true;
		}
		return this.editorColumnList;
	}

	protected abstract void createColumnList ();

}
