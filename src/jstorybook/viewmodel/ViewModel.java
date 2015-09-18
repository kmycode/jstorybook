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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WritableObjectValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import jstorybook.viewtool.messenger.Messenger;

/**
 *
 * @author KMY
 */
public abstract class ViewModel {

	private List<PropertyContainer> propertyList = new ArrayList<>();
	private List<CommandContainer> commandList = new ArrayList<>();
	private static final ObjectProperty nullObject = new NullObjectProperty();
	private static final BooleanProperty nullBooleanObject = new SimpleBooleanProperty();
	private boolean isPropertyStored = false;
	private boolean isCommandStored = false;

	protected void applyProperty (String propertyName, Property property) {
		this.propertyList.add(new PropertyContainer(propertyName, property, property.getValue() != null ? property.
													getValue().getClass() : Object.class));
	}

	protected void applyCommand (String commandName, EventHandler event) {
		this.commandList.add(new CommandContainer(commandName, event));
	}

	protected void applyCommand (String commandName, EventHandler event, BooleanProperty canExecute) {
		this.commandList.add(new CommandContainer(commandName, event, canExecute));
	}

	@Deprecated
	protected void applyEmptyProperty () {
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

	public void executeCommand (String commandName) {
		if (!this.isCommandStored) {
			this.storeCommand();
			this.isCommandStored = true;
		}
		for (CommandContainer cc : this.commandList) {
			if (cc.getName().equals(commandName)) {
				cc.execute();
			}
		}
	}

	public ReadOnlyBooleanProperty canExecuteCommandProperty (String commandName) {
		if (!this.isCommandStored) {
			this.storeCommand();
			this.isCommandStored = true;
		}
		for (CommandContainer cc : this.commandList) {
			if (cc.getName().equals(commandName)) {
				return cc.getCanExecute();
			}
		}
		return ViewModel.nullBooleanObject;
	}

	public static boolean isNullProperty (Property property) {
		return ViewModel.nullObject == property || ViewModel.nullBooleanObject == property;
	}

	public static boolean isNullProperty (ReadOnlyBooleanProperty property) {
		return ViewModel.nullBooleanObject == property;
	}

	public static Property getNullProperty () {
		return ViewModel.nullObject;
	}

	public static ReadOnlyBooleanProperty getNullBooleanProperty () {
		return ViewModel.nullBooleanObject;
	}

	public <T> void setProperty (String propertyName, T value) {
		Property property = this.getProperty(propertyName);
		if (property != null && property instanceof WritableObjectValue) {
			((WritableObjectValue) property).set(value);
		}
	}

	abstract protected void storeProperty ();

	abstract public void storeMessenger (Messenger messenger);

	abstract protected void storeCommand ();

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
	 * コマンド情報を積みこむコンテナ
	 */
	private static class CommandContainer {

		private final String commandName;
		private final EventHandler event;
		private final BooleanProperty canExecute;

		public CommandContainer (String name, EventHandler event) {
			this(name, event, new SimpleBooleanProperty(true));
		}

		public CommandContainer (String name, EventHandler event, BooleanProperty canExecute) {
			this.commandName = name;
			this.event = event;
			this.canExecute = canExecute;
		}

		public String getName () {
			return this.commandName;
		}

		public ReadOnlyBooleanProperty getCanExecute () {
			return this.canExecute;
		}

		public void execute () {
			if (this.canExecute.get()) {
				this.event.handle(new Event(EventType.ROOT));
			}
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
