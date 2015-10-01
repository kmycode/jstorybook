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
package jstorybook.view.control.tablecell;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import jstorybook.common.manager.FontManager;
import jstorybook.model.entity.Sex;

/**
 * TableViewで性を表示する
 *
 * @author KMY
 */
public class SexCell<E> extends TableCell<E, Long> {

	private final ObservableList<Sex> sexList;

	public SexCell (ObservableList<Sex> sexList) {
		this.sexList = sexList;
		this.fontProperty().bind(FontManager.getInstance().fontProperty());
	}

	@Override
	protected void updateItem (Long item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			this.setText("");
		}
		else {
			boolean hit = false;
			for (Sex sex : this.sexList) {
				if (sex.idProperty().get() == item) {
					this.setText(sex.nameProperty().get());
					this.setTextFill(sex.colorProperty().get());
					hit = true;
					break;
				}
			}
			if (!hit) {
				this.setText("");
			}
		}
	}
}
