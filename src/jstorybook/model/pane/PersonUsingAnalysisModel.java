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
package jstorybook.model.pane;

import java.util.List;
import jstorybook.model.entity.Person;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.pane.chart.Data2DSendMessage;

/**
 * 登場人物の使用状況を解析するモデル
 *
 * @author KMY
 */
public class PersonUsingAnalysisModel implements IUseMessenger {

	private Messenger messenger = Messenger.getInstance();

	public void load () {

		// データの取得
		CurrentStoryModelGetMessage message = new CurrentStoryModelGetMessage();
		this.messenger.send(message);
		StoryModel storyModel = message.storyModelProperty().get();
		if (storyModel != null) {
			List<Person> list = storyModel.getPersonDAO().modelListProperty().get();
			for (Person p : list) {
				this.messenger.send(new Data2DSendMessage<>(p.titleProperty().get(), storyModel.getScenePersonRelation_Scene(p.
															idProperty().get()).size()));
			}
		}
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
