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
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

// ノート のエンティティを扱うクラス
public class Person extends Entity implements Comparable<Entity> {

	private StringProperty firstName;
	private StringProperty lastName;
	private ObjectProperty<Calendar> birthday;
	private ObjectProperty<Calendar> dayOfDeath;
	private ObjectProperty<Color> color;
	private StringProperty notes;

	public Person () {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.birthday = new SimpleObjectProperty<Calendar>();
		this.dayOfDeath = new SimpleObjectProperty<Calendar>();
		this.color = new SimpleObjectProperty<Color>();
		this.notes = new SimpleStringProperty();
	}

	public Person (String firstName, String lastName, Calendar birthday, Calendar dayOfDeath, Color color, String notes) {
		this();
		this.firstName.set(firstName);
		this.lastName.set(lastName);
		this.birthday.set(birthday);
		this.dayOfDeath.set(dayOfDeath);
		this.color.set(color);
		this.notes.set(notes);
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

	/*
	 * ノート
	 */
	public StringProperty notesProperty () {
		return this.notes;
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
			ret &= this.equalsProperty(this.notes, test.notes);
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
		hash = hash * 31 + this.propertyHashCode(this.notes);
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

}