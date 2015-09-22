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
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.Messenger;

/**
 * メインウィンドウのビューモデル
 *
 * @author KMY
 */
public class StoryViewModel extends ViewModel {

	private final ObjectProperty<StoryModel> storyModel = new SimpleObjectProperty<>(new StoryModel());

	@Override
	protected void storeProperty () {
		this.applyProperty("storyModel", this.storyModel);

		this.applyProperty("storyTitle", this.storyModel.get().getCore().titleProperty());
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
	}

	@Override
	public void storeMessenger (Messenger messenger) {
		this.storyModel.get().setMessenger(messenger);
	}

	@Override
	protected void storeCommand () {
		this.applyCommand("personNew", (ev) -> this.storyModel.get().newPerson());
		this.applyCommand("personEdit", (ev) -> this.storyModel.get().editPerson(), this.storyModel.get().
						  getPersonEntity().canEditProperty());
		this.applyCommand("personDelete", (ev) -> this.storyModel.get().deletePerson(), this.storyModel.get().getPersonEntity().
						  canEditProperty());
		this.applyCommand("personUp", (ev) -> this.storyModel.get().upPerson(), this.storyModel.get().getPersonEntity().
						  canEditProperty());
		this.applyCommand("personDown", (ev) -> this.storyModel.get().downPerson(), this.storyModel.get().getPersonEntity().
						  canEditProperty());

		this.applyCommand("groupNew", (ev) -> this.storyModel.get().newGroup());
		this.applyCommand("groupEdit", (ev) -> this.storyModel.get().editGroup(), this.storyModel.get().
						  getGroupEntity().canEditProperty());
		this.applyCommand("groupDelete", (ev) -> this.storyModel.get().deleteGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());
		this.applyCommand("groupUp", (ev) -> this.storyModel.get().upGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());
		this.applyCommand("groupDown", (ev) -> this.storyModel.get().downGroup(), this.storyModel.get().getGroupEntity().
						  canEditProperty());

		this.applyCommand("save", (ev) -> this.storyModel.get().save(), this.storyModel.get().canSaveProperty());
	}

}
