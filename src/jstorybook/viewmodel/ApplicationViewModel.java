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

import jstorybook.model.ApplicationModel;
import jstorybook.viewtool.messenger.Messenger;

/**
 * アプリの終了
 *
  * @author KMY
 */
public class ApplicationViewModel extends ViewModel {

	ApplicationModel model = new ApplicationModel();

	@Override
	protected void storeProperty () {
		this.applyProperty("isExitable", this.model.isExitableProperty());
		this.applyProperty("windowWidth", this.model.windowWidthProperty());
		this.applyProperty("windowHeight", this.model.windowHeightProperty());
		this.applyProperty("windowMax", this.model.windowMaxProperty());
	}

	@Override
	public void storeMessenger (Messenger messenger) {
		this.model.setMessenger(messenger);
	}

	@Override
	protected void storeCommand () {
		this.applyCommand("newStory", (ev) -> this.model.newStory());
		this.applyCommand("loadStory", (ev) -> this.model.loadStory());
		this.applyCommand("about", (ev) -> this.model.about());
		this.applyCommand("preference", (ev) -> this.model.preference());
		this.applyCommand("exit", (ev) -> this.model.exit());
	}

}
