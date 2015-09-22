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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.contract.DialogResult;
import jstorybook.common.contract.StorySettingName;
import jstorybook.common.manager.ResourceManager;
import jstorybook.model.dao.ChapterDAO;
import jstorybook.model.dao.ChapterSceneRelationDAO;
import jstorybook.model.dao.DAO;
import jstorybook.model.dao.GroupDAO;
import jstorybook.model.dao.GroupPersonRelationDAO;
import jstorybook.model.dao.PersonDAO;
import jstorybook.model.dao.PersonPersonRelationDAO;
import jstorybook.model.dao.PlaceDAO;
import jstorybook.model.dao.SceneDAO;
import jstorybook.model.dao.ScenePersonRelationDAO;
import jstorybook.model.dao.ScenePlaceRelationDAO;
import jstorybook.model.dao.StorySettingDAO;
import jstorybook.model.entity.Chapter;
import jstorybook.model.entity.ChapterSceneRelation;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Group;
import jstorybook.model.entity.GroupPersonRelation;
import jstorybook.model.entity.ISortableEntity;
import jstorybook.model.entity.Person;
import jstorybook.model.entity.PersonPersonRelation;
import jstorybook.model.entity.Place;
import jstorybook.model.entity.Scene;
import jstorybook.model.entity.ScenePersonRelation;
import jstorybook.model.entity.ScenePlaceRelation;
import jstorybook.model.entity.StorySetting;
import jstorybook.model.entity.columnfactory.ChapterColumnFactory;
import jstorybook.model.entity.columnfactory.ColumnFactory;
import jstorybook.model.entity.columnfactory.GroupColumnFactory;
import jstorybook.model.entity.columnfactory.PersonColumnFactory;
import jstorybook.model.entity.columnfactory.PlaceColumnFactory;
import jstorybook.model.entity.columnfactory.SceneColumnFactory;
import jstorybook.model.story.sync.StoryLoadSync;
import jstorybook.model.story.sync.StorySaveSync;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.ProgressDialogShowMessage;
import jstorybook.viewtool.messenger.exception.StoryFileLoadFailedMessage;
import jstorybook.viewtool.messenger.general.DeleteDialogMessage;
import jstorybook.viewtool.messenger.pane.ChapterEditorShowMessage;
import jstorybook.viewtool.messenger.pane.EntityEditorCloseMessage;
import jstorybook.viewtool.messenger.pane.EntityEditorShowMessage;
import jstorybook.viewtool.messenger.pane.GroupEditorShowMessage;
import jstorybook.viewtool.messenger.pane.PersonEditorShowMessage;
import jstorybook.viewtool.messenger.pane.PlaceEditorShowMessage;
import jstorybook.viewtool.messenger.pane.SceneEditorShowMessage;

/**
 * ストーリーファイルのモデル
 *
 * @author KMY
 */
public class StoryModel implements IUseMessenger {

	private final ObjectProperty<StoryCoreModel> core = new SimpleObjectProperty<>(new StoryCoreModel());
	private final StringProperty storyFileName = new SimpleStringProperty("");

	private final ObjectProperty<StoryEntityColumnModel> entityColumn = new SimpleObjectProperty<>(
			new StoryEntityColumnModel());

	// エンティティをあらわすインスタンス
	private final StoryEntityModel<StorySetting, StorySettingDAO> storySettingEntity = new StoryEntityModel<>(new StorySettingDAO());
	private final StoryEntityModel<Person, PersonDAO> personEntity = new StoryEntityModel<>(new PersonDAO());
	private final StoryEntityModel<Group, GroupDAO> groupEntity = new StoryEntityModel<>(new GroupDAO());
	private final StoryEntityModel<Place, PlaceDAO> placeEntity = new StoryEntityModel<>(new PlaceDAO());
	private final StoryEntityModel<Scene, SceneDAO> sceneEntity = new StoryEntityModel<>(new SceneDAO());
	private final StoryEntityModel<Chapter, ChapterDAO> chapterEntity = new StoryEntityModel<>(new ChapterDAO());
	private final StoryEntityModel<PersonPersonRelation, PersonPersonRelationDAO> personPersonEntity = new StoryEntityModel<>(
			new PersonPersonRelationDAO());
	private final StoryEntityModel<GroupPersonRelation, GroupPersonRelationDAO> groupPersonEntity = new StoryEntityModel<>(
			new GroupPersonRelationDAO());
	private final StoryEntityModel<ChapterSceneRelation, ChapterSceneRelationDAO> chapterSceneEntity = new StoryEntityModel<>(
			new ChapterSceneRelationDAO());
	private final StoryEntityModel<ScenePersonRelation, ScenePersonRelationDAO> scenePersonEntity = new StoryEntityModel<>(
			new ScenePersonRelationDAO());
	private final StoryEntityModel<ScenePlaceRelation, ScenePlaceRelationDAO> scenePlaceEntity = new StoryEntityModel<>(
			new ScenePlaceRelationDAO());

	// 非公開のプロパティ
	private final ObjectProperty<StoryFileModel> storyFile = new SimpleObjectProperty<>();
	private final BooleanProperty canSave = new SimpleBooleanProperty(false);
	private Messenger messenger = Messenger.getInstance();

	public StoryModel () {
		// ファイル名変更時のイベント
		this.storyFileName.addListener((obj) -> {
			try {
				this.storyFile.set(new StoryFileModel(((StringProperty) obj).get()));
				this.setDAO();
			} catch (SQLException e) {
				this.messenger.send(new StoryFileLoadFailedMessage(((StringProperty) obj).get()));
				e.printStackTrace();
			}
		});
	}

	// ファイル名を変更した時に呼び出して、情報を取得する
	private void setDAO () throws SQLException {
		// 設定の読み込み
		this.storySettingEntity.dao.get().setStoryFileModel(this.storyFile.get());

		StoryLoadSync.StoryLoadService service = new StoryLoadSync.StoryLoadService(this);

		// 進捗状況を表示
		this.messenger.send(new ProgressDialogShowMessage(service.myProgressProperty()));

		// 読み込み処理（非同期）
		service.stepProperty().addListener((obj) -> {
			// getバグ
			service.stepProperty().get();
		});
		service.start();

		// 初期化・データの読み込み
		/*
		 		this.personEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.groupEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.placeEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.sceneEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.chapterEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.personPersonEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.groupPersonEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.chapterSceneEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.scenePersonEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.scenePlaceEntity.dao.get().setStoryFileModel(this.storyFile.get());
		 */

		// 関連付け
		/*
		 		this.personPersonEntity.dao.get().readPersonDAO(this.personEntity.dao.get());
		this.groupPersonEntity.dao.get().readPersonDAO(this.personEntity.dao.get());
		this.groupPersonEntity.dao.get().readGroupDAO(this.groupEntity.dao.get());
		this.chapterSceneEntity.dao.get().readChapterDAO(this.chapterEntity.dao.get());
		this.chapterSceneEntity.dao.get().readSceneDAO(this.sceneEntity.dao.get());
		this.scenePersonEntity.dao.get().readSceneDAO(this.sceneEntity.dao.get());
		this.scenePersonEntity.dao.get().readPersonDAO(this.personEntity.dao.get());
		this.scenePlaceEntity.dao.get().readSceneDAO(this.sceneEntity.dao.get());
		this.scenePlaceEntity.dao.get().readPlaceDAO(this.placeEntity.dao.get());
		 */

		this.canSave.set(true);
	}

	// ストーリーモデル全体のファイルへの保存
	public void save () {
		/*
		 		try {
			this.personEntity.dao.get().saveList();
			this.groupEntity.dao.get().saveList();
			this.placeEntity.dao.get().saveList();
			this.sceneEntity.dao.get().saveList();
			this.chapterEntity.dao.get().saveList();
			this.personPersonEntity.dao.get().saveList();
			this.groupPersonEntity.dao.get().saveList();
			this.chapterSceneEntity.dao.get().saveList();
			this.scenePersonEntity.dao.get().saveList();
			this.scenePlaceEntity.dao.get().saveList();
		} catch (SQLException e) {
			this.messenger.send(new StoryFileSaveFailedMessage(this.storyFileName.get()));
			e.printStackTrace();
		}
		 */

		StorySaveSync.StorySaveService service = new StorySaveSync.StorySaveService(this);

		// 進捗状況を表示
		this.messenger.send(new ProgressDialogShowMessage(service.myProgressProperty()));

		// 保存処理（非同期）
		service.stepProperty().addListener((obj) -> {
			// getバグ
			service.stepProperty().get();
		});
		service.start();
	}

	// 保存や読み込みで利用
	public List<DAO> getDAOList () {
		List<DAO> daoList = new ArrayList<>();
		daoList.add(this.personEntity.dao.get());
		daoList.add(this.groupEntity.dao.get());
		daoList.add(this.placeEntity.dao.get());
		daoList.add(this.sceneEntity.dao.get());
		daoList.add(this.chapterEntity.dao.get());
		daoList.add(this.personPersonEntity.dao.get());
		daoList.add(this.groupPersonEntity.dao.get());
		daoList.add(this.chapterSceneEntity.dao.get());
		daoList.add(this.scenePersonEntity.dao.get());
		daoList.add(this.scenePlaceEntity.dao.get());

		// 設定は最後
		daoList.add(this.storySettingEntity.dao.get());

		return daoList;
	}

	public BooleanProperty canSaveProperty () {
		return this.canSave;
	}

	// -------------------------------------------------------
	// 関連するモデル
	public ObjectProperty<StoryCoreModel> coreProperty () {
		return this.core;
	}

	public StoryCoreModel getCore () {
		return this.core.get();
	}

	public StoryFileModel getStoryFile () {
		return this.storyFile.get();
	}

	public ObjectProperty<StoryEntityColumnModel> entityColumnProperty () {
		return this.entityColumn;
	}

	// -------------------------------------------------------
	// StoryModelそのものが持つプロパティ

	public StringProperty fileNameProperty () {
		return this.storyFileName;
	}

	// -------------------------------------------------------
	// エンティティをあらわすモデルクラス
	public StoryEntityModel<Person, PersonDAO> getPersonEntity () {
		return this.personEntity;
	}

	public StoryEntityModel<Group, GroupDAO> getGroupEntity () {
		return this.groupEntity;
	}

	public StoryEntityModel<Place, PlaceDAO> getPlaceEntity () {
		return this.placeEntity;
	}

	public StoryEntityModel<Scene, SceneDAO> getSceneEntity () {
		return this.sceneEntity;
	}

	public StoryEntityModel<Chapter, ChapterDAO> getChapterEntity () {
		return this.chapterEntity;
	}

	// -------------------------------------------------------
	// エンティティ同士の関係
	public List<Long> getPersonPersonRelation (long personId) {
		return this.personPersonEntity.dao.get().getRelatedIdList(personId);
	}

	public void setPersonPersonRelation (long personId, List<Long> list) {
		this.personPersonEntity.dao.get().setRelatedIdList(personId, list);
	}

	public List<Long> getGroupPersonRelation_Person (long personId) {
		return this.groupPersonEntity.dao.get().getRelatedIdList(personId, true);
	}

	public void setGroupPersonRelation_Person (long personId, List<Long> list) {
		this.groupPersonEntity.dao.get().setRelatedIdList(personId, list, true);
	}

	public List<Long> getGroupPersonRelation_Group (long groupId) {
		return this.groupPersonEntity.dao.get().getRelatedIdList(groupId, false);
	}

	public void setGroupPersonRelation_Group (long groupId, List<Long> list) {
		this.groupPersonEntity.dao.get().setRelatedIdList(groupId, list, false);
	}

	public List<Long> getChapterSceneRelation_Scene (long sceneId) {
		return this.chapterSceneEntity.dao.get().getRelatedIdList(sceneId, true);
	}

	public void setChapterSceneRelation_Scene (long sceneId, List<Long> list) {
		this.chapterSceneEntity.dao.get().setRelatedIdList(sceneId, list, true);
	}

	public List<Long> getChapterSceneRelation_Chapter (long chapterId) {
		return this.chapterSceneEntity.dao.get().getRelatedIdList(chapterId, false);
	}

	public void setChapterSceneRelation_Chapter (long chapterId, List<Long> list) {
		this.chapterSceneEntity.dao.get().setRelatedIdList(chapterId, list, false);
	}

	public List<Long> getScenePersonRelation_Person (long personId) {
		return this.scenePersonEntity.dao.get().getRelatedIdList(personId, true);
	}

	public void setScenePersonRelation_Person (long personId, List<Long> list) {
		this.scenePersonEntity.dao.get().setRelatedIdList(personId, list, true);
	}

	public List<Long> getScenePersonRelation_Scene (long sceneId) {
		return this.scenePersonEntity.dao.get().getRelatedIdList(sceneId, false);
	}

	public void setScenePersonRelation_Scene (long sceneId, List<Long> list) {
		this.scenePersonEntity.dao.get().setRelatedIdList(sceneId, list, false);
	}

	public List<Long> getScenePlaceRelation_Place (long placeId) {
		return this.scenePlaceEntity.dao.get().getRelatedIdList(placeId, true);
	}

	public void setScenePlaceRelation_Place (long placeId, List<Long> list) {
		this.scenePlaceEntity.dao.get().setRelatedIdList(placeId, list, true);
	}

	public List<Long> getScenePlaceRelation_Scene (long sceneId) {
		return this.scenePlaceEntity.dao.get().getRelatedIdList(sceneId, false);
	}

	public void setScenePlaceRelation_Scene (long sceneId, List<Long> list) {
		this.scenePlaceEntity.dao.get().setRelatedIdList(sceneId, list, false);
	}

	// -------------------------------------------------------
	// それぞれのエンティティの新規作成、編集、削除など
	private void newEntity (Entity newEntity, DAO dao, EntityEditorShowMessage messageInstance, ColumnFactory columnFactory) {
		EntityAdapter adapter = new EntityAdapter(newEntity, dao);
		this.messenger.send(
				messageInstance.newMessage(columnFactory.createColumnList(), columnFactory.createColumnList(newEntity, adapter)));
	}

	private void editEntity (List<? extends Entity> selectedList, EntityEditorShowMessage messageInstance, ColumnFactory columnFactory) {
		if (selectedList != null && selectedList.size() > 0) {
			for (Entity selected : selectedList) {
				this.messenger.send(
						messageInstance.newMessage(columnFactory.createColumnList(selected.entityClone()), columnFactory.
												   createColumnList(selected)));
			}
		}
	}

	private void deleteEntity (List<? extends Entity> selectedList, ColumnFactory columnFactory, DAO dao) {
		if (selectedList != null && selectedList.size() > 0) {
			DeleteDialogMessage delmes = new DeleteDialogMessage(selectedList.size() <= 1 ? selectedList.get(0).titleProperty().get()
																		 : ResourceManager.getMessage("msg.confirm.delete.multi",
																									  selectedList.
																									  get(0).titleProperty().get(),
																									  selectedList.size()));
			this.messenger.send(delmes);
			if (delmes.getResult() == DialogResult.YES) {

				// 削除するエンティティリストのクローンを作る
				List<Entity> deleteList = new ArrayList<>();
				deleteList.addAll(selectedList);

				// 削除実行
				for (Entity selected : deleteList) {
					this.messenger.send(new EntityEditorCloseMessage(columnFactory.createColumnList(selected)));
					dao.deleteModel(selected);
				}
			}
		}
	}

	private void upEntity (List<? extends ISortableEntity> selectedList, DAO<? extends Entity> dao) {
		if (selectedList != null && selectedList.size() > 0) {
			List entityList = dao.modelListProperty().get();
			Collections.sort(entityList);
			int entityNum = entityList.size();
			for (ISortableEntity selected : selectedList) {
				for (int i = 0; i < entityNum; i++) {
					if (((Entity) entityList.get(i)).idProperty().get() == selected.idProperty().get()) {
						if (i > 0) {
							((ISortableEntity) selected).replaceOrder((ISortableEntity) entityList.get(i - 1));
						}
					}
				}
			}
			Collections.sort(entityList);
		}
	}

	private void downEntity (List<? extends ISortableEntity> selectedList, DAO<? extends Entity> dao) {
		if (selectedList != null && selectedList.size() > 0) {
			List entityList = dao.modelListProperty().get();
			Collections.sort(entityList);
			int entityNum = entityList.size();
			for (ISortableEntity selected : selectedList) {
				for (int i = 0; i < entityNum; i++) {
					if (((Entity) entityList.get(i)).idProperty().get() == selected.idProperty().get()) {
						if (i < entityNum - 1) {
							((ISortableEntity) selected).replaceOrder((ISortableEntity) entityList.get(i + 1));
						}
					}
				}
			}
			Collections.sort(entityList);
		}
	}

	public void newPerson () {
		this.newEntity(new Person(), this.personEntity.dao.get(), PersonEditorShowMessage.getInstance(), PersonColumnFactory.
					   getInstance());
	}

	public void editPerson () {
		this.editEntity(this.personEntity.selectedEntityList.get(), PersonEditorShowMessage.getInstance(), PersonColumnFactory.
						getInstance());
	}

	public void deletePerson () {
		this.deleteEntity(this.personEntity.selectedEntityList.get(), PersonColumnFactory.getInstance(), this.personEntity.dao.get());
	}

	public void upPerson () {
		this.upEntity(this.personEntity.selectedEntityList.get(), this.personEntity.dao.get());
	}

	public void downPerson () {
		this.downEntity(this.personEntity.selectedEntityList.get(), this.personEntity.dao.get());
	}

	public void newGroup () {
		this.newEntity(new Group(), this.groupEntity.dao.get(), GroupEditorShowMessage.getInstance(), GroupColumnFactory.
					   getInstance());
	}

	public void editGroup () {
		this.editEntity(this.groupEntity.selectedEntityList.get(), GroupEditorShowMessage.getInstance(), GroupColumnFactory.
						getInstance());
	}

	public void deleteGroup () {
		this.deleteEntity(this.groupEntity.selectedEntityList.get(), GroupColumnFactory.getInstance(), this.groupEntity.dao.get());
	}

	public void upGroup () {
		this.upEntity(this.groupEntity.selectedEntityList.get(), this.groupEntity.dao.get());
	}

	public void downGroup () {
		this.downEntity(this.groupEntity.selectedEntityList.get(), this.groupEntity.dao.get());
	}

	public void newPlace () {
		this.newEntity(new Place(), this.placeEntity.dao.get(), PlaceEditorShowMessage.getInstance(), PlaceColumnFactory.
					   getInstance());
	}

	public void editPlace () {
		this.editEntity(this.placeEntity.selectedEntityList.get(), PlaceEditorShowMessage.getInstance(), PlaceColumnFactory.
						getInstance());
	}

	public void deletePlace () {
		this.deleteEntity(this.placeEntity.selectedEntityList.get(), PlaceColumnFactory.getInstance(), this.placeEntity.dao.get());
	}

	public void upPlace () {
		this.upEntity(this.placeEntity.selectedEntityList.get(), this.placeEntity.dao.get());
	}

	public void downPlace () {
		this.downEntity(this.placeEntity.selectedEntityList.get(), this.placeEntity.dao.get());
	}

	public void newScene () {
		this.newEntity(new Scene(), this.sceneEntity.dao.get(), SceneEditorShowMessage.getInstance(), SceneColumnFactory.
					   getInstance());
	}

	public void editScene () {
		this.editEntity(this.sceneEntity.selectedEntityList.get(), SceneEditorShowMessage.getInstance(), SceneColumnFactory.
						getInstance());
	}

	public void deleteScene () {
		this.deleteEntity(this.sceneEntity.selectedEntityList.get(), SceneColumnFactory.getInstance(), this.sceneEntity.dao.get());
	}

	public void upScene () {
		this.upEntity(this.sceneEntity.selectedEntityList.get(), this.sceneEntity.dao.get());
	}

	public void downScene () {
		this.downEntity(this.sceneEntity.selectedEntityList.get(), this.sceneEntity.dao.get());
	}

	public void newChapter () {
		this.newEntity(new Chapter(), this.chapterEntity.dao.get(), ChapterEditorShowMessage.getInstance(), ChapterColumnFactory.
					   getInstance());
	}

	public void editChapter () {
		this.editEntity(this.chapterEntity.selectedEntityList.get(), ChapterEditorShowMessage.getInstance(), ChapterColumnFactory.
						getInstance());
	}

	public void deleteChapter () {
		this.
				deleteEntity(this.chapterEntity.selectedEntityList.get(), ChapterColumnFactory.getInstance(), this.chapterEntity.dao.
							 get());
	}

	public void upChapter () {
		this.upEntity(this.chapterEntity.selectedEntityList.get(), this.chapterEntity.dao.get());
	}

	public void downChapter () {
		this.downEntity(this.chapterEntity.selectedEntityList.get(), this.chapterEntity.dao.get());
	}

	// -------------------------------------------------------
	// 設定を取得
	public StorySetting getSetting (StorySettingName key) {
		return this.storySettingEntity.dao.get().getSetting(key.getKey());
	}

	// -------------------------------------------------------
	// IUseMessenger
	@Override
	public void setMessenger (Messenger messenger) {
		if (messenger != null) {
			this.messenger = messenger;
		}
	}

	/*
	 * エンティティの種類ごとに処理を共通化
	 * E: エンティティの型
	 * D: DAOの型
	 */
	public class StoryEntityModel<E extends Entity, D> {

		private final ObjectProperty<D> dao = new SimpleObjectProperty<>();
		private final ObjectProperty<List<E>> selectedEntityList = new SimpleObjectProperty<>(new ArrayList<>());
		private final BooleanProperty canEdit = new SimpleBooleanProperty(false);

		private StoryEntityModel (D dao) {
			this.dao.set(dao);

			// 選択中のエンティティが変更された場合
			this.selectedEntityList.addListener((obj) -> {
				List<Entity> entityList = ((ObjectProperty<List<Entity>>) obj).get();
				this.canEdit.set(entityList != null && entityList.size() > 0);
			});
		}

		public ObjectProperty<D> DAOProperty () {
			return this.dao;
		}

		// TableViewなどで選択されたエンティティ
		public ObjectProperty<List<E>> selectedEntityProperty () {
			return this.selectedEntityList;
		}

		public BooleanProperty canEditProperty () {
			return this.canEdit;
		}
	}

	/*
	 * 全く別のクラス内でエンティティを新規作成、削除できるようにしたアダプタ
	 */
	public static class EntityAdapter {

		private final DAO dao;
		private final Entity entity;
		private boolean isDid = false;

		private EntityAdapter (Entity entity, DAO dao) {
			this.dao = dao;
			this.entity = entity;
		}

		public void addEntity () {
			if (isDid) {
				throw new RuntimeException("The EntityAdapter was used already.");
			}
			this.dao.addModel(this.entity);
			this.isDid = true;
		}

		public void deleteEntity () {
			if (isDid) {
				throw new RuntimeException("The EntityAdapter was used already.");
			}
			this.dao.deleteModel(this.entity);
			this.isDid = true;
		}
	}

}
