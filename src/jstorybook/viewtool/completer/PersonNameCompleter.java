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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.util.PropertyUtil;

/**
 * 登場人物氏名のコンプリータ
 *
 * @author KMY
 */
public class PersonNameCompleter {

	// 結果
	private final StringProperty name = new SimpleStringProperty();

	// 必要なデータ
	private final StringProperty firstName = new SimpleStringProperty();
	private final StringProperty lastName = new SimpleStringProperty();

	public PersonNameCompleter () {
		// プロパティ変更時に結果を更新
		PropertyUtil.addAllListener((obj) -> {
			PersonNameCompleter.this.complete();
		}, this.firstName, this.lastName);
	}

	private void complete () {
		String result;
		result = this.lastName.get() + " " + this.firstName.get();
		this.name.set(result);
	}

	public StringProperty nameProperty () {
		return this.name;
	}

	public StringProperty firstNameProperty () {
		return this.firstName;
	}

	public StringProperty lastNameProperty () {
		return this.lastName;
	}

}
