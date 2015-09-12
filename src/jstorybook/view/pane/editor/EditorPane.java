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
package jstorybook.view.pane.editor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jstorybook.view.pane.MyPane;
import jstorybook.viewtool.converter.LocalDateCalendarConverter;
import jstorybook.viewtool.model.EditorColumn;

/**
 * 編集画面のパネル
 *
 * @author KMY
 */
public abstract class EditorPane extends MyPane {

	private final ObjectProperty<List<EditorColumn>> columnList = new SimpleObjectProperty<>();

	protected final AnchorPane rootPane = new AnchorPane();
	protected final VBox vbox = new VBox();
	protected final GridPane gridPane = new GridPane();
	private List<WeakReference<Property>> editPropertyList = new ArrayList<>();

	protected EditorPane (String title) {
		super(title);
		this.columnList.addListener((obj) -> {
			EditorPane.this.generateEditor(((ObjectProperty<List<EditorColumn>>) obj).get());
		});
		this.setContent(this.rootPane);
		this.rootPane.getChildren().add(this.vbox);
		this.vbox.getChildren().add(this.gridPane);
		VBox.setVgrow(this.vbox, Priority.ALWAYS);

		// （まだ検証してないけど）循環参照によるメモリリークを予防する意味合い
		this.setOnClosed((ev) -> {
			for (WeakReference<Property> p : EditorPane.this.editPropertyList) {
				if (p.get() != null) {
					p.get().unbind();
				}
			}
			this.editPropertyList.clear();
		});
	}

	public ObjectProperty<List<EditorColumn>> columnListProperty () {
		return this.columnList;
	}

	// この、めぢょっとぉ、ゎ、エディタ、をねっ、つくつくちちゃうのー！
	protected void generateEditor (List<EditorColumn> columnList) {
		int row = 0;
		for (EditorColumn column : columnList) {
			HBox hbox = new HBox();

			Label label = new Label(column.getColumnName());
			label.setPrefWidth(65.0);

			Control node = null;
			if (column.getColumnType() == EditorColumn.ColumnType.TEXT) {
				node = this.generateTextEdit(column);
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.DATE) {
				node = this.generateDateEdit(column);
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.COLOR) {
				node = this.generateColorEdit(column);
			}

			if (node != null) {
				node.setPrefWidth(250.0);
				GridPane.setConstraints(label, 0, row, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER,
										new Insets(5));
				GridPane.setConstraints(node, 1, row, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER,
										new Insets(5));
				this.gridPane.getChildren().addAll(label, node);
				row++;
			}
		}
	}

	// ColumnType.TEXT
	private Control generateTextEdit (EditorColumn column) {
		TextField node = new TextField();
		node.textProperty().bindBidirectional(column.getProperty());
		this.editPropertyList.add(new WeakReference<>(node.textProperty()));
		return node;
	}

	// ColumnType.DATE
	private Control generateDateEdit (EditorColumn column) {
		DatePicker node = new DatePicker();
		node.setPromptText("yyyy/MM/dd");
		
		LocalDateCalendarConverter converter = new LocalDateCalendarConverter();
		converter.calendarProperty().bindBidirectional(column.getProperty());
		converter.localDateProperty().bindBidirectional(node.valueProperty());

		this.editPropertyList.add(new WeakReference<>(node.valueProperty()));
		this.editPropertyList.add(new WeakReference<>(converter.calendarProperty()));
		return node;
	}

	// ColumnType.COLOR
	private Control generateColorEdit (EditorColumn column) {
		ColorPicker node = new ColorPicker();
		node.valueProperty().bindBidirectional(column.getProperty());
		this.editPropertyList.add(new WeakReference<>(node.valueProperty()));
		return node;
	}

}
