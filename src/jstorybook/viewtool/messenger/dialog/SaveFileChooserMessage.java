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
package jstorybook.viewtool.messenger.dialog;

import javafx.beans.property.StringProperty;
import jstorybook.viewtool.messenger.Message;

/**
 * 保存先ファイル選択ダイアログ表示メッセージ
 *
 * @author KMY
 */
public class SaveFileChooserMessage extends Message {

	private StringProperty fileName;

	public SaveFileChooserMessage (StringProperty fileName) {
		this.fileName = fileName;
	}

	public StringProperty fileNameProperty () {
		return this.fileName;
	}

}