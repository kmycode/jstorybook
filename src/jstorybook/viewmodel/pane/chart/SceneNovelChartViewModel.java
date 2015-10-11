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
package jstorybook.viewmodel.pane.chart;

import jstorybook.model.pane.SceneNovelModel;
import jstorybook.viewmodel.ViewModel;
import jstorybook.viewtool.messenger.Messenger;

/**
 * シーン一括編集のビューモデル
 *
 * @author KMY
 */
public class SceneNovelChartViewModel extends ViewModel {

	private final SceneNovelModel model = new SceneNovelModel();

	@Override
	protected void storeProperty () {
		this.applyProperty("chapterId", this.model.chapterIdProperty());
	}

	@Override
	public void storeMessenger (Messenger messenger) {
		this.model.setMessenger(messenger);
	}

	@Override
	protected void storeCommand () {
		this.applyCommand("draw", (ev) -> this.model.draw());
	}

}
