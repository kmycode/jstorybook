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
import jstorybook.viewtool.messenger.pane.chart.PersonUsingChartShowMessage;
import jstorybook.viewtool.messenger.pane.list.AttributeListShowMessage;
import jstorybook.viewtool.messenger.pane.list.ChapterListShowMessage;
import jstorybook.viewtool.messenger.pane.list.GroupListShowMessage;
import jstorybook.viewtool.messenger.pane.list.KeywordListShowMessage;
import jstorybook.viewtool.messenger.pane.list.PersonListShowMessage;
import jstorybook.viewtool.messenger.pane.list.PlaceListShowMessage;
import jstorybook.viewtool.messenger.pane.list.SceneListShowMessage;
import jstorybook.viewtool.messenger.pane.list.SexListShowMessage;
import jstorybook.viewtool.messenger.pane.list.TagListShowMessage;
import jstorybook.viewtool.messenger.pane.pane.SearchEntityPaneShowMessage;

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

	public void showSexList () {
		this.messenger.send(new SexListShowMessage());
	}

	public void showAttributeList () {
		this.messenger.send(new AttributeListShowMessage());
	}

	public void showKeywordList () {
		this.messenger.send(new KeywordListShowMessage());
	}

	public void showTagList () {
		this.messenger.send(new TagListShowMessage());
	}

	public void showStorySetting () {
		this.messenger.send(new StorySettingDialogShowMessage());
	}

	public void showSearchEntityPane () {
		this.messenger.send(new SearchEntityPaneShowMessage());
	}

	public void showPersonUsingChart () {
		this.messenger.send(new PersonUsingChartShowMessage());
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}
}
