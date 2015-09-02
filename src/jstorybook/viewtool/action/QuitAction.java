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
package jstorybook.viewtool.action;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import jstorybook.common.manager.ResourceManager;

/**
 * 全てのストーリーを閉じ、アプリを終了する
 *
 * @author KMY
 */
public class QuitAction extends Action {

	private Stage stage;

	public QuitAction (Stage stage) {
		this.name.set(ResourceManager.getMessage("msg.exit"));
		this.stage = stage;
	}

	@Override
	public void onAction () {
		this.stage.close();
	}

}
