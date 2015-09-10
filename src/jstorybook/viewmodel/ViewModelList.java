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
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableObjectValue;
import jstorybook.viewtool.messenger.Messenger;

/**
 * ビューモデルのリスト。ビューで、複数のビューモデルを一括管理するときに使う
 *
 * @author KMY
 */
public class ViewModelList extends ArrayList<ViewModel> {

	public ViewModelList (ViewModel... args) {
		for (ViewModel vm : args) {
			this.add(vm);
		}
	}

	// 型安全性を重視するため、基本はgetProperty(String, Class<T>)を使うこと
	public Property getProperty (String propertyName) {
		return this.getProperty(propertyName, Object.class);
	}

	public <T> Property<T> getProperty (String propertyName, Class<T> propertyClass) {
		Property<T> result = ViewModel.getNullProperty();
		for (ViewModel vm : this) {
			result = vm.getProperty(propertyName, propertyClass);
			if (!ViewModel.isNullProperty(result)) {
				break;
			}
		}
		return result;
	}

	public void setProperty (String propertyName, Object value) {
		for (ViewModel vm : this) {
			vm.setProperty(propertyName, value);
		}
	}

	public void storeMessenger (Messenger messenger) {
		for (ViewModel vm : this) {
			vm.storeMessenger(messenger);
		}
	}

}
