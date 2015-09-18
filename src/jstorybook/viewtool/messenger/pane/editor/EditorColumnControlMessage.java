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
package jstorybook.viewtool.messenger.pane.editor;

import javafx.beans.property.Property;
import jstorybook.viewtool.messenger.Message;

/**
 * 編集画面になにかコントロールを追加するメッセージ
 *
 * @author KMY
 */
public abstract class EditorColumnControlMessage extends Message {

	private String columnTitle;
	private Property property;

	public EditorColumnControlMessage (String title, Property property) {
		this.setColumnTitle(title);
		this.setProperty(property);
	}

	public String getColumnTitle () {
		return this.columnTitle;
	}

	public final void setColumnTitle (String title) {
		this.columnTitle = title;
	}

	public Property getProperty () {
		return this.property;
	}

	public final void setProperty (Property p) {
		this.property = p;
	}

}
