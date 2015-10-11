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

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jstorybook.model.entity.Keyword;
import jstorybook.model.entity.Person;
import jstorybook.model.entity.Place;
import jstorybook.model.entity.Scene;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.pane.chart.KeywordDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.PersonDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.PlaceDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.SceneNovelBoxCreateMessage;

/**
 * シーン一括編集画面のモデル
 *
 * @author KMY
 */
public class SceneNovelModel implements IUseMessenger {

	private Messenger messenger;
	private StoryModel storyModel;

	private final LongProperty chapterId = new SimpleLongProperty();

	public void draw () {
		// ストーリーモデルを取得
		CurrentStoryModelGetMessage getMessage = new CurrentStoryModelGetMessage();
		this.messenger.send(getMessage);
		this.storyModel = getMessage.storyModelProperty().get();

		// シーンの一覧を取得
		List<Long> sceneIdList = this.storyModel.getChapterSceneRelation_Scene(this.chapterId.get());

		// 描画処理
		if (sceneIdList != null) {

			// シーンのリストを作成（ならべ替え用）
			List<Scene> sceneList = FXCollections.observableArrayList();
			for (long sceneId : sceneIdList) {
				Scene scene = this.storyModel.getSceneDAO().getModelById(sceneId);
				if (scene != null) {
					sceneList.add(scene);
				}
			}
			FXCollections.sort((ObservableList) sceneList);

			for (Scene scene : sceneList) {

				long sceneId = scene.idProperty().get();

				// 関連エンティティを取得
				List<Long> personIdList = this.storyModel.getScenePersonRelation_Person(sceneId);
				List<Long> placeIdList = this.storyModel.getScenePlaceRelation_Place(sceneId);
				List<Long> keywordIdList = this.storyModel.getSceneKeywordRelation_Keyword(sceneId);

				// シーンの箱を作る
				Calendar startTime = scene.starttimeProperty().get();
				if (startTime != null) {
					startTime = (Calendar) startTime.clone();
					startTime.add(Calendar.MILLISECOND, TimeZone.getDefault().getRawOffset() * -1);
				}
				Calendar endTime = scene.endtimeProperty().get();
				if (endTime != null) {
					endTime = (Calendar) endTime.clone();
					endTime.add(Calendar.MILLISECOND, TimeZone.getDefault().getRawOffset() * -1);
				}
				SceneNovelBoxCreateMessage message = new SceneNovelBoxCreateMessage(scene.nameProperty(), scene.textProperty(),
																					startTime, endTime);
				for (long personId : personIdList) {
					Person person = this.storyModel.getPersonDAO().getModelById(personId);
					if (person != null) {
						message.getPersonList().add(new PersonDrawMessage(person.titleProperty().get()));
					}
				}
				for (long placeId : placeIdList) {
					Place place = this.storyModel.getPlaceDAO().getModelById(placeId);
					if (place != null) {
						message.getPlaceList().add(new PlaceDrawMessage(place.nameProperty().get()));
					}
				}
				for (long keywordId : keywordIdList) {
					Keyword keyword = this.storyModel.getKeywordDAO().getModelById(keywordId);
					if (keyword != null) {
						message.getKeywordList().add(new KeywordDrawMessage(keyword.nameProperty().get()));
					}
				}

				// メッセージを送信
				this.messenger.send(message);
			}
		}
	}

	public LongProperty chapterIdProperty () {
		return this.chapterId;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
