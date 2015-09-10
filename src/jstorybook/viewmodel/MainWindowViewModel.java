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
public class MainWindowViewModel extends ViewModel {

	private final ObjectProperty<StoryModel> storyModel = new SimpleObjectProperty<>(new StoryModel());

	@Override
	protected void storeProperty () {
		this.applyProperty("storyModel", this.storyModel);
		this.applyProperty("storyTitle", this.storyModel.get().getCore().titleProperty());
		this.applyProperty("storyFileName", this.storyModel.get().fileNameProperty());
		this.applyProperty("authorName", this.storyModel.get().getCore().authorProperty());
	}

	@Override
	public void storeMessenger (Messenger messenger) {
		this.storyModel.get().setMessenger(messenger);
	}

}
