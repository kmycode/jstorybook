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
package jstorybook.model.pane;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.ResourceManager;
import jstorybook.model.column.EditorColumn;
import jstorybook.model.column.EditorColumnList;
import jstorybook.model.column.EntityColumn;
import jstorybook.model.column.StringColumn;
import jstorybook.model.dao.DAO;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Tag;
import jstorybook.model.entity.columnfactory.TagColumnFactory;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.ExceptionMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;

/**
 * エンティティ検索のモデル
 *
 * @author KMY
 */
public class SearchEntityModel implements IUseMessenger {

	private Messenger messenger;
	private StoryModel storyModel;

	// タグ選択
	private static final ObjectProperty<EditorColumnList> tagColumnList = new SimpleObjectProperty<>(TagColumnFactory.getInstance().
			createColumnList());
	private final ObjectProperty<ObservableList<Tag>> tagList = new SimpleObjectProperty<>();

	// 検索結果のテーブルセル
	private static final ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>(new EditorColumnList());

	// テーブルのカラムを定義
	static {
		EditorColumn column;
		column = new EntityColumn("", "entityType");
		column.setColumnWidth(40);
		column.setDefaultShow(true);
		column.setCellType(EditorColumn.CellType.ENTITY);
		columnList.get().add(column);
		column = new StringColumn(ResourceManager.getMessage("msg.name"), "title");
		column.setColumnWidth(240);
		column.setDefaultShow(true);
		columnList.get().add(column);
	}

	// 検索条件
	private final ObjectProperty<List<EntityType>> entityTypeList = new SimpleObjectProperty<>(new ArrayList());
	private final ObjectProperty<List<Long>> tagIdList = new SimpleObjectProperty<>(new ArrayList());

	// 検索結果
	private final ObjectProperty<ObservableList<Entity>> searchResultList = new SimpleObjectProperty<>(FXCollections.
			observableArrayList());

	public SearchEntityModel () {
		this.entityTypeList.addListener((obj) -> this.search());
		this.tagIdList.addListener((obj) -> this.search());
	}

	public void search () {
		if (this.storyModel == null) {
			this.messenger.send(new ExceptionMessage(new NullPointerException("Search Entity pane doesn't have Story Model!")));
		}
		else {
			ObservableList<Entity> result = this.searchResultList.get();
			result.clear();

			for (EntityType entityType : this.entityTypeList.get()) {
				DAO dao;
				switch (entityType) {
				case PERSON:
					dao = storyModel.getPersonDAO();
					break;
				case GROUP:
					dao = storyModel.getGroupDAO();
					break;
				case PLACE:
					dao = storyModel.getPlaceDAO();
					break;
				case SCENE:
					dao = storyModel.getSceneDAO();
					break;
				case CHAPTER:
					dao = storyModel.getChapterDAO();
					break;
				case KEYWORD:
					dao = storyModel.getKeywordDAO();
					break;
				case TAG:
					dao = storyModel.getTagDAO();
					break;
				default:
					dao = null;
				}

				// DAOからリストを取得して、検索処理
				if (dao != null) {
					List<Entity> entityList = (List<Entity>) dao.modelListProperty().get();
					for (Entity entity : entityList) {
						boolean hit = false;

						// タグ検索
						if (this.tagIdList.get().isEmpty()) {
							hit = true;
						}
						else {
							for (Long tagId : this.tagIdList.get()) {
								if (this.storyModel.hasTag(entity, tagId)) {
									hit = true;
									break;
								}
							}
						}

						if (hit) {
							result.add(entity);
						}
					}
				}
			}
		}
	}

	public ObjectProperty<ObservableList<Entity>> searchResultListProperty () {
		return this.searchResultList;
	}

	public ObjectProperty<List<EntityType>> entityTypeListProperty () {
		return this.entityTypeList;
	}

	public static ObjectProperty<EditorColumnList> columnListProperty () {
		return SearchEntityModel.columnList;
	}

	public static ObjectProperty<EditorColumnList> tagColumnListProperty () {
		return SearchEntityModel.tagColumnList;
	}

	public ObjectProperty<ObservableList<Tag>> tagListProperty () {
		return this.tagList;
	}

	public ObjectProperty<List<Long>> tagIdListProperty () {
		return this.tagIdList;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;

		if (this.messenger != null) {
			CurrentStoryModelGetMessage mes = new CurrentStoryModelGetMessage();
			this.messenger.send(mes);
			this.storyModel = mes.storyModelProperty().get();
			this.tagList.bind(this.storyModel.getTagDAO().modelListProperty());
		}
	}

}
