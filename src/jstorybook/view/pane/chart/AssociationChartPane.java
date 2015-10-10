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
package jstorybook.view.pane.chart;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.pane.IReloadable;
import jstorybook.view.pane.MyPane;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.pane.chart.AssociationViewModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.ResetMessage;
import jstorybook.viewtool.messenger.pane.chart.AssociationChartShowMessage;
import jstorybook.viewtool.messenger.pane.chart.ChapterDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.EntityDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.EntityRelateMessage;
import jstorybook.viewtool.messenger.pane.chart.GroupDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.PersonDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.PlaceDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.SceneDrawMessage;
import jstorybook.viewtool.messenger.pane.editor.ChapterEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.GroupEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.PersonEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.PlaceEditorShowMessage;
import jstorybook.viewtool.messenger.pane.editor.SceneEditorShowMessage;

/**
 * 関連図
 *
 * @author KMY
 */
public class AssociationChartPane extends MyPane implements IReloadable {

	private AnchorPane canvasArea = new AnchorPane();
	private Messenger messenger = new Messenger();
	private Messenger mainMessenger = Messenger.getInstance();
	private ViewModelList viewModelList = new ViewModelList(new AssociationViewModel());

	private boolean isEmpty = true;
	private final List<Canvas> canvasList = new ArrayList<>();

	// どの隅にどのエンティティを配置するか
	private List<EntityType> cornerList = new ArrayList<>();
	private List<Integer> cornerNumList = new ArrayList<>();

	public AssociationChartPane (AssociationChartShowMessage message, Messenger messenger) {
		super(ResourceManager.getMessage("msg.association"));

		this.mainMessenger = messenger;

		this.canvasArea.setPrefSize(700.0, 500.0);

		ScrollPane mainScrollPane = new ScrollPane(this.canvasArea);
		this.setContent(mainScrollPane);

		// メッセージを設定
		this.messenger.apply(EntityRelateMessage.class, this, (ev) -> this.releate((EntityRelateMessage) ev));
		this.messenger.apply(PersonDrawMessage.class, this, (ev) -> this.drawPerson((PersonDrawMessage) ev));
		this.messenger.apply(GroupDrawMessage.class, this, (ev) -> this.drawGroup((GroupDrawMessage) ev));
		this.messenger.apply(PlaceDrawMessage.class, this, (ev) -> this.drawPlace((PlaceDrawMessage) ev));
		this.messenger.apply(SceneDrawMessage.class, this, (ev) -> this.drawScene((SceneDrawMessage) ev));
		this.messenger.apply(ChapterDrawMessage.class, this, (ev) -> this.drawChapter((ChapterDrawMessage) ev));
		this.messenger.apply(ResetMessage.class, this, (ev) -> this.reset());

		// メッセンジャ
		this.messenger.relay(CurrentStoryModelGetMessage.class, this, this.mainMessenger);
		this.messenger.relay(AssociationChartShowMessage.class, this, this.mainMessenger);
		this.messenger.relay(PersonEditorShowMessage.class, this, this.mainMessenger);
		this.messenger.relay(GroupEditorShowMessage.class, this, this.mainMessenger);
		this.messenger.relay(PlaceEditorShowMessage.class, this, this.mainMessenger);
		this.messenger.relay(SceneEditorShowMessage.class, this, this.mainMessenger);
		this.messenger.relay(ChapterEditorShowMessage.class, this, this.mainMessenger);
		this.viewModelList.storeMessenger(this.messenger);
		this.viewModelList.getProperty("entity").bind(message.entityProperty());
	}

	@Override
	public void reload () {
		this.reset();
		this.viewModelList.executeCommand("draw");
	}

	private void reset () {
		this.canvasArea.getChildren().clear();
		this.canvasList.clear();
		this.cornerList.clear();
		this.cornerNumList.clear();
		this.isEmpty = true;
	}

	private Canvas drawNode (EntityDrawMessage message, int width, int height, String iconName, EntityType entityType) {

		Canvas canvas = new Canvas(width, height);

		// 中心となるオブジェクト
		if (this.isEmpty) {
			GUIUtil.setAnchor(canvas, (this.canvasArea.getPrefHeight() - canvas.getHeight()) / 2, null, null, (this.canvasArea.
							  getPrefWidth() - canvas.getWidth()) / 2);
			this.setText(ResourceManager.getMessage("msg.association") + " [" + message.getName() + "]");
			this.setGraphic(ResourceManager.getMiniIconNode(iconName));
		}

		// まわりのオブジェクト
		else {
			int cornerId = this.cornerList.indexOf(entityType);
			if (cornerId < 0) {
				this.cornerList.add(entityType);
				cornerId = this.cornerList.size() - 1;
				this.cornerNumList.add(0);
			}

			// コンテキストメニュー
			MenuItem showMenu = new MenuItem(ResourceManager.getMessage("msg.association"));
			showMenu.setOnAction(message.getEvent());
			GUIUtil.bindFontStyle(showMenu);
			showMenu.setGraphic(ResourceManager.getMiniIconNode(iconName));
			MenuItem editMenu = new MenuItem(ResourceManager.getMessage("msg.edit"));
			editMenu.setOnAction(message.getOptionEvent());
			GUIUtil.bindFontStyle(editMenu);
			editMenu.setGraphic(ResourceManager.getMiniIconNode("edit.png"));
			ContextMenu menu = new ContextMenu(showMenu, editMenu);
			canvas.setOnContextMenuRequested((ev) -> menu.show(canvas, ev.getScreenX(), ev.getScreenY()));

			// すでに配置されているエンティティ数
			int cornerNum = this.cornerNumList.get(cornerId);
			this.cornerNumList.set(cornerId, cornerNum + 1);

			// エンティティを何行目に配置するか
			int lineNum = cornerNum / 5;

			// とりあえず左上からの座標
			double y = lineNum * (height + 20) + 20;
			double x = (cornerNum % 5) * (width + 15) + 20;

			// 左上
			if (cornerId == 0) {
				GUIUtil.setAnchor(canvas, y, null, null, x);
			}
			// 右上
			else if (cornerId == 1) {
				GUIUtil.setAnchor(canvas, y, x, null, null);
			}
			// 右下
			else if (cornerId == 2) {
				GUIUtil.setAnchor(canvas, null, x, y, null);
			}
			// 左下
			else if (cornerId == 3) {
				GUIUtil.setAnchor(canvas, null, null, y, x);
			}
		}
		canvas.setOnMouseClicked((ev) -> {
			// 左クリック
			if (ev.getButton() == MouseButton.PRIMARY) {
				message.getEvent().handle(ev);
			}
		});
		canvas.setOnMouseEntered((ev) -> {
			canvas.setOpacity(0.5);
		});
		canvas.setOnMouseExited((ev) -> {
			for (Canvas c : this.canvasList) {
				c.setOpacity(1.0);
			}
		});

		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.setFont(FontManager.getInstance().chartFontProperty().get());
		gc.fillText(message.getName(), 0, height - 15, width);

		gc.drawImage(new Image(ResourceManager.getIconResource(iconName), width, height - 30, false, true), 0, 0);

		// IDを発行
		this.canvasList.add(canvas);
		message.setDrawId(this.canvasList.size() - 1);

		// 他のエンティティと関連付けられてたらイベントを設定
		if (message.isReleated()) {
			this.releate(new EntityRelateMessage(message.getReleatedId(), message.getDrawId()));
		}

		return canvas;
	}

	private Canvas addNode (EntityDrawMessage message, String iconName, EntityType entityType) {
		if (this.isEmpty) {
			// 中心となるエンティティを描画
			Canvas result = this.drawNode(message, 100, 130, iconName, entityType);
			this.isEmpty = false;
			return result;
		}
		else {
			// まわりのエンティティを描画
			return this.drawNode(message, 50, 80, iconName, entityType);
		}
	}

	private void releate (EntityRelateMessage message) {
		Canvas canvas = this.canvasList.get(message.getId1());
		Canvas other = this.canvasList.get(message.getId2());
		if (other != null && canvas != null) {
			EventHandler eh = canvas.getOnMouseEntered();
			canvas.setOnMouseEntered((ev) -> {
				eh.handle(ev);
				other.setOpacity(0.5);
			});
			EventHandler eh2 = other.getOnMouseEntered();
			other.setOnMouseEntered((ev) -> {
				eh2.handle(ev);
				canvas.setOpacity(0.5);
			});
		}
	}

	private void drawPerson (PersonDrawMessage message) {
		this.canvasArea.getChildren().add(this.addNode(message, "person_large.png", EntityType.PERSON));
	}

	private void drawGroup (GroupDrawMessage message) {
		this.canvasArea.getChildren().add(this.addNode(message, "group_large.png", EntityType.GROUP));
	}

	private void drawPlace (PlaceDrawMessage message) {
		this.canvasArea.getChildren().add(this.addNode(message, "place_large.png", EntityType.PLACE));
	}

	private void drawScene (SceneDrawMessage message) {
		this.canvasArea.getChildren().add(this.addNode(message, "scene_large.png", EntityType.SCENE));
	}

	private void drawChapter (ChapterDrawMessage message) {
		this.canvasArea.getChildren().add(this.addNode(message, "chapter_large.png", EntityType.CHAPTER));
	}

}
