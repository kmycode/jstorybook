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
import jstorybook.model.column.DateTimeColumn;
import jstorybook.model.column.EditorColumn;
import jstorybook.model.column.EditorColumnList;
import jstorybook.model.column.ListOnlyColumn;
import jstorybook.model.column.StringColumn;
import jstorybook.model.entity.Event;

/**
 * イベントリストのカラム
 *
 * @author KMY
 */
public class EventColumnFactory extends ColumnFactory<Event> {

	private static final EventColumnFactory defaultInstance = new EventColumnFactory();

	private EventColumnFactory () {
	}

	public static EventColumnFactory getInstance () {
		return EventColumnFactory.defaultInstance;
	}

	@Override
	public EditorColumnList createColumnList (Event model) {
		if (model == null) {
			model = new Event();
		}

		EditorColumnList columnList = new EditorColumnList();
		columnList.titleProperty().bind(model.titleProperty());
		columnList.idProperty().bindBidirectional(model.idProperty());
		columnList.noteProperty().bindBidirectional(model.noteProperty());
		columnList.addRelation(EntityRelation.PERSON_EVENT);
		columnList.addRelation(EntityRelation.GROUP_EVENT);
		columnList.addRelation(EntityRelation.PLACE_EVENT);
		columnList.addRelation(EntityRelation.SCENE_EVENT);
		columnList.addRelation(EntityRelation.EVENT_KEYWORD);
		columnList.addRelation(EntityRelation.EVENT_TAG);
		columnList.entityTypeProperty().set(EntityType.EVENT);
		EditorColumn column;

		column = new ListOnlyColumn(ResourceManager.getMessage("msg.order"), "order");
		column.setColumnWidth(40);
		column.setDefaultShow(true);
		column.setProperty(model.orderProperty());
		columnList.add(column);

		column = new StringColumn(ResourceManager.getMessage("msg.event.name"), "name");
		column.setColumnWidth(120);
		column.setDefaultShow(true);
		column.setProperty(model.nameProperty());
		columnList.add(column);

		column = new DateTimeColumn(ResourceManager.getMessage("msg.event.starttime"), "starttime");
		column.setColumnWidth(160);
		column.setDefaultShow(true);
		column.setProperty(model.starttimeProperty());
		column.setCellType(EditorColumn.CellType.DATETIME);
		columnList.add(column);

		column = new DateTimeColumn(ResourceManager.getMessage("msg.event.endtime"), "endtime");
		column.setColumnWidth(160);
		column.setDefaultShow(true);
		column.setProperty(model.endtimeProperty());
		column.setCellType(EditorColumn.CellType.DATETIME);
		columnList.add(column);

		return columnList;
	}

}
