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

import java.sql.SQLException;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jstorybook.model.dao.PersonDAO;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Person;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.exception.StoryFileModelFailedMessage;
import jstorybook.viewtool.messenger.pane.PersonEditorShowMessage;

/**
 * ストーリーファイルのモデル
 *
 * @author KMY
 */
public class StoryModel implements IUseMessenger {

	private final ObjectProperty<StoryCoreModel> core = new SimpleObjectProperty<>(new StoryCoreModel());
	private final StringProperty storyFileName = new SimpleStringProperty("");

	private final ObjectProperty<PersonDAO> personDAO = new SimpleObjectProperty<>(new PersonDAO());

	private final ObjectProperty<Entity> selectedEntity = new SimpleObjectProperty<>();
	private final ObjectProperty<StoryEntityColumnModel> entityColumn = new SimpleObjectProperty<>(
			new StoryEntityColumnModel());

	// 非公開のプロパティ
	private final ObjectProperty<StoryFileModel> storyFile = new SimpleObjectProperty<>();
	private Messenger messenger = Messenger.getInstance();

	public StoryModel () {
		// ファイル名変更時のイベント
		this.storyFileName.addListener((obj) -> {
			try {
				this.storyFile.set(new StoryFileModel(((StringProperty) obj).get()));
				this.setDAO();
			} catch (SQLException e) {
				this.messenger.send(new StoryFileModelFailedMessage(((StringProperty) obj).get()));
			}
		});

		// TableViewなどでモデルを選択した時のイベント
		this.selectedEntity.addListener((Observable obj) -> {
			Entity selected = ((ObjectProperty<Entity>) obj).get();
			if (selected != null) {
				if (selected instanceof Person) {
					Person model = ((Person) selected).entityClone();
					this.messenger.send(
							new PersonEditorShowMessage(this.entityColumn.get().getPersonColumnList(model),
														this.entityColumn.get().getPersonColumnList((Person) selected)));
				}
			}
		});
	}

	// ファイル名を変更した時に呼び出して、情報を取得する
	private void setDAO () throws SQLException {
		this.personDAO.get().setStoryFileModel(this.storyFile.get());
	}

	// -------------------------------------------------------
	// 関連するモデル
	public ObjectProperty<StoryCoreModel> coreProperty () {
		return this.core;
	}

	public ObjectProperty<StoryEntityColumnModel> entityColumnProperty () {
		return this.entityColumn;
	}

	// -------------------------------------------------------
	// StoryModelそのものが持つプロパティ

	public StringProperty fileNameProperty () {
		return this.storyFileName;
	}

	// TableViewなどで選択されたエンティティ
	public ObjectProperty<Entity> selectedEntityProperty () {
		return this.selectedEntity;
	}

	// -------------------------------------------------------
	// DAO
	public ObjectProperty<PersonDAO> personDAOProperty () {
		return this.personDAO;
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
