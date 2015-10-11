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
import javafx.application.Platform;
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
import jstorybook.model.dao.ChapterTagRelationDAO;
import jstorybook.model.dao.DAO;
import jstorybook.model.dao.GroupDAO;
import jstorybook.model.dao.GroupKeywordRelationDAO;
import jstorybook.model.dao.GroupPersonRelationDAO;
import jstorybook.model.dao.GroupTagRelationDAO;
import jstorybook.model.dao.KeywordDAO;
import jstorybook.model.dao.KeywordTagRelationDAO;
import jstorybook.model.dao.PersonDAO;
import jstorybook.model.dao.PersonKeywordRelationDAO;
import jstorybook.model.dao.PersonPersonRelationDAO;
import jstorybook.model.dao.PersonTagRelationDAO;
import jstorybook.model.dao.PlaceDAO;
import jstorybook.model.dao.PlaceKeywordRelationDAO;
import jstorybook.model.dao.PlaceTagRelationDAO;
import jstorybook.model.dao.SceneDAO;
import jstorybook.model.dao.SceneKeywordRelationDAO;
import jstorybook.model.dao.ScenePersonRelationDAO;
import jstorybook.model.dao.ScenePlaceRelationDAO;
import jstorybook.model.dao.SceneTagRelationDAO;
import jstorybook.model.dao.SexDAO;
import jstorybook.model.dao.StorySettingDAO;
import jstorybook.model.dao.TagDAO;
import jstorybook.model.dao.TagTagRelationDAO;
import jstorybook.model.entity.Chapter;
import jstorybook.model.entity.ChapterSceneRelation;
import jstorybook.model.entity.ChapterTagRelation;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Group;
import jstorybook.model.entity.GroupKeywordRelation;
import jstorybook.model.entity.GroupPersonRelation;
import jstorybook.model.entity.GroupTagRelation;
import jstorybook.model.entity.ISortableEntity;
import jstorybook.model.entity.Keyword;
import jstorybook.model.entity.KeywordTagRelation;
import jstorybook.model.entity.Person;
import jstorybook.model.entity.PersonKeywordRelation;
import jstorybook.model.entity.PersonPersonRelation;
import jstorybook.model.entity.PersonTagRelation;
import jstorybook.model.entity.Place;
import jstorybook.model.entity.PlaceKeywordRelation;
import jstorybook.model.entity.PlaceTagRelation;
import jstorybook.model.entity.Scene;
import jstorybook.model.entity.SceneKeywordRelation;
import jstorybook.model.entity.ScenePersonRelation;
import jstorybook.model.entity.ScenePlaceRelation;
import jstorybook.model.entity.SceneTagRelation;
import jstorybook.model.entity.Sex;
import jstorybook.model.entity.StorySetting;
import jstorybook.model.entity.Tag;
import jstorybook.model.entity.TagTagRelation;
import jstorybook.model.entity.columnfactory.ChapterColumnFactory;
import jstorybook.model.entity.columnfactory.ColumnFactory;
import jstorybook.model.entity.columnfactory.GroupColumnFactory;
import jstorybook.model.entity.columnfactory.KeywordColumnFactory;
import jstorybook.model.entity.columnfactory.PersonColumnFactory;
import jstorybook.model.entity.columnfactory.PlaceColumnFactory;
import jstorybook.model.entity.columnfactory.SceneColumnFactory;
import jstorybook.model.entity.columnfactory.SexColumnFactory;
import jstorybook.model.entity.columnfactory.TagColumnFactory;
import jstorybook.model.story.sync.StoryLoadSync;
import jstorybook.model.story.sync.StorySaveSync;
import jstorybook.viewtool.messenger.ExceptionMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.MainWindowClearMessage;
import jstorybook.viewtool.messenger.MainWindowResetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.ProgressDialogShowMessage;
import jstorybook.viewtool.messenger.exception.StoryFileLoadFailedMessage;
import jstorybook.viewtool.messenger.general.DeleteDialogMessage;
import jstorybook.viewtool.messenger.pane.EntityEditorCloseMessage;
import jstorybook.viewtool.messenger.pane.EntityListNoSelectMessage;
import jstorybook.viewtool.messenger.pane.chart.AssociationChartShowMessage;
import jstorybook.viewtool.messenger.pane.chart.SceneNovelChartShowMessage;
import jstorybook.viewtool.messenger.pane.editor.ChapterEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.EntityEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.GroupEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.KeywordEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.PersonEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.PlaceEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.SceneEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.SexEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.TagEditorShowMessage;

/**
 * ストーリーファイルのモデル
 *
 * @author KMY
 */
public class StoryModel implements IUseMessenger {

	private final ObjectProperty<StoryCoreModel> core = new SimpleObjectProperty<>(new StoryCoreModel());
	private final StringProperty storyFileName = new SimpleStringProperty("");
	private boolean isCreating = false;

	private final ObjectProperty<StoryEntityColumnModel> entityColumn = new SimpleObjectProperty<>(
			new StoryEntityColumnModel());

	// エンティティをあらわすインスタンス
	private final StoryEntityModel<StorySetting, StorySettingDAO> storySettingEntity = new StoryEntityModel<>(new StorySettingDAO());
	private final StoryEntityModel<Person, PersonDAO> personEntity = new StoryEntityModel<>(new PersonDAO());
	private final StoryEntityModel<Group, GroupDAO> groupEntity = new StoryEntityModel<>(new GroupDAO());
	private final StoryEntityModel<Place, PlaceDAO> placeEntity = new StoryEntityModel<>(new PlaceDAO());
	private final StoryEntityModel<Scene, SceneDAO> sceneEntity = new StoryEntityModel<>(new SceneDAO());
	private final StoryEntityModel<Chapter, ChapterDAO> chapterEntity = new StoryEntityModel<>(new ChapterDAO());
	private final StoryEntityModel<Sex, SexDAO> sexEntity = new StoryEntityModel<>(new SexDAO());
	private final StoryEntityModel<Keyword, KeywordDAO> keywordEntity = new StoryEntityModel<>(new KeywordDAO());
	private final StoryEntityModel<Tag, TagDAO> tagEntity = new StoryEntityModel<>(new TagDAO());
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
	private final StoryEntityModel<PersonKeywordRelation, PersonKeywordRelationDAO> personKeywordEntity = new StoryEntityModel<>(
			new PersonKeywordRelationDAO());
	private final StoryEntityModel<GroupKeywordRelation, GroupKeywordRelationDAO> groupKeywordEntity = new StoryEntityModel<>(
			new GroupKeywordRelationDAO());
	private final StoryEntityModel<PlaceKeywordRelation, PlaceKeywordRelationDAO> placeKeywordEntity = new StoryEntityModel<>(
			new PlaceKeywordRelationDAO());
	private final StoryEntityModel<SceneKeywordRelation, SceneKeywordRelationDAO> sceneKeywordEntity = new StoryEntityModel<>(
			new SceneKeywordRelationDAO());
	private final StoryEntityModel<PersonTagRelation, PersonTagRelationDAO> personTagEntity = new StoryEntityModel<>(
			new PersonTagRelationDAO());
	private final StoryEntityModel<GroupTagRelation, GroupTagRelationDAO> groupTagEntity = new StoryEntityModel<>(
			new GroupTagRelationDAO());
	private final StoryEntityModel<PlaceTagRelation, PlaceTagRelationDAO> placeTagEntity = new StoryEntityModel<>(
			new PlaceTagRelationDAO());
	private final StoryEntityModel<SceneTagRelation, SceneTagRelationDAO> sceneTagEntity = new StoryEntityModel<>(
			new SceneTagRelationDAO());
	private final StoryEntityModel<ChapterTagRelation, ChapterTagRelationDAO> chapterTagEntity = new StoryEntityModel<>(
			new ChapterTagRelationDAO());
	private final StoryEntityModel<KeywordTagRelation, KeywordTagRelationDAO> keywordTagEntity = new StoryEntityModel<>(
			new KeywordTagRelationDAO());
	private final StoryEntityModel<TagTagRelation, TagTagRelationDAO> tagTagEntity = new StoryEntityModel<>(
			new TagTagRelationDAO());

	// 非公開のプロパティ
	private final ObjectProperty<StoryFileModel> storyFile = new SimpleObjectProperty<>();
	private final BooleanProperty canSave = new SimpleBooleanProperty(false);
	private final BooleanProperty canEdit = new SimpleBooleanProperty(false);
	private final BooleanProperty setDaoFinish = new SimpleBooleanProperty(false);
	private Messenger messenger = Messenger.getInstance();
	private StoryCreateModel storyCreateModel;

	public StoryModel () {

		this.core.get().settingDAOProperty().bind(this.storySettingEntity.dao);

		// ファイル名変更時のイベント
		this.storyFileName.addListener((obj) -> {
			this.canEdit.set(false);
			if (this.storyFileName.get() != null && !this.storyFileName.get().isEmpty()) {
				try {
					this.storyFile.set(new StoryFileModel(((StringProperty) obj).get()));
					if (this.isCreating) {
						this.createDAO();
						this.isCreating = false;
					}
					this.setDAO();

					this.canEdit.set(true);
					this.messenger.send(new MainWindowResetMessage());
				} catch (SQLException e) {
					this.messenger.send(new StoryFileLoadFailedMessage(((StringProperty) obj).get()));
				}
			}
			else {
				this.storyFile.set(null);
			}
		});

		// ストーリー新規作成時、ストーリーの設定を保存する
		this.setDaoFinish.addListener((objs) -> {
			if (this.storyCreateModel != null) {
				if (this.setDaoFinish.get()) {
					this.core.get().storyNameProperty().set(this.storyCreateModel.storyNameProperty().get());
					Platform.runLater(() -> this.save());
					this.storyCreateModel = null;
				}
			}
		});

		// 保存可能か？（今はいったんcanEditにバインドしてる）
		this.canSave.bind(this.canEdit);
	}

	// ファイル名を変更した時に呼び出して、情報を取得する
	private void setDAO () throws SQLException {

		this.setDaoFinish.set(false);

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
		service.setOnSucceeded((obj) -> {
				this.setDaoFinish.set(true);
		});
		service.start();
	}

	// ストーリーモデル全体のファイルへの保存
	public void save () {

		// ストーリーの設定をDAOの中に保存
		this.core.get().save();
		this.core.get().saveStorySetting();

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

	// ストーリーの新規作成
	public void create (StoryCreateModel createModel) {

		this.storyCreateModel = createModel;

		// たったこれだけで初期化処理がなされる
		this.isCreating = true;
		this.storyFileName.set(createModel.fileNameProperty().get());

		// メイン画面の表示をリセット
		this.messenger.send(new MainWindowResetMessage());
	}

	// ストーリー新規作成・ファイルへの書き込み【後で非同期にする】
	private void createDAO () {
		try {
			DAO.createStorySystemTable(this.getStoryFile());
			for (DAO dao : this.getDAOList()) {
				dao.setStoryFileModel(this.getStoryFile(), true);
			}
		} catch (SQLException e) {
			this.messenger.send(new ExceptionMessage(e));
		}
	}

	// ストーリーを閉じる
	public void close () {
		this.storyFileName.set(null);
		this.messenger.send(new MainWindowClearMessage());
	}

	// 保存や読み込みで利用
	public List<DAO> getDAOList () {
		List<DAO> daoList = new ArrayList<>();
		daoList.add(this.personEntity.dao.get());
		daoList.add(this.groupEntity.dao.get());
		daoList.add(this.placeEntity.dao.get());
		daoList.add(this.sceneEntity.dao.get());
		daoList.add(this.chapterEntity.dao.get());
		daoList.add(this.sexEntity.dao.get());
		daoList.add(this.keywordEntity.dao.get());
		daoList.add(this.tagEntity.dao.get());
		daoList.add(this.personPersonEntity.dao.get());
		daoList.add(this.groupPersonEntity.dao.get());
		daoList.add(this.chapterSceneEntity.dao.get());
		daoList.add(this.scenePersonEntity.dao.get());
		daoList.add(this.scenePlaceEntity.dao.get());
		daoList.add(this.personKeywordEntity.dao.get());
		daoList.add(this.groupKeywordEntity.dao.get());
		daoList.add(this.placeKeywordEntity.dao.get());
		daoList.add(this.sceneKeywordEntity.dao.get());
		daoList.add(this.personTagEntity.dao.get());
		daoList.add(this.groupTagEntity.dao.get());
		daoList.add(this.placeTagEntity.dao.get());
		daoList.add(this.sceneTagEntity.dao.get());
		daoList.add(this.chapterTagEntity.dao.get());
		daoList.add(this.keywordTagEntity.dao.get());
		daoList.add(this.tagTagEntity.dao.get());

		// 設定は最後
		daoList.add(this.storySettingEntity.dao.get());

		return daoList;
	}

	public BooleanProperty canSaveProperty () {
		return this.canSave;
	}

	public BooleanProperty canEditProperty () {
		return this.canEdit;
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
	// DAO
	public PersonDAO getPersonDAO () {
		return this.personEntity.dao.get();
	}

	public GroupDAO getGroupDAO () {
		return this.groupEntity.dao.get();
	}

	public PlaceDAO getPlaceDAO () {
		return this.placeEntity.dao.get();
	}

	public SceneDAO getSceneDAO () {
		return this.sceneEntity.dao.get();
	}

	public ChapterDAO getChapterDAO () {
		return this.chapterEntity.dao.get();
	}

	public SexDAO getSexDAO () {
		return this.sexEntity.dao.get();
	}

	public KeywordDAO getKeywordDAO () {
		return this.keywordEntity.dao.get();
	}

	public TagDAO getTagDAO () {
		return this.tagEntity.dao.get();
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

	public StoryEntityModel<Sex, SexDAO> getSexEntity () {
		return this.sexEntity;
	}

	public StoryEntityModel<Keyword, KeywordDAO> getKeywordEntity () {
		return this.keywordEntity;
	}

	public StoryEntityModel<Tag, TagDAO> getTagEntity () {
		return this.tagEntity;
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

	public List<Long> getPersonKeywordRelation_Keyword (long keywordId) {
		return this.personKeywordEntity.dao.get().getRelatedIdList(keywordId, true);
	}

	public void setPersonKeywordRelation_Keyword (long keywordId, List<Long> list) {
		this.personKeywordEntity.dao.get().setRelatedIdList(keywordId, list, true);
	}

	public List<Long> getPersonKeywordRelation_Person (long personId) {
		return this.personKeywordEntity.dao.get().getRelatedIdList(personId, false);
	}

	public void setPersonKeywordRelation_Person (long personId, List<Long> list) {
		this.personKeywordEntity.dao.get().setRelatedIdList(personId, list, false);
	}

	public List<Long> getGroupKeywordRelation_Keyword (long keywordId) {
		return this.groupKeywordEntity.dao.get().getRelatedIdList(keywordId, true);
	}

	public void setGroupKeywordRelation_Keyword (long keywordId, List<Long> list) {
		this.groupKeywordEntity.dao.get().setRelatedIdList(keywordId, list, true);
	}

	public List<Long> getGroupKeywordRelation_Group (long groupId) {
		return this.groupKeywordEntity.dao.get().getRelatedIdList(groupId, false);
	}

	public void setGroupKeywordRelation_Group (long groupId, List<Long> list) {
		this.groupKeywordEntity.dao.get().setRelatedIdList(groupId, list, false);
	}

	public List<Long> getPlaceKeywordRelation_Keyword (long keywordId) {
		return this.placeKeywordEntity.dao.get().getRelatedIdList(keywordId, true);
	}

	public void setPlaceKeywordRelation_Keyword (long keywordId, List<Long> list) {
		this.placeKeywordEntity.dao.get().setRelatedIdList(keywordId, list, true);
	}

	public List<Long> getPlaceKeywordRelation_Place (long placeId) {
		return this.placeKeywordEntity.dao.get().getRelatedIdList(placeId, false);
	}

	public void setPlaceKeywordRelation_Place (long placeId, List<Long> list) {
		this.placeKeywordEntity.dao.get().setRelatedIdList(placeId, list, false);
	}

	public List<Long> getSceneKeywordRelation_Keyword (long keywordId) {
		return this.sceneKeywordEntity.dao.get().getRelatedIdList(keywordId, true);
	}

	public void setSceneKeywordRelation_Keyword (long keywordId, List<Long> list) {
		this.sceneKeywordEntity.dao.get().setRelatedIdList(keywordId, list, true);
	}

	public List<Long> getSceneKeywordRelation_Scene (long sceneId) {
		return this.sceneKeywordEntity.dao.get().getRelatedIdList(sceneId, false);
	}

	public void setSceneKeywordRelation_Scene (long sceneId, List<Long> list) {
		this.sceneKeywordEntity.dao.get().setRelatedIdList(sceneId, list, false);
	}

	public List<Long> getPersonTagRelation_Tag (long tagId) {
		return this.personTagEntity.dao.get().getRelatedIdList(tagId, true);
	}

	public void setPersonTagRelation_Tag (long tagId, List<Long> list) {
		this.personTagEntity.dao.get().setRelatedIdList(tagId, list, true);
	}

	public List<Long> getPersonTagRelation_Person (long personId) {
		return this.personTagEntity.dao.get().getRelatedIdList(personId, false);
	}

	public void setPersonTagRelation_Person (long personId, List<Long> list) {
		this.personTagEntity.dao.get().setRelatedIdList(personId, list, false);
	}

	public List<Long> getGroupTagRelation_Tag (long tagId) {
		return this.groupTagEntity.dao.get().getRelatedIdList(tagId, true);
	}

	public void setGroupTagRelation_Tag (long tagId, List<Long> list) {
		this.groupTagEntity.dao.get().setRelatedIdList(tagId, list, true);
	}

	public List<Long> getGroupTagRelation_Group (long groupId) {
		return this.groupTagEntity.dao.get().getRelatedIdList(groupId, false);
	}

	public void setGroupTagRelation_Group (long groupId, List<Long> list) {
		this.groupTagEntity.dao.get().setRelatedIdList(groupId, list, false);
	}

	public List<Long> getPlaceTagRelation_Tag (long tagId) {
		return this.placeTagEntity.dao.get().getRelatedIdList(tagId, true);
	}

	public void setPlaceTagRelation_Tag (long tagId, List<Long> list) {
		this.placeTagEntity.dao.get().setRelatedIdList(tagId, list, true);
	}

	public List<Long> getPlaceTagRelation_Place (long placeId) {
		return this.placeTagEntity.dao.get().getRelatedIdList(placeId, false);
	}

	public void setPlaceTagRelation_Place (long placeId, List<Long> list) {
		this.placeTagEntity.dao.get().setRelatedIdList(placeId, list, false);
	}

	public List<Long> getSceneTagRelation_Tag (long tagId) {
		return this.sceneTagEntity.dao.get().getRelatedIdList(tagId, true);
	}

	public void setSceneTagRelation_Tag (long tagId, List<Long> list) {
		this.sceneTagEntity.dao.get().setRelatedIdList(tagId, list, true);
	}

	public List<Long> getSceneTagRelation_Scene (long sceneId) {
		return this.sceneTagEntity.dao.get().getRelatedIdList(sceneId, false);
	}

	public void setSceneTagRelation_Scene (long sceneId, List<Long> list) {
		this.sceneTagEntity.dao.get().setRelatedIdList(sceneId, list, false);
	}

	public List<Long> getChapterTagRelation_Tag (long tagId) {
		return this.chapterTagEntity.dao.get().getRelatedIdList(tagId, true);
	}

	public void setChapterTagRelation_Tag (long tagId, List<Long> list) {
		this.chapterTagEntity.dao.get().setRelatedIdList(tagId, list, true);
	}

	public List<Long> getChapterTagRelation_Chapter (long chapterId) {
		return this.chapterTagEntity.dao.get().getRelatedIdList(chapterId, false);
	}

	public void setChapterTagRelation_Chapter (long chapterId, List<Long> list) {
		this.chapterTagEntity.dao.get().setRelatedIdList(chapterId, list, false);
	}

	public List<Long> getKeywordTagRelation_Tag (long tagId) {
		return this.keywordTagEntity.dao.get().getRelatedIdList(tagId, true);
	}

	public void setKeywordTagRelation_Tag (long tagId, List<Long> list) {
		this.keywordTagEntity.dao.get().setRelatedIdList(tagId, list, true);
	}

	public List<Long> getKeywordTagRelation_Keyword (long keywordId) {
		return this.keywordTagEntity.dao.get().getRelatedIdList(keywordId, false);
	}

	public void setKeywordTagRelation_Keyword (long keywordId, List<Long> list) {
		this.keywordTagEntity.dao.get().setRelatedIdList(keywordId, list, false);
	}

	public List<Long> getTagTagRelation (long tagId) {
		return this.tagTagEntity.dao.get().getRelatedIdList(tagId);
	}

	public void setTagTagRelation (long tagId, List<Long> list) {
		this.tagTagEntity.dao.get().setRelatedIdList(tagId, list);
	}

	// -------------------------------------------------------
	// タグを持っているか
	public boolean hasTag (Entity entity, long tagId) {
		List<Long> tagIdList;
		switch (entity.getEntityType()) {
		case PERSON:
			tagIdList = this.getPersonTagRelation_Person(tagId);
			break;
		case GROUP:
			tagIdList = this.getGroupTagRelation_Group(tagId);
			break;
		case PLACE:
			tagIdList = this.getPlaceTagRelation_Place(tagId);
			break;
		case SCENE:
			tagIdList = this.getSceneTagRelation_Scene(tagId);
			break;
		case CHAPTER:
			tagIdList = this.getChapterTagRelation_Chapter(tagId);
			break;
		case KEYWORD:
			tagIdList = this.getKeywordTagRelation_Keyword(tagId);
			break;
		case TAG:
			tagIdList = this.getTagTagRelation(tagId);
			break;
		default:
			tagIdList = null;
		}
		if (tagIdList == null) {
			return false;
		}
		else {
			return tagIdList.indexOf(entity.idProperty().get()) >= 0;
		}
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

				// 何も選ばないというメッセージを送る
				this.messenger.send(new EntityListNoSelectMessage(columnFactory.createColumnList().getEntityType()));
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

	private void associationEntity (List<? extends Entity> selectedList) {
		if (selectedList != null && selectedList.size() > 0) {
			for (Entity selected : selectedList) {
				this.messenger.send(new AssociationChartShowMessage(selected));
			}
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

	public void associationPerson () {
		this.associationEntity(this.personEntity.selectedEntityList.get());
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

	public void associationGroup () {
		this.associationEntity(this.groupEntity.selectedEntityList.get());
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

	public void associationPlace () {
		this.associationEntity(this.placeEntity.selectedEntityList.get());
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

	public void associationScene () {
		this.associationEntity(this.sceneEntity.selectedEntityList.get());
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

	public void associationChapter () {
		this.associationEntity(this.chapterEntity.selectedEntityList.get());
	}

	public void sceneNovelEditorChapter () {
		if (this.chapterEntity.selectedEntityList.get().size() > 0) {
			this.messenger.send(new SceneNovelChartShowMessage(this.chapterEntity.selectedEntityList.get().get(0).idProperty().get()));
		}
	}

	public void newSex () {
		this.newEntity(new Sex(), this.sexEntity.dao.get(), SexEditorShowMessage.getInstance(), SexColumnFactory.
					   getInstance());
	}

	public void editSex () {
		this.editEntity(this.sexEntity.selectedEntityList.get(), SexEditorShowMessage.getInstance(), SexColumnFactory.
						getInstance());
	}

	public void deleteSex () {
		this.
				deleteEntity(this.sexEntity.selectedEntityList.get(), SexColumnFactory.getInstance(), this.sexEntity.dao.
							 get());
	}

	public void upSex () {
		this.upEntity(this.sexEntity.selectedEntityList.get(), this.sexEntity.dao.get());
	}

	public void downSex () {
		this.downEntity(this.sexEntity.selectedEntityList.get(), this.sexEntity.dao.get());
	}

	public void newKeyword () {
		this.newEntity(new Keyword(), this.keywordEntity.dao.get(), KeywordEditorShowMessage.getInstance(), KeywordColumnFactory.
					   getInstance());
	}

	public void editKeyword () {
		this.editEntity(this.keywordEntity.selectedEntityList.get(), KeywordEditorShowMessage.getInstance(), KeywordColumnFactory.
						getInstance());
	}

	public void deleteKeyword () {
		this.
				deleteEntity(this.keywordEntity.selectedEntityList.get(), KeywordColumnFactory.getInstance(), this.keywordEntity.dao.
							 get());
	}

	public void upKeyword () {
		this.upEntity(this.keywordEntity.selectedEntityList.get(), this.keywordEntity.dao.get());
	}

	public void downKeyword () {
		this.downEntity(this.keywordEntity.selectedEntityList.get(), this.keywordEntity.dao.get());
	}

	public void associationKeyword () {
		this.associationEntity(this.keywordEntity.selectedEntityList.get());
	}

	public void newTag () {
		this.newEntity(new Tag(), this.tagEntity.dao.get(), TagEditorShowMessage.getInstance(), TagColumnFactory.
					   getInstance());
	}

	public void editTag () {
		this.editEntity(this.tagEntity.selectedEntityList.get(), TagEditorShowMessage.getInstance(), TagColumnFactory.
						getInstance());
	}

	public void deleteTag () {
		this.
				deleteEntity(this.tagEntity.selectedEntityList.get(), TagColumnFactory.getInstance(), this.tagEntity.dao.
							 get());
	}

	public void upTag () {
		this.upEntity(this.tagEntity.selectedEntityList.get(), this.tagEntity.dao.get());
	}

	public void downTag () {
		this.downEntity(this.tagEntity.selectedEntityList.get(), this.tagEntity.dao.get());
	}

	public void associationTag () {
		this.associationEntity(this.tagEntity.selectedEntityList.get());
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

		public void resetOrder () {
			this.dao.resetOrder();
		}
	}

}
