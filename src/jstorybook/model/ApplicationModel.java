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
package jstorybook.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.ApplicationQuitMessage;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.MainWindowResetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.NewStoryDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.OpenFileChooserMessage;

/**
 * アプリケーションモデル
 *
 * @author KMY
 */
public class ApplicationModel implements IUseMessenger {

	private Messenger messenger = Messenger.getInstance();

	public void exit () {
		this.messenger.send(new ApplicationQuitMessage());
	}

	public void newStory () {
		this.messenger.send(new NewStoryDialogShowMessage());
	}

	public void loadStory () {
		StringProperty fileName = new SimpleStringProperty();
		this.messenger.send(new OpenFileChooserMessage(fileName));
		if (fileName.get() != null && !fileName.get().isEmpty()) {
			CurrentStoryModelGetMessage mes = new CurrentStoryModelGetMessage();
			this.messenger.send(mes);
			StoryModel storyModel = mes.storyModelProperty().get();
			if (storyModel != null) {
				storyModel.fileNameProperty().set(fileName.get());
				this.messenger.send(new MainWindowResetMessage());
			}
		}
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
