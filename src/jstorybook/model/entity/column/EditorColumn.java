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
package jstorybook.model.entity.column;

/**
 * エンティティの編集項目をあらわす抽象クラス
 *
 * @author KMY
 * @param <T> カラムに格納できるデータの型
 */
public abstract class EditorColumn<T> {

	protected final String columnName;
	protected final String propertyName;
	private int columnWidth = 100;
	private boolean isDefaultShowFlag = false;

	public EditorColumn (String columnName, String propertyName) {
		this.columnName = columnName;
		this.propertyName = propertyName;
	}

	public int getColumnWidth () {
		return this.columnWidth;
	}

	public void setColumnWidth (int val) {
		this.columnWidth = val;
	}

	public boolean isDefaultShow () {
		return this.isDefaultShowFlag;
	}

	public void setDefaultShow (boolean val) {
		this.isDefaultShowFlag = val;
	}

	public String getColumnName () {
		return this.columnName;
	}

	public String getPropertyName () {
		return this.propertyName;
	}

}
