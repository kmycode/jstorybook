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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.ExceptionMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.SaveFileChooserMessage;
import jstorybook.viewtool.messenger.general.CloseMessage;

/**
 * ストーリーを新規作成するモデル
 *
 * @author KMY
 */
public class StoryCreateModel implements IUseMessenger {

	private Messenger messenger;
	private final StringProperty storyName = new SimpleStringProperty("");
	private final StringProperty fileName = new SimpleStringProperty("");
	private final BooleanProperty canCreate = new SimpleBooleanProperty(false);

	public StoryCreateModel () {
		this.storyName.addListener((obj) -> {
			this.checkCanCreate();
		});
		this.fileName.addListener((obj) -> {
			this.checkCanCreate();
		});
	}

	private void checkCanCreate () {
		this.canCreate.set(this.storyName.get().length() > 0 && this.fileName.get().length() > 0);
	}

	public void create () {
		CurrentStoryModelGetMessage message = new CurrentStoryModelGetMessage();
		this.messenger.send(message);

		StoryModel storyModel = message.storyModelProperty().get();
		if (storyModel != null) {
			storyModel.create(this.fileName.get(), this.storyName.get());
		}
		else {
			this.messenger.send(new ExceptionMessage(new NullPointerException("storyModel cannot get")));
		}
		this.messenger.send(new CloseMessage());
	}

	public void fileSelect () {
		this.messenger.send(new SaveFileChooserMessage(this.fileName));
	}

	public void cancel () {
		this.messenger.send(new CloseMessage());
	}

	public StringProperty storyNameProperty () {
		return this.storyName;
	}

	public StringProperty fileNameProperty () {
		return this.fileName;
	}

	public BooleanProperty canCreateProperty () {
		return this.canCreate;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
