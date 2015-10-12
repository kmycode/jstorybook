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

import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jstorybook.common.manager.DateTimeManager;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.view.pane.IComparablePane;
import jstorybook.view.pane.IReloadable;
import jstorybook.view.pane.MyPane;
import jstorybook.view.pane.PaneType;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.pane.chart.SceneNovelChartViewModel;
import jstorybook.viewtool.completer.EditorPaneTitleCompleter;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.ResetMessage;
import jstorybook.viewtool.messenger.pane.chart.EntityDrawMessage;
import jstorybook.viewtool.messenger.pane.chart.SceneNovelBoxCreateMessage;
import jstorybook.viewtool.messenger.pane.chart.SceneNovelChartShowMessage;

/**
 * シーン一括編集
 *
 * @author KMY
 */
public class SceneNovelChartPane extends MyPane implements IComparablePane, IReloadable {

	private Messenger messenger = new Messenger();
	private Messenger mainMessenger = Messenger.getInstance();
	private ViewModelList viewModelList = new ViewModelList(new SceneNovelChartViewModel());
	private final StringProperty chapterName;

	private VBox mainVBox = new VBox();

	public SceneNovelChartPane (SceneNovelChartShowMessage message, Messenger messenger) {
		super(ResourceManager.getMessage("msg.scenenovel"));

		EditorPaneTitleCompleter completer = new EditorPaneTitleCompleter();
		completer.setEntityTypeName(ResourceManager.getMessage("msg.scenenovel"));
		completer.bindEntityTitle(message.chapterNameProperty());
		this.textProperty().bind(completer.titleProperty());
		this.chapterName = message.chapterNameProperty();

		ScrollPane scrollPane = new ScrollPane(this.mainVBox);
		scrollPane.setPannable(true);
		this.setContent(scrollPane);

		this.mainVBox.setSpacing(10.0);

		// 真ん中に置く
		this.tabPaneProperty().addListener((obj) -> {
			InvalidationListener listener = ((obj2) -> {

				if (this.getTabPane() != null) {
					// タブの幅に合わせて横の長さを調整
					double tabWidth = this.getTabPane().getWidth();
					double width = tabWidth - 300;
					if (width < 600) {
						width = 600;
					}
					this.mainVBox.setPrefWidth(width);

					// 位置を調整
					double cx = (this.getTabPane().widthProperty().get() - width) / 2;
					if (cx < 0) {
						cx = 0;
					}
					this.mainVBox.setTranslateX(cx);
				}
			});
			if (this.getTabPane() != null) {
				this.getTabPane().widthProperty().addListener(listener);
				listener.invalidated(null);
			}
		});

		this.mainMessenger = messenger;

		this.messenger.apply(SceneNovelBoxCreateMessage.class, this, (ev) -> this.addSceneNovelBox((SceneNovelBoxCreateMessage) ev));
		this.messenger.apply(ResetMessage.class, this, (ev) -> this.reset());
		this.messenger.relay(CurrentStoryModelGetMessage.class, this, this.mainMessenger);

		this.viewModelList.storeMessenger(this.messenger);
		this.viewModelList.getProperty("chapterId").bind(message.chapterIdProperty());
		this.reload();
	}

	@Override
	public void reload () {
		this.reset();

		// タイトルの設定
		Label chapterName = new Label();
		chapterName.textProperty().bind(this.chapterName);
		chapterName.fontProperty().bind(FontManager.getInstance().titleFontProperty());
		VBox.setMargin(chapterName, new Insets(10, 5, 10, 5));
		this.mainVBox.getChildren().add(chapterName);

		this.viewModelList.executeCommand("draw");
	}

	private void reset () {
		this.mainVBox.getChildren().clear();
	}

	private void addSceneNovelBox (SceneNovelBoxCreateMessage message) {
		this.mainVBox.getChildren().add(new SceneNovelBox(message));
	}

	@Override
	public PaneType getPaneType () {
		return PaneType.SCENE_NOVEL;
	}

	@Override
	public long getPaneId () {
		return this.viewModelList.getProperty("chapterId", Long.class).getValue();
	}

	/*
	 * シーンの箱
	 */
	private static class SceneNovelBox extends TitledPane {

		private SceneNovelBox (SceneNovelBoxCreateMessage message) {
			this.setCollapsible(true);
			this.setAnimated(true);
			this.textProperty().bind(message.sceneNameProperty());
			this.fontProperty().bind(FontManager.getInstance().boldFontProperty());

			VBox vBox = new VBox();
			vBox.setSpacing(10.0);

			HBox sceneNameHBox = new HBox();
			sceneNameHBox.setAlignment(Pos.CENTER);
			vBox.getChildren().add(sceneNameHBox);

			ImageView sceneIcon = ResourceManager.getIconNode("scene.png");
			sceneNameHBox.getChildren().add(sceneIcon);

			TextField sceneName = new TextField();
			sceneName.fontProperty().bind(FontManager.getInstance().fontProperty());
			sceneName.textProperty().bindBidirectional(message.sceneNameProperty());
			sceneNameHBox.getChildren().add(sceneName);
			HBox.setHgrow(sceneName, Priority.ALWAYS);

			VBox timeVBox = new VBox();
			vBox.getChildren().add(timeVBox);

			Label startTime = new Label();
			startTime.fontProperty().bind(FontManager.getInstance().fontProperty());
			startTime.setText(ResourceManager.getMessage("msg.scene.starttime") + ": " + DateTimeManager.getInstance().
					dateTimeToString(message.getStartTime()));
			timeVBox.getChildren().add(startTime);

			Label endTime = new Label();
			endTime.fontProperty().bind(FontManager.getInstance().fontProperty());
			endTime.setText(ResourceManager.getMessage("msg.scene.endtime") + ": " + DateTimeManager.getInstance().dateTimeToString(
					message.getEndTime()));
			timeVBox.getChildren().add(endTime);

			HBox hBox = new HBox();
			hBox.setSpacing(10.0);

			// 関連エンティティをリストアップ
			ScrollPane relateScrollPane = new ScrollPane();
			relateScrollPane.setPannable(true);
			VBox relateList = new VBox();
			for (EntityDrawMessage mes : message.getPlaceList()) {
				relateList.getChildren().add(this.getRelate(mes, "place.png"));
			}
			for (EntityDrawMessage mes : message.getPersonList()) {
				relateList.getChildren().add(this.getRelate(mes, "person.png"));
			}
			for (EntityDrawMessage mes : message.getKeywordList()) {
				relateList.getChildren().add(this.getRelate(mes, "keyword.png"));
			}
			relateScrollPane.setMaxWidth(200.0);
			relateScrollPane.setMinWidth(200.0);
			relateScrollPane.setPrefWidth(200.0);
			relateList.setSpacing(6.0);
			relateScrollPane.setContent(relateList);
			hBox.getChildren().add(relateScrollPane);

			TextArea sceneText = new TextArea();
			sceneText.fontProperty().bind(FontManager.getInstance().fontProperty());
			sceneText.textProperty().bindBidirectional(message.sceneTextProperty());
			hBox.getChildren().add(sceneText);
			HBox.setHgrow(sceneText, Priority.ALWAYS);

			vBox.getChildren().add(hBox);
			this.setContent(vBox);
		}

		private HBox getRelate (EntityDrawMessage mes, String iconName) {
			HBox hBox = new HBox();
			hBox.setSpacing(3.0);

			ImageView icon = ResourceManager.getMiniIconNode(iconName);
			hBox.getChildren().add(icon);

			Label label = new Label();
			label.setText(mes.getName());
			label.fontProperty().bind(FontManager.getInstance().fontProperty());
			hBox.getChildren().add(label);

			return hBox;
		}
	}

}
