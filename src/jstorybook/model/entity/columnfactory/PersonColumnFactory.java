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

import jstorybook.common.manager.ResourceManager;
import jstorybook.model.entity.column.EditorColumn;
import jstorybook.model.entity.column.StringColumn;

/**
 * 登場人物リストのカラム
 *
 * @author KMY
 */
public class PersonColumnFactory extends ColumnFactory {

	private static PersonColumnFactory defaultInstance = new PersonColumnFactory();

	private PersonColumnFactory () {
	}

	public static PersonColumnFactory getInstance () {
		return PersonColumnFactory.defaultInstance;
	}

	@Override
	protected void createColumnList () {
		EditorColumn column;

		column = new StringColumn(ResourceManager.getMessage("msg.person.lastname"), "lastName");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		this.editorColumnList.add(column);

		column = new StringColumn(ResourceManager.getMessage("msg.person.firstname"), "firstName");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		this.editorColumnList.add(column);
	}

}
