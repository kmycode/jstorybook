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
import jstorybook.common.contract.EntityType;

// PersonAttributeRelation のエンティティを扱うクラス
public class PersonAttributeRelation extends Entity implements Comparable<Entity> {

	private final LongProperty personId = new SimpleLongProperty();
	private final LongProperty groupId = new SimpleLongProperty();
	private final LongProperty attributeId = new SimpleLongProperty();

	public PersonAttributeRelation () {
		this.storeProperty(this.personId, this.groupId, this.attributeId);
	}

	@Override
	public EntityType getEntityType () {
		return EntityType.PERSON_ATTRIBUTE;
	}

	/*
	 * 登場人物ID
	 */
	public LongProperty personIdProperty () {
		return this.personId;
	}

	/*
	 * 集団ID
	 */
	public LongProperty groupIdProperty () {
		return this.groupId;
	}

	/*
	 * アトリブID
	 */
	public LongProperty attributeIdProperty () {
		return this.attributeId;
	}

	// -------------------------------------------------------
	@Override
	public boolean equals (Object obj) {
		boolean ret = super.equals(obj);
		if (ret) {
			PersonAttributeRelation test = (PersonAttributeRelation) obj;
			ret &= this.equalsProperty(this.personId, test.personId);
			ret &= this.equalsProperty(this.groupId, test.groupId);
			ret &= this.equalsProperty(this.attributeId, test.attributeId);
		}
		return ret;
	}

	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.personId);
		hash = hash * 31 + this.propertyHashCode(this.groupId);
		hash = hash * 31 + this.propertyHashCode(this.attributeId);
		return hash;
	}

	@Override
	public int compareTo (Entity obj) {
		if (obj instanceof PersonAttributeRelation) {
			return this.toString().compareTo(obj.toString());
		}
		else {
			return super.compareTo(obj);
		}
	}

	@Override
	public PersonAttributeRelation entityClone () {
		PersonAttributeRelation obj = new PersonAttributeRelation();
		this.copyTo(obj);

		obj.personId.set(this.personId.get());
		obj.groupId.set(this.groupId.get());
		obj.attributeId.set(this.attributeId.get());

		return obj;
	}

	@Override
	public String toString () {
		return "Person:" + this.personId.get() + " Group:" + this.groupId.get() + " Attribute:" + this.attributeId.get();
	}

}
