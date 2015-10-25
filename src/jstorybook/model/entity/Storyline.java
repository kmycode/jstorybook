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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import jstorybook.common.contract.EntityType;

// Storyline のエンティティを扱うクラス
public class Storyline extends Entity implements Comparable<Entity>, ISortableEntity {

	private final StringProperty name = new SimpleStringProperty("");
	private final LongProperty order = new SimpleLongProperty();
	private ObjectProperty<Color> color = new SimpleObjectProperty<>();

	public Storyline () {
		this.title.bind(this.name);
		this.storeProperty(this.name, this.order, this.color);
	}

	@Override
	public EntityType getEntityType () {
		return EntityType.STORYLINE;
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

	/*
	 * 色
	 */
	public ObjectProperty<Color> colorProperty () {
		return this.color;
	}

	// -------------------------------------------------------
	@Override
	public boolean equals (Object obj) {
		boolean ret = super.equals(obj);
		if (ret) {
			Storyline test = (Storyline) obj;
			ret &= this.equalsProperty(this.name, test.name);
			ret &= this.equalsProperty(this.order, test.order);
			ret &= this.equalsProperty(this.color, test.color);
		}
		return ret;
	}

	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.name);
		hash = hash * 31 + this.propertyHashCode(this.order);
		hash = hash * 31 + this.propertyHashCode(this.color);
		return hash;
	}

	@Override
	public int compareTo (Entity obj) {
		if (obj instanceof Storyline) {
			return (int) (this.order.get() - ((ISortableEntity) obj).orderProperty().get());
		}
		else {
			return super.compareTo(obj);
		}
	}

	@Override
	public Storyline entityClone () {
		Storyline obj = new Storyline();
		this.copyTo(obj);

		obj.name.set(this.name.get());
		obj.order.set(this.order.get());
		obj.color.set((Color) this.color.get());

		return obj;
	}

	@Override
	public String toString () {
		return this.name.getValue();
	}

}
