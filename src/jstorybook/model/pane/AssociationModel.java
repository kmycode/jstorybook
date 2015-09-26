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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import jstorybook.common.contract.EntityType;
import jstorybook.model.dao.DAO;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Group;
import jstorybook.model.entity.Person;
import jstorybook.model.entity.Place;
import jstorybook.model.entity.Scene;
import jstorybook.model.entity.columnfactory.GroupColumnFactory;
import jstorybook.model.entity.columnfactory.PersonColumnFactory;
import jstorybook.model.entity.columnfactory.PlaceColumnFactory;
import jstorybook.model.entity.columnfactory.SceneColumnFactory;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.ResetMessage;
import jstorybook.viewtool.messenger.pane.GroupEditorShowMessage;
import jstorybook.viewtool.messenger.pane.PersonEditorShowMessage;
import jstorybook.viewtool.messenger.pane.PlaceEditorShowMessage;
import jstorybook.viewtool.messenger.pane.SceneEditorShowMessage;
import jstorybook.viewtool.messenger.pane.chart.AssociationChartShowMessage;
import jstorybook.viewtool.messenger.pane.chart.EntityDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.EntityRelateMessage;
import jstorybook.viewtool.messenger.pane.chart.GroupDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.PersonDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.PlaceDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.SceneDrawMessage;

/**
 * エンティティ同士の関連
 *
 * @author KMY
 */
public class AssociationModel implements IUseMessenger {

	private final ObjectProperty<Entity> entity = new SimpleObjectProperty<>();
	private Messenger messenger = Messenger.getInstance();
	private final BooleanProperty canDraw = new SimpleBooleanProperty(false);

	// 画面に描画されたIDとエンティティの関連付け
	private final Map<Long, Integer> personIdList = new HashMap<>();
	private final Map<Long, Integer> groupIdList = new HashMap<>();
	private final Map<Long, Integer> placeIdList = new HashMap<>();

	private StoryModel storyModel;

	public AssociationModel () {
		this.entity.addListener((obj) -> {
			this.checkCanDraw();
			if (this.canDraw.get()) {
				this.draw();
			}
		});
	}

	public void draw () {
		this.messenger.send(new ResetMessage());
		this.personIdList.clear();
		this.groupIdList.clear();
		this.placeIdList.clear();

		Entity entity = this.entity.get();
		long entityId = entity.idProperty().get();

		// ストーリーモデルを取得
		CurrentStoryModelGetMessage getMessage = new CurrentStoryModelGetMessage();
		this.messenger.send(getMessage);
		StoryModel storyModel = getMessage.storyModelProperty().get();
		this.storyModel = storyModel;
		if (storyModel != null) {

			// 画面の中心に描画するもの
			EntityDrawMessage message = null;
			if (entity.getEntityType() == EntityType.SCENE) {
				message = new SceneDrawMessage(entity.titleProperty().get(), (ev) -> this.messenger.send(new SceneEditorShowMessage(
											   SceneColumnFactory.getInstance().createColumnList((Scene) entity.entityClone()),
											   SceneColumnFactory.getInstance().createColumnList((Scene) entity))));
			}
			else if (entity.getEntityType() == EntityType.GROUP) {
				message = new GroupDrawMessage(entity.titleProperty().get(), (ev) -> this.messenger.send(new GroupEditorShowMessage(
											   GroupColumnFactory.getInstance().createColumnList((Group) entity.entityClone()),
											   GroupColumnFactory.getInstance().createColumnList((Group) entity))));
			}
			else if (entity.getEntityType() == EntityType.PERSON) {
				message = new PersonDrawMessage(entity.titleProperty().get(), (ev) -> this.messenger.send(new PersonEditorShowMessage(
												PersonColumnFactory.getInstance().createColumnList((Person) entity.entityClone()),
												PersonColumnFactory.getInstance().createColumnList((Person) entity))));
			}
			else if (entity.getEntityType() == EntityType.PLACE) {
				message = new PlaceDrawMessage(entity.titleProperty().get(), (ev) -> this.messenger.send(new PlaceEditorShowMessage(
											   PlaceColumnFactory.getInstance().createColumnList((Place) entity.entityClone()),
											   PlaceColumnFactory.getInstance().createColumnList((Place) entity))));
			}
			this.messenger.send(message);

			// まわりに描画するもの
			if (entity.getEntityType() == EntityType.SCENE) {
				this.drawArea(EntityType.SCENE, entityId, EntityType.SCENE, entityId, -1, EntityType.PERSON, EntityType.GROUP);
				this.drawArea(EntityType.SCENE, entityId, EntityType.SCENE, entityId, -1, EntityType.PLACE);
			}
			else if (entity.getEntityType() == EntityType.GROUP) {
				this.drawArea(EntityType.GROUP, entityId, EntityType.GROUP, entityId, -1, EntityType.PERSON, EntityType.SCENE);
			}
			else if (entity.getEntityType() == EntityType.PERSON) {
				this.drawArea(EntityType.PERSON, entityId, EntityType.PERSON, entityId, -1, EntityType.GROUP, EntityType.PERSON);
				this.drawArea(EntityType.PERSON, entityId, EntityType.PERSON, entityId, -1, EntityType.PERSON);
			}
			else if (entity.getEntityType() == EntityType.PLACE) {
				this.drawArea(EntityType.PLACE, entityId, EntityType.PLACE, entityId, -1, EntityType.SCENE, EntityType.PERSON);
			}
		}
	}

	private void drawArea (EntityType rootType, long rootEntityId, EntityType from, long fromId, int fromDrawId, EntityType to,
						   EntityType... wrapTypeArgs) {

		List<Long> list = null;
		Map<Long, Integer> map = null;
		DAO dao = null;
		if (from == EntityType.SCENE && to == EntityType.PERSON) {
			list = this.storyModel.getScenePersonRelation_Person(fromId);
		}
		else if (from == EntityType.PERSON && to == EntityType.GROUP) {
			list = storyModel.getGroupPersonRelation_Group(fromId);
		}
		else if (from == EntityType.SCENE && to == EntityType.PLACE) {
			list = storyModel.getScenePlaceRelation_Place(fromId);
		}
		else if (from == EntityType.GROUP && to == EntityType.PERSON) {
			list = storyModel.getGroupPersonRelation_Person(fromId);
		}
		else if (from == EntityType.PERSON && to == EntityType.SCENE) {
			list = storyModel.getScenePersonRelation_Scene(fromId);
		}
		else if (from == EntityType.PERSON && to == EntityType.PERSON) {
			list = storyModel.getPersonPersonRelation(fromId);
		}
		else if (from == EntityType.PLACE && to == EntityType.SCENE) {
			list = storyModel.getScenePlaceRelation_Scene(fromId);
		}
		if (to == EntityType.GROUP) {
			map = this.groupIdList;
			dao = this.storyModel.getGroupDAO();
		}
		else if (to == EntityType.PERSON) {
			map = this.personIdList;
			dao = this.storyModel.getPersonDAO();
		}
		else if (to == EntityType.PLACE) {
			map = this.placeIdList;
			dao = this.storyModel.getPlaceDAO();
		}
		else if (to == EntityType.SCENE) {
			map = this.placeIdList;
			dao = this.storyModel.getSceneDAO();
		}
		if (list != null && map != null) {

			// 指定したエンティティに関連するエンティティのリストを反復
			for (long toId : list) {

				if (rootType == to && rootEntityId == toId) {
					continue;
				}

				Integer mapId = map.get(toId);
				if (mapId != null && mapId < 0) {
					mapId = null;
				}

				// 新しくエンティティを描画する場合
				if (mapId == null) {
					Entity toEntity = dao.getModelById(toId);
					if (toEntity != null) {

						// 描画メッセージを作成
						EntityDrawMessage ddMessage = null;
						EventHandler ev = (obj) -> {
							this.messenger.send(new AssociationChartShowMessage(toEntity));
						};
						if (to == EntityType.GROUP) {
							ddMessage = new GroupDrawMessage(toEntity.titleProperty().get(), ev);
						}
						else if (to == EntityType.PERSON) {
							ddMessage = new PersonDrawMessage(toEntity.titleProperty().get(), ev);
						}
						else if (to == EntityType.PLACE) {
							ddMessage = new PlaceDrawMessage(toEntity.titleProperty().get(), ev);
						}
						else if (to == EntityType.SCENE) {
							ddMessage = new SceneDrawMessage(toEntity.titleProperty().get(), ev);
						}
						if (ddMessage != null) {
							if (fromDrawId >= 0) {
								ddMessage.setReleatedId(fromDrawId);
							}
							this.messenger.send(ddMessage);
							mapId = ddMessage.getDrawId();
							map.put(toId, mapId);
						}
					}
				}

				// そのエンティティがすでに描画されていた場合
				else {
					if (fromDrawId >= 0) {
						this.messenger.send(new EntityRelateMessage(mapId, fromDrawId));
					}
				}

				// 別の種類のエンティティを描画する場合
				if (wrapTypeArgs != null && mapId != null) {
					for (EntityType otherType : wrapTypeArgs) {
						if (otherType != null) {
							this.drawArea(rootType, rootEntityId, to, toId, mapId, otherType);
						}
					}
				}
			}
		}
	}

	public void checkCanDraw () {
		this.canDraw.set(this.entity.get() != null);
	}

	public ObjectProperty<Entity> entityProperty () {
		return this.entity;
	}

	public BooleanProperty canDrawProperty () {
		return this.canDraw;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
