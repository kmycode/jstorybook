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

import jstorybook.viewmodel.ViewModel;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.model.ExceptionModel;

/**
 * 内部エラーダイアログのビューモデル
 *
 * @author KMY
 */
public class ExceptionDialogViewModel extends ViewModel {

	private ExceptionModel exceptionModel = new ExceptionModel();

	public ExceptionDialogViewModel () {
		this.storeProperty();
	}

	protected void storeProperty () {
		this.applyProperty("exception", this.exceptionModel.exceptionProperty());
		this.applyProperty("stackTrace", this.exceptionModel.stackTraceProperty());
		this.applyProperty("exceptionTitle", this.exceptionModel.exceptionTitleProperty());
	}

}
