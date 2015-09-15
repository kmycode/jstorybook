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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jstorybook.common.contract.PreferenceKey;
import jstorybook.common.contract.SystemKey;
import jstorybook.common.manager.ResourceManager;
import jstorybook.view.control.DockableAreaGroupPane;
import jstorybook.view.control.DockablePane;
import jstorybook.view.control.DockableTab;
import jstorybook.view.control.DockableTabPane;
import jstorybook.view.pane.editor.EditorPane;
import jstorybook.view.pane.list.PersonListPane;
import jstorybook.viewmodel.StoryViewModel;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.action.ExitAction;
import jstorybook.viewtool.completer.EditorPaneTitleCompleter;
import jstorybook.viewtool.completer.WindowTitleCompleter;
import jstorybook.viewtool.messenger.ApplicationQuitMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.StoryModelCurrentGetMessage;
import jstorybook.viewtool.messenger.exception.StoryFileModelFailedMessage;
import jstorybook.viewtool.messenger.pane.EntityEditorShowMessage;
import jstorybook.viewtool.messenger.pane.PersonEditorShowMessage;
import jstorybook.viewtool.model.EditorColumnList;

/**
 *
 * @author KMY
 */
public class MainWindow extends MyStage {

	private final Messenger messenger = new Messenger();
	private final WindowTitleCompleter titleCompleter = new WindowTitleCompleter();

	private final ViewModelList viewModelList = new ViewModelList(new StoryViewModel());
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
		this.setAnchor(this.mainPane.get(), 0.0);
		root.getChildren().add(this.mainPane.get());

		// メインパネルを設定
		this.setAnchor(root, 0.0);

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
		this.titleProperty().bind(this.titleCompleter.titleProperty());

		// -------------------------------------------------------
		// シーンを設定
		Scene scene = new Scene(root, (Integer) PreferenceKey.WINDOW_WIDTH.getDefaultValue(),
								(Integer) PreferenceKey.WINDOW_HEIGHT.getDefaultValue());
		scene.getStylesheets().add(getClass().getResource("/jstorybook/resource/stylesheet/default.css").
				toExternalForm());
		this.setScene(scene);

		// TODO:【テスト】
		this.viewModelList.setProperty("storyFileName", "teststory/test.db");
		this.addPersonListTab();
		this.addPersonEditorTab(null);
	}

	// メインメニューバーを作成
	private void modelingMainMenuBar () {
		MenuBar menuBar = new MenuBar();
		MenuItem menu;

		// ファイルメニュー
		Menu fileMenu = new Menu(ResourceManager.getMessage("msg.story"));
		{
			menu = new ExitAction(this.messenger).createMenuItem();
			fileMenu.getItems().add(menu);
		}

		// 編集メニュー
		Menu editMenu = new Menu(ResourceManager.getMessage("msg.edit"));
		{
			menu = new MenuItem(ResourceManager.getMessage("msg.preference"));
			editMenu.getItems().add(menu);
		}

		menuBar.getMenus().addAll(fileMenu, editMenu);
		this.mainMenuBar.set(menuBar);
	}

	// メインツールバーを作成
	private void modelingMainToolBar () {
		ToolBar toolBar = new ToolBar();
		List<Button> buttonList = new ArrayList<>();

		buttonList.add(new Button("dummy"));

		toolBar.getItems().addAll(buttonList);
		this.mainToolBar.set(toolBar);
	}

	// メッセンジャを設定
	private void applyMessenger () {
		this.messenger.apply(ApplicationQuitMessage.class, this, (ev) -> {
			MainWindow.this.quitApplication();
		});
		this.messenger.apply(StoryModelCurrentGetMessage.class, this, (ev) -> {
			MainWindow.this.mountCurrentStoryModel((StoryModelCurrentGetMessage) ev);
		});
		this.messenger.apply(StoryFileModelFailedMessage.class, this, (ev) -> {
			MainWindow.this.showErrorMessage(ResourceManager.getMessage("msg.storyfile.failed",
																		((StoryFileModelFailedMessage) ev).
																		filePathProperty().get()));
		});
		this.messenger.apply(PersonEditorShowMessage.class, this, (ev) -> {
			MainWindow.this.addPersonEditorTab((PersonEditorShowMessage) ev);
		});
		this.viewModelList.storeMessenger(this.messenger);
	}

	// -------------------------------------------------------
	// タブを追加
	private void addTab (DockableTab tab) {

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

	// 登場人物リストタブを追加
	private void addPersonListTab () {
		PersonListPane tab = new PersonListPane();
		this.addTab(tab);
		tab.columnListProperty().bind(this.viewModelList.getProperty("personColumnList"));
		tab.itemsProperty().bind(this.viewModelList.getProperty("personList"));
		this.viewModelList.getProperty("selectedEntity").bind(tab.selectedItemProperty());
	}

	// エンティティ編集タブ
	private void addEntityEditorTab (EntityEditorShowMessage<?> message, String entityTypeName,
									 String viewModelColumnListName) {

		EditorPane tab = new EditorPane();
		EditorPaneTitleCompleter completer = new EditorPaneTitleCompleter();
		completer.entityTypeNameProperty().set(entityTypeName);

		if (message != null) {

			// すでに同じエンティティの編集タブが開いてないか？
			for (DockableTabPane dtabPane : this.rootGroupPane.get().getTabPaneList()) {
				for (Tab dtab : dtabPane.getTabs()) {
					if (dtab instanceof EditorPane) {
						EditorColumnList otherList = (EditorColumnList) ((EditorPane) dtab).columnListProperty().get();
						if (otherList.isEqualEntity(message.columnListProperty().get())) {
							// すでに開いているタブを表示し、新規タブ生成を中止する
							dtabPane.getSelectionModel().select(dtab);
							return;
						}
					}
				}
			}

			// バインド
			tab.columnListProperty().bind(message.columnListProperty());
			tab.baseColumnListProperty().bind(message.baseColumnListProperty());
			completer.entityTitleProperty().bind(message.columnListProperty().get().titleProperty());

			// こうやって get() を呼び出さないとなぜかきちんとバインディングされないorz
			// JavaFX自体のバグ？bindが複雑過ぎた？
			message.columnListProperty().get().titleProperty().addListener((obj) -> {
				completer.titleProperty().get();
			});
		}
		else {
			// 新規作成
			tab.columnListProperty().bind(this.viewModelList.getProperty(viewModelColumnListName));
		}

		tab.textProperty().bind(completer.titleProperty());
		this.addTab(tab);
	}

	// 登場人物編集タブ
	private void addPersonEditorTab (PersonEditorShowMessage message) {
		this.addEntityEditorTab(message, ResourceManager.getMessage("msg.edit.person"), "personColumnList");
	}

	// -------------------------------------------------------
	// ストーリーモデルを返す
	private void mountCurrentStoryModel (StoryModelCurrentGetMessage message) {
		message.setStoryModel(this.viewModelList.getProperty("storyModel"));
	}

	// プログラムを終了
	private void quitApplication () {
		this.close();
	}

	// 一般的なエラーメッセージを表示するダイアログ
	private void showErrorMessage (String mes) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(ResourceManager.getMessage("msg.error"));
		alert.setHeaderText(ResourceManager.getMessage("msg.error.caption"));
		alert.setTitle("Error");
		alert.setContentText(mes);
		alert.showAndWait();
	}

}
