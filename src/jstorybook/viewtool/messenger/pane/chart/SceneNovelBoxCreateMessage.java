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
package jstorybook.viewtool.messenger.pane.chart;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;
import jstorybook.viewtool.messenger.Message;

/**
 * シーン一括編集で、シーンの箱を作るメッセージ
 *
 * @author KMY
 */
public class SceneNovelBoxCreateMessage extends Message {

	private final StringProperty sceneName;
	private final StringProperty sceneText;

	private final List<PersonDrawMessage> personDrawMessageList = new ArrayList<>();
	private final List<PlaceDrawMessage> placeDrawMessageList = new ArrayList<>();
	private final List<KeywordDrawMessage> keywordDrawMessageList = new ArrayList<>();

	public SceneNovelBoxCreateMessage (StringProperty sceneName, StringProperty sceneText) {
		this.sceneName = sceneName;
		this.sceneText = sceneText;
	}

	public List<PersonDrawMessage> getPersonList () {
		return this.personDrawMessageList;
	}

	public List<PlaceDrawMessage> getPlaceList () {
		return this.placeDrawMessageList;
	}

	public List<KeywordDrawMessage> getKeywordList () {
		return this.keywordDrawMessageList;
	}

	public StringProperty sceneNameProperty () {
		return this.sceneName;
	}

	public StringProperty sceneTextProperty () {
		return this.sceneText;
	}

}
