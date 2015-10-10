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
package jstorybook.model;

import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.common.contract.PreferenceKey;
import jstorybook.model.story.StoryModel;
import jstorybook.viewtool.messenger.ApplicationQuitMessage;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.ExceptionMessage;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.AboutDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.AskExitDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.NewStoryDialogShowMessage;
import jstorybook.viewtool.messenger.dialog.OpenFileChooserMessage;
import jstorybook.viewtool.messenger.dialog.PreferenceDialogShowMessage;

/**
 * アプリケーションモデル
 *
 * @author KMY
 */
public class ApplicationModel implements IUseMessenger {

	private Messenger messenger = Messenger.getInstance();
	private PreferenceFileModel preferenceFile;
	private final BooleanProperty isExitable = new SimpleBooleanProperty(true);

	public ApplicationModel () {
		// 設定ファイルの読込
		try {
			this.preferenceFile = new PreferenceFileModel();
			this.preferenceFile.loadSettingsSync();
		} catch (SQLException | IOException e) {

			// 設定ファイルの読込に失敗した場合
			this.messenger.send(new ExceptionMessage(e));

			// 設定ファイルを削除してアプリ終了
			this.preferenceFile.reset();
			this.exit();
		}
	}

	public void exit () {

		boolean doExit = true;

		if (PreferenceKey.CONFIRM_EXIT.getBoolean()) {
			AskExitDialogShowMessage message = new AskExitDialogShowMessage();
			this.messenger.send(message);
			doExit = message.isExitable();
		}

		if (doExit) {
			this.isExitable.set(true);

			// 設定ファイルを保存
			this.preferenceFile.saveSettingsSync();

			this.messenger.send(new ApplicationQuitMessage());
		}
		else {
			this.isExitable.set(false);
		}
	}

	public void about () {
		this.messenger.send(new AboutDialogShowMessage());
	}

	public void newStory () {
		this.messenger.send(new NewStoryDialogShowMessage());
	}

	public void preference () {
		this.messenger.send(new PreferenceDialogShowMessage());
	}

	public void loadStory () {
		StringProperty fileName = new SimpleStringProperty();
		this.messenger.send(new OpenFileChooserMessage(fileName));
		if (fileName.get() != null && !fileName.get().isEmpty()) {
			CurrentStoryModelGetMessage mes = new CurrentStoryModelGetMessage();
			this.messenger.send(mes);
			StoryModel storyModel = mes.storyModelProperty().get();
			if (storyModel != null) {
				storyModel.fileNameProperty().set(fileName.get());
			}
		}
	}

	public BooleanProperty isExitableProperty () {
		return this.isExitable;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
		this.preferenceFile.setMessenger(messenger);
	}

}
