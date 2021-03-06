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
package jstorybook.viewtool.messenger.general;

import jstorybook.common.contract.DialogResult;
import jstorybook.viewtool.messenger.Message;

/**
 * 何かを削除するダイアログを作る
 *
 * @author KMY
 */
public class DeleteDialogMessage extends Message {

	private final String targetName;
	private DialogResult result;

	public DeleteDialogMessage (String targetName) {
		this.targetName = targetName;
	}

	public String getTargetName () {
		return this.targetName;
	}

	public void setResult (DialogResult result) {
		this.result = result;
	}

	public DialogResult getResult () {
		return this.result;
	}

}
