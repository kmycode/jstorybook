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

import jstorybook.common.contract.EntityType;
import jstorybook.common.manager.ResourceManager;
import jstorybook.model.entity.Storyline;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewtool.messenger.Messenger;

/**
 * 話の線のリスト
 *
  * @author KMY
 */
public class StorylineListPane extends EntityListPane<Storyline> {

	public StorylineListPane (Messenger messenger) {
		super(ResourceManager.getMessage("msg.storyline"), EntityType.STORYLINE, messenger);
	}

	@Override
	public void setViewModelList (ViewModelList viewModelList) {
		super.setViewModelList(viewModelList);
	}

	@Override
	protected String getEntityTypeName () {
		return "storyline";
	}

}
