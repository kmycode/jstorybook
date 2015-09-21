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
package jstorybook.viewtool.model;

import javafx.beans.property.Property;

/**
 * エンティティの編集項目をあらわす抽象クラス
 *
 * @author KMY
 * @param <T> カラムに格納できるデータの型
 */
public abstract class EditorColumn<T> {

	protected final String columnName;
	protected final String propertyName;
	protected final ColumnType columnType;
	private Property property;
	private int columnWidth = 100;
	private boolean isDefaultShowFlag = false;
	private CellType cellType = CellType.NONE;

	// カラムの種類
	public enum ColumnType {
		NONE,
		TEXT,
		DATE,
		COLOR,
		SEX,;
	}

	// セル表示の種類
	public enum CellType {
		NONE,
		DATE,
		DATETIME,
		TIME,
		IMAGE,
		COLOR,
		SEX,;
	}

	public EditorColumn (String columnName, String propertyName) {
		this(columnName, propertyName, ColumnType.NONE);
	}

	protected EditorColumn (String columnName, String propertyName, ColumnType columnType) {
		this.columnName = columnName;
		this.propertyName = propertyName;
		this.columnType = columnType;
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

	public Property getProperty () {
		return this.property;
	}

	public void setProperty (Property p) {
		this.property = p;
	}

	public CellType getCellType () {
		return this.cellType;
	}

	public void setCellType (CellType cell) {
		this.cellType = cell;
	}

	// -------------------------------------------------------

	public String getColumnName () {
		return this.columnName;
	}

	public String getPropertyName () {
		return this.propertyName;
	}

	public ColumnType getColumnType () {
		return this.columnType;
	}

}
