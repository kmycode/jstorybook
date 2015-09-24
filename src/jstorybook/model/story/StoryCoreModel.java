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
package jstorybook.model.story;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.contract.StorySettingName;
import jstorybook.model.dao.StorySettingDAO;
import jstorybook.model.entity.StorySetting;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.CloseMessage;

/**
 * ストーリーのコアデータのモデル
 *
 * @author KMY
 */
public class StoryCoreModel implements IUseMessenger {

	private final ObjectProperty<StorySettingDAO> settingDAO = new SimpleObjectProperty<>();

	private final StringProperty storyName = new SimpleStringProperty("");
	private final StringProperty author = new SimpleStringProperty("");

	private Messenger messenger = Messenger.getInstance();
	private StoryCoreModel baseModel;
	
	public StoryCoreModel () {
	}

	public void reload () {
		if (this.settingDAO.get() != null) {
			StorySetting setting = settingDAO.get().getSetting(StorySettingName.STORY_NAME.getKey());
			if (setting != null) {
				this.storyName.set(setting.valueProperty().get());
			}
		}
	}

	public void save () {
		if (this.baseModel != null) {
			this.baseModel.storyName.set(this.storyName.get());
			this.settingDAO.get().setSetting(StorySettingName.STORY_NAME.getKey(), this.storyName.get());
		}
		this.messenger.send(new CloseMessage());
	}

	public void cancel () {
		this.messenger.send(new CloseMessage());
	}

	public ObjectProperty<StorySettingDAO> settingDAOProperty () {
		return this.settingDAO;
	}

	public StringProperty storyNameProperty () {
		return this.storyName;
	}

	public StringProperty authorProperty () {
		return this.author;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;

		// 現在のストーリーモデルを取得して、バインド
		CurrentStoryModelGetMessage mes = new CurrentStoryModelGetMessage();
		this.messenger.send(mes);
		if (mes.storyModelProperty().get() != null) {
			StoryCoreModel obj = mes.storyModelProperty().get().getCore();
			this.storyName.set(obj.storyName.get());
			this.settingDAO.set(obj.settingDAO.get());
			this.baseModel = obj;
		}
	}

}
