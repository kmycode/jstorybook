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
package jstorybook.view;

import javafx.stage.Stage;
import jstorybook.common.manager.ResourceManager;

/**
 *
 * @author KMY
 */
public abstract class MyStage extends Stage {

	protected final Stage parent;

	public MyStage (Stage parent) {
		this.parent = parent;

		// アイコン
		this.getIcons().add(ResourceManager.getIconNode("appicon.png").getImage());
	}

}
