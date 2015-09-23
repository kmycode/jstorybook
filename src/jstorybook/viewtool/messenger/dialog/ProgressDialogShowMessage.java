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

import javafx.beans.property.DoubleProperty;
import jstorybook.viewtool.messenger.Message;

/**
 * 進捗状況を表示したい時に使うメッセージ
 *
 * @author KMY
 */
public class ProgressDialogShowMessage extends Message {

	private final DoubleProperty progress;

	public ProgressDialogShowMessage (DoubleProperty progress) {
		this.progress = progress;
	}

	public DoubleProperty progressProperty () {
		return this.progress;
	}

}
