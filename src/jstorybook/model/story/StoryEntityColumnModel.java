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
package jstorybook.model.story;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jstorybook.model.entity.columnfactory.ChapterColumnFactory;
import jstorybook.model.entity.columnfactory.GroupColumnFactory;
import jstorybook.model.entity.columnfactory.KeywordColumnFactory;
import jstorybook.model.entity.columnfactory.PersonColumnFactory;
import jstorybook.model.entity.columnfactory.PlaceColumnFactory;
import jstorybook.model.entity.columnfactory.SceneColumnFactory;
import jstorybook.model.entity.columnfactory.SexColumnFactory;
import jstorybook.model.entity.columnfactory.TagColumnFactory;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * 各エンティティのカラムをまとめたモデル
 *
 * @author KMY
 */
public class StoryEntityColumnModel {

	private final ObjectProperty<EditorColumnList> personColumnList = new SimpleObjectProperty<>(PersonColumnFactory.
			getInstance().createColumnList());
	private final ObjectProperty<EditorColumnList> groupColumnList = new SimpleObjectProperty<>(GroupColumnFactory.
			getInstance().createColumnList());
	private final ObjectProperty<EditorColumnList> placeColumnList = new SimpleObjectProperty<>(PlaceColumnFactory.
			getInstance().createColumnList());
	private final ObjectProperty<EditorColumnList> sceneColumnList = new SimpleObjectProperty<>(SceneColumnFactory.
			getInstance().createColumnList());
	private final ObjectProperty<EditorColumnList> chapterColumnList = new SimpleObjectProperty<>(ChapterColumnFactory.
			getInstance().createColumnList());
	private final ObjectProperty<EditorColumnList> sexColumnList = new SimpleObjectProperty<>(SexColumnFactory.
			getInstance().createColumnList());
	private final ObjectProperty<EditorColumnList> keywordColumnList = new SimpleObjectProperty<>(KeywordColumnFactory.
			getInstance().createColumnList());
	private final ObjectProperty<EditorColumnList> tagColumnList = new SimpleObjectProperty<>(TagColumnFactory.
			getInstance().createColumnList());

	public ObjectProperty<EditorColumnList> personColumnListProperty () {
		return this.personColumnList;
	}

	public ObjectProperty<EditorColumnList> groupColumnListProperty () {
		return this.groupColumnList;
	}

	public ObjectProperty<EditorColumnList> placeColumnListProperty () {
		return this.placeColumnList;
	}

	public ObjectProperty<EditorColumnList> sceneColumnListProperty () {
		return this.sceneColumnList;
	}

	public ObjectProperty<EditorColumnList> chapterColumnListProperty () {
		return this.chapterColumnList;
	}

	public ObjectProperty<EditorColumnList> sexColumnListProperty () {
		return this.sexColumnList;
	}

	public ObjectProperty<EditorColumnList> keywordColumnListProperty () {
		return this.keywordColumnList;
	}

	public ObjectProperty<EditorColumnList> tagColumnListProperty () {
		return this.tagColumnList;
	}

}
