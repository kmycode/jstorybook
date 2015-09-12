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
package jstorybook.view.pane.list;

import java.util.ArrayList;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jstorybook.model.entity.Entity;
import jstorybook.view.pane.MyPane;
import jstorybook.viewtool.model.EditorColumn;

/**
 * エンティティのリストを表示するパネル
 * T : リストで扱うエンティティの型
 *
 * @author KMY
 */
public abstract class EntityListPane<T extends Entity> extends MyPane {

	private final TableView<T> tableView;
	private final ObjectProperty<ArrayList<EditorColumn>> columnList = new SimpleObjectProperty<>();
	private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();

	protected EntityListPane (String title) {
		super(title);

		this.columnList.addListener((obj) -> {
			EntityListPane.this.setColumnList(((ObjectProperty<ArrayList<EditorColumn>>) obj).get());
		});

		this.tableView = new TableView<>();
		this.setContent(tableView);

		this.tableView.getItems().addListener((Observable observable) -> {
		});

		this.tableView.setOnMouseClicked((ev) -> {
			// ダブルクリックで選択
			if (ev.getClickCount() >= 2) {
				this.selectedItem.set(EntityListPane.this.tableView.getSelectionModel().getSelectedItem());
			}
		});
	}

	protected void setColumnList (ArrayList<EditorColumn> cl) {
		this.tableView.getColumns().clear();
		for (EditorColumn column : cl) {
			this.addTableColumn(column);
		}
	}

	private <S> void addTableColumn (EditorColumn<S> columnData) {
		TableColumn<T, S> column = new TableColumn<>(columnData.getColumnName());
		column.setCellValueFactory(new PropertyValueFactory<>(columnData.getPropertyName()));
		column.setPrefWidth(columnData.getColumnWidth());
		column.setVisible(columnData.isDefaultShow());
		this.tableView.getColumns().add(column);
	}

	public ObjectProperty<ObservableList<T>> itemsProperty () {
		return this.tableView.itemsProperty();
	}

	public ObjectProperty<ArrayList<EditorColumn>> columnListProperty () {
		return this.columnList;
	}

	public ObjectProperty<T> selectedItemProperty () {
		return this.selectedItem;
	}

}
