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
import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.control.DockableTabPane;
import jstorybook.view.control.editor.SexControl;
import jstorybook.view.pane.MyPane;
import jstorybook.view.pane.editor.relation.ChapterRelationTab;
import jstorybook.view.pane.editor.relation.EntityRelationTab;
import jstorybook.view.pane.editor.relation.GroupRelationTab;
import jstorybook.view.pane.editor.relation.PersonRelationTab;
import jstorybook.view.pane.editor.relation.PlaceRelationTab;
import jstorybook.view.pane.editor.relation.SceneRelationTab;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.pane.EntityEditViewModel;
import jstorybook.viewtool.converter.LocalDateCalendarConverter;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.CloseMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnColorMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnDateMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnSexMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnTextMessage;
import jstorybook.viewtool.messenger.pane.editor.PropertyNoteSetMessage;
import jstorybook.viewtool.messenger.pane.relation.ChapterRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.ChapterRelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.ChapterRelationShowMessage;
import jstorybook.viewtool.messenger.pane.relation.GroupRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.GroupRelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.GroupRelationShowMessage;
import jstorybook.viewtool.messenger.pane.relation.PersonRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.PersonRelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.PersonRelationShowMessage;
import jstorybook.viewtool.messenger.pane.relation.PlaceRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.PlaceRelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.PlaceRelationShowMessage;
import jstorybook.viewtool.messenger.pane.relation.RelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.RelationRenewTriggerMessage;
import jstorybook.viewtool.messenger.pane.relation.RelationShowMessage;
import jstorybook.viewtool.messenger.pane.relation.SceneRelationListGetMessage;
import jstorybook.viewtool.messenger.pane.relation.SceneRelationRenewMessage;
import jstorybook.viewtool.messenger.pane.relation.SceneRelationShowMessage;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * 編集画面のパネル
 *
 * @author KMY
 */
public class EntityEditorPane extends MyPane {

	// 全ての編集画面
	private static final List<WeakReference<EntityEditorPane>> paneList = new LinkedList<>();

	private final ViewModelList viewModelList = new ViewModelList(new EntityEditViewModel());
	private ViewModelList storyViewModelList;

	private final ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>();
	private final ObjectProperty<EditorColumnList> baseColumnList = new SimpleObjectProperty<>();

	// 独自のメッセンジャを持つ
	protected final Messenger messenger = new Messenger();

	// 基本となるコントロール
	protected final AnchorPane rootPane = new AnchorPane();
	protected final TabPane tabPane = new TabPane();
	protected final VBox vbox = new VBox();
	protected final VBox mainVbox = new VBox();
	protected final GridPane mainGridPane = new GridPane();
	protected final TextArea noteArea = new TextArea();
	private List<WeakReference<Property>> editPropertyList = new ArrayList<>();

	// エンティティ関連付けのタブ
	protected PersonRelationTab personRelationTab;
	protected GroupRelationTab groupRelationTab;
	protected PlaceRelationTab placeRelationTab;
	protected SceneRelationTab sceneRelationTab;
	protected ChapterRelationTab chapterRelationTab;

	private int mainVboxRow = 0;

	public EntityEditorPane (String title) {
		super(title);

		// モデルに値を渡す
		this.viewModelList.getProperty("columnList").bind(this.columnList);
		this.viewModelList.getProperty("baseColumnList").bind(this.baseColumnList);

		// タブ全体のコンテンツの設定
		this.rootPane.setMaxWidth(365.0);
		this.rootPane.setMinWidth(365.0);
		this.setContent(this.rootPane);

		// タイトルラベル
		Label titleLabel = new Label();
		titleLabel.fontProperty().bind(FontManager.getInstance().titleFontProperty());
		titleLabel.textProperty().bind(this.textProperty());
		GUIUtil.setAnchor(titleLabel, 5.0, null, null, 10.0);
		this.rootPane.getChildren().add(titleLabel);

		// タブペイン
		GUIUtil.setAnchor(this.tabPane, 40.0, 0.0, 40.0, 0.0);
		this.rootPane.getChildren().add(this.tabPane);

		// -------------------------------------------------------
		// 基本タブの作成
		Tab mainTab = new Tab();
		mainTab.setClosable(false);
		mainTab.setText(ResourceManager.getMessage("msg.edit.base"));
		this.tabPane.getTabs().add(mainTab);

		// 基本タブ用のスクロールペイン
		ScrollPane mainScrollPane = new ScrollPane();
		mainScrollPane.setContent(this.mainVbox);
		mainTab.setContent(mainScrollPane);

		// 基本タブに乗せる編集項目（グリッド）
		this.mainVbox.getChildren().add(this.mainGridPane);
		VBox.setVgrow(this.mainVbox, Priority.ALWAYS);

		// -------------------------------------------------------
		// ノートペイン
		Tab noteTab = new Tab();
		noteTab.setClosable(false);
		noteTab.setText(ResourceManager.getMessage("msg.edit.note"));
		this.tabPane.getTabs().add(noteTab);

		// ノートペインの内容（テキストエリア）
		GUIUtil.setAnchor(this.noteArea, 5.0, 5.0, 5.0, 5.0);
		this.noteArea.setPrefSize(330.0, 460.0);
		this.noteArea.setWrapText(true);
		this.noteArea.fontProperty().bind(FontManager.getInstance().fontProperty());

		// スクロールペインのメインコンテンツとなるアンサーペイン
		AnchorPane noteAnchorPane = new AnchorPane();
		noteAnchorPane.getChildren().add(this.noteArea);
		GUIUtil.setAnchor(noteAnchorPane, 0.0);

		// ノートペイン用のスクロールペイン
		ScrollPane noteScrollPane = new ScrollPane();
		noteScrollPane.setContent(noteAnchorPane);
		GUIUtil.setAnchor(noteScrollPane, 0.0);
		noteTab.setContent(noteScrollPane);

		// -------------------------------------------------------
		// 編集画面下のボタン
		Button okButton = GUIUtil.createCommandButton(this.viewModelList, "save");
		okButton.setText(ResourceManager.getMessage("msg.edit.ok"));
		okButton.setPrefWidth(90.0);
		GUIUtil.setAnchor(okButton, null, 195.0, 15.0, null);
		this.rootPane.getChildren().add(okButton);
		Button cancelButton = GUIUtil.createCommandButton(this.viewModelList, "cancel");
		cancelButton.setText(ResourceManager.getMessage("msg.edit.cancel"));
		cancelButton.setPrefWidth(90.0);
		GUIUtil.setAnchor(cancelButton, null, 100.0, 15.0, null);
		this.rootPane.getChildren().add(cancelButton);
		Button applyButton = GUIUtil.createCommandButton(this.viewModelList, "apply");
		applyButton.setText(ResourceManager.getMessage("msg.edit.apply"));
		applyButton.setPrefWidth(90.0);
		GUIUtil.setAnchor(applyButton, null, 5.0, 15.0, null);
		this.rootPane.getChildren().add(applyButton);

		// メッセンジャを登録
		this.applyMessenger();

		// 自身をstatic配列に登録
		EntityEditorPane.paneList.add(new WeakReference<>(this));
	}

	public EntityEditorPane () {
		this("No Titled");
	}

	public void setViewModelList (ViewModelList list) {
		this.storyViewModelList = list;
	}

	// （まだ検証してないけど）循環参照によるメモリリークを予防する意味合い
	@Override
	protected void onClosed (Event ev) {
		for (WeakReference<Property> p : EntityEditorPane.this.editPropertyList) {
			if (p.get() != null) {
				p.get().unbind();
			}
		}
		this.editPropertyList.clear();
	}

	// メッセンジャの設定
	private void applyMessenger () {
		// 閉じる
		this.messenger.apply(CloseMessage.class, this, (ev) -> {
			this.close();
		});

		// テキストコントロールを追加
		this.messenger.apply(EditorColumnTextMessage.class, this, (ev) -> {
			EditorColumnTextMessage mes = (EditorColumnTextMessage) ev;
			EntityEditorPane.this.addTextEdit(mes.getColumnTitle(), mes.getProperty());
		});
		// 日付ピッカーコントロールを追加
		this.messenger.apply(EditorColumnDateMessage.class, this, (ev) -> {
			EditorColumnDateMessage mes = (EditorColumnDateMessage) ev;
			EntityEditorPane.this.addDateEdit(mes.getColumnTitle(), mes.getProperty());
		});
		// カラーピッカーコントロールを追加
		this.messenger.apply(EditorColumnColorMessage.class, this, (ev) -> {
			EditorColumnColorMessage mes = (EditorColumnColorMessage) ev;
			EntityEditorPane.this.addColorEdit(mes.getColumnTitle(), mes.getProperty());
		});
		// 性選択コントロールを追加
		this.messenger.apply(EditorColumnSexMessage.class, this, (ev) -> {
			EditorColumnSexMessage mes = (EditorColumnSexMessage) ev;
			EntityEditorPane.this.addSexEdit(mes.getColumnTitle(), mes.getProperty());
		});

		// ノートのテキストを設定
		this.messenger.apply(PropertyNoteSetMessage.class, this, (ev) -> {
			PropertyNoteSetMessage mes = (PropertyNoteSetMessage) ev;
			EntityEditorPane.this.setNoteProperty(mes);
		});

		// 関連人物タブを設定
		this.messenger.apply(PersonRelationShowMessage.class, this, (ev) -> {
			PersonRelationShowMessage mes = (PersonRelationShowMessage) ev;
			EntityEditorPane.this.addPersonRelationTab(mes);
		});
		this.messenger.apply(PersonRelationRenewMessage.class, this, (ev) -> {
			PersonRelationRenewMessage mes = (PersonRelationRenewMessage) ev;
			EntityEditorPane.this.renewPersonRelationTab(mes);
		});

		// 関連集団タブを設定
		this.messenger.apply(GroupRelationShowMessage.class, this, (ev) -> {
			GroupRelationShowMessage mes = (GroupRelationShowMessage) ev;
			EntityEditorPane.this.addGroupRelationTab(mes);
		});
		this.messenger.apply(GroupRelationRenewMessage.class, this, (ev) -> {
			GroupRelationRenewMessage mes = (GroupRelationRenewMessage) ev;
			EntityEditorPane.this.renewGroupRelationTab(mes);
		});

		// 関連場所タブを設定
		this.messenger.apply(PlaceRelationShowMessage.class, this, (ev) -> {
			PlaceRelationShowMessage mes = (PlaceRelationShowMessage) ev;
			EntityEditorPane.this.addPlaceRelationTab(mes);
		});
		this.messenger.apply(PlaceRelationRenewMessage.class, this, (ev) -> {
			PlaceRelationRenewMessage mes = (PlaceRelationRenewMessage) ev;
			EntityEditorPane.this.renewPlaceRelationTab(mes);
		});

		// 関連シーンタブを設定
		this.messenger.apply(SceneRelationShowMessage.class, this, (ev) -> {
			SceneRelationShowMessage mes = (SceneRelationShowMessage) ev;
			EntityEditorPane.this.addSceneRelationTab(mes);
		});
		this.messenger.apply(SceneRelationRenewMessage.class, this, (ev) -> {
			SceneRelationRenewMessage mes = (SceneRelationRenewMessage) ev;
			EntityEditorPane.this.renewSceneRelationTab(mes);
		});

		// 関連章タブを設定
		this.messenger.apply(ChapterRelationShowMessage.class, this, (ev) -> {
			ChapterRelationShowMessage mes = (ChapterRelationShowMessage) ev;
			EntityEditorPane.this.addChapterRelationTab(mes);
		});
		this.messenger.apply(ChapterRelationRenewMessage.class, this, (ev) -> {
			ChapterRelationRenewMessage mes = (ChapterRelationRenewMessage) ev;
			EntityEditorPane.this.renewChapterRelationTab(mes);
		});

		// 関連タブを更新するためのトリガー
		this.messenger.apply(RelationRenewTriggerMessage.class, this, (ev) -> {
			EntityEditorPane.renewRelationTab();
		});

		// ストーリーモデルを渡す
		this.messenger.apply(CurrentStoryModelGetMessage.class, this, (ev) -> {
			((CurrentStoryModelGetMessage) ev).storyModelProperty().bind(this.storyViewModelList.getProperty("storyModel"));
		});
		// 関連するエンティティのリストを渡す
		this.messenger.apply(PersonRelationListGetMessage.class, this, (ev) -> {
			if (this.personRelationTab != null) {
				((PersonRelationListGetMessage) ev).setRelationList(this.personRelationTab.getSelectedIdList());
			}
		});
		this.messenger.apply(GroupRelationListGetMessage.class, this, (ev) -> {
			if (this.groupRelationTab != null) {
				((GroupRelationListGetMessage) ev).setRelationList(this.groupRelationTab.getSelectedIdList());
			}
		});
		this.messenger.apply(PlaceRelationListGetMessage.class, this, (ev) -> {
			if (this.placeRelationTab != null) {
				((PlaceRelationListGetMessage) ev).setRelationList(this.placeRelationTab.getSelectedIdList());
			}
		});
		this.messenger.apply(SceneRelationListGetMessage.class, this, (ev) -> {
			if (this.sceneRelationTab != null) {
				((SceneRelationListGetMessage) ev).setRelationList(this.sceneRelationTab.getSelectedIdList());
			}
		});
		this.messenger.apply(ChapterRelationListGetMessage.class, this, (ev) -> {
			if (this.chapterRelationTab != null) {
				((ChapterRelationListGetMessage) ev).setRelationList(this.chapterRelationTab.getSelectedIdList());
			}
		});

		this.viewModelList.storeMessenger(this.messenger);
	}

	public void close () {
		((DockableTabPane) this.getTabPane()).removeTab(this);
	}

	// -------------------------------------------------------
	// すべての編集画面に一斉に出す処理
	private static void renewRelationTab () {
		List<WeakReference<EntityEditorPane>> deleteList = new ArrayList<>();
		for (WeakReference<EntityEditorPane> pane : EntityEditorPane.paneList) {
			if (pane.get() == null) {
				deleteList.add(pane);
			}
			else {
				pane.get().viewModelList.executeCommand("relationListRenew");
				if (pane.get().personRelationTab != null) {
					pane.get().personRelationTab.resetChanged();
				}
				if (pane.get().groupRelationTab != null) {
					pane.get().groupRelationTab.resetChanged();
				}
				if (pane.get().placeRelationTab != null) {
					pane.get().placeRelationTab.resetChanged();
				}
				if (pane.get().sceneRelationTab != null) {
					pane.get().sceneRelationTab.resetChanged();
				}
				if (pane.get().chapterRelationTab != null) {
					pane.get().chapterRelationTab.resetChanged();
				}
			}
		}
		EntityEditorPane.paneList.removeAll(deleteList);
	}

	// -------------------------------------------------------

	public ObjectProperty<EditorColumnList> columnListProperty () {
		return this.columnList;
	}

	public ObjectProperty<EditorColumnList> baseColumnListProperty () {
		return this.baseColumnList;
	}

	// -------------------------------------------------------
	// 関連付けのタブを追加

	private boolean checkCanSave () {
		boolean result = false;
		if (this.personRelationTab != null) {
			result |= this.personRelationTab.changedProperty().get();
		}
		if (this.groupRelationTab != null) {
			result |= this.groupRelationTab.changedProperty().get();
		}
		if (this.placeRelationTab != null) {
			result |= this.placeRelationTab.changedProperty().get();
		}
		if (this.sceneRelationTab != null) {
			result |= this.sceneRelationTab.changedProperty().get();
		}
		if (this.chapterRelationTab != null) {
			result |= this.chapterRelationTab.changedProperty().get();
		}
		return result;
	}

	private void addRelationTab (RelationShowMessage mes, EntityRelationTab tab, String entityListName) {
		tab.itemsProperty().bind(this.storyViewModelList.getProperty(entityListName));
		tab.setSelectedIdList(mes.getRelatedEntityIdList());
		tab.resetChanged();
		tab.changedProperty().addListener((obj) -> this.viewModelList.getProperty("canSave").setValue(this.checkCanSave()));
		this.tabPane.getTabs().add(tab);
	}

	private void renewRelationTab (RelationRenewMessage mes, EntityRelationTab tab) {
		if (tab != null) {
			tab.setSelectedIdList(mes.getRelatedEntityIdList());
		}
	}

	private void addPersonRelationTab (PersonRelationShowMessage mes) {
		this.personRelationTab = new PersonRelationTab(this.columnList.get().idProperty().get());
		this.addRelationTab(mes, this.personRelationTab, "personList");
	}

	private void renewPersonRelationTab (PersonRelationRenewMessage mes) {
		this.renewRelationTab(mes, this.personRelationTab);
	}

	private void addGroupRelationTab (GroupRelationShowMessage mes) {
		this.groupRelationTab = new GroupRelationTab(this.columnList.get().idProperty().get());
		this.addRelationTab(mes, this.groupRelationTab, "groupList");
	}

	private void renewGroupRelationTab (GroupRelationRenewMessage mes) {
		this.renewRelationTab(mes, this.groupRelationTab);
	}

	private void addPlaceRelationTab (PlaceRelationShowMessage mes) {
		this.placeRelationTab = new PlaceRelationTab(this.columnList.get().idProperty().get());
		this.addRelationTab(mes, this.placeRelationTab, "placeList");
	}

	private void renewPlaceRelationTab (PlaceRelationRenewMessage mes) {
		this.renewRelationTab(mes, this.placeRelationTab);
	}

	private void addSceneRelationTab (SceneRelationShowMessage mes) {
		this.sceneRelationTab = new SceneRelationTab(this.columnList.get().idProperty().get());
		this.addRelationTab(mes, this.sceneRelationTab, "sceneList");
	}

	private void renewSceneRelationTab (SceneRelationRenewMessage mes) {
		this.renewRelationTab(mes, this.sceneRelationTab);
	}

	private void addChapterRelationTab (ChapterRelationShowMessage mes) {
		this.chapterRelationTab = new ChapterRelationTab(this.columnList.get().idProperty().get());
		this.chapterRelationTab.setSingleSelect(mes.isSingleSelect());
		this.addRelationTab(mes, this.chapterRelationTab, "chapterList");
	}

	private void renewChapterRelationTab (ChapterRelationRenewMessage mes) {
		this.renewRelationTab(mes, this.chapterRelationTab);
	}

	// -------------------------------------------------------

	private void addEditControlRow (String title, Node editControl) {
		Label label = new Label(title);
		label.setPrefWidth(85.0);

		if (editControl instanceof Control) {
			((Control) editControl).setPrefWidth(240.0);
		}

		GridPane.
				setConstraints(label, 0, this.mainVboxRow, 1, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER,
							   new Insets(5));
		GridPane.
				setConstraints(editControl, 1, this.mainVboxRow, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS,
							   Priority.NEVER,
							   new Insets(5));
		this.mainGridPane.getChildren().addAll(label, editControl);

		this.mainVboxRow++;
	}

	// ColumnType.TEXT
	private void addTextEdit (String title, Property property) {
		TextField node = new TextField();
		node.textProperty().bindBidirectional(property);
		this.editPropertyList.add(new WeakReference<>(node.textProperty()));
		this.addEditControlRow(title, node);
	}

	// ColumnType.DATE
	private void addDateEdit (String title, Property property) {
		DatePicker node = new DatePicker();
		node.setPromptText("yyyy/MM/dd");
		
		LocalDateCalendarConverter converter = new LocalDateCalendarConverter();
		node.valueProperty().bindBidirectional(converter.localDateProperty());
		converter.calendarProperty().set((Calendar) property.getValue());
		property.bind(converter.calendarProperty());

		this.editPropertyList.add(new WeakReference<>(node.valueProperty()));
		this.editPropertyList.add(new WeakReference<>(converter.calendarProperty()));

		this.addEditControlRow(title, node);
	}

	// ColumnType.COLOR
	private void addColorEdit (String title, Property property) {
		ColorPicker node = new ColorPicker();
		node.valueProperty().bindBidirectional(property);
		this.editPropertyList.add(new WeakReference<>(node.valueProperty()));
		this.addEditControlRow(title, node);
	}

	// ColumnType.SEX
	private void addSexEdit (String title, Property property) {
		SexControl node = new SexControl();
		node.valueProperty().bindBidirectional(property);
		this.editPropertyList.add(new WeakReference<>(node.valueProperty()));
		this.addEditControlRow(title, node);
	}

	// -------------------------------------------------------
	private void setNoteProperty (PropertyNoteSetMessage message) {
		this.noteArea.textProperty().bindBidirectional(message.noteProperty());
	}

}
