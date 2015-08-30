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

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.viewmodel.dialog.ExceptionDialogViewModel;

/**
 *
 * @author KMY
 */
public class ExceptionModel {

	private ObjectProperty<Exception> exception = new SimpleObjectProperty<>();
	private StringProperty stackTrace = new SimpleStringProperty();
	private StringProperty exceptionTitle = new SimpleStringProperty();

	public ExceptionModel () {
		this.exception.addListener((obj) -> {
			Exception newObj = ((ObjectProperty<Exception>) obj).get();
			ExceptionModel.this.stackTrace.set(ExceptionModel.this.getStackTrace());
			ExceptionModel.this.exceptionTitle.set(ExceptionModel.this.getExceptionTitle());
		});
	}

	public ExceptionModel (Exception e) {
		this.exception.set(e);
	}

	public ObjectProperty<Exception> exceptionProperty () {
		return this.exception;
	}

	public StringProperty stackTraceProperty () {
		return this.stackTrace;
	}

	public StringProperty exceptionTitleProperty () {
		return this.exceptionTitle;
	}

	// -------------------------------------------------------

	public boolean hasException () {
		return this.exception.get() != null;
	}

	public String getStackTrace () {
		if (this.hasException()) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			this.exception.get().printStackTrace(pw);
			return sw.toString();
		}
		else {
			return "";
		}
	}

	public String getExceptionTitle () {
		return this.hasException() ? this.exception.get().toString() : "";
	}

}
