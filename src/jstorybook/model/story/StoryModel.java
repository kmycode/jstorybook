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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.exception.StoryFileModelFailedMessage;

/**
 * ストーリーファイルのモデル
 *
 * @author KMY
 */
public class StoryModel implements IUseMessenger {

	private final ObjectProperty<StoryCoreModel> core = new SimpleObjectProperty<>(new StoryCoreModel());
	private final StringProperty storyFileName = new SimpleStringProperty("");

	// 非公開のプロパティ
	private final ObjectProperty<StoryFileModel> storyFile = new SimpleObjectProperty<>();
	private Messenger messenger = Messenger.getInstance();

	public StoryModel () {
		// ファイル名変更時のイベント
		this.storyFileName.addListener((obj) -> {
			try {
				this.storyFile.set(new StoryFileModel(((StringProperty) obj).get()));
			} catch (SQLException e) {
				this.messenger.send(new StoryFileModelFailedMessage(((StringProperty) obj).get()));
			}
		});
	}

	// -------------------------------------------------------

	public ObjectProperty<StoryCoreModel> coreProperty () {
		return this.core;
	}

	public StringProperty fileNameProperty () {
		return this.storyFileName;
	}

	// -------------------------------------------------------

	public StoryCoreModel getCore () {
		return this.core.get();
	}

	// -------------------------------------------------------
	// IUseMessenger
	@Override
	public void setMessenger (Messenger messenger) {
		if (messenger != null) {
			this.messenger = messenger;
		}
	}

}
