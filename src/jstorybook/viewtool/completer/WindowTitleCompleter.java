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

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.util.PropertyUtil;

/**
 * ウィンドウのタイトルのコンプリータ
 *
 * @author KMY
 */
public class WindowTitleCompleter {

	// 結果
	private final StringProperty title = new SimpleStringProperty();

	// 必要なデータ
	private final StringProperty appTitle = new SimpleStringProperty();
	private final StringProperty version = new SimpleStringProperty();
	private final StringProperty storyFileName = new SimpleStringProperty();
	private final StringProperty storyName = new SimpleStringProperty();

	private String resultTmp;

	public WindowTitleCompleter () {
		// プロパティ変更時に結果を更新
		PropertyUtil.addAllListener((obj) -> {
			WindowTitleCompleter.this.complete();
		}, this.appTitle, this.version, this.storyFileName, this.storyName);
	}

	private void complete () {
		String result = "";
		if (this.storyFileName.get() != null && !this.storyFileName.get().isEmpty()) {
			result = this.appTitle.get() + " - " + this.version.get() + " [" + (this.storyName.get().isEmpty() ? this.storyFileName.
					get() : this.storyName.get()) + "]";
		}
		else {
			result = this.appTitle.get() + " - " + this.version.get();
		}
		this.resultTmp = result;
		Platform.runLater(() -> this.title.set(this.resultTmp));
	}

	public StringProperty titleProperty () {
		return this.title;
	}

	public StringProperty appTitleProperty () {
		return this.appTitle;
	}

	public StringProperty versionProperty () {
		return this.version;
	}

	public StringProperty storyFileNameProperty () {
		return this.storyFileName;
	}

	public StringProperty storyNameProperty () {
		return this.storyName;
	}

}
