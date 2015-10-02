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
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.contract.EntityType;

// Tag のエンティティを扱うクラス
public class Tag extends Entity implements Comparable<Entity>, ISortableEntity {

	private final StringProperty name = new SimpleStringProperty("");
	private final LongProperty order = new SimpleLongProperty();

	public Tag () {
		this.title.bind(this.name);
		this.storeProperty(name, order);
	}

	@Override
	public EntityType getEntityType () {
		return EntityType.TAG;
	}

	/*
	 * 名前
	 */
	public StringProperty nameProperty () {
		return this.name;
	}

	/*
	 * 順番
	 */
	@Override
	public LongProperty orderProperty () {
		return this.order;
	}

	// -------------------------------------------------------
	@Override
	public boolean equals (Object obj) {
		boolean ret = super.equals(obj);
		if (ret) {
			Tag test = (Tag) obj;
			ret &= this.equalsProperty(this.name, test.name);
			ret &= this.equalsProperty(this.order, test.order);
		}
		return ret;
	}

	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.name);
		hash = hash * 31 + this.propertyHashCode(this.order);
		return hash;
	}

	@Override
	public int compareTo (Entity obj) {
		if (obj instanceof Tag) {
			return (int) (this.order.get() - ((ISortableEntity) obj).orderProperty().get());
		}
		else {
			return super.compareTo(obj);
		}
	}

	@Override
	public Tag entityClone () {
		Tag obj = new Tag();
		this.copyTo(obj);

		obj.name.set(this.name.get());
		obj.order.set(this.order.get());

		return obj;
	}

	@Override
	public String toString () {
		return this.name.getValue();
	}

}
