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

import jstorybook.common.contract.EntityRelation;
import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.ResourceManager;
import jstorybook.model.entity.Group;
import jstorybook.viewtool.model.EditorColumn;
import jstorybook.viewtool.model.EditorColumnList;
import jstorybook.viewtool.model.ListOnlyColumn;
import jstorybook.viewtool.model.StringColumn;

/**
 * 登場人物リストのカラム
 *
 * @author KMY
 */
public class GroupColumnFactory extends ColumnFactory<Group> {

	private static final GroupColumnFactory defaultInstance = new GroupColumnFactory();

	private GroupColumnFactory () {
	}

	public static GroupColumnFactory getInstance () {
		return GroupColumnFactory.defaultInstance;
	}

	@Override
	public EditorColumnList createColumnList (Group model) {
		if (model == null) {
			model = new Group();
		}

		EditorColumnList columnList = new EditorColumnList();
		columnList.titleProperty().bind(model.titleProperty());
		columnList.idProperty().bind(model.idProperty());
		columnList.noteProperty().bindBidirectional(model.noteProperty());
		columnList.entityTypeProperty().set(EntityType.GROUP);
		columnList.addRelation(EntityRelation.GROUP_PERSON);
		EditorColumn column;

		column = new ListOnlyColumn(ResourceManager.getMessage("msg.order"), "order");
		column.setColumnWidth(30);
		column.setDefaultShow(true);
		column.setProperty(model.orderProperty());
		columnList.add(column);

		column = new StringColumn(ResourceManager.getMessage("msg.group.name"), "name");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		column.setProperty(model.nameProperty());
		columnList.add(column);

		return columnList;
	}

}