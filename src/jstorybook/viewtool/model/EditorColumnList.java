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
package jstorybook.viewtool.model;

import java.util.ArrayList;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.contract.EntityType;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.ExceptionMessage;
import jstorybook.viewtool.messenger.Messenger;

/**
 * EditorColumn専用のリスト
 *
 * @author KMY
 */
public class EditorColumnList extends ArrayList<EditorColumn> {

	private final StringProperty title = new SimpleStringProperty();
	private final LongProperty id = new SimpleLongProperty();
	private final StringProperty note = new SimpleStringProperty();
	private final ObjectProperty<EntityType> entityType = new SimpleObjectProperty<>();
	private StoryModel.EntityAdapter entityAdapter;

	public StringProperty titleProperty () {
		return this.title;
	}

	public LongProperty idProperty () {
		return this.id;
	}

	public StringProperty noteProperty () {
		return this.note;
	}

	public ObjectProperty<EntityType> entityTypeProperty () {
		return this.entityType;
	}

	public void setEntityAdapter (StoryModel.EntityAdapter adapter) {
		this.entityAdapter = adapter;
	}

	public void copyProperty (EditorColumnList from) {
		try {
			if (from.size() != this.size()) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int i = 0; i < this.size(); i++) {
				this.get(i).getProperty().setValue(from.get(i).getProperty().getValue());
			}
			this.note.set(from.note.get());

			if (this.entityAdapter != null) {
				this.entityAdapter.addEntity();
				this.entityAdapter = null;
			}
		} catch (Throwable e) {
			Messenger.getInstance().send(new ExceptionMessage(e));
		}
	}

	public boolean isEqualEntity (EditorColumnList other) {
		return this.id.get() != 0 && this.id.get() == other.id.get() && this.entityType.get() == other.entityType.get();
	}

}
