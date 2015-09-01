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
package jstorybook.viewmodel;

import com.sun.javafx.binding.ExpressionHelper;
import java.util.HashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WritableObjectValue;

/**
 *
 * @author KMY
 */
public abstract class ViewModel {

	private HashMap<String, Property> propertyList = new HashMap<>();
	private static final ObjectProperty nullObject = new NullObjectProperty();

	protected void applyProperty (String propertyName, Property property) {
		this.propertyList.put(propertyName, property);
	}

	public Property getProperty (String propertyName) {
		Property result = this.propertyList.get(propertyName);
		if (result == null) {
			result = ViewModel.nullObject;
		}
		return result;
	}

	public static boolean isNullProperty (Property property) {
		return ViewModel.nullObject == property;
	}

	public static Property getNullProperty () {
		return ViewModel.nullObject;
	}

	public void setProperty (String propertyName, Object value) {
		Property property = this.getProperty(propertyName);
		if (property != null && property instanceof WritableObjectValue) {
			((WritableObjectValue) property).set(value);
		}
	}

	abstract protected void storeProperty ();

	/*
	 * NULLオブジェクトプロパティ
	 * いくら設定しても状態が変化しない
	 */
	private static class NullObjectProperty extends SimpleObjectProperty<Object> {

		public NullObjectProperty () {
			super.set(new Object());
		}

		@Override
		public void set (Object obj) {
		}

		@Override
		public void addListener (InvalidationListener listener) {
		}

		@Override
		public void removeListener (InvalidationListener listener) {
		}

		@Override
		public void addListener (ChangeListener<? super Object> listener) {
		}

		@Override
		public void removeListener (ChangeListener<? super Object> listener) {
		}

	}

}
