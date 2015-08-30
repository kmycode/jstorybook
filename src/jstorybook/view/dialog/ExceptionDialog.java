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
package jstorybook.view.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jstorybook.common.manager.ResourceManager;
import jstorybook.viewmodel.ViewModel;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.dialog.ExceptionDialogViewModel;

/**
 * 内部エラーダイアログ
 *
 * @author KMY
 */
public class ExceptionDialog extends Alert {

	private ViewModelList viewModelList;
	private TextArea textArea;
	private Label exceptionLabel;

	public ExceptionDialog (AlertType alertType) {
		super(alertType);

		this.viewModelList = new ViewModelList(new ExceptionDialogViewModel());

		this.setTitle(ResourceManager.getMessage("msg.exception"));
		this.setHeaderText(ResourceManager.getMessage("msg.exception.occured"));
		Label label = new Label(ResourceManager.getMessage("msg.exception.stacktrace"));

		this.exceptionLabel = new Label();
		this.exceptionLabel.textProperty().bind(this.viewModelList.getProperty("exceptionTitle"));

		this.textArea = new TextArea();
		this.textArea.setEditable(false);
		this.textArea.setWrapText(true);
		this.textArea.setMinWidth(600.0);
		this.textArea.setMaxWidth(Double.MAX_VALUE);
		this.textArea.setMaxHeight(Double.MAX_VALUE);
		this.textArea.textProperty().bind(this.viewModelList.getProperty("stackTrace"));
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(exceptionLabel, 0, 0);
		expContent.add(label, 0, 1);
		expContent.add(textArea, 0, 2);
		this.getDialogPane().setContent(expContent);
	}

	public ExceptionDialog () {
		this(AlertType.ERROR);
	}

	public ExceptionDialog (Exception e) {
		this();
		this.setException(e);
	}

	public static void showAndWait (Exception e) {
		ExceptionDialog dlg = new ExceptionDialog(e);
		dlg.showAndWait();
	}

	public static void showExpenditure (Exception e) {
		e.printStackTrace();
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(e.toString());
		alert.setTitle("Exception");
		alert.showAndWait();
	}

	public void setException (Exception e) {
		this.viewModelList.setProperty("exception", e);
	}

}
