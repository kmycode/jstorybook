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
package jstorybook.view.pane.list;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.model.entity.Chapter;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.messenger.Messenger;

/**
 * 章のリスト
 *
  * @author KMY
 */
public class ChapterListPane extends EntityListPane<Chapter> {

	public ChapterListPane (Messenger messenger) {
		super(ResourceManager.getMessage("msg.chapter"), EntityType.CHAPTER, messenger);
	}

	@Override
	protected void addContextMenu (ViewModelList viewModelList, ContextMenu contextMenu) {

		// シーン一括編集メニューの設定
		MenuItem editMenu = GUIUtil.createMenuItem(viewModelList, "chapterSceneNovel");
		editMenu.setText(ResourceManager.getMessage("msg.scenenovel"));

		contextMenu.getItems().addAll(editMenu);
	}

	@Override
	public void setViewModelList (ViewModelList viewModelList) {
		super.setViewModelList(viewModelList);
		this.setOrderButton();
	}

	@Override
	protected String getEntityTypeName () {
		return "chapter";
	}

}
