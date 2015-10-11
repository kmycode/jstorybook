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

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import jstorybook.viewtool.messenger.Message;

/**
 * シーン一括編集画面を開くメッセージ
 *
 * @author KMY
 */
public class SceneNovelChartShowMessage extends Message {

	private final LongProperty chapterId = new SimpleLongProperty();

	public SceneNovelChartShowMessage (long chapterId) {
		this.chapterId.set(chapterId);
	}

	public LongProperty chapterIdProperty () {
		return this.chapterId;
	}

}
