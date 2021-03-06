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
import jstorybook.model.entity.Chapter;
import jstorybook.model.column.EditorColumn;
import jstorybook.model.column.EditorColumnList;
import jstorybook.model.column.ListOnlyColumn;
import jstorybook.model.column.StringColumn;

/**
 * 章リストのカラム
 *
 * @author KMY
 */
public class ChapterColumnFactory extends ColumnFactory<Chapter> {

	private static final ChapterColumnFactory defaultInstance = new ChapterColumnFactory();

	private ChapterColumnFactory () {
	}

	public static ChapterColumnFactory getInstance () {
		return ChapterColumnFactory.defaultInstance;
	}

	@Override
	public EditorColumnList createColumnList (Chapter model) {
		if (model == null) {
			model = new Chapter();
		}

		EditorColumnList columnList = new EditorColumnList();
		columnList.titleProperty().bind(model.titleProperty());
		columnList.idProperty().bindBidirectional(model.idProperty());
		columnList.noteProperty().bindBidirectional(model.noteProperty());
		columnList.entityTypeProperty().set(EntityType.CHAPTER);
		columnList.addRelation(EntityRelation.CHAPTER_SCENE);
		columnList.addRelation(EntityRelation.CHAPTER_TAG);
		EditorColumn column;

		column = new ListOnlyColumn(ResourceManager.getMessage("msg.order"), "order");
		column.setColumnWidth(40);
		column.setDefaultShow(true);
		column.setProperty(model.orderProperty());
		columnList.add(column);

		column = new StringColumn(ResourceManager.getMessage("msg.chapter.name"), "name");
		column.setColumnWidth(240);
		column.setDefaultShow(true);
		column.setProperty(model.nameProperty());
		columnList.add(column);

		return columnList;
	}

}
