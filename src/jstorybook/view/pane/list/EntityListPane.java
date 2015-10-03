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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.model.entity.Entity;
import jstorybook.view.control.EntityTableView;
import jstorybook.view.pane.MyPane;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.model.column.EditorColumnList;

/**
 * エンティティのリストを表示するパネル
 * T : リストで扱うエンティティの型
 *
 * @author KMY
 */
public abstract class EntityListPane<T extends Entity> extends MyPane {

	private ViewModelList viewModelList;

	private final EntityType entityType;
	private final EntityTableView<T> tableView;
	private final ContextMenu contextMenu = new ContextMenu();
	protected final HBox commandButtonBar;
	private final ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>();
	private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();
	private final ObjectProperty<List<T>> selectedItemList = new SimpleObjectProperty<>(new ArrayList<T>());

	private final Messenger mainMessenger;

	protected EntityListPane (String title, EntityType entityType, Messenger messenger) {
		super(title);

		this.entityType = entityType;
		this.mainMessenger = messenger;

		this.columnList.addListener((obj) -> {
			EntityListPane.this.tableView.setColumnList(((ObjectProperty<EditorColumnList>) obj).get());
		});

		// テーブルビュー
		this.tableView = new EntityTableView<>(this.mainMessenger);
		GUIUtil.setAnchor(this.tableView, 5.0, 5.0, 55.0, 5.0);
		VBox.setMargin(this.tableView, new Insets(5.0, 5.0, 5.0, 5.0));
		VBox.setVgrow(this.tableView, Priority.ALWAYS);
		GUIUtil.bindFontStyle(this.tableView);

		// コマンドボタンバー（新規とか編集とか）
		this.commandButtonBar = new HBox();
		VBox.setMargin(this.commandButtonBar, new Insets(0, 0, 10.0, 15.0));

		// コンテンツを設定
		this.setContent(new VBox(this.tableView, this.commandButtonBar));
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

		// コンテキストメニュー作り
		this.modelingContextMenu();
		FontManager.getInstance().fontStyleProperty().addListener((obj) -> {
			this.modelingContextMenu();
		});

		// ボタン作り
		Button newButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "New");
		newButton.setText(ResourceManager.getMessage("msg.new"));
		newButton.setGraphic(ResourceManager.getIconNode("new.png"));
		newButton.setMinSize(100.0, 45.0);
		Button editButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "Edit");
		editButton.setText(ResourceManager.getMessage("msg.edit"));
		editButton.setGraphic(ResourceManager.getIconNode("edit.png"));
		editButton.setMinSize(100.0, 45.0);
		Button delButton = GUIUtil.createCommandButton(this.viewModelList, this.getEntityTypeName() + "Delete");
		delButton.setText(ResourceManager.getMessage("msg.delete"));
		delButton.setGraphic(ResourceManager.getIconNode("cancel.png"));
		delButton.setMinSize(100.0, 45.0);
		this.commandButtonBar.getChildren().addAll(newButton, editButton, delButton);

		// テーブルビューを選択した時のイベント
		this.selectedItem.bind(this.tableView.getSelectionModel().selectedItemProperty());
		this.selectedItem.addListener((obj) -> this.selectedItemList.set(this.tableView.getSelectionModel().getSelectedItems()));
		this.tableView.setOnMouseClicked((ev) -> {
			if (ev.getClickCount() >= 2) {
				this.viewModelList.executeCommand(this.getEntityTypeName() + "Edit");
			}
		});
	}

	private void modelingContextMenu () {
		this.contextMenu.getItems().clear();

		MenuItem newMenu = GUIUtil.createMenuItem(this.viewModelList, this.getEntityTypeName() + "New");
		newMenu.setText(ResourceManager.getMessage("msg.new"));
		newMenu.setGraphic(ResourceManager.getMiniIconNode("new.png"));
		MenuItem editMenu = GUIUtil.createMenuItem(this.viewModelList, this.getEntityTypeName() + "Edit");
		editMenu.setText(ResourceManager.getMessage("msg.edit"));
		editMenu.setGraphic(ResourceManager.getMiniIconNode("edit.png"));
		MenuItem delMenu = GUIUtil.createMenuItem(this.viewModelList, this.getEntityTypeName() + "Delete");
		delMenu.setText(ResourceManager.getMessage("msg.delete"));
		delMenu.setGraphic(ResourceManager.getMiniIconNode("cancel.png"));

		MenuItem upMenu = GUIUtil.createMenuItem(this.viewModelList, this.getEntityTypeName() + "Up");
		upMenu.setText(ResourceManager.getMessage("msg.order.up"));
		MenuItem downMenu = GUIUtil.createMenuItem(this.viewModelList, this.getEntityTypeName() + "Down");
		downMenu.setText(ResourceManager.getMessage("msg.order.down"));
		MenuItem resetOrder = GUIUtil.createMenuItem(this.viewModelList, this.getEntityTypeName() + "OrderReset");
		resetOrder.setText(ResourceManager.getMessage("msg.entity.order.reset"));

		MenuItem viewOnAssociation = GUIUtil.createMenuItem(this.viewModelList, this.getEntityTypeName() + "Association");
		viewOnAssociation.setText(ResourceManager.getMessage("msg.association.view"));
		this.contextMenu.getItems().addAll(newMenu, editMenu, delMenu, new SeparatorMenuItem(), upMenu, downMenu, resetOrder,
										   new SeparatorMenuItem(), viewOnAssociation);
		this.tableView.setContextMenu(contextMenu);
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

	// 何も選択しない
	public void noSelect () {
		this.tableView.getSelectionModel().clearSelection();
	}

	public boolean isEqualPane (EntityListPane other) {
		return this.columnList.get().isEqualEntity((EditorColumnList) other.columnList.get());
	}

	protected abstract String getEntityTypeName ();

	public EntityType getEntityType () {
		return this.entityType;
	}

}
