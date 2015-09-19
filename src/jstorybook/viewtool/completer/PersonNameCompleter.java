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
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 * 登場人物氏名のコンプリータ
 *
 * @author KMY
 */
public class PersonNameCompleter extends Completer {

	// 結果
	private final StringProperty name = new SimpleStringProperty();

	// 必要なデータ
	private WeakReference<ObservableValue<String>> firstName = null;
	private WeakReference<ObservableValue<String>> lastName = null;

	@Override
	protected void complete () {
		String result;
		result = (this.isPropertySet(this.lastName) ? this.lastName.get().getValue() : "") + " " + (this.isPropertySet(
				this.firstName) ? this.firstName.get().getValue() : "");
		this.name.set(result);
	}

	public ReadOnlyStringProperty nameProperty () {
		return this.name;
	}

	public void bindFirstName (ObservableValue<String> property) {
		this.firstName = this.bindValue(property);
		this.complete();
	}

	public void bindLastName (ObservableValue<String> property) {
		this.lastName = this.bindValue(property);
		this.complete();
	}

}
