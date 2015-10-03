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
import jstorybook.model.entity.Scene;
import jstorybook.model.column.DateColumn;
import jstorybook.model.column.EditorColumn;
import jstorybook.model.column.EditorColumnList;
import jstorybook.model.column.ListOnlyColumn;
import jstorybook.model.column.StringColumn;

/**
 * シーンリストのカラム
 *
 * @author KMY
 */
public class SceneColumnFactory extends ColumnFactory<Scene> {

	private static final SceneColumnFactory defaultInstance = new SceneColumnFactory();

	private SceneColumnFactory () {
	}

	public static SceneColumnFactory getInstance () {
		return SceneColumnFactory.defaultInstance;
	}

	@Override
	public EditorColumnList createColumnList (Scene model) {
		if (model == null) {
			model = new Scene();
		}

		EditorColumnList columnList = new EditorColumnList();
		columnList.titleProperty().bind(model.titleProperty());
		columnList.idProperty().bindBidirectional(model.idProperty());
		columnList.noteProperty().bindBidirectional(model.noteProperty());
		columnList.addRelation(EntityRelation.CHAPTER_SCENE);
		columnList.addRelation(EntityRelation.SCENE_PERSON);
		columnList.addRelation(EntityRelation.SCENE_PLACE);
		columnList.addRelation(EntityRelation.SCENE_TAG);
		columnList.entityTypeProperty().set(EntityType.SCENE);
		EditorColumn column;

		column = new ListOnlyColumn(ResourceManager.getMessage("msg.order"), "order");
		column.setColumnWidth(40);
		column.setDefaultShow(true);
		column.setProperty(model.orderProperty());
		columnList.add(column);

		column = new StringColumn(ResourceManager.getMessage("msg.scene.name"), "name");
		column.setColumnWidth(240);
		column.setDefaultShow(true);
		column.setProperty(model.nameProperty());
		columnList.add(column);

		column = new DateColumn(ResourceManager.getMessage("msg.scene.starttime"), "starttime");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		column.setProperty(model.starttimeProperty());
		column.setCellType(EditorColumn.CellType.DATE);
		columnList.add(column);

		column = new DateColumn(ResourceManager.getMessage("msg.scene.endtime"), "endtime");
		column.setColumnWidth(100);
		column.setDefaultShow(true);
		column.setProperty(model.endtimeProperty());
		column.setCellType(EditorColumn.CellType.DATE);
		columnList.add(column);

		return columnList;
	}

}
