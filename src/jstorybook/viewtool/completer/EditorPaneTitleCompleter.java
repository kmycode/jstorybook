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
package jstorybook.viewtool.completer;

import java.lang.ref.WeakReference;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 * エディタパネルのタイトルのコンプリータ
 *
 * @author KMY
 */
public class EditorPaneTitleCompleter extends Completer {

	// 結果
	private final StringProperty title = new SimpleStringProperty();

	// 必要なデータ
	private WeakReference<ObservableValue<String>> entityTitle = new WeakReference<>(null);
	private WeakReference<ObservableValue<String>> entityTypeName = new WeakReference<>(null);
	private ObservableValue<String> entityTypeNameValue = null;

	@Override
	protected void complete () {
		String result = "";
		result = (this.entityTypeName.get() != null ? this.entityTypeName.get().getValue() : "") + " ["
				+ (this.entityTitle.get() != null ? this.entityTitle.get().getValue() : "") + "]";
		this.title.set(result);
	}

	public StringProperty titleProperty () {
		return this.title;
	}

	public void bindEntityTitle (ObservableValue<String> property) {
		this.entityTitle = this.bindValue(property);
		this.complete();
	}

	public void setEntityTypeName (String str) {
		this.entityTypeNameValue = new SimpleStringProperty(str);
		this.bindEntityTypeName(this.entityTypeNameValue);
	}

	public void bindEntityTypeName (ObservableValue<String> property) {
		this.entityTypeName = this.bindValue(property);
		this.complete();
	}

}
