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
import javafx.beans.property.SimpleBooleanProperty;
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

		this.applyProperty("eventColumnList", this.storyModel.get().entityColumnProperty().get().
						   eventColumnListProperty());
		this.applyProperty("eventList", this.storyModel.get().getEventEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("eventSelected", this.storyModel.get().getEventEntity().selectedEntityProperty());

		this.applyProperty("chapterColumnList", this.storyModel.get().entityColumnProperty().get().
						   chapterColumnListProperty());
		this.applyProperty("chapterList", this.storyModel.get().getChapterEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("chapterSelected", this.storyModel.get().getChapterEntity().selectedEntityProperty());

		this.applyProperty("partColumnList", this.storyModel.get().entityColumnProperty().get().
						   partColumnListProperty());
		this.applyProperty("partList", this.storyModel.get().getPartEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("partSelected", this.storyModel.get().getPartEntity().selectedEntityProperty());

		this.applyProperty("storylineColumnList", this.storyModel.get().entityColumnProperty().get().
						   storylineColumnListProperty());
		this.applyProperty("storylineList", this.storyModel.get().getStorylineEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("storylineSelected", this.storyModel.get().getStorylineEntity().selectedEntityProperty());

		this.applyProperty("sexColumnList", this.storyModel.get().entityColumnProperty().get().
						   sexColumnListProperty());
		this.applyProperty("sexList", this.storyModel.get().getSexEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("sexSelected", this.storyModel.get().getSexEntity().selectedEntityProperty());

		this.applyProperty("attributeColumnList", this.storyModel.get().entityColumnProperty().get().
						   attributeColumnListProperty());
		this.applyProperty("attributeList", this.storyModel.get().getAttributeEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("attributeSelected", this.storyModel.get().getAttributeEntity().selectedEntityProperty());

		this.applyProperty("keywordColumnList", this.storyModel.get().entityColumnProperty().get().
						   keywordColumnListProperty());
		this.applyProperty("keywordList", this.storyModel.get().getKeywordEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("keywordSelected", this.storyModel.get().getKeywordEntity().selectedEntityProperty());

		this.applyProperty("tagColumnList", this.storyModel.get().entityColumnProperty().get().
						   tagColumnListProperty());
		this.applyProperty("tagList", this.storyModel.get().getTagEntity().DAOProperty().get().
						   modelListProperty());
		this.applyProperty("tagSelected", this.storyModel.get().getTagEntity().selectedEntityProperty());
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
		this.applyCommand("personEdit", (ev) -> this.storyModel.get().editPerson(), this.storyModel.get().getPersonEntity().
						  canEditProperty());
		this.applyCommand("personDelete", (ev) -> this.storyModel.get().deletePerson(), this.storyModel.get().getPersonEntity().
						  canEditProperty());
		this.applyCommand("personUp", (ev) -> this.storyModel.get().upPerson(), this.storyModel.get().getPersonEntity().
						  canEditProperty());
		this.applyCommand("personDown", (ev) -> this.storyModel.get().downPerson(), this.storyModel.get().getPersonEntity().
						  canEditProperty());
		this.applyCommand("personAssociation", (ev) -> this.storyModel.get().associationPerson(), this.storyModel.get().
						  getPersonEntity().canEditProperty());
		this.applyCommand("personOrderReset", (ev) -> this.storyModel.get().getPersonDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showGroupList", (ev) -> this.applicationModel.showGroupList(), this.storyModel.get().canEditProperty());
		this.applyCommand("groupNew", (ev) -> this.storyModel.get().newGroup(), this.storyModel.get().canEditProperty());
		this.applyCommand("groupEdit", (ev) -> this.storyModel.get().editGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());
		this.applyCommand("groupDelete", (ev) -> this.storyModel.get().deleteGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());
		this.applyCommand("groupUp", (ev) -> this.storyModel.get().upGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());
		this.applyCommand("groupDown", (ev) -> this.storyModel.get().downGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());
		this.applyCommand("groupAssociation", (ev) -> this.storyModel.get().associationGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());
		this.applyCommand("groupOrderReset", (ev) -> this.storyModel.get().getGroupDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showPlaceList", (ev) -> this.applicationModel.showPlaceList(), this.storyModel.get().canEditProperty());
		this.applyCommand("placeNew", (ev) -> this.storyModel.get().newPlace(), this.storyModel.get().canEditProperty());
		this.applyCommand("placeEdit", (ev) -> this.storyModel.get().editPlace(), this.storyModel.get().getPlaceEntity().
						  canEditProperty());
		this.applyCommand("placeDelete", (ev) -> this.storyModel.get().deletePlace(), this.storyModel.get().getPlaceEntity().
						  canEditProperty());
		this.applyCommand("placeUp", (ev) -> this.storyModel.get().upPlace(), this.storyModel.get().getPlaceEntity().
						  canEditProperty());
		this.applyCommand("placeDown", (ev) -> this.storyModel.get().downPlace(), this.storyModel.get().getPlaceEntity().
						  canEditProperty());
		this.applyCommand("placeAssociation", (ev) -> this.storyModel.get().associationPlace(), this.storyModel.get().getPlaceEntity().
						  canEditProperty());
		this.applyCommand("placeOrderReset", (ev) -> this.storyModel.get().getPlaceDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showEventList", (ev) -> this.applicationModel.showEventList(), this.storyModel.get().canEditProperty());
		this.applyCommand("eventNew", (ev) -> this.storyModel.get().newEvent(), this.storyModel.get().canEditProperty());
		this.applyCommand("eventEdit", (ev) -> this.storyModel.get().editEvent(), this.storyModel.get().getEventEntity().
						  canEditProperty());
		this.applyCommand("eventDelete", (ev) -> this.storyModel.get().deleteEvent(), this.storyModel.get().getEventEntity().
						  canEditProperty());
		this.applyCommand("eventUp", (ev) -> this.storyModel.get().upEvent(), this.storyModel.get().getEventEntity().
						  canEditProperty());
		this.applyCommand("eventDown", (ev) -> this.storyModel.get().downEvent(), this.storyModel.get().getEventEntity().
						  canEditProperty());
		this.applyCommand("eventAssociation", (ev) -> this.storyModel.get().associationEvent(), this.storyModel.get().getEventEntity().
						  canEditProperty());
		this.applyCommand("eventOrderReset", (ev) -> this.storyModel.get().getEventDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showSceneList", (ev) -> this.applicationModel.showSceneList(), this.storyModel.get().canEditProperty());
		this.applyCommand("sceneNew", (ev) -> this.storyModel.get().newScene(), this.storyModel.get().canEditProperty());
		this.applyCommand("sceneEdit", (ev) -> this.storyModel.get().editScene(), this.storyModel.get().getSceneEntity().
						  canEditProperty());
		this.applyCommand("sceneDelete", (ev) -> this.storyModel.get().deleteScene(), this.storyModel.get().getSceneEntity().
						  canEditProperty());
		this.applyCommand("sceneUp", (ev) -> this.storyModel.get().upScene(), this.storyModel.get().getSceneEntity().
						  canEditProperty());
		this.applyCommand("sceneDown", (ev) -> this.storyModel.get().downScene(), this.storyModel.get().getSceneEntity().
						  canEditProperty());
		this.applyCommand("sceneAssociation", (ev) -> this.storyModel.get().associationScene(), this.storyModel.get().getSceneEntity().
						  canEditProperty());
		this.applyCommand("sceneOrderReset", (ev) -> this.storyModel.get().getSceneDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showChapterList", (ev) -> this.applicationModel.showChapterList(), this.storyModel.get().canEditProperty());
		this.applyCommand("chapterNew", (ev) -> this.storyModel.get().newChapter(), this.storyModel.get().canEditProperty());
		this.applyCommand("chapterEdit", (ev) -> this.storyModel.get().editChapter(), this.storyModel.get().getChapterEntity().
						  canEditProperty());
		this.applyCommand("chapterDelete", (ev) -> this.storyModel.get().deleteChapter(), this.storyModel.get().getChapterEntity().
						  canEditProperty());
		this.applyCommand("chapterUp", (ev) -> this.storyModel.get().upChapter(), this.storyModel.get().getChapterEntity().
						  canEditProperty());
		this.applyCommand("chapterDown", (ev) -> this.storyModel.get().downChapter(), this.storyModel.get().getChapterEntity().
						  canEditProperty());
		this.applyCommand("chapterAssociation", (ev) -> this.storyModel.get().associationChapter(), this.storyModel.get().
						  getChapterEntity().
						  canEditProperty());
		this.applyCommand("chapterOrderReset", (ev) -> this.storyModel.get().getChapterDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("chapterSceneNovel", (ev) -> this.storyModel.get().sceneNovelEditorChapter(), this.storyModel.get().
						  getChapterEntity().canEditProperty());

		this.applyCommand("showPartList", (ev) -> this.applicationModel.showPartList(), this.storyModel.get().canEditProperty());
		this.applyCommand("partNew", (ev) -> this.storyModel.get().newPart(), this.storyModel.get().canEditProperty());
		this.applyCommand("partEdit", (ev) -> this.storyModel.get().editPart(), this.storyModel.get().getPartEntity().
						  canEditProperty());
		this.applyCommand("partDelete", (ev) -> this.storyModel.get().deletePart(), this.storyModel.get().getPartEntity().
						  canEditProperty());
		this.applyCommand("partUp", (ev) -> this.storyModel.get().upPart(), this.storyModel.get().getPartEntity().
						  canEditProperty());
		this.applyCommand("partDown", (ev) -> this.storyModel.get().downPart(), this.storyModel.get().getPartEntity().
						  canEditProperty());
		this.applyCommand("partAssociation", (ev) -> this.storyModel.get().associationPart(), this.storyModel.get().getPartEntity().
						  canEditProperty());
		this.applyCommand("partOrderReset", (ev) -> this.storyModel.get().getPartDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showStorylineList", (ev) -> this.applicationModel.showStorylineList(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("storylineNew", (ev) -> this.storyModel.get().newStoryline(), this.storyModel.get().canEditProperty());
		this.applyCommand("storylineEdit", (ev) -> this.storyModel.get().editStoryline(), this.storyModel.get().getStorylineEntity().
						  canEditProperty());
		this.applyCommand("storylineDelete", (ev) -> this.storyModel.get().deleteStoryline(), this.storyModel.get().
						  getStorylineEntity().
						  canEditProperty());
		this.applyCommand("storylineUp", (ev) -> this.storyModel.get().upStoryline(), this.storyModel.get().getStorylineEntity().
						  canEditProperty());
		this.applyCommand("storylineDown", (ev) -> this.storyModel.get().downStoryline(), this.storyModel.get().getStorylineEntity().
						  canEditProperty());
		this.applyCommand("storylineAssociation", (ev) -> this.storyModel.get().associationStoryline(), this.storyModel.get().
						  getStorylineEntity().
						  canEditProperty());
		this.applyCommand("storylineOrderReset", (ev) -> this.storyModel.get().getStorylineDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showSexList", (ev) -> this.applicationModel.showSexList(), this.storyModel.get().canEditProperty());
		this.applyCommand("sexNew", (ev) -> this.storyModel.get().newSex(), this.storyModel.get().canEditProperty());
		this.applyCommand("sexEdit", (ev) -> this.storyModel.get().editSex(), this.storyModel.get().getSexEntity().
						  canEditProperty());
		this.applyCommand("sexDelete", (ev) -> this.storyModel.get().deleteSex(), this.storyModel.get().getSexEntity().
						  canEditProperty());
		this.applyCommand("sexUp", (ev) -> this.storyModel.get().upSex(), this.storyModel.get().getSexEntity().
						  canEditProperty());
		this.applyCommand("sexDown", (ev) -> this.storyModel.get().downSex(), this.storyModel.get().getSexEntity().
						  canEditProperty());
		this.applyCommand("sexAssociation", null, new SimpleBooleanProperty(false));
		this.applyCommand("sexOrderReset", (ev) -> this.storyModel.get().getSexDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showAttributeList", (ev) -> this.applicationModel.showAttributeList(), this.storyModel.get().
						  canEditProperty());
		this.applyCommand("attributeNew", (ev) -> this.storyModel.get().newAttribute(), this.storyModel.get().canEditProperty());
		this.applyCommand("attributeEdit", (ev) -> this.storyModel.get().editAttribute(), this.storyModel.get().
						  getAttributeEntity().canEditProperty());
		this.applyCommand("attributeDelete", (ev) -> this.storyModel.get().deleteAttribute(), this.storyModel.get().
						  getAttributeEntity().canEditProperty());
		this.applyCommand("attributeUp", (ev) -> this.storyModel.get().upAttribute(), this.storyModel.get().
						  getAttributeEntity().canEditProperty());
		this.applyCommand("attributeDown", (ev) -> this.storyModel.get().downAttribute(), this.storyModel.get().
						  getAttributeEntity().canEditProperty());
		this.applyCommand("attributeAssociation", null, new SimpleBooleanProperty(false));
		this.applyCommand("attributeOrderReset", (ev) -> this.storyModel.get().getAttributeDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showKeywordList", (ev) -> this.applicationModel.showKeywordList(), this.storyModel.get().canEditProperty());
		this.applyCommand("keywordNew", (ev) -> this.storyModel.get().newKeyword(), this.storyModel.get().canEditProperty());
		this.applyCommand("keywordEdit", (ev) -> this.storyModel.get().editKeyword(), this.storyModel.get().getKeywordEntity().
						  canEditProperty());
		this.applyCommand("keywordDelete", (ev) -> this.storyModel.get().deleteKeyword(), this.storyModel.get().getKeywordEntity().
						  canEditProperty());
		this.applyCommand("keywordUp", (ev) -> this.storyModel.get().upKeyword(), this.storyModel.get().getKeywordEntity().
						  canEditProperty());
		this.applyCommand("keywordDown", (ev) -> this.storyModel.get().downKeyword(), this.storyModel.get().getKeywordEntity().
						  canEditProperty());
		this.applyCommand("keywordAssociation", (ev) -> this.storyModel.get().associationKeyword(), this.storyModel.get().
						  getKeywordEntity().canEditProperty());
		this.applyCommand("keywordOrderReset", (ev) -> this.storyModel.get().getKeywordDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("showTagList", (ev) -> this.applicationModel.showTagList(), this.storyModel.get().canEditProperty());
		this.applyCommand("tagNew", (ev) -> this.storyModel.get().newTag(), this.storyModel.get().canEditProperty());
		this.applyCommand("tagEdit", (ev) -> this.storyModel.get().editTag(), this.storyModel.get().getTagEntity().
						  canEditProperty());
		this.applyCommand("tagDelete", (ev) -> this.storyModel.get().deleteTag(), this.storyModel.get().getTagEntity().
						  canEditProperty());
		this.applyCommand("tagUp", (ev) -> this.storyModel.get().upTag(), this.storyModel.get().getTagEntity().
						  canEditProperty());
		this.applyCommand("tagDown", (ev) -> this.storyModel.get().downTag(), this.storyModel.get().getTagEntity().
						  canEditProperty());
		this.applyCommand("tagAssociation", null, new SimpleBooleanProperty(false));
		this.applyCommand("tagOrderReset", (ev) -> this.storyModel.get().getTagDAO().resetOrder(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("searchEntityPane", (ev) -> this.applicationModel.showSearchEntityPane(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("personUsingChart", (ev) -> this.applicationModel.showPersonUsingChart(), this.storyModel.get().
						  canEditProperty());

		this.applyCommand("save", (ev) -> this.storyModel.get().save(), this.storyModel.get().canSaveProperty());
		this.applyCommand("close", (ev) -> this.storyModel.get().close(), this.storyModel.get().canEditProperty());
		this.applyCommand("storySetting", (ev) -> this.applicationModel.showStorySetting(), this.storyModel.get().canEditProperty());
	}

}
