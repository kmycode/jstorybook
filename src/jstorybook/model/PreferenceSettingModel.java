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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import jstorybook.common.contract.PreferenceKey;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.CloseMessage;

/**
 * 環境設定を行うモデル
 *
 * @author KMY
 */
public class PreferenceSettingModel implements IUseMessenger {

	private Messenger messenger = Messenger.getInstance();

	private final StringProperty fontFamily = new SimpleStringProperty(PreferenceKey.FONT_FAMILY.getString());
	private final StringProperty fontSize = new SimpleStringProperty(PreferenceKey.FONT_SIZE.getDouble().toString());
	private final BooleanProperty isUseSystemMenu = new SimpleBooleanProperty(PreferenceKey.MENUBAR_USESYSTEM.getBoolean());
	private final BooleanProperty isSearchEntityOrientation = new SimpleBooleanProperty(PreferenceKey.SEARCH_ENTITY_ORIENTATION.
			getValue() == Orientation.VERTICAL);
	private final BooleanProperty isConfirmExit = new SimpleBooleanProperty(PreferenceKey.CONFIRM_EXIT.getBoolean());
	private final BooleanProperty isStartupOpenLastFile = new SimpleBooleanProperty(PreferenceKey.STARTUP_OPEN_LAST_FILE.getBoolean());

	public void save () {
		try {
			PreferenceKey.FONT_FAMILY.setValue(this.fontFamily.get());
			PreferenceKey.MENUBAR_USESYSTEM.setValue(this.isUseSystemMenu.get());
			PreferenceKey.CONFIRM_EXIT.setValue(this.isConfirmExit.get());
			PreferenceKey.STARTUP_OPEN_LAST_FILE.setValue(this.isStartupOpenLastFile.get());
			PreferenceKey.SEARCH_ENTITY_ORIENTATION.setValue(this.isSearchEntityOrientation.get() ? Orientation.VERTICAL
																	 : Orientation.HORIZONTAL);

			PreferenceKey.FONT_SIZE.setValue(Double.parseDouble(this.fontSize.get()));
		} catch (NumberFormatException e) {
		}
		this.messenger.send(new CloseMessage());
	}

	public void cancel () {
		this.messenger.send(new CloseMessage());
	}

	public StringProperty fontFamilyProperty () {
		return this.fontFamily;
	}

	public StringProperty fontSizeProperty () {
		return this.fontSize;
	}

	public BooleanProperty isUseSystemMenuProperty () {
		return this.isUseSystemMenu;
	}

	public BooleanProperty isSearchEntityOrientationProperty () {
		return this.isSearchEntityOrientation;
	}

	public BooleanProperty isConfirmExitProperty () {
		return this.isConfirmExit;
	}

	public BooleanProperty isStartupOpenLastFileProperty () {
		return this.isStartupOpenLastFile;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
