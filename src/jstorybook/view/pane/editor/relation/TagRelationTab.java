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
package jstorybook.view.pane.editor.relation;

import jstorybook.common.manager.ResourceManager;
import jstorybook.model.entity.Tag;
import jstorybook.model.entity.columnfactory.TagColumnFactory;
import jstorybook.viewtool.messenger.Messenger;

/**
 * 関係するタグを設定するタブ
 *
 * @author KMY
 */
public class TagRelationTab extends EntityRelationTab<Tag> {

	public TagRelationTab (long entityId, Messenger messenger) {
		super(ResourceManager.getMessage("msg.relation.tag"), entityId, messenger);
		this.tableView.setColumnList(TagColumnFactory.getInstance().createColumnList());
	}

}
