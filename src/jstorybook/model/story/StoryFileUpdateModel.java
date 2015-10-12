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

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.paint.Color;
import jstorybook.common.contract.SystemKey;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.SQLiteUtil;

/**
 * ストーリーファイルをアップデートするためのロジック
 *
 * @author KMY
 */
public class StoryFileUpdateModel {

	private static final StoryFileUpdateModel model = new StoryFileUpdateModel();

	private StoryFileUpdateModel () {
	}

	public static StoryFileUpdateModel getInstance () {
		return StoryFileUpdateModel.model;
	}

	// アップデートの必要があるかチェック
	public boolean checkUpdate (StoryFileModel db) throws SQLException {
		ResultSet rs = db.executeQuery("select intvalue from setting where key = 'fileversion';");
		rs.next();
		int fileVersion = rs.getInt("intvalue");
		int systemFileVersion = (Integer) SystemKey.FILE_VERSION.getValue();
		return fileVersion < systemFileVersion;
	}

	public void update (StoryFileModel db) throws SQLException {
		ResultSet rs = db.executeQuery("select intvalue from setting where key = 'fileversion';");
		rs.next();
		int fileVersion = rs.getInt("intvalue");
		int systemFileVersion = (Integer) SystemKey.FILE_VERSION.getValue();

		if (fileVersion < systemFileVersion) {
			if (fileVersion == 1) {
				this.update1to2(db);
				fileVersion = 2;
			}
			db.updateQuery("update setting set intvalue=" + fileVersion + " where key = 'fileversion';");
		}
	}

	// fileversion 1から2へ
	private void update1to2 (StoryFileModel db) throws SQLException {
		db.updateQuery(
				"CREATE TABLE attribute (id INTEGER PRIMARY KEY NOT NULL, note TEXT, `order` INTEGER NOT NULL, name TEXT NOT NULL);");
		db.updateQuery(
				"CREATE TABLE keyword (id INTEGER PRIMARY KEY NOT NULL, note TEXT, `order` INTEGER NOT NULL, name TEXT NOT NULL);");
		db.updateQuery(
				"CREATE TABLE sex (id INTEGER PRIMARY KEY NOT NULL, note TEXT, `order` INTEGER NOT NULL, name TEXT NOT NULL, color INTEGER NOT NULL);");
		db.updateQuery("insert into sex(id,name,`order`,note,color) values(1,'" + ResourceManager.getMessage("msg.person.sex.male")
				+ "',1,''," + SQLiteUtil.getInteger(Color.BLUE) + ")");
		db.updateQuery("insert into sex(id,name,`order`,note,color) values(2,'" + ResourceManager.getMessage("msg.person.sex.female")
				+ "',2,''," + SQLiteUtil.getInteger(Color.RED) + ")");
		db.updateQuery(
				"CREATE TABLE tag (id INTEGER PRIMARY KEY NOT NULL, note TEXT, `order` INTEGER NOT NULL, name TEXT NOT NULL);");
		db.updateQuery(
				"CREATE TABLE personattribute (id INTEGER PRIMARY KEY NOT NULL, note TEXT, personid INTEGER NOT NULL, groupid INTEGER NOT NULL, attributeid INTEGER NOT NULL);");

		db.updateQuery("ALTER TABLE scene ADD COLUMN [text];");
		db.updateQuery("UPDATE person SET sex=sex+1");

		this.addRelationTable(db, "persontag", "grouptag", "placetag", "scenetag", "chaptertag", "keywordtag", "tagtag",
							  "personkeyword", "groupkeyword", "placekeyword", "scenekeyword", "groupattribute", "attributetag");
		this.addIdTable(db, "persontag", "grouptag", "placetag", "scenetag", "chaptertag", "keywordtag", "tagtag",
						"personkeyword", "groupkeyword", "placekeyword", "scenekeyword", "groupattribute", "attributetag", "sex",
						"attribute", "keyword", "tag", "personattribute");
		db.updateQuery("update idtable set value=2 where key = 'sex';");
	}

	private void addRelationTable (StoryFileModel db, String... tableNames) throws SQLException {
		for (String tableName : tableNames) {
			db.updateQuery("CREATE TABLE `" + tableName
					+ "`(id INTEGER PRIMARY KEY NOT NULL, entity1 INTEGER NOT NULL, entity2 INTEGER NOT NULL, note TEXT)");
		}
	}

	private void addIdTable (StoryFileModel db, String... tableNames) throws SQLException {
		for (String tableName : tableNames) {
			db.updateQuery("insert into idtable(key,value) values ('" + tableName + "',0);");
		}
	}

}
