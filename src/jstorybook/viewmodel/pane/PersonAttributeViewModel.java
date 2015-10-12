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
package jstorybook.viewmodel.pane;

import jstorybook.model.story.PersonAttributeModel;
import jstorybook.viewmodel.ViewModel;
import jstorybook.viewtool.messenger.Messenger;

/**
 * 登場人物の属性設定画面のビューモデル
 *
 * @author KMY
 */
public class PersonAttributeViewModel extends ViewModel {

	private final PersonAttributeModel model = new PersonAttributeModel();

	@Override
	protected void storeProperty () {
		this.applyProperty("columnList", this.model.columnListProperty());
	}

	@Override
	public void storeMessenger (Messenger messenger) {
		this.model.setMessenger(messenger);
	}

	@Override
	protected void storeCommand () {
		this.applyCommand("draw", (ev) -> this.model.draw());
		this.applyCommand("save", (ev) -> this.model.save());
	}

}
