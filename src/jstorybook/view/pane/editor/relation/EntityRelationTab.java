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
package jstorybook.view.pane.editor.relation;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import jstorybook.common.util.GUIUtil;
import jstorybook.model.entity.Entity;
import jstorybook.view.control.SelectableEntityTableView;
import jstorybook.viewtool.messenger.Messenger;

/**
 * エンティティの関係を設定するタブ
 *
 * @author KMY
 */
public abstract class EntityRelationTab<E extends Entity> extends Tab {

	protected final SelectableEntityTableView<E> tableView;

	protected EntityRelationTab (String title, long entityId, Messenger messenger) {
		super(title);
		this.setClosable(false);

		this.tableView = new SelectableEntityTableView<>(messenger);
		GUIUtil.setAnchor(this.tableView, 5.0, 5.0, 5.0, 5.0);

		this.setContent(new AnchorPane(this.tableView));
	}

	public ObjectProperty<ObservableList<E>> itemsProperty () {
		return this.tableView.itemsProperty();
	}

	public void setSelectedIdList (List<Long> idList) {
		this.tableView.setSelectedIdList(idList);
	}

	public List<Long> getSelectedIdList () {
		return this.tableView.getSelectedList();
	}

	public ReadOnlyBooleanProperty changedProperty () {
		return this.tableView.changedProperty();
	}

	public void resetChanged () {
		this.tableView.resetChanged();
	}

	public void setSingleSelect (boolean value) {
		this.tableView.singleSelectProperty().set(value);
	}
}
