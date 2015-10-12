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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.model.column.EditorColumnList;
import jstorybook.model.entity.Attribute;
import jstorybook.model.entity.Group;
import jstorybook.model.entity.PersonAttributeRelation;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.pane.relation.PersonAttributeColumnGroupAddMessage;

/**
 * 登場人物の関連付けを行うモデル
 *
 * @author KMY
 */
public class PersonAttributeModel implements IUseMessenger {

	private Messenger messenger = Messenger.getInstance();
	private StoryModel storyModel;
	private final ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>();
	private final List<AttributeColumn> attributeColumnList = new ArrayList<>();

	public void draw () {
		this.attributeColumnList.clear();

		CurrentStoryModelGetMessage storyModelMessage = new CurrentStoryModelGetMessage();
		this.messenger.send(storyModelMessage);
		this.storyModel = storyModelMessage.storyModelProperty().get();

		if (this.storyModel != null && this.columnList.get() != null) {

			// 集団のリストを作成
			long personId = this.columnList.get().idProperty().get();
			List<Long> groupIdList = this.storyModel.getGroupPersonRelation_Group(personId);

			// 描画
			for (long groupId : groupIdList) {

				// 集団が存在するか？
				Group group = this.storyModel.getGroupDAO().getModelById(groupId);
				if (group != null) {

					// 属性一覧を取得して、メッセージを作る
					List<Long> attributeIdList = this.storyModel.getGroupAttributeRelation_Attribute(groupId);
					PersonAttributeColumnGroupAddMessage message = new PersonAttributeColumnGroupAddMessage(group.nameProperty().get());

					for (long attributeId : attributeIdList) {
						Attribute attribute = this.storyModel.getAttributeDAO().getModelById(attributeId);
						if (attribute != null) {
							PersonAttributeRelation relation = this.storyModel.getPersonAttributeRelationDAO().getModelById(personId,
																															groupId,
																															attributeId);
							String value;
							if (relation != null) {
								value = relation.noteProperty().get();
							}
							else {
								value = "";
							}

							AttributeColumn column = new AttributeColumn(personId, groupId, attributeId);
							this.attributeColumnList.add(column);
							column.text.set(value);
							message.addColumn(attribute.nameProperty().get(), column.text, attribute.orderProperty().get());
						}
					}

					this.messenger.send(message);
				}
			}
		}
	}

	public void save () {

		if (this.storyModel != null && this.columnList.get() != null) {

			// 保存処理
			for (PersonAttributeModel.AttributeColumn column : this.attributeColumnList) {
				PersonAttributeRelation model = this.storyModel.getPersonAttributeRelationDAO().getModelById(
						column.personId, column.groupId, column.attributeId);
				if (model == null) {
					model = new PersonAttributeRelation();
					model.personIdProperty().set(column.personId);
					model.groupIdProperty().set(column.groupId);
					model.attributeIdProperty().set(column.attributeId);
					this.storyModel.getPersonAttributeRelationDAO().addModel(model);
				}
				model.noteProperty().set(column.text.get());
			}
		}
	}

	public ObjectProperty<EditorColumnList> columnListProperty () {
		return this.columnList;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

	private static class AttributeColumn {

		private final long personId;
		private final long groupId;
		private final long attributeId;
		private final StringProperty text = new SimpleStringProperty();

		public AttributeColumn (long personId, long groupId, long attributeId) {
			this.personId = personId;
			this.groupId = groupId;
			this.attributeId = attributeId;
		}

		public long getPersonId () {
			return this.personId;
		}

		public long getGroupId () {
			return this.groupId;
		}

		public long getAttributeId () {
			return this.attributeId;
		}

		public StringProperty textProperty () {
			return this.text;
		}
	}

}
