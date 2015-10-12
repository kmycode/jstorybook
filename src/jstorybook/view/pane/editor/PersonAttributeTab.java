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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jstorybook.common.manager.FontManager;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.model.column.EditorColumnList;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.pane.PersonAttributeViewModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.ResetMessage;
import jstorybook.viewtool.messenger.pane.relation.PersonAttributeColumnGroupAddMessage;

/**
 * 登場人物の属性のタブ
 *
 * @author KMY
 */
public class PersonAttributeTab extends Tab {

	private final ViewModelList viewModelList = new ViewModelList(new PersonAttributeViewModel());
	private final Messenger mainMessenger;
	private final Messenger messenger = new Messenger();

	private final VBox contents = new VBox();

	private final BooleanProperty changed = new SimpleBooleanProperty(false);

	public PersonAttributeTab (ObjectProperty<EditorColumnList> columnList, Messenger messenger) {
		super(ResourceManager.getMessage("msg.attribute"));

		this.contents.setSpacing(10.0);
		AnchorPane root2 = new AnchorPane(this.contents);
		GUIUtil.setAnchor(this.contents, 5.0, 10.0, 5.0, 10.0);
		ScrollPane root = new ScrollPane(root2);
		root.widthProperty().addListener((ev) -> root2.setPrefWidth(root.getWidth() - 30));

		this.setContent(root);
		this.setClosable(false);

		this.mainMessenger = messenger;
		this.messenger.relay(CurrentStoryModelGetMessage.class, this, this.mainMessenger);
		this.viewModelList.storeMessenger(this.messenger);

		this.messenger.apply(ResetMessage.class, this, (ev) -> this.reset());
		this.messenger.apply(PersonAttributeColumnGroupAddMessage.class, this,
							 (ev) -> this.addColumnGroup((PersonAttributeColumnGroupAddMessage) ev));
		// データをバインド
		this.viewModelList.getProperty("columnList").bind(columnList);

		this.reload();
	}

	private void reset () {
		this.contents.getChildren().clear();
	}

	public void reload () {
		this.reset();
		this.viewModelList.executeCommand("draw");
	}

	private void addColumnGroup (PersonAttributeColumnGroupAddMessage message) {
		TitledPane group = new TitledPane();
		group.setText(message.getGroupName());
		group.setCollapsible(true);
		group.setAnimated(true);
		group.fontProperty().bind(FontManager.getInstance().fontProperty());

		GridPane attributes = new GridPane();
		attributes.setHgap(10.0);
		attributes.setVgap(5.0);
		int row = 0;
		for (PersonAttributeColumnGroupAddMessage.Column column : message.getColumnList()) {
			Label columnName = new Label(column.getName());
			columnName.fontProperty().bind(FontManager.getInstance().fontProperty());
			columnName.setPrefWidth(100.0);
			GridPane.setConstraints(columnName, 0, row);

			TextField textField = new TextField();
			textField.textProperty().bindBidirectional(column.valueProperty());
			textField.textProperty().addListener((obj) -> this.changed.set(true));
			GridPane.setConstraints(textField, 1, row);
			GridPane.setHgrow(textField, Priority.ALWAYS);

			attributes.getChildren().addAll(columnName, textField);
			row++;
		}

		group.setContent(attributes);
		this.contents.getChildren().add(group);
	}

	public void save () {
		this.viewModelList.executeCommand("save");
		this.resetChanged();
	}

	public void resetChanged () {
		this.changed.set(false);
	}

	public BooleanProperty changedProperty () {
		return this.changed;
	}

}
