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
package jstorybook.view.pane;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.column.EditorColumn;
import jstorybook.model.entity.columnfactory.ColumnFactory;

/**
 * エンティティのリストを表示するパネル
 * T : リストで扱うエンティティの型
 *
 * @author KMY
 */
public abstract class EntityListPane<T extends Entity> extends MyPane {

	private TableView<T> tableView;

	protected EntityListPane (String title) {
		super(title);

		this.tableView = new TableView<>();
		this.setContent(tableView);
	}

	protected void setColumnFactory (ColumnFactory cf) {
		this.tableView.getColumns().clear();
		for (EditorColumn column : cf.getColumnList()) {
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

}
