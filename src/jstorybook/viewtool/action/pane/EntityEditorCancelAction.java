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
package jstorybook.viewtool.action.pane;

import jstorybook.common.manager.ResourceManager;
import jstorybook.viewtool.action.Action;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.pane.PaneCancelMessage;

/**
 * エンティティエディタでキャンセルを押した時のアクション
 *
 * @author KMY
 */
public class EntityEditorCancelAction extends Action {

	private final Messenger messenger;

	public EntityEditorCancelAction (Messenger messenger) {
		this.name.set(ResourceManager.getMessage("msg.edit.cancel"));
		this.messenger = messenger;
	}

	@Override
	public void onAction () {
		this.messenger.send(new PaneCancelMessage());
	}

}
