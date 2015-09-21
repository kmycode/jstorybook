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
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.contract.DialogResult;
import jstorybook.model.dao.DAO;
import jstorybook.model.dao.PersonDAO;
import jstorybook.model.dao.PersonPersonRelationDAO;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Person;
import jstorybook.model.entity.PersonPersonRelation;
import jstorybook.model.entity.columnfactory.PersonColumnFactory;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.exception.StoryFileLoadFailedMessage;
import jstorybook.viewtool.messenger.exception.StoryFileSaveFailedMessage;
import jstorybook.viewtool.messenger.general.DeleteDialogMessage;
import jstorybook.viewtool.messenger.pane.EntityEditorCloseMessage;
import jstorybook.viewtool.messenger.pane.PersonEditorShowMessage;

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
	private final StoryEntityModel<Person, PersonDAO> personEntity = new StoryEntityModel<>(new PersonDAO());
	private final StoryEntityModel<PersonPersonRelation, PersonPersonRelationDAO> personPersonEntity = new StoryEntityModel<>(
			new PersonPersonRelationDAO());

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
		this.personEntity.dao.get().setStoryFileModel(this.storyFile.get());
		this.personPersonEntity.dao.get().setStoryFileModel(this.storyFile.get());

		this.personPersonEntity.dao.get().readPersonDAO(this.personEntity.dao.get());

		this.canSave.set(true);
	}

	// ストーリーモデル全体のファイルへの保存
	public void save () {
		try {
			this.personEntity.dao.get().saveList();
			this.personPersonEntity.dao.get().saveList();
		} catch (SQLException e) {
			this.messenger.send(new StoryFileSaveFailedMessage(this.storyFileName.get()));
			e.printStackTrace();
		}
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

	// -------------------------------------------------------
	// エンティティ同士の関係
	public List<Long> getPersonPersonRelation (long personId) {
		return this.personPersonEntity.dao.get().getRelatedIdList(personId);
	}

	public void setPersonPersonRelation (long personId, List<Long> list) {
		this.personPersonEntity.dao.get().setRelatedIdList(personId, list);
	}

	// -------------------------------------------------------
	// それぞれのエンティティの新規作成、編集、削除など
	public void newPerson () {
		Person newModel = new Person();
		EntityAdapter adapter = new EntityAdapter(newModel, this.personEntity.dao.get());
		this.messenger.send(
				new PersonEditorShowMessage(PersonColumnFactory.getInstance().createColumnList(),
											PersonColumnFactory.getInstance().createColumnList(newModel, adapter)));
	}

	public void editPerson () {
		Person selected = this.personEntity.selectedEntity.get();
		if (selected != null) {
			this.messenger.send(
					new PersonEditorShowMessage(PersonColumnFactory.getInstance().createColumnList(selected.entityClone()),
												PersonColumnFactory.getInstance().createColumnList(selected)));
		}
	}

	public void deletePerson () {
		Person selected = this.personEntity.selectedEntity.get();
		if (selected != null) {
			DeleteDialogMessage delmes = new DeleteDialogMessage(selected.titleProperty().get());
			this.messenger.send(delmes);
			if (delmes.getResult() == DialogResult.YES) {
				this.messenger.send(new EntityEditorCloseMessage(PersonColumnFactory.getInstance().createColumnList(selected)));
				this.personEntity.dao.get().deleteModel(selected);
			}
		}
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
		private final ObjectProperty<E> selectedEntity = new SimpleObjectProperty<>();
		private final BooleanProperty canEdit = new SimpleBooleanProperty(false);

		private StoryEntityModel (D dao) {
			this.dao.set(dao);

			// 選択中のエンティティが変更された場合
			this.selectedEntity.addListener((obj) -> {
				this.canEdit.set(((ObjectProperty<Entity>) obj).get() != null);
			});
		}

		public ObjectProperty<D> DAOProperty () {
			return this.dao;
		}

		// TableViewなどで選択されたエンティティ
		public ObjectProperty<E> selectedEntityProperty () {
			return this.selectedEntity;
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
