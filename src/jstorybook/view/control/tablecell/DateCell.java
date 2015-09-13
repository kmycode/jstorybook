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

import java.util.Calendar;
import javafx.scene.control.TableCell;
import jstorybook.common.manager.DateTimeManager;

/**
 * TableViewでDateを表示する
 *
 * @author KMY
 */
public class DateCell<E> extends TableCell<E, Calendar> {

	@Override
	protected void updateItem (Calendar item, boolean empty) {
		super.updateItem(item, empty);
		this.setText(DateTimeManager.getInstance().dateToString(item));
	}
}
