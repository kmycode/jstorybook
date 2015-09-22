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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.contract.EntityRelation;
import jstorybook.common.contract.EntityType;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.CloseMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnColorMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnDateMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnSexMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnTextMessage;
import jstorybook.viewtool.messenger.pane.editor.PropertyNoteSetMessage;
import jstorybook.viewtool.messenger.pane.relation.GroupRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.GroupRelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.GroupRelationShowMessage;
import jstorybook.viewtool.messenger.pane.relation.PersonRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.PersonRelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.PersonRelationShowMessage;
import jstorybook.viewtool.messenger.pane.relation.PlaceRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.RelationRenewTriggerMessage;
import jstorybook.viewtool.messenger.pane.relation.SceneRelationListGetMessage;
import jstorybook.viewtool.model.EditorColumn;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * エンティティ編集に利用するモデル
 *
 * @author KMY
 */
public class EntityEditModel implements IUseMessenger {

	private ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>();
	private ObjectProperty<EditorColumnList> baseColumnList = new SimpleObjectProperty<>();

	private BooleanProperty isChanged = new SimpleBooleanProperty(false);
	private BooleanProperty canSave = new SimpleBooleanProperty(false);
	private BooleanProperty canSaveByOut = new SimpleBooleanProperty(false);		// 外部から設定された変更

	private Messenger messenger = Messenger.getInstance();

	public EntityEditModel () {
		this.columnList.addListener((obj) -> {
			EntityEditModel.this.generateEditor();
			EntityEditModel.this.checkCanSave();
		});
		this.baseColumnList.addListener((obj) -> {
			EntityEditModel.this.checkCanSave();
		});
		this.isChanged.addListener((obj) -> {
			EntityEditModel.this.checkCanSave();
		});

		this.canSaveByOut.addListener((obj) -> {
			if (this.canSaveByOut.get()) {
				this.isChanged.set(true);
			}
		});
	}

	public ObjectProperty<EditorColumnList> columnListProperty () {
		return this.columnList;
	}

	public ObjectProperty<EditorColumnList> baseColumnListProperty () {
		return this.baseColumnList;
	}

	public BooleanProperty canSaveByOutProperty () {
		return this.canSaveByOut;
	}

	public StringProperty titleProperty () {
		if (this.columnList.get() != null) {
			return this.columnList.get().titleProperty();
		}
		else {
			return new SimpleStringProperty();
		}
	}

	public StringProperty noteProperty () {
		if (this.columnList.get() != null) {
			return this.columnList.get().noteProperty();
		}
		else {
			return new SimpleStringProperty();
		}
	}

	private void generateEditor () {
		EditorColumnList list = this.columnList.get();
		if (list == null) {
			return;
		}

		// 編集項目
		for (EditorColumn column : list) {
			if (column.getColumnType() == EditorColumn.ColumnType.TEXT) {
				this.messenger.send(new EditorColumnTextMessage(column.getColumnName(), column.getProperty()));
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.DATE) {
				this.messenger.send(new EditorColumnDateMessage(column.getColumnName(), column.getProperty()));
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.COLOR) {
				this.messenger.send(new EditorColumnColorMessage(column.getColumnName(), column.getProperty()));
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.SEX) {
				this.messenger.send(new EditorColumnSexMessage(column.getColumnName(), column.getProperty()));
			}

			// エディタで編集した時のイベント
			column.getProperty().addListener((obj) -> {
				EntityEditModel.this.isChanged.set(true);
			});
		}

		// ノートを設定
		this.messenger.send(new PropertyNoteSetMessage(this.noteProperty()));
		this.noteProperty().addListener((obj) -> {
			EntityEditModel.this.isChanged.set(true);
		});

		// ストーリーモデルを取得
		StoryModel storyModel = this.getStoryModel();

		if (storyModel != null) {
			// 関連エンティティを選択するためのタブを設定
			for (EntityRelation relation : list.getEntityRelationList()) {
				if (relation == EntityRelation.PERSON_PERSON) {
					this.messenger.send(new PersonRelationShowMessage(storyModel.getPersonPersonRelation(
							this.columnList.get().idProperty().get())));
				}
				else if (relation == EntityRelation.GROUP_PERSON) {
					if (list.getEntityType() == EntityType.GROUP) {
						this.messenger.send(new PersonRelationShowMessage(storyModel.getGroupPersonRelation_Person(
								this.columnList.get().idProperty().get())));
					}
					else if (list.getEntityType() == EntityType.PERSON) {
						this.messenger.send(new GroupRelationShowMessage(storyModel.getGroupPersonRelation_Group(
								this.columnList.get().idProperty().get())));
					}
				}
			}
		}
	}

	private StoryModel getStoryModel () {
		CurrentStoryModelGetMessage storyModelMessage = new CurrentStoryModelGetMessage();
		this.messenger.send(storyModelMessage);
		return storyModelMessage.storyModelProperty().get();
	}

	// -------------------------------------------------------
	// コマンド

	public void save () {
		this.apply();
		this.messenger.send(new CloseMessage());
		this.isChanged.set(false);
	}

	private void checkCanSave () {
		EntityEditModel.this.canSave.set(this.columnList.get() != null && this.baseColumnList.get() != null
				&& this.isChanged.get());
	}

	public BooleanProperty canSaveProperty () {
		return this.canSave;
	}

	public void cancel () {
		this.messenger.send(new CloseMessage());
	}

	public void apply () {
		// エンティティ同士の関連を保存
		StoryModel storyModel = this.getStoryModel();
		EntityType entityType = this.columnList.get().getEntityType();
		PersonRelationListGetMessage personRelationListMessage = new PersonRelationListGetMessage();
		GroupRelationListGetMessage groupRelationListMessage = new GroupRelationListGetMessage();
		PlaceRelationListGetMessage placeRelationListMessage = new PlaceRelationListGetMessage();
		SceneRelationListGetMessage sceneRelationListMessage = new SceneRelationListGetMessage();
		this.messenger.send(personRelationListMessage);
		this.messenger.send(groupRelationListMessage);
		this.messenger.send(placeRelationListMessage);
		this.messenger.send(sceneRelationListMessage);
		if (personRelationListMessage.getRelationList() != null) {
			if (entityType == EntityType.PERSON) {
				storyModel.setPersonPersonRelation(this.columnList.get().idProperty().get(), personRelationListMessage.
												   getRelationList());
			}
			else if (entityType == EntityType.GROUP) {
				storyModel.
						setGroupPersonRelation_Person(this.columnList.get().idProperty().get(), personRelationListMessage.
													  getRelationList());
			}
		}
		if (groupRelationListMessage.getRelationList() != null) {
			if (entityType == EntityType.PERSON) {
				storyModel.
						setGroupPersonRelation_Group(this.columnList.get().idProperty().get(), groupRelationListMessage.
													 getRelationList());
			}
		}
		if (placeRelationListMessage.getRelationList() != null) {
		}
		if (sceneRelationListMessage.getRelationList() != null) {
		}

		// エンティティそのものの値をコピー
		this.baseColumnList.get().copyProperty(this.columnList.get());
		this.isChanged.set(false);

		// 他の編集画面の関連付け設定も全部変えに行く
		this.messenger.send(new RelationRenewTriggerMessage());
	}

	public void relationListRenew () {
		StoryModel storyModel = this.getStoryModel();

		if (storyModel != null) {
			if (this.columnList.get().isRelation(EntityRelation.PERSON_PERSON)) {
				this.messenger.send(new PersonRelationRenewMessage(storyModel.getPersonPersonRelation(
						this.columnList.get().idProperty().get())));
			}
			if (this.columnList.get().isRelation(EntityRelation.GROUP_PERSON)) {
				if (this.columnList.get().getEntityType() == EntityType.GROUP) {
					this.messenger.send(new PersonRelationRenewMessage(storyModel.getGroupPersonRelation_Person(
							this.columnList.get().idProperty().get())));
				}
				else if (this.columnList.get().getEntityType() == EntityType.PERSON) {
					this.messenger.send(new GroupRelationRenewMessage(storyModel.getGroupPersonRelation_Group(
							this.columnList.get().idProperty().get())));
				}
			}
		}
	}

	// -------------------------------------------------------
	// IUseMessenger

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
