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
package jstorybook.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jstorybook.model.story.StoryApplicationModel;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.Messenger;

/**
 * メインウィンドウのビューモデル
 *
 * @author KMY
 */
public class StoryViewModel extends ViewModel {

	private final ObjectProperty<StoryModel> storyModel = new SimpleObjectProperty<>(new StoryModel());
	private final StoryApplicationModel applicationModel = new StoryApplicationModel();

	@Override
	protected void storeProperty () {
		this.applyProperty("storyModel", this.storyModel);

		this.applyProperty("storyName", this.storyModel.get().getCore().storyNameProperty());
		this.applyProperty("storyFileName", this.storyModel.get().fileNameProperty());
		this.applyProperty("authorName", this.storyModel.get().getCore().authorProperty());

		this.applyProperty("personColumnList", this.storyModel.get().entityColumnProperty().get().
						   personColumnListProperty());
		this.applyProperty("personList", this.storyModel.get().getPersonEntity().DAOProperty().get().
							  modelListProperty());
		this.applyProperty("personSelected", this.storyModel.get().getPersonEntity().selectedEntityProperty());

		this.applyProperty("groupColumnList", this.storyModel.get().entityColumnProperty().get().
						   groupColumnListProperty());
		this.applyProperty("groupList", this.storyModel.get().getGroupEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("groupSelected", this.storyModel.get().getGroupEntity().selectedEntityProperty());

		this.applyProperty("placeColumnList", this.storyModel.get().entityColumnProperty().get().
						   placeColumnListProperty());
		this.applyProperty("placeList", this.storyModel.get().getPlaceEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("placeSelected", this.storyModel.get().getPlaceEntity().selectedEntityProperty());

		this.applyProperty("sceneColumnList", this.storyModel.get().entityColumnProperty().get().
						   sceneColumnListProperty());
		this.applyProperty("sceneList", this.storyModel.get().getSceneEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("sceneSelected", this.storyModel.get().getSceneEntity().selectedEntityProperty());

		this.applyProperty("chapterColumnList", this.storyModel.get().entityColumnProperty().get().
						   chapterColumnListProperty());
		this.applyProperty("chapterList", this.storyModel.get().getChapterEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("chapterSelected", this.storyModel.get().getChapterEntity().selectedEntityProperty());
	}

	@Override
	public void storeMessenger (Messenger messenger) {
		this.storyModel.get().setMessenger(messenger);
		this.applicationModel.setMessenger(messenger);
	}

	@Override
	protected void storeCommand () {
		this.applyCommand("showPersonList", (ev) -> this.applicationModel.showPersonList(), this.storyModel.get().canEditProperty());
		this.applyCommand("personNew", (ev) -> this.storyModel.get().newPerson(), this.storyModel.get().canEditProperty());
		this.applyCommand("personEdit", (ev) -> this.storyModel.get().editPerson(), this.storyModel.get().canEditProperty());
		this.applyCommand("personDelete", (ev) -> this.storyModel.get().deletePerson(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("personUp", (ev) -> this.storyModel.get().upPerson(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("personDown", (ev) -> this.storyModel.get().downPerson(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showGroupList", (ev) -> this.applicationModel.showGroupList(), this.storyModel.get().canEditProperty());
		this.applyCommand("groupNew", (ev) -> this.storyModel.get().newGroup(), this.storyModel.get().canEditProperty());
		this.applyCommand("groupEdit", (ev) -> this.storyModel.get().editGroup(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("groupDelete", (ev) -> this.storyModel.get().deleteGroup(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("groupUp", (ev) -> this.storyModel.get().upGroup(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("groupDown", (ev) -> this.storyModel.get().downGroup(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showPlaceList", (ev) -> this.applicationModel.showPlaceList(), this.storyModel.get().canEditProperty());
		this.applyCommand("placeNew", (ev) -> this.storyModel.get().newPlace(), this.storyModel.get().canEditProperty());
		this.applyCommand("placeEdit", (ev) -> this.storyModel.get().editPlace(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("placeDelete", (ev) -> this.storyModel.get().deletePlace(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("placeUp", (ev) -> this.storyModel.get().upPlace(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("placeDown", (ev) -> this.storyModel.get().downPlace(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showSceneList", (ev) -> this.applicationModel.showSceneList(), this.storyModel.get().canEditProperty());
		this.applyCommand("sceneNew", (ev) -> this.storyModel.get().newScene(), this.storyModel.get().canEditProperty());
		this.applyCommand("sceneEdit", (ev) -> this.storyModel.get().editScene(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("sceneDelete", (ev) -> this.storyModel.get().deleteScene(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("sceneUp", (ev) -> this.storyModel.get().upScene(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("sceneDown", (ev) -> this.storyModel.get().downScene(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("sceneAssociation", (ev) -> this.storyModel.get().associationScene(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showChapterList", (ev) -> this.applicationModel.showChapterList(), this.storyModel.get().canEditProperty());
		this.applyCommand("chapterNew", (ev) -> this.storyModel.get().newChapter(), this.storyModel.get().canEditProperty());
		this.applyCommand("chapterEdit", (ev) -> this.storyModel.get().editChapter(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("chapterDelete", (ev) -> this.storyModel.get().deleteChapter(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("chapterUp", (ev) -> this.storyModel.get().upChapter(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("chapterDown", (ev) -> this.storyModel.get().downChapter(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("save", (ev) -> this.storyModel.get().save(), this.storyModel.get().canSaveProperty());
		this.applyCommand("close", (ev) -> this.storyModel.get().close(), this.storyModel.get().canEditProperty());
		this.applyCommand("storySetting", (ev) -> this.applicationModel.showStorySetting(), this.storyModel.get().canEditProperty());
	}

}
