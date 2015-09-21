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

/**
 * 登場人物同士の関係
 *
 * @author KMY
 */
public class PersonPersonRelation extends Entity {

	private ObjectProperty<Person> person1 = new SimpleObjectProperty<>();
	private LongProperty person1Id = new SimpleLongProperty();
	private ObjectProperty<Person> person2 = new SimpleObjectProperty<>();
	private LongProperty person2Id = new SimpleLongProperty();

	public PersonPersonRelation () {
		this.person1.addListener((obj) -> {
			if (person1.get() != null) {
				person1Id.set(person1.get().idProperty().get());
			}
			else {
				person1Id.set(0);
			}
		});
		this.person2.addListener((obj) -> {
			if (person2.get() != null) {
				person2Id.set(person2.get().idProperty().get());
			}
			else {
				person2Id.set(0);
			}
		});
	}

	public ObjectProperty<Person> person1Property () {
		return this.person1;
	}

	public LongProperty person1IdProperty () {
		return this.person1Id;
	}

	public ObjectProperty<Person> person2Property () {
		return this.person2;
	}

	public LongProperty person2IdProperty () {
		return this.person2Id;
	}

	@Override
	public boolean equals (Object obj) {
		boolean ret = super.equals(obj);
		if (ret) {
			PersonPersonRelation test = (PersonPersonRelation) obj;
			ret &= this.equalsProperty(this.person1, test.person1);
			ret &= this.equalsProperty(this.person2, test.person2);
		}
		return ret;
	}

	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.person1);
		hash = hash * 31 + this.propertyHashCode(this.person2);
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
		return this.person1.get().toString() + " + " + person2.get().toString();
	}

	@Override
	public Entity entityClone () {
		PersonPersonRelation obj = new PersonPersonRelation();
		super.copyTo(obj);
		obj.person1.set(this.person1.get());
		obj.person2.set(this.person2.get());
		return obj;
	}

}
