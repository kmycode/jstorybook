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
package jstorybook.viewtool.messenger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import jstorybook.model.story.StoryModel;

/**
 *
 * @author KMY
 */
public class StoryModelCurrentGetMessage extends Message {

	private ObjectProperty<StoryModel> storyModel = new SimpleObjectProperty<>(null);

	public ObjectProperty<StoryModel> storyModelProperty () {
		return this.storyModel;
	}

	public void setStoryModel (Property<?> storyModel) {
		if (storyModel.getValue() != null && storyModel.getValue().getClass() == StoryModel.class) {
			this.storyModel.set((StoryModel) storyModel.getValue());
		}
	}

}
