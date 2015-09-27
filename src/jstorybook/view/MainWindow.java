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
package jstorybook.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jstorybook.common.contract.DialogResult;
import jstorybook.common.contract.EntityType;
import jstorybook.common.contract.PreferenceKey;
import jstorybook.common.contract.SystemKey;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.control.DockableAreaGroupPane;
import jstorybook.view.control.DockablePane;
import jstorybook.view.control.DockableTab;
import jstorybook.view.control.DockableTabPane;
import jstorybook.view.dialog.AboutDialog;
import jstorybook.view.dialog.NewStoryDialog;
import jstorybook.view.dialog.PreferenceDialog;
import jstorybook.view.dialog.ProgressDialog;
import jstorybook.view.dialog.StorySettingDialog;
import jstorybook.view.pane.IReloadable;
import jstorybook.view.pane.chart.AssociationChartPane;
import jstorybook.view.pane.chart.PersonUsingChartPane;
import jstorybook.view.pane.editor.EntityEditorPane;
import jstorybook.view.pane.list.ChapterListPane;
import jstorybook.view.pane.list.EntityListPane;
import jstorybook.view.pane.list.GroupListPane;
import jstorybook.view.pane.list.PersonListPane;
import jstorybook.view.pane.list.PlaceListPane;
import jstorybook.view.pane.list.SceneListPane;
import jstorybook.viewmodel.ApplicationViewModel;
import jstorybook.viewmodel.StoryViewModel;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.completer.EditorPaneTitleCompleter;
import jstorybook.viewtool.completer.WindowTitleCompleter;
import jstorybook.viewtool.messenger.ApplicationQuitMessage;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.MainWindowClearMessage;
import jstorybook.viewtool.messenger.MainWindowResetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.AboutDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.NewStoryDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.OpenFileChooserMessage;
import jstorybook.viewtool.messenger.dialog.PreferenceDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.ProgressDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.StorySettingDialogShowMessage;
import jstorybook.viewtool.messenger.exception.StoryFileLoadFailedMessage;
import jstorybook.viewtool.messenger.exception.StoryFileSaveFailedMessage;
import jstorybook.viewtool.messenger.general.DeleteDialogMessage;
import jstorybook.viewtool.messenger.pane.AllTabReloadMessage;
import jstorybook.viewtool.messenger.pane.ChapterEditorShowMessage;
import jstorybook.viewtool.messenger.pane.ChapterListShowMessage;
import jstorybook.viewtool.messenger.pane.EntityEditorCloseMessage;
import jstorybook.viewtool.messenger.pane.EntityEditorShowMessage;
import jstorybook.viewtool.messenger.pane.EntityListNoSelectMessage;
import jstorybook.viewtool.messenger.pane.GroupEditorShowMessage;
import jstorybook.viewtool.messenger.pane.GroupListShowMessage;
import jstorybook.viewtool.messenger.pane.PersonEditorShowMessage;
import jstorybook.viewtool.messenger.pane.PersonListShowMessage;
import jstorybook.viewtool.messenger.pane.PlaceEditorShowMessage;
import jstorybook.viewtool.messenger.pane.PlaceListShowMessage;
import jstorybook.viewtool.messenger.pane.SceneEditorShowMessage;
import jstorybook.viewtool.messenger.pane.SceneListShowMessage;
import jstorybook.viewtool.messenger.pane.chart.AssociationChartShowMessage;
import jstorybook.viewtool.messenger.pane.chart.PersonUsingChartShowMessage;
import jstorybook.viewtool.model.EditorColumnList;

/**
 *
 * @author KMY
 */
public class MainWindow extends MyStage {

	private final Messenger messenger = new Messenger();
	private final WindowTitleCompleter titleCompleter = new WindowTitleCompleter();

	private final ViewModelList viewModelList = new ViewModelList(new StoryViewModel(), new ApplicationViewModel());
	private final ObjectProperty<DockablePane> mainPane = new SimpleObjectProperty<>();
	private final ObjectProperty<DockableAreaGroupPane> rootGroupPane = new SimpleObjectProperty<>();
	private final ObjectProperty<DockableTabPane> activeTabPane = new SimpleObjectProperty<>();
	private final ObjectProperty<MenuBar> mainMenuBar = new SimpleObjectProperty<>();
	private final ObjectProperty<ToolBar> mainToolBar = new SimpleObjectProperty<>();

	public MainWindow (Stage parent) {
		super(parent);

		Pane root = new VBox();

		// メインメニューを作成
		this.modelingMainMenuBar();
		root.getChildren().add(this.mainMenuBar.get());
		FontManager.getInstance().fontStyleProperty().addListener((obj) -> {
			int lastIndex = root.getChildren().indexOf(this.mainMenuBar.get());
			if (lastIndex >= 0) {
				this.modelingMainMenuBar();
				root.getChildren().set(lastIndex, this.mainMenuBar.get());
			}
		});

		// -------------------------------------------------------

		// メインツールバーを作成
		this.modelingMainToolBar();
		root.getChildren().add(this.mainToolBar.get());

		// -------------------------------------------------------

		// メインパネルを作成・取得
		this.mainPane.set(new DockablePane(this));
		this.rootGroupPane.bind(this.mainPane.get().rootGroupProperty());

		// メインパネルのマージンを設定
		VBox.setVgrow(this.mainPane.get(), Priority.ALWAYS);
		GUIUtil.setAnchor(this.mainPane.get(), 0.0);
		root.getChildren().add(this.mainPane.get());

		// メインパネルを設定
		GUIUtil.setAnchor(root, 0.0);

		// -------------------------------------------------------
		// プログラム終了時の処理を登録
		this.setOnCloseRequest((ev) -> {
			this.quitApplication();
		});

		// -------------------------------------------------------
		// メッセンジャを設定
		this.applyMessenger();

		// -------------------------------------------------------
		// ウィンドウタイトルを設定
		this.titleCompleter.appTitleProperty().set(SystemKey.SYSTEM_NAME.getValue().toString());
		this.titleCompleter.versionProperty().set(SystemKey.SYSTEM_VERSION.getValue().toString());
		this.titleCompleter.storyFileNameProperty().bind(this.viewModelList.getProperty("storyFileName"));
		this.titleCompleter.storyNameProperty().bind(this.viewModelList.getProperty("storyName"));
		this.titleProperty().bind(this.titleCompleter.titleProperty());

		// -------------------------------------------------------
		// シーンを設定
		Scene scene = new Scene(root, (Integer) PreferenceKey.WINDOW_WIDTH.getDefaultValue(),
								(Integer) PreferenceKey.WINDOW_HEIGHT.getDefaultValue());
		scene.getStylesheets().add(ResourceManager.getCss("default.css"));
		this.setScene(scene);

		// テスト
		//this.viewModelList.getProperty("storyFileName").setValue("teststory/test.db");
	}

	// メインメニューバーを作成
	private void modelingMainMenuBar () {
		MenuBar menuBar = new MenuBar();
		MenuItem menu;

		// アプリメニュー
		Menu appMenu = new Menu(ResourceManager.getMessage("msg.menu.app"));
		GUIUtil.bindFontStyle(appMenu);
		{
			menu = GUIUtil.createMenuItem(this.viewModelList, "preference");
			menu.setText(ResourceManager.getMessage("msg.preference"));
			menu.setGraphic(ResourceManager.getMiniIconNode("setting.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shortcut+."));
			appMenu.getItems().add(menu);
			menu = GUIUtil.createMenuItem(this.viewModelList, "about");
			menu.setText(ResourceManager.getMessage("msg.app.about"));
			appMenu.getItems().add(menu);

			appMenu.getItems().add(new SeparatorMenuItem());

			menu = GUIUtil.createMenuItem(this.viewModelList, "exit");
			menu.setText(ResourceManager.getMessage("msg.exit"));
			menu.setGraphic(ResourceManager.getMiniIconNode("exit.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shortcut+Q"));
			appMenu.getItems().add(menu);
		}

		// ファイルメニュー
		Menu fileMenu = new Menu(ResourceManager.getMessage("msg.story"));
		GUIUtil.bindFontStyle(fileMenu);
		{
			menu = GUIUtil.createMenuItem(this.viewModelList, "newStory");
			menu.setText(ResourceManager.getMessage("msg.new.story"));
			menu.setGraphic(ResourceManager.getMiniIconNode("new.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shortcut+N"));
			fileMenu.getItems().add(menu);
			menu = GUIUtil.createMenuItem(this.viewModelList, "loadStory");
			menu.setText(ResourceManager.getMessage("msg.menu.load.story"));
			menu.setGraphic(ResourceManager.getMiniIconNode("open.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shortcut+O"));
			fileMenu.getItems().add(menu);
			menu = GUIUtil.createMenuItem(this.viewModelList, "save");
			menu.setText(ResourceManager.getMessage("msg.story.save"));
			menu.setGraphic(ResourceManager.getMiniIconNode("save.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shortcut+S"));
			fileMenu.getItems().add(menu);
			menu = GUIUtil.createMenuItem(this.viewModelList, "close");
			menu.setText(ResourceManager.getMessage("msg.story.close"));
			menu.setAccelerator(KeyCombination.valueOf("Shortcut+W"));
			fileMenu.getItems().add(menu);
			fileMenu.getItems().add(new SeparatorMenuItem());
			menu = new MenuItem();
			menu = GUIUtil.createMenuItem(this.viewModelList, "storySetting");
			menu.setText(ResourceManager.getMessage("msg.story.setting"));
			fileMenu.getItems().add(menu);
		}

		// 編集メニュー
		Menu editMenu = new Menu(ResourceManager.getMessage("msg.edit"));
		GUIUtil.bindFontStyle(editMenu);
		{
			menu = GUIUtil.createMenuItem(this.viewModelList, "showPersonList");
			menu.setText(ResourceManager.getMessage("msg.person"));
			menu.setGraphic(ResourceManager.getMiniIconNode("person.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shift+P"));
			editMenu.getItems().add(menu);
			menu = GUIUtil.createMenuItem(this.viewModelList, "showGroupList");
			menu.setText(ResourceManager.getMessage("msg.group"));
			menu.setGraphic(ResourceManager.getMiniIconNode("group.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shift+G"));
			editMenu.getItems().add(menu);
			menu = GUIUtil.createMenuItem(this.viewModelList, "showPlaceList");
			menu.setText(ResourceManager.getMessage("msg.place"));
			menu.setGraphic(ResourceManager.getMiniIconNode("place.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shift+L"));
			editMenu.getItems().add(menu);

			editMenu.getItems().add(new SeparatorMenuItem());

			menu = GUIUtil.createMenuItem(this.viewModelList, "showSceneList");
			menu.setText(ResourceManager.getMessage("msg.scene"));
			menu.setGraphic(ResourceManager.getMiniIconNode("scene.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shift+S"));
			editMenu.getItems().add(menu);
			menu = GUIUtil.createMenuItem(this.viewModelList, "showChapterList");
			menu.setText(ResourceManager.getMessage("msg.chapter"));
			menu.setGraphic(ResourceManager.getMiniIconNode("chapter.png"));
			menu.setAccelerator(KeyCombination.valueOf("Shift+C"));
			editMenu.getItems().add(menu);
		}

		// チャートメニュー
		Menu chartMenu = new Menu(ResourceManager.getMessage("msg.chart"));
		GUIUtil.bindFontStyle(chartMenu);
		{
			menu = GUIUtil.createMenuItem(this.viewModelList, "personUsingChart");
			menu.setText(ResourceManager.getMessage("msg.personusing"));
			chartMenu.getItems().add(menu);
		}

		menuBar.getMenus().addAll(appMenu, fileMenu, editMenu, chartMenu);
		menuBar.useSystemMenuBarProperty().bind(PreferenceKey.MENUBAR_USESYSTEM.getProperty());
		this.mainMenuBar.set(menuBar);
	}

	// メインツールバーを作成
	private void modelingMainToolBar () {
		ToolBar toolBar = new ToolBar();
		List<Node> buttonList = new ArrayList<>();

		Button button = null;
		{
			button = GUIUtil.createCommandButton(this.viewModelList, "newStory", ResourceManager.getMessage("msg.new.story"));
			button.setGraphic(ResourceManager.getIconNode("new.png"));
			buttonList.add(button);
			button = GUIUtil.createCommandButton(this.viewModelList, "loadStory", ResourceManager.getMessage("msg.menu.load.story"));
			button.setGraphic(ResourceManager.getIconNode("open.png"));
			buttonList.add(button);
			button = GUIUtil.createCommandButton(this.viewModelList, "save", ResourceManager.getMessage("msg.save"));
			button.setGraphic(ResourceManager.getIconNode("save.png"));
			buttonList.add(button);
			buttonList.add(new Separator());
			button = GUIUtil.createCommandButton(this.viewModelList, "showPersonList", ResourceManager.getMessage("msg.person"));
			button.setGraphic(ResourceManager.getIconNode("person.png"));
			buttonList.add(button);
			button = GUIUtil.createCommandButton(this.viewModelList, "showGroupList", ResourceManager.getMessage("msg.group"));
			button.setGraphic(ResourceManager.getIconNode("group.png"));
			buttonList.add(button);
			button = GUIUtil.createCommandButton(this.viewModelList, "showPlaceList", ResourceManager.getMessage("msg.place"));
			button.setGraphic(ResourceManager.getIconNode("place.png"));
			buttonList.add(button);
			button = GUIUtil.createCommandButton(this.viewModelList, "showSceneList", ResourceManager.getMessage("msg.scene"));
			button.setGraphic(ResourceManager.getIconNode("scene.png"));
			buttonList.add(button);
			button = GUIUtil.createCommandButton(this.viewModelList, "showChapterList", ResourceManager.getMessage("msg.chapter"));
			button.setGraphic(ResourceManager.getIconNode("chapter.png"));
			buttonList.add(button);
		}

		toolBar.getItems().addAll(buttonList);
		this.mainToolBar.set(toolBar);
	}

	// メッセンジャを設定
	private void applyMessenger () {
		this.messenger.apply(AllTabReloadMessage.class, this, (ev) -> {
			MainWindow.this.reloadTab();
		});
		this.messenger.apply(ApplicationQuitMessage.class, this, (ev) -> {
			MainWindow.this.quitApplication();
		});
		this.messenger.apply(AboutDialogShowMessage.class, this, (ev) -> {
			MainWindow.this.showAboutDialog();
		});
		this.messenger.apply(PreferenceDialogShowMessage.class, this, (ev) -> {
			MainWindow.this.showPreferenceDialog();
		});
		this.messenger.apply(NewStoryDialogShowMessage.class, this, (ev) -> {
			MainWindow.this.showNewStoryDialog();
		});
		this.messenger.apply(StorySettingDialogShowMessage.class, this, (ev) -> {
			MainWindow.this.showStorySettingDialog();
		});
		this.messenger.apply(ProgressDialogShowMessage.class, this, (ev) -> {
			MainWindow.this.showProgress(((ProgressDialogShowMessage) ev));
		});
		this.messenger.apply(CurrentStoryModelGetMessage.class, this, (ev) -> {
			MainWindow.this.mountCurrentStoryModel((CurrentStoryModelGetMessage) ev);
		});
		this.messenger.apply(StoryFileLoadFailedMessage.class, this, (ev) -> {
			MainWindow.this.showErrorMessage(
					ResourceManager.getMessage("msg.storyfile.failed", ((StoryFileLoadFailedMessage) ev).filePathProperty().get()));
		});
		this.messenger.apply(StoryFileSaveFailedMessage.class, this, (ev) -> {
			MainWindow.this.showErrorMessage(
					ResourceManager.getMessage("msg.storyfile.save.failed", ((StoryFileSaveFailedMessage) ev).
											   getFilePath()));
		});
		this.messenger.apply(EntityEditorCloseMessage.class, this, (ev) -> {
			MainWindow.this.removeEntityEditorTab((EntityEditorCloseMessage) ev);
		});
		this.messenger.apply(OpenFileChooserMessage.class, this, (ev) -> {
			this.openFileDialog((OpenFileChooserMessage) ev);
		});
		this.messenger.apply(DeleteDialogMessage.class, this, (ev) -> {
			this.deleteDialog((DeleteDialogMessage) ev);
		});
		this.messenger.apply(EntityListNoSelectMessage.class, this, (ev) -> {
			this.entityListPaneNoSelect((EntityListNoSelectMessage) ev);
		});
		this.messenger.apply(MainWindowResetMessage.class, this, (ev) -> {
			this.resetTab();
		});
		this.messenger.apply(MainWindowClearMessage.class, this, (ev) -> {
			this.clearTab();
		});

		this.messenger.apply(PersonListShowMessage.class, this, (ev) -> {
			MainWindow.this.addPersonListTab();
		});
		this.messenger.apply(GroupListShowMessage.class, this, (ev) -> {
			MainWindow.this.addGroupListTab();
		});
		this.messenger.apply(PlaceListShowMessage.class, this, (ev) -> {
			MainWindow.this.addPlaceListTab();
		});
		this.messenger.apply(SceneListShowMessage.class, this, (ev) -> {
			MainWindow.this.addSceneListTab();
		});
		this.messenger.apply(ChapterListShowMessage.class, this, (ev) -> {
			MainWindow.this.addChapterListTab();
		});

		this.messenger.apply(PersonEditorShowMessage.class, this, (ev) -> {
			MainWindow.this.addPersonEditorTab((PersonEditorShowMessage) ev);
		});
		this.messenger.apply(GroupEditorShowMessage.class, this, (ev) -> {
			MainWindow.this.addGroupEditorTab((GroupEditorShowMessage) ev);
		});
		this.messenger.apply(PlaceEditorShowMessage.class, this, (ev) -> {
			MainWindow.this.addPlaceEditorTab((PlaceEditorShowMessage) ev);
		});
		this.messenger.apply(SceneEditorShowMessage.class, this, (ev) -> {
			MainWindow.this.addSceneEditorTab((SceneEditorShowMessage) ev);
		});
		this.messenger.apply(ChapterEditorShowMessage.class, this, (ev) -> {
			MainWindow.this.addChapterEditorTab((ChapterEditorShowMessage) ev);
		});

		this.messenger.apply(AssociationChartShowMessage.class, this, (ev) -> {
			MainWindow.this.addAssociationChartTab((AssociationChartShowMessage) ev);
		});
		this.messenger.apply(PersonUsingChartShowMessage.class, this, (ev) -> {
			MainWindow.this.addPersonUsingChartTab((PersonUsingChartShowMessage) ev);
		});

		this.viewModelList.storeMessenger(this.messenger);
	}

	// -------------------------------------------------------
	// 表示タブを初期状態にリセット
	private void resetTab () {
		this.clearTab();
		this.addPersonListTab();
		this.addGroupListTab();
		this.addPlaceListTab();
		this.addSceneListTab();
		this.addChapterListTab();
		this.addPersonListTab();
	}

	// タブを追加
	private void addTab (DockableTab tab) {

		// フォントを設定
		GUIUtil.bindFontStyle(tab);

		// アクティブなTabPaneを探す、なければてきとーなTabPaneをアクティブにする
		// そもそもTabPaneが全く無ければ、新しく作ってしまう
		if (this.activeTabPane.get() == null) {
			// 何かTabPaneがないか探す
			for (Node node : this.rootGroupPane.get().getItems()) {
				if (node instanceof DockableTabPane) {
					this.activeTabPane.set((DockableTabPane) node);
					break;
				}
			}
		}
		if (this.activeTabPane.get() == null) {
			this.activeTabPane.set(this.rootGroupPane.get().add(0));
		}

		// タブを作って、一番前に表示する
		this.activeTabPane.get().getTabs().add(tab);
		this.activeTabPane.get().setOnMouseClicked((obj) -> {
			MainWindow.this.activeTabPane.set((DockableTabPane) obj.getSource());
		});
		this.activeTabPane.get().getSelectionModel().select(tab);
	}

	// タブを全部消す
	private void clearTab () {
		for (Node node : this.rootGroupPane.get().getItems()) {
			if (node instanceof DockableTabPane) {
				((DockableTabPane) node).removeTabPane();
			}
		}
	}

	// タブを全部リロード
	private void reloadTab () {
		for (DockableTabPane dtabPane : this.rootGroupPane.get().getTabPaneList()) {
			for (Tab dtab : dtabPane.getTabs()) {
				if (dtab instanceof IReloadable) {
					((IReloadable) dtab).reload();
				}
			}
		}
	}

	// エンティティリストタブを追加
	private void addEntityListTab (EntityListPane tab) {
		EntityListPane otherTab = this.findEntityListPane(tab.getEntityType());
		if (otherTab != null) {
			otherTab.getTabPane().getSelectionModel().select(otherTab);
		}
		else {
			tab.setViewModelList(this.viewModelList);
			this.addTab(tab);
		}
	}

	// 指定したエンティティリストと同じリストタブを探す
	private EntityListPane findEntityListPane (EntityType entityType) {
		for (DockableTabPane dtabPane : this.rootGroupPane.get().getTabPaneList()) {
			for (Tab dtab : dtabPane.getTabs()) {
				if (dtab instanceof EntityListPane) {
					if (((EntityListPane) dtab).getEntityType() == entityType) {
						return (EntityListPane) dtab;
					}
				}
			}
		}
		return null;
	}

	// 指定した種類のエンティティリストで、何も選ばない
	private void entityListPaneNoSelect (EntityListNoSelectMessage mes) {
		EntityListPane tab = this.findEntityListPane(mes.getEntityType());
		if (tab != null) {
			tab.noSelect();
		}
	}

	// 登場人物リストタブを追加
	private void addPersonListTab () {
		EntityListPane tab = new PersonListPane();
		tab.setGraphic(ResourceManager.getMiniIconNode("person.png"));
		this.addEntityListTab(tab);
	}

	// 集団リストタブを追加
	private void addGroupListTab () {
		EntityListPane tab = new GroupListPane();
		tab.setGraphic(ResourceManager.getMiniIconNode("group.png"));
		this.addEntityListTab(tab);
	}

	// 場所リストタブを追加
	private void addPlaceListTab () {
		EntityListPane tab = new PlaceListPane();
		tab.setGraphic(ResourceManager.getMiniIconNode("place.png"));
		this.addEntityListTab(tab);
	}

	// シーンリストタブを追加
	private void addSceneListTab () {
		EntityListPane tab = new SceneListPane();
		tab.setGraphic(ResourceManager.getMiniIconNode("scene.png"));
		this.addEntityListTab(tab);
	}

	// 章リストタブを追加
	private void addChapterListTab () {
		EntityListPane tab = new ChapterListPane();
		tab.setGraphic(ResourceManager.getMiniIconNode("chapter.png"));
		this.addEntityListTab(tab);
	}

	// エンティティ編集タブ
	private void addEntityEditorTab (EntityEditorShowMessage<?> message, String entityTypeName) {

		// すでに同じエンティティの編集タブが開いてないか？
		EntityEditorPane otherTab = this.findEntityEditorPane(message.columnListProperty().get());
		if (otherTab != null) {
			otherTab.getTabPane().getSelectionModel().select(otherTab);
			return;
		}

		// タブを作成
		EntityEditorPane tab = new EntityEditorPane(this.messenger);
		tab.setViewModelList(this.viewModelList);
		EditorPaneTitleCompleter completer = new EditorPaneTitleCompleter();
		completer.setEntityTypeName(entityTypeName);

		// バインド
		tab.columnListProperty().bind(message.columnListProperty());
		tab.baseColumnListProperty().bind(message.baseColumnListProperty());
		completer.bindEntityTitle(message.columnListProperty().get().titleProperty());

		// タブの表示設定
		tab.textProperty().bind(completer.titleProperty());
		this.addTab(tab);

	}

	// エンティティ編集タブを閉じる
	private void removeEntityEditorTab (EntityEditorCloseMessage message) {
		EntityEditorPane tab = this.findEntityEditorPane(message.getEditorColumnList());
		if (tab != null) {
			tab.close();
		}
	}

	// 指定したエンティティリストと同じ編集タブを探す
	private EntityEditorPane findEntityEditorPane (EditorColumnList list) {
		for (DockableTabPane dtabPane : this.rootGroupPane.get().getTabPaneList()) {
			for (Tab dtab : dtabPane.getTabs()) {
				if (dtab instanceof EntityEditorPane) {
					EditorColumnList otherList = (EditorColumnList) ((EntityEditorPane) dtab).columnListProperty().
							get();
					if (otherList.isEqualEntity(list)) {
						return (EntityEditorPane) dtab;
					}
				}
			}
		}
		return null;
	}

	// 登場人物編集タブ
	private void addPersonEditorTab (PersonEditorShowMessage message) {
		this.addEntityEditorTab(message, ResourceManager.getMessage("msg.edit.person"));
	}

	// 集団編集タブ
	private void addGroupEditorTab (GroupEditorShowMessage message) {
		this.addEntityEditorTab(message, ResourceManager.getMessage("msg.edit.group"));
	}

	// 場所編集タブ
	private void addPlaceEditorTab (PlaceEditorShowMessage message) {
		this.addEntityEditorTab(message, ResourceManager.getMessage("msg.edit.place"));
	}

	// シーン編集タブ
	private void addSceneEditorTab (SceneEditorShowMessage message) {
		this.addEntityEditorTab(message, ResourceManager.getMessage("msg.edit.scene"));
	}

	// 章編集タブ
	private void addChapterEditorTab (ChapterEditorShowMessage message) {
		this.addEntityEditorTab(message, ResourceManager.getMessage("msg.edit.chapter"));
	}

	// -------------------------------------------------------
	// 関連図
	private void addAssociationChartTab (AssociationChartShowMessage message) {
		this.addTab(new AssociationChartPane(message, this.messenger));
	}

	// 登場人物の使用状況
	private void addPersonUsingChartTab (PersonUsingChartShowMessage message) {
		this.addTab(new PersonUsingChartPane(this.messenger));
	}

	// -------------------------------------------------------
	// 削除確認ダイアログ
	private void deleteDialog (DeleteDialogMessage message) {
		DialogResult result = this.askYesNo(ResourceManager.getMessage("msg.confirm.delete", message.getTargetName()));
		message.setResult(result);
	}

	// -------------------------------------------------------
	// ストーリーモデルを返す
	private void mountCurrentStoryModel (CurrentStoryModelGetMessage message) {
		message.storyModelProperty().bind(this.viewModelList.getProperty("storyModel"));
	}

	// プログラムを終了
	private void quitApplication () {
		this.close();
	}

	// -------------------------------------------------------
	private void showAboutDialog () {
		AboutDialog dialog = new AboutDialog(this);
		dialog.showAndWait();
	}

	private void showPreferenceDialog () {
		PreferenceDialog dialog = new PreferenceDialog(this);
		dialog.showAndWait();
	}

	private void showNewStoryDialog () {
		NewStoryDialog dialog = new NewStoryDialog(this, this.messenger);
		dialog.showAndWait();
	}

	private void showStorySettingDialog () {
		StorySettingDialog dialog = new StorySettingDialog(this, this.messenger);
		dialog.showAndWait();
	}

	// -------------------------------------------------------

	// 一般的なエラーメッセージを表示するダイアログ
	private void showErrorMessage (String mes) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(ResourceManager.getMessage("msg.error"));
		alert.setHeaderText(ResourceManager.getMessage("msg.error.caption"));
		alert.setContentText(mes);
		alert.showAndWait();
	}

	// 進捗を表示するダイアログ
	private void showProgress (ProgressDialogShowMessage mes) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.progressProperty().bind(mes.progressProperty());
		dialog.show();
	}

	// ファイルを開くダイアログ
	private void openFileDialog (OpenFileChooserMessage mes) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jStorybook files (*.db)", "*.db"));
		File file = fileChooser.showOpenDialog(this);
		if (file != null) {
			mes.fileNameProperty().set(file.getPath());
		}
	}

	// 二択
	private DialogResult askYesNo (String mes) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(ResourceManager.getMessage("msg.confirm"));
		alert.setHeaderText(ResourceManager.getMessage("msg.confirm"));
		alert.setContentText(mes);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setDefaultButton(true);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			return DialogResult.YES;
		}
		else if (alert.getResult() == ButtonType.NO) {
			return DialogResult.NO;
		}
		return null;
	}

}
