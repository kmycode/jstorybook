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
package jstorybook.model.entity;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import jstorybook.common.contract.EntityType;

/**
 * エンティティ同士の関係を記録するためのエンティティ
 *
 * @author KMY
 */
public class EntityRelation<S extends Entity, T extends Entity> extends Entity {

	protected ObjectProperty<S> entity1 = new SimpleObjectProperty<>();
	protected ObjectProperty<T> entity2 = new SimpleObjectProperty<>();
	protected LongProperty entity1Id = new SimpleLongProperty();
	protected LongProperty entity2Id = new SimpleLongProperty();

	protected EntityRelation () {
		this.entity1.addListener((obj) -> {
			if (entity1.get() != null) {
				entity1Id.set(entity1.get().idProperty().get());
			}
			else {
				entity1Id.set(0);
			}
		});
		this.entity2.addListener((obj) -> {
			if (entity2.get() != null) {
				entity2Id.set(entity2.get().idProperty().get());
			}
			else {
				entity2Id.set(0);
			}
		});
	}

	@Override
	public EntityType getEntityType () {
		return null;
	}

	public ObjectProperty<S> entity1Property () {
		return this.entity1;
	}

	public LongProperty entity1IdProperty () {
		return this.entity1Id;
	}

	public ObjectProperty<T> entity2Property () {
		return this.entity2;
	}

	public LongProperty entity2IdProperty () {
		return this.entity2Id;
	}

	@Override
	public boolean equals (Object obj) {
		boolean ret = super.equals(obj);
		if (ret) {
			EntityRelation test = (EntityRelation) obj;
			ret &= this.equalsProperty(this.entity1, test.entity1);
			ret &= this.equalsProperty(this.entity2, test.entity2);
		}
		return ret;
	}

	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.entity1);
		hash = hash * 31 + this.propertyHashCode(this.entity2);
		return hash;
	}

	@Override
	public int compareTo (Entity obj) {
		if (obj instanceof PersonPersonRelation) {
			return this.toString().compareTo(obj.toString());
		}
		else {
			return super.compareTo(obj);
		}
	}

	@Override
	public String toString () {
		Object entity1 = this.entity1.get();
		Object entity2 = this.entity2.get();
		return entity1 + " + " + entity2;
	}

	@Override
	public Entity entityClone () {
		EntityRelation obj = new PersonPersonRelation();
		super.copyTo(obj);
		obj.entity1.set(this.entity1.get());
		obj.entity2.set(this.entity2.get());
		return obj;
	}

}
