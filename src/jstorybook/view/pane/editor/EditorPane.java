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
package jstorybook.view.pane.editor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.view.control.DockableTabPane;
import jstorybook.view.pane.MyPane;
import jstorybook.viewtool.action.pane.EntityEditorApplyAction;
import jstorybook.viewtool.action.pane.EntityEditorCancelAction;
import jstorybook.viewtool.action.pane.EntityEditorOKAction;
import jstorybook.viewtool.converter.LocalDateCalendarConverter;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.pane.PaneApplyMessage;
import jstorybook.viewtool.messenger.pane.PaneCancelMessage;
import jstorybook.viewtool.messenger.pane.PaneOKMessage;
import jstorybook.viewtool.model.EditorColumn;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * 編集画面のパネル
 *
 * @author KMY
 */
public class EditorPane extends MyPane {

	private final ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>();
	private final ObjectProperty<EditorColumnList> baseColumnList = new SimpleObjectProperty<>();

	// 独自のメッセンジャを持つ
	protected final Messenger messenger = new Messenger();

	protected final AnchorPane rootPane = new AnchorPane();
	protected final TabPane tabPane = new TabPane();
	protected final VBox vbox = new VBox();
	protected final VBox mainVbox = new VBox();
	protected final GridPane mainGridPane = new GridPane();
	private List<WeakReference<Property>> editPropertyList = new ArrayList<>();

	public EditorPane (String title) {
		super(title);
		this.rootPane.setMaxWidth(365.0);
		this.rootPane.setMinWidth(365.0);

		// 編集項目変更時の処理を指定
		this.columnList.addListener((obj) -> {
			EditorPane.this.generateEditor(((ObjectProperty<EditorColumnList>) obj).get());
		});
		this.setContent(this.rootPane);

		// タイトルラベル
		Label titleLabel = new Label("aaa");
		titleLabel.fontProperty().bind(FontManager.getInstance().titleFontProperty());
		titleLabel.textProperty().bind(this.textProperty());
		AnchorPane.setTopAnchor(titleLabel, 5.0);
		AnchorPane.setLeftAnchor(titleLabel, 10.0);
		this.rootPane.getChildren().add(titleLabel);

		// タブペイン
		AnchorPane.setTopAnchor(this.tabPane, 40.0);
		AnchorPane.setLeftAnchor(this.tabPane, 0.0);
		AnchorPane.setRightAnchor(this.tabPane, 0.0);
		AnchorPane.setBottomAnchor(this.tabPane, 40.0);
		this.rootPane.getChildren().add(this.tabPane);

		// mainVbox用のスクロールペイン
		ScrollPane mainScrollPane = new ScrollPane();
		mainScrollPane.setContent(this.mainVbox);

		// 基本タブの作成
		Tab mainTab = new Tab();
		mainTab.setContent(mainScrollPane);
		mainTab.setClosable(false);
		mainTab.setText(ResourceManager.getMessage("msg.edit.base"));
		this.tabPane.getTabs().add(mainTab);

		// 基本タブに乗せる編集項目について
		this.mainVbox.getChildren().add(this.mainGridPane);
		VBox.setVgrow(this.mainVbox, Priority.ALWAYS);

		// 編集画面下のボタン
		Button okButton = new EntityEditorOKAction(this.messenger).createButton();
		okButton.setPrefWidth(90.0);
		AnchorPane.setRightAnchor(okButton, 195.0);
		AnchorPane.setBottomAnchor(okButton, 15.0);
		this.rootPane.getChildren().add(okButton);
		Button cancelButton = new EntityEditorCancelAction(this.messenger).createButton();
		cancelButton.setPrefWidth(90.0);
		AnchorPane.setRightAnchor(cancelButton, 100.0);
		AnchorPane.setBottomAnchor(cancelButton, 15.0);
		this.rootPane.getChildren().add(cancelButton);
		Button applyButton = new EntityEditorApplyAction(this.messenger).createButton();
		applyButton.setPrefWidth(90.0);
		AnchorPane.setRightAnchor(applyButton, 5.0);
		AnchorPane.setBottomAnchor(applyButton, 15.0);
		this.rootPane.getChildren().add(applyButton);

		// メッセンジャを登録
		this.applyMessenger();
	}

	public EditorPane () {
		this("No Titled");
	}

	// （まだ検証してないけど）循環参照によるメモリリークを予防する意味合い
	@Override
	protected void onClosed (Event ev) {
		for (WeakReference<Property> p : EditorPane.this.editPropertyList) {
			if (p.get() != null) {
				p.get().unbind();
			}
		}
		this.editPropertyList.clear();
	}

	// メッセンジャの設定
	private void applyMessenger () {
		// OKボタン
		this.messenger.apply(PaneOKMessage.class, this, (ev) -> {
			// Viewからロジックを呼び出してることになるので要修正
			// （根本的な原因は、EditorColumnがviewtool.modelに置かれてることなので
			// 　かなりの修正を要するかも）
			EditorPane.this.save();
			EditorPane.this.close();
		});
		// キャンセルボタン
		this.messenger.apply(PaneCancelMessage.class, this, (ev) -> {
			this.close();
		});
		// 適用ボタン
		this.messenger.apply(PaneApplyMessage.class, this, (ev) -> {
			EditorPane.this.baseColumnList.get().copyProperty(this.columnList.get());
		});
	}

	private void save () {
		// 既存のエンティティを更新
		if (this.baseColumnList.get() != null) {
			this.baseColumnList.get().copyProperty(this.columnList.get());
		}
		// 新しくエンティティを作成
		else {
		}
	}

	private void close () {
		((DockableTabPane) this.getTabPane()).removeTab(this);
	}

	public ObjectProperty<EditorColumnList> columnListProperty () {
		return this.columnList;
	}

	public ObjectProperty<EditorColumnList> baseColumnListProperty () {
		return this.baseColumnList;
	}

	// この、めぢょっとぉ、ゎ、エディタ、をねっ、つくつくちちゃうのー！
	protected void generateEditor (EditorColumnList columnList) {
		int row = 0;
		for (EditorColumn column : columnList) {
			Label label = new Label(column.getColumnName());
			label.setPrefWidth(85.0);

			Control node = null;
			if (column.getColumnType() == EditorColumn.ColumnType.TEXT) {
				node = this.generateTextEdit(column);
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.DATE) {
				node = this.generateDateEdit(column);
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.COLOR) {
				node = this.generateColorEdit(column);
			}

			if (node != null) {
				node.setPrefWidth(240.0);
				GridPane.setConstraints(label, 0, row, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER,
										new Insets(5));
				GridPane.setConstraints(node, 1, row, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER,
										new Insets(5));
				this.mainGridPane.getChildren().addAll(label, node);
				row++;
			}
		}
	}

	// ColumnType.TEXT
	private Control generateTextEdit (EditorColumn column) {
		TextField node = new TextField();
		node.textProperty().bindBidirectional(column.getProperty());
		this.editPropertyList.add(new WeakReference<>(node.textProperty()));
		return node;
	}

	// ColumnType.DATE
	private Control generateDateEdit (EditorColumn column) {
		DatePicker node = new DatePicker();
		node.setPromptText("yyyy/MM/dd");
		
		LocalDateCalendarConverter converter = new LocalDateCalendarConverter();
		node.valueProperty().bindBidirectional(converter.localDateProperty());
		converter.calendarProperty().set((Calendar) column.getProperty().getValue());
		//converter.calendarProperty().bindBidirectional(column.getProperty());
		column.getProperty().bind(converter.calendarProperty());

		this.editPropertyList.add(new WeakReference<>(node.valueProperty()));
		this.editPropertyList.add(new WeakReference<>(converter.calendarProperty()));
		return node;
	}

	// ColumnType.COLOR
	private Control generateColorEdit (EditorColumn column) {
		ColorPicker node = new ColorPicker();
		node.valueProperty().bindBidirectional(column.getProperty());
		this.editPropertyList.add(new WeakReference<>(node.valueProperty()));
		return node;
	}

}
