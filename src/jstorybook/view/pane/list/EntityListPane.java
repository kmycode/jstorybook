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
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.model.entity.Entity;
import jstorybook.view.control.EntityTableView;
import jstorybook.view.pane.MyPane;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * エンティティのリストを表示するパネル
 * T : リストで扱うエンティティの型
 *
 * @author KMY
 */
public abstract class EntityListPane<T extends Entity> extends MyPane {

	private ViewModelList viewModelList;

	private final EntityTableView<T> tableView;
	protected final HBox commandButtonBar;
	private final ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>();
	private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();
	private final ObjectProperty<List<T>> selectedItemList = new SimpleObjectProperty<>(new ArrayList<T>());

	protected EntityListPane (String title) {
		super(title);

		this.columnList.addListener((obj) -> {
			EntityListPane.this.tableView.setColumnList(((ObjectProperty<EditorColumnList>) obj).get());
		});

		// テーブルビュー
		this.tableView = new EntityTableView<>();
		GUIUtil.setAnchor(this.tableView, 5.0, 5.0, 55.0, 5.0);

		// コマンドボタンバー（新規とか編集とか）
		this.commandButtonBar = new HBox();
		GUIUtil.setAnchor(this.commandButtonBar, null, null, 10.0, 15.0);

		// コンテンツを設定
		this.setContent(new AnchorPane(this.tableView, this.commandButtonBar));
	}

	// 第二のコンストラクタ。ビューモデルを設定、ボタンとかも作る
	public void setViewModelList (ViewModelList viewModelList) {
		if (this.viewModelList != null) {
			throw new RuntimeException("ViewModelList already set.");
		}

		// ビューモデル、バインドとか
		this.viewModelList = viewModelList;
		this.columnList.bind(this.viewModelList.getProperty(this.getEntityTypeName() + "ColumnList"));
		this.tableView.itemsProperty().bind(this.viewModelList.getProperty(this.getEntityTypeName() + "List"));
		this.viewModelList.getProperty(this.getEntityTypeName() + "Selected").bind(this.selectedItemList);

		// ボタン作り
		Button newButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "New");
		newButton.setText(ResourceManager.getMessage("msg.new"));
		newButton.setPrefSize(100.0, 45.0);
		Button editButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "Edit");
		editButton.setText(ResourceManager.getMessage("msg.edit"));
		editButton.setPrefSize(100.0, 45.0);
		Button delButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "Delete");
		delButton.setText(ResourceManager.getMessage("msg.delete"));
		delButton.setPrefSize(100.0, 45.0);
		this.commandButtonBar.getChildren().addAll(newButton, editButton, delButton);

		// テーブルビューを選択した時のイベント
		this.tableView.setOnMouseClicked((ev) -> {
			this.selectedItem.set(EntityListPane.this.tableView.getSelectionModel().getSelectedItem());
			this.selectedItemList.set(EntityListPane.this.tableView.getSelectionModel().getSelectedItems());
			if (ev.getClickCount() >= 2) {
				this.viewModelList.executeCommand(this.getEntityTypeName() + "Edit");
			}
		});
	}

	// ならべ替えのボタン
	protected void setOrderButton () {
		Button upButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "Up");
		upButton.setText(ResourceManager.getMessage("msg.order.up"));
		upButton.setPrefSize(50.0, 45.0);
		Button downButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "Down");
		downButton.setText(ResourceManager.getMessage("msg.order.down"));
		downButton.setPrefSize(50.0, 45.0);
		this.commandButtonBar.getChildren().addAll(upButton, downButton);
	}

	public boolean isEqualPane (EntityListPane other) {
		return this.columnList.get().isEqualEntity((EditorColumnList) other.columnList.get());
	}

	protected abstract String getEntityTypeName ();

}
