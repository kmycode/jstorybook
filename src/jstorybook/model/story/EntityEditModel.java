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
package jstorybook.model.story;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.CloseMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnColorMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnDateMessage;
import jstorybook.viewtool.messenger.pane.editor.EditorColumnTextMessage;
import jstorybook.viewtool.model.EditorColumn;
import jstorybook.viewtool.model.EditorColumnList;

/**
 * エンティティ編集に利用するモデル
 *
 * @author KMY
 */
public class EntityEditModel implements IUseMessenger {

	private ObjectProperty<EditorColumnList> columnList = new SimpleObjectProperty<>();
	private ObjectProperty<EditorColumnList> baseColumnList = new SimpleObjectProperty<>();

	private BooleanProperty canSave = new SimpleBooleanProperty(false);

	private Messenger messenger = Messenger.getInstance();

	public EntityEditModel () {
		this.columnList.addListener((obj) -> {
			EntityEditModel.this.generateEditor();
			EntityEditModel.this.checkCanSave();
		});
		this.baseColumnList.addListener((obj) -> {
			EntityEditModel.this.checkCanSave();
		});
	}

	public ObjectProperty<EditorColumnList> columnListProperty () {
		return this.columnList;
	}

	public ObjectProperty<EditorColumnList> baseColumnListProperty () {
		return this.baseColumnList;
	}

	public StringProperty titleProperty () {
		if (this.columnList.get() != null) {
			return this.columnList.get().titleProperty();
		}
		else {
			return new SimpleStringProperty();
		}
	}

	private void generateEditor () {
		EditorColumnList list = this.columnList.get();
		if (list == null) {
			return;
		}

		for (EditorColumn column : list) {
			if (column.getColumnType() == EditorColumn.ColumnType.TEXT) {
				this.messenger.send(new EditorColumnTextMessage(column.getColumnName(), column.getProperty()));
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.DATE) {
				this.messenger.send(new EditorColumnDateMessage(column.getColumnName(), column.getProperty()));
			}
			else if (column.getColumnType() == EditorColumn.ColumnType.COLOR) {
				this.messenger.send(new EditorColumnColorMessage(column.getColumnName(), column.getProperty()));
			}
		}
	}

	// -------------------------------------------------------
	// コマンド

	public void save () {
		this.apply();
		this.messenger.send(new CloseMessage());
	}

	private void checkCanSave () {
		this.canSave.set(this.columnList.get() != null && this.baseColumnList.get() != null);
	}

	public BooleanProperty canSaveProperty () {
		return this.canSave;
	}

	public void cancel () {
		this.messenger.send(new CloseMessage());
	}

	public void apply () {
		this.baseColumnList.get().copyProperty(this.columnList.get());
	}

	// -------------------------------------------------------
	// IUseMessenger

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}