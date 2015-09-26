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
// このプログラムには、tool\entitygenerate.nakoによって自動生成されたコードが含まれます。
// author: KMY
package jstorybook.model.entity;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import jstorybook.common.contract.EntityType;

// ID のエンティティを扱うクラス
public abstract class Entity implements Comparable<Entity> {

	protected LongProperty id = new SimpleLongProperty(0);
	protected final StringProperty title = new SimpleStringProperty();
	protected StringProperty note = new SimpleStringProperty("");

	public Entity () {
	}

	public Entity (Long id) {
		this();
		this.id.set(id);
	}

	// -------------------------------------------------------
	// ロジック
	// -------------------------------------------------------
	// 新規作成中？
	public boolean isCreating () {
		return this.id.get() == 0;
	}

	public boolean isSortable (Entity other) {
		return this instanceof ISortableEntity;
	}

	abstract public EntityType getEntityType ();

	protected int propertyHashCode (ObservableValue o) {
		return o.getValue() != null ? o.getValue().hashCode() : 0;
	}

	protected boolean equalsProperty (ObservableValue o1, ObservableValue o2) {
		return this.equalsObject(o1.getValue(), o2.getValue());
	}

	protected boolean equalsObject (Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			return o1.equals(o2);
		}
		else {
			return false;
		}
	}

	protected int compareProperty (ObservableValue<? extends Comparable> o1, ObservableValue<? extends Comparable> o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 != null && o2 == null) {
			return 1;
		}
		if (o1 == null && o2 != null) {
			return -1;
		}
		return this.compareObject(o1.getValue(), o2.getValue());
	}

	protected <T extends Comparable> int compareObject (T o1, T o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 != null && o2 == null) {
			return 1;
		}
		if (o1 == null && o2 != null) {
			return -1;
		}
		return o1.compareTo(o2);
	}

	// -------------------------------------------------------

	public LongProperty idProperty () {
		return this.id;
	}

	public ReadOnlyStringProperty titleProperty () {
		return this.title;
	}

	public StringProperty noteProperty () {
		return this.note;
	}

	@Override
	public boolean equals (Object obj) {
		boolean ret = super.equals(obj);
		if (ret) {
			Entity test = (Entity) obj;
			ret &= this.equalsProperty(this.id, test.id);
			ret &= this.equalsProperty(this.note, test.note);
		}
		return ret;
	}

	// -------------------------------------------------------
	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.id);
		hash = hash * 31 + this.propertyHashCode(this.note);
		return hash;
	}

	@Override
	public int compareTo (Entity obj) {
		if (obj == this) {
			return 0;
		}
		if (obj == null) {
			return 1;
		}
		return this.id.getValue().compareTo(obj.id.getValue());
	}

	@Override
	public abstract String toString ();

	public final void copyTo (Entity obj) {
		obj.id.set(this.id.get());
		if (!obj.title.isBound()) {
			obj.title.set(this.title.get());
		}
		obj.note.set(this.note.get());
	}

	public abstract Entity entityClone ();

}
