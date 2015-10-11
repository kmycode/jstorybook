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
import jstorybook.model.column.EditorColumn;
import jstorybook.model.column.EditorColumnList;
import jstorybook.model.entity.Entity;
import jstorybook.view.control.tablecell.ColorCell;
import jstorybook.view.control.tablecell.DateCell;
import jstorybook.view.control.tablecell.DateTimeCell;
import jstorybook.view.control.tablecell.EntityCell;
import jstorybook.view.control.tablecell.NormalCell;
import jstorybook.view.control.tablecell.SexCell;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;

/**
 * エンティティ専用テーブルビュー
 *
  * @author KMY
 */
public class EntityTableView<E extends Entity> extends TableView<E> {

	private final Messenger mainMessenger;

	public EntityTableView (Messenger messenger) {
		this.mainMessenger = messenger;
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
		else if (columnData.getCellType() == EditorColumn.CellType.DATETIME) {
			column.setCellFactory((arg) -> {
				return new DateTimeCell();
			});
		}
		else if (columnData.getCellType() == EditorColumn.CellType.COLOR) {
			column.setCellFactory((arg) -> {
				return new ColorCell();
			});
		}
		else if (columnData.getCellType() == EditorColumn.CellType.ENTITY) {
			column.setCellFactory((arg) -> {
				return new EntityCell();
			});
		}
		else if (columnData.getCellType() == EditorColumn.CellType.SEX) {

			// ストーリーモデルを取得（ロジックをビュー内に書き込んでいるため、ビューからメッセージを発行している）
			column.setCellFactory((arg) -> {
				CurrentStoryModelGetMessage mes = new CurrentStoryModelGetMessage();
				this.mainMessenger.send(mes);
				return new SexCell(mes.storyModelProperty().get().getSexDAO().modelListProperty().get());
			});
		}
		else {
			column.setCellFactory((arg) -> {
				return new NormalCell();
			});
		}

		this.getColumns().add(column);
	}
}
