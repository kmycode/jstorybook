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
package jstorybook.view.pane;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import jstorybook.common.manager.ResourceManager;
import jstorybook.model.entity.Person;
import jstorybook.viewtool.model.EditorColumn;
import jstorybook.model.entity.columnfactory.PersonColumnFactory;
import jstorybook.viewmodel.ViewModel;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.pane.list.PersonListViewModel;

/**
 * 登場人物のリスト
 *
  * @author KMY
 */
public class PersonListPane extends EntityListPane<Person> {

	public PersonListPane () {
		super(ResourceManager.getMessage("msg.person"));
		this.viewModelList.add(new PersonListViewModel());

		Property<ArrayList> listp = this.viewModelList.getProperty("columnList", ArrayList.class);
		if (!ViewModel.isNullProperty(listp)) {
			this.setColumnList(listp.getValue());
		}
	}

}
