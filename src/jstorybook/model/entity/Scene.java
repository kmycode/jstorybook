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
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Scene のエンティティを扱うクラス
public class Scene extends Entity implements Comparable<Entity>, ISortableEntity {

	private final StringProperty name = new SimpleStringProperty("");
	private final ObjectProperty<Calendar> starttime = new SimpleObjectProperty<Calendar>();
	private final ObjectProperty<Calendar> endtime = new SimpleObjectProperty<Calendar>();
	private final LongProperty order = new SimpleLongProperty();

	public Scene () {
		this.title.bind(this.name);
	}

	/*
	 * 名前
	 */
	public StringProperty nameProperty () {
		return this.name;
	}

	/*
	 * 開始時刻
	 */
	public ObjectProperty<Calendar> starttimeProperty () {
		return this.starttime;
	}

	/*
	 * 終了時刻
	 */
	public ObjectProperty<Calendar> endtimeProperty () {
		return this.endtime;
	}

	/*
	 * 順番
	 */
	public LongProperty orderProperty () {
		return this.order;
	}

	// -------------------------------------------------------
	@Override
	public boolean equals (Object obj) {
		boolean ret = super.equals(obj);
		if (ret) {
			Scene test = (Scene) obj;
			ret &= this.equalsProperty(this.name, test.name);
			ret &= this.equalsProperty(this.starttime, test.starttime);
			ret &= this.equalsProperty(this.endtime, test.endtime);
			ret &= this.equalsProperty(this.order, test.order);
		}
		return ret;
	}

	@Override
	public int hashCode () {
		int hash = super.hashCode();
		hash = hash * 31 + this.propertyHashCode(this.name);
		hash = hash * 31 + this.propertyHashCode(this.starttime);
		hash = hash * 31 + this.propertyHashCode(this.endtime);
		hash = hash * 31 + this.propertyHashCode(this.order);
		return hash;
	}

	@Override
	public int compareTo (Entity obj) {
		if (obj instanceof Scene) {
			return (int) (this.order.get() - ((ISortableEntity) obj).orderProperty().get());
		}
		else {
			return super.compareTo(obj);
		}
	}

	@Override
	public Scene entityClone () {
		Scene obj = new Scene();
		this.copyTo(obj);

		obj.name.set(this.name.get());
		if (this.starttime.get() != null) {
			obj.starttime.set((Calendar) this.starttime.get().clone());
		}
		if (this.endtime.get() != null) {
			obj.endtime.set((Calendar) this.endtime.get().clone());
		}
		obj.order.set(this.order.get());

		return obj;
	}

	@Override
	public String toString () {
		return this.name.getValue();
	}

}
