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
package jstorybook.viewmodel.dialog;

import jstorybook.model.PreferenceSettingModel;
import jstorybook.viewmodel.ViewModel;
import jstorybook.viewtool.messenger.Messenger;

/**
 * 環境設定のビューモデル
 *
 * @author KMY
 */
public class PreferenceViewModel extends ViewModel {

	private final PreferenceSettingModel model = new PreferenceSettingModel();

	@Override
	protected void storeProperty () {
		this.applyProperty("fontFamily", model.fontFamilyProperty());
		this.applyProperty("fontSize", model.fontSizeProperty());
		this.applyProperty("isUseSystemMenu", model.isUseSystemMenuProperty());
		this.applyProperty("isConfirmExit", model.isConfirmExitProperty());
		this.applyProperty("isStartupOpenLastFile", model.isStartupOpenLastFileProperty());
		this.applyProperty("isSearchEntityOrientation", model.isSearchEntityOrientationProperty());
	}

	@Override
	public void storeMessenger (Messenger messenger) {
		this.model.setMessenger(messenger);
	}

	@Override
	protected void storeCommand () {
		this.applyCommand("save", (ev) -> model.save());
		this.applyCommand("cancel", (ev) -> model.cancel());
	}

}
