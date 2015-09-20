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

import java.util.Calendar;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import jstorybook.viewtool.completer.PersonNameCompleter;

// ノート のエンティティを扱うクラス
public class Person extends Entity implements Comparable<Entity> {

	private StringProperty firstName = new SimpleStringProperty();
	private StringProperty lastName = new SimpleStringProperty();
	private ObjectProperty<Calendar> birthday = new SimpleObjectProperty<>();
	private ObjectProperty<Calendar> dayOfDeath = new SimpleObjectProperty<>();
	private ObjectProperty<Color> color = new SimpleObjectProperty<>();
	private PersonNameCompleter nameCompleter = null;

	public Person () {
	}

	/*
	 * 名前
	 */
	public StringProperty firstNameProperty () {
		return this.firstName;
	}

	/*
	 * 苗字
	 */
	public StringProperty lastNameProperty () {
		return this.lastName;
	}

	/*
	 * 誕生日
	 */
	public ObjectProperty<Calendar> birthdayProperty () {
		return this.birthday;
	}

	/*
	 * 死亡日
	 */
	public ObjectProperty<Calendar> dayOfDeathProperty () {
		return this.dayOfDeath;
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
			Person test = (Person) obj;
			ret &= this.equalsProperty(this.firstName, test.firstName);
			ret &= this.equalsProperty(this.lastName, test.lastName);
			ret &= this.equalsProperty(this.birthday, test.birthday);
			ret &= this.equalsProperty(this.dayOfDeath, test.dayOfDeath);
			ret &= this.equalsProperty(this.color, test.color);
		}
		return ret;
	}

	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.firstName);
		hash = hash * 31 + this.propertyHashCode(this.lastName);
		hash = hash * 31 + this.propertyHashCode(this.birthday);
		hash = hash * 31 + this.propertyHashCode(this.dayOfDeath);
		hash = hash * 31 + this.propertyHashCode(this.color);
		return hash;
	}

	@Override
	public int compareTo (Entity obj) {
		if (obj instanceof Person) {
			return this.toString().compareTo(obj.toString());
		}
		else {
			return super.compareTo(obj);
		}
	}

	@Override
	public String toString () {
		return this.firstName.getValue() + this.lastName.getValue();
	}

	@Override
	public ReadOnlyStringProperty titleProperty () {
		if (this.nameCompleter == null) {
			this.nameCompleter = new PersonNameCompleter();
			this.nameCompleter.bindFirstName(this.firstName);
			this.nameCompleter.bindLastName(this.lastName);
		}
		return this.nameCompleter.nameProperty();
	}

	@Override
	public Person entityClone () {
		Person obj = new Person();
		this.copyTo(obj);
		obj.firstName.set(this.firstName.get());
		obj.lastName.set(this.lastName.get());
		if (this.birthday.get() != null) {
			obj.birthday.set((Calendar) this.birthday.get().clone());
		}
		if (this.dayOfDeath.get() != null) {
			obj.dayOfDeath.set((Calendar) this.dayOfDeath.get().clone());
		}
		obj.color.set((Color) this.color.get());
		obj.nameCompleter = null;

		return obj;
	}

}
