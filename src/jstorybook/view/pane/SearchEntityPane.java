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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.model.column.EditorColumnList;
import jstorybook.model.entity.Entity;
import jstorybook.view.control.EntityTableView;
import jstorybook.view.control.SelectableEntityTableView;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.pane.SearchEntityViewModel;
import jstorybook.viewtool.messenger.Messenger;

/**
 * エンティティ検索パネル
 *
 * @author KMY
 */
public class SearchEntityPane extends MyPane implements IReloadable {

	private final Messenger mainMessenger;
	private final ViewModelList viewModelList = new ViewModelList(new SearchEntityViewModel());

	// 検索条件
	private final ObjectProperty<List<EntityType>> entityTypeList = new SimpleObjectProperty<>(new ArrayList<EntityType>());
	private final ObjectProperty<List<Long>> tagList = new SimpleObjectProperty<>(new ArrayList<Long>());

	public SearchEntityPane (Messenger messenger) {
		super(ResourceManager.getMessage("msg.search.entity"));
		this.mainMessenger = messenger;
		this.viewModelList.storeMessenger(this.mainMessenger);

		// 画面を作成
		SplitPane rootPane = new SplitPane();
		this.setContent(rootPane);

		EntityTableView<Entity> tableView = new EntityTableView(this.mainMessenger);
		GUIUtil.bindFontStyle(tableView);
		tableView.itemsProperty().bind(this.viewModelList.getProperty("searchResult"));
		tableView.setColumnList(this.viewModelList.getProperty("columnList", EditorColumnList.class).getValue());
		rootPane.getItems().add(tableView);

		VBox searchPaneVBox = new VBox();
		rootPane.getItems().add(searchPaneVBox);

		Label paneTitle = new Label(ResourceManager.getMessage("msg.search.entity"));
		paneTitle.fontProperty().bind(FontManager.getInstance().titleFontProperty());
		searchPaneVBox.getChildren().add(paneTitle);

		TabPane searchWayTabPane = new TabPane();
		searchPaneVBox.getChildren().add(searchWayTabPane);
		VBox.getVgrow(searchWayTabPane);
		VBox.setVgrow(searchWayTabPane, Priority.ALWAYS);

		// 検索条件：エンティティの種類
		Tab entityTypeTab = new Tab(ResourceManager.getMessage("msg.entity.type"));
		GUIUtil.bindFontStyle(entityTypeTab);
		VBox entityTypeVBox = new VBox();
		entityTypeVBox.setSpacing(6.0);
		entityTypeVBox.getChildren()
				.addAll(this.createEntityTypeCheckBox(ResourceManager.getMessage("msg.person"), EntityType.PERSON),
						this.createEntityTypeCheckBox(ResourceManager.getMessage("msg.group"), EntityType.GROUP),
						this.createEntityTypeCheckBox(ResourceManager.getMessage("msg.place"), EntityType.PLACE),
						this.createEntityTypeCheckBox(ResourceManager.getMessage("msg.keyword"), EntityType.KEYWORD),
						this.createEntityTypeCheckBox(ResourceManager.getMessage("msg.scene"), EntityType.SCENE),
						this.createEntityTypeCheckBox(ResourceManager.getMessage("msg.chapter"), EntityType.CHAPTER),
						this.createEntityTypeCheckBox(ResourceManager.getMessage("msg.tag"), EntityType.TAG));
		entityTypeTab.setContent(entityTypeVBox);
		entityTypeTab.setClosable(false);
		searchWayTabPane.getTabs().add(entityTypeTab);

		// 検索条件：タグ
		Tab tagTab = new Tab(ResourceManager.getMessage("msg.tag"));
		SelectableEntityTableView tagTableView = new SelectableEntityTableView(this.mainMessenger);
		tagTableView.setColumnList(this.viewModelList.getProperty("tagColumnList", EditorColumnList.class).getValue());
		tagTableView.itemsProperty().bind(this.viewModelList.getProperty("tagList"));
		tagTableView.resetChanged();
		tagTableView.changedProperty().addListener((obj) -> {
			if (((BooleanProperty) obj).get()) {
				SearchEntityPane.this.tagList.set(tagTableView.getSelectedList());
				tagTableView.resetChanged();
			}
		});
		tagTab.setContent(tagTableView);
		tagTab.setClosable(false);
		searchWayTabPane.getTabs().add(tagTab);

		// データをリンク
		this.viewModelList.getProperty("entityType").bind(this.entityTypeList);
		this.viewModelList.getProperty("tagIdList").bind(this.tagList);

		// 検索
		this.viewModelList.executeCommand("search");
	}

	private CheckBox createEntityTypeCheckBox (String name, EntityType entityType) {
		CheckBox check = new CheckBox(name);
		check.fontProperty().bind(FontManager.getInstance().fontProperty());
		check.selectedProperty().addListener((obj) -> {
			boolean selected = ((BooleanProperty) obj).get();
			SearchEntityPane.this.entityTypeList.get().remove(entityType);
			if (selected) {
				SearchEntityPane.this.entityTypeList.get().add(entityType);
			}
			this.entityTypeList.get().sort(null);
			SearchEntityPane.this.viewModelList.executeCommand("search");
		});
		check.setSelected(true);
		return check;
	}

	@Override
	public void reload () {
		this.viewModelList.executeCommand("search");
	}

}
