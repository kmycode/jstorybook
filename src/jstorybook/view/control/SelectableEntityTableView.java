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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import jstorybook.model.entity.Entity;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * 選択可能な、エンティティ専用テーブルビュー
 *
  * @author KMY
 */
public class SelectableEntityTableView<E extends Entity> extends EntityTableView<E> {

	private final Map<Long, BooleanProperty> selectedList = new HashMap<>();
	private final BooleanProperty isChanged = new SimpleBooleanProperty(false);
	private boolean lockIsChanged = false;

	public SelectableEntityTableView () {
		this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	@Override
	public void setColumnList (EditorColumnList cl) {
		super.setColumnList(cl);

		// チェックボックスを登録
		TableColumn<E, Boolean> selectColumn = new TableColumn();
		selectColumn.setCellValueFactory(
				new Callback<CellDataFeatures<E, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call (CellDataFeatures<E, Boolean> param) {
						return SelectableEntityTableView.this.selectedProperty(param.getValue().idProperty().get());
					}
				});
		selectColumn.setEditable(true);
		this.setEditable(true);
		selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
		this.getColumns().add(0, selectColumn);
	}

	public BooleanProperty selectedProperty (long entityId) {
		BooleanProperty result = this.selectedList.get(entityId);
		if (result == null) {
			result = new SimpleBooleanProperty(false);
			result.addListener((obj) -> {
				if (!SelectableEntityTableView.this.lockIsChanged) {
					SelectableEntityTableView.this.isChanged.set(true);
				}
			});
			this.selectedList.put(entityId, result);
		}
		return result;
	}

	public void setSelectedList (List<E> list) {
		this.lockIsChanged = true;
		this.resetSelected();
		for (E entity : list) {
			if (entity.idProperty().get() > 0) {
				this.selectedProperty(entity.idProperty().get()).set(true);
			}
		}
		this.lockIsChanged = false;
	}

	public void setSelectedIdList (List<Long> list) {
		this.lockIsChanged = true;
		this.resetSelected();
		for (Long entityId : list) {
			if (entityId > 0) {
				this.selectedProperty(entityId).set(true);
			}
		}
		this.lockIsChanged = false;
	}

	private void resetSelected () {
		this.selectedList.forEach((k, v) -> {
			v.set(false);
		});
	}

	public List<Long> getSelectedList () {
		List<Long> result = new ArrayList<>();
		this.selectedList.forEach((k, v) -> {
			if (v.get()) {
				result.add(k);
			}
		});
		return result;
	}

	public ReadOnlyBooleanProperty changedProperty () {
		return this.isChanged;
	}

	public void resetChanged () {
		this.isChanged.set(false);
	}
}
