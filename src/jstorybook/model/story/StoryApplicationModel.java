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

import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.StorySettingDialogShowMessage;
import jstorybook.viewtool.messenger.pane.ChapterListShowMessage;
import jstorybook.viewtool.messenger.pane.GroupListShowMessage;
import jstorybook.viewtool.messenger.pane.PersonListShowMessage;
import jstorybook.viewtool.messenger.pane.PlaceListShowMessage;
import jstorybook.viewtool.messenger.pane.SceneListShowMessage;

/**
 * ストーリーのアプリケーションモデル
 *
 * @author KMY
 */
public class StoryApplicationModel implements IUseMessenger {

	private Messenger messenger = Messenger.getInstance();

	public void showPersonList () {
		this.messenger.send(new PersonListShowMessage());
	}

	public void showGroupList () {
		this.messenger.send(new GroupListShowMessage());
	}

	public void showPlaceList () {
		this.messenger.send(new PlaceListShowMessage());
	}

	public void showSceneList () {
		this.messenger.send(new SceneListShowMessage());
	}

	public void showChapterList () {
		this.messenger.send(new ChapterListShowMessage());
	}

	public void showStorySetting () {
		this.messenger.send(new StorySettingDialogShowMessage());
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}
}
