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

import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.ResourceManager;
import jstorybook.model.entity.Person;
import jstorybook.viewtool.model.ColorColumn;
import jstorybook.viewtool.model.DateColumn;
import jstorybook.viewtool.model.EditorColumn;
import jstorybook.viewtool.model.EditorColumnList;
import jstorybook.viewtool.model.StringColumn;

/**
 * 登場人物リストのカラム
 *
 * @author KMY
 */
public class PersonColumnFactory extends ColumnFactory<Person> {

	private static PersonColumnFactory defaultInstance = new PersonColumnFactory();

	private PersonColumnFactory () {
	}

	public static PersonColumnFactory getInstance () {
		return PersonColumnFactory.defaultInstance;
	}

	@Override
	public EditorColumnList createColumnList () {
		return this.createColumnList(null);
	}

	@Override
	public EditorColumnList createColumnList (Person model) {
		if (model == null) {
			model = new Person();
		}

		EditorColumnList columnList = new EditorColumnList();
		columnList.titleProperty().bind(model.titleProperty());
		columnList.idProperty().bind(model.idProperty());
		columnList.noteProperty().bind(model.noteProperty());
		columnList.entityTypeProperty().set(EntityType.PERSON);
		EditorColumn column;

		column = new StringColumn(ResourceManager.getMessage("msg.person.lastname"), "lastName");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		column.setProperty(model.lastNameProperty());
		columnList.add(column);

		column = new StringColumn(ResourceManager.getMessage("msg.person.firstname"), "firstName");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		column.setProperty(model.firstNameProperty());
		columnList.add(column);

		column = new ColorColumn(ResourceManager.getMessage("msg.person.color"), "color");
		column.setColumnWidth(40);
		column.setDefaultShow(true);
		column.setProperty(model.colorProperty());
		column.setCellType(EditorColumn.CellType.COLOR);
		columnList.add(column);

		column = new DateColumn(ResourceManager.getMessage("msg.person.birthday"), "birthday");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		column.setProperty(model.birthdayProperty());
		column.setCellType(EditorColumn.CellType.DATE);
		columnList.add(column);

		column = new DateColumn(ResourceManager.getMessage("msg.person.dayofdeath"), "dayOfDeath");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		column.setProperty(model.dayOfDeathProperty());
		column.setCellType(EditorColumn.CellType.DATE);
		columnList.add(column);

		return columnList;
	}

}
