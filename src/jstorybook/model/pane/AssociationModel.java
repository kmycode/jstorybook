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
import jstorybook.common.contract.EntityType;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Group;
import jstorybook.model.entity.Person;
import jstorybook.model.entity.Place;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
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

	public AssociationModel () {
		this.entity.addListener((obj) -> {
			this.checkCanDraw();
			if (this.canDraw.get()) {
				this.draw();
			}
		});
	}

	public void draw () {
		Entity entity = this.entity.get();
		long entityId = entity.idProperty().get();

		// ストーリーモデルを取得
		CurrentStoryModelGetMessage getMessage = new CurrentStoryModelGetMessage();
		this.messenger.send(getMessage);
		StoryModel storyModel = getMessage.storyModelProperty().get();
		if (storyModel != null) {

			// 画面の中心に描画するもの
			EntityDrawMessage message = null;
			if (entity.getEntityType() == EntityType.SCENE) {
				message = new SceneDrawMessage(entity.titleProperty().get(), null);
			}
			this.messenger.send(message);

			// まわりに描画するもの
			if (entity.getEntityType() == EntityType.SCENE) {
				List<Long> personList = storyModel.getScenePersonRelation_Person(entityId);
				for (long personId : personList) {
					Person model = storyModel.getPersonDAO().getModelById(personId);
					if (model != null) {
						EntityDrawMessage drawMessage = new PersonDrawMessage(model.titleProperty().get(), null);
						this.messenger.send(drawMessage);
						this.personIdList.put(personId, drawMessage.getDrawId());

						List<Long> groupList = storyModel.getGroupPersonRelation_Group(personId);
						for (long groupId : groupList) {
							Integer groupIdPut = this.groupIdList.get(groupId);
							if (groupIdPut == null) {
								Group gmodel = storyModel.getGroupDAO().getModelById(groupId);
								if (gmodel != null) {
									EntityDrawMessage ddMessage = new GroupDrawMessage(gmodel.titleProperty().get(), null);
									ddMessage.setReleatedId(drawMessage.getDrawId());
									this.messenger.send(ddMessage);
									this.groupIdList.put(groupId, ddMessage.getDrawId());
								}
							}
							else {
								this.messenger.send(new EntityRelateMessage(groupIdPut, drawMessage.getDrawId()));
							}
						}
					}
				}
				List<Long> placeList = storyModel.getScenePlaceRelation_Place(entityId);
				for (long placeId : placeList) {
					Place model = storyModel.getPlaceDAO().getModelById(placeId);
					if (model != null) {
						EntityDrawMessage drawMessage = new PlaceDrawMessage(model.titleProperty().get(), null);
						this.messenger.send(drawMessage);
						this.placeIdList.put(placeId, drawMessage.getDrawId());
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
