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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WritableObjectValue;
import jstorybook.view.dialog.ExceptionDialog;
import jstorybook.viewtool.messenger.Messenger;
import org.jfree.ui.about.resources.AboutResources_pl;

/**
 *
 * @author KMY
 */
public abstract class ViewModel {

	private List<PropertyContainer> propertyList = new ArrayList<>();
	private static final ObjectProperty nullObject = new NullObjectProperty();
	private boolean isPropertyStored = false;

	protected void applyProperty (String propertyName, Property property) {
		this.propertyList.add(new PropertyContainer(propertyName, property, property.getValue().getClass()));
		this.isPropertyStored = true;
	}

	protected void applyEmptyProperty () {
		this.isPropertyStored = true;
	}

	public Property getProperty (String propertyName) {
		return this.getProperty(propertyName, Object.class);
	}

	public <T> Property<T> getProperty (String propertyName, Class<T> propertyClass) {
		// まだstorePropertyが実行されてない時は、実行する
		if (!this.isPropertyStored) {
			this.storeProperty();
			this.isPropertyStored = true;
		}
		Property<T> result = null;
		for (PropertyContainer pc : this.propertyList) {
			if (pc.getName().equals(propertyName) && (propertyClass == Object.class || pc.getPropertyClass()
					== propertyClass)) {
				result = pc.getProperty();
				break;
			}
		}
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

	public <T> void setProperty (String propertyName, T value) {
		Property property = this.getProperty(propertyName);
		if (property != null && property instanceof WritableObjectValue) {
			((WritableObjectValue) property).set(value);
		}
	}

	abstract protected void storeProperty ();

	abstract public void storeMessenger (Messenger messenger);

	/**
	 * プロパティ情報を積み込むコンテナ
	 */
	private static class PropertyContainer<T> {
		private final String propertyName;
		private final Property<T> property;

		public PropertyContainer (String name, Property<T> prop) {
			this.propertyName = name;
			this.property = prop;
		}

		public PropertyContainer (String name, Property<T> prop, Class<T> cl) {
			this.propertyName = name;
			this.property = prop;
		}

		public String getName () {
			return this.propertyName;
		}

		public Property<T> getProperty () {
			return this.property;
		}

		public Class getPropertyClass () {
			return this.property.getValue().getClass();
		}
	}

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
