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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jstorybook.common.contract.SystemKey;
import jstorybook.common.contract.PreferenceKey;
import jstorybook.common.manager.ResourceManager;
import jstorybook.view.control.DockableAreaGroupPane;
import jstorybook.view.control.DockablePane;
import jstorybook.view.control.DockableTabPane;
import jstorybook.view.pane.PersonListPane;
import jstorybook.viewmodel.MainWindowViewModel;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.action.ExitAction;
import jstorybook.viewtool.completer.WindowTitleCompleter;
import jstorybook.viewtool.messenger.ApplicationQuitMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.StoryModelCurrentGetMessage;
import jstorybook.viewtool.messenger.exception.StoryFileModelFailedMessage;

/**
 *
 * @author KMY
 */
public class MainWindow extends MyStage {

	private final Messenger messenger = new Messenger();
	private final WindowTitleCompleter titleCompleter = new WindowTitleCompleter();

	private final ViewModelList viewModelList = new ViewModelList(new MainWindowViewModel());
	private final ObjectProperty<DockablePane> mainPane = new SimpleObjectProperty<>();
	private final ObjectProperty<DockableAreaGroupPane> rootGroupPane = new SimpleObjectProperty<>();
	private final ObjectProperty<TabPane> activeTabPane = new SimpleObjectProperty<>();
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

		// メインパネルで、現在使用されているタブパネルが変わった時に通知
		DockableTabPane tabPane = rootGroupPane.get().add(0);
		tabPane.getTabs().add(new PersonListPane());
		tabPane.setOnMouseClicked((obj) -> {
			MainWindow.this.activeTabPane.set((TabPane) obj.getSource());
		});

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
		this.setScene(scene);
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
