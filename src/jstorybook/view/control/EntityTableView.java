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
package jstorybook.view.control;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jstorybook.model.entity.Entity;
import jstorybook.view.control.tablecell.ColorCell;
import jstorybook.view.control.tablecell.DateCell;
import jstorybook.viewtool.model.EditorColumn;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * エンティティ専用テーブルビュー
 *
  * @author KMY
 */
public class EntityTableView<E extends Entity> extends TableView<E> {

	public EntityTableView () {
		this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void setColumnList (EditorColumnList cl) {
		this.getColumns().clear();
		for (EditorColumn column : cl) {
			this.addTableColumn(column);
		}
	}

	private <S> void addTableColumn (EditorColumn<S> columnData) {
		TableColumn<E, S> column = new TableColumn<>(columnData.getColumnName());
		column.setCellValueFactory(new PropertyValueFactory<>(columnData.getPropertyName()));
		column.setPrefWidth(columnData.getColumnWidth());
		column.setVisible(columnData.isDefaultShow());

		// 特殊なセルのファクトリ
		if (columnData.getCellType() == EditorColumn.CellType.DATE) {
			column.setCellFactory((arg) -> {
				return new DateCell();
			});
		}
		else if (columnData.getCellType() == EditorColumn.CellType.COLOR) {
			column.setCellFactory((arg) -> {
				return new ColorCell();
			});
		}

		this.getColumns().add(column);
	}
}
