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
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jstorybook.model.db.SQLiteFile;

/**
 * ストーリーファイルをあらわすイミュータブルなクラス
 * SQLiteFileの移譲（今後SQLite以外の手段を取ることになった時のため）
 *
 * @author KMY
 */
public class StoryFileModel {

	private final SQLiteFile db;
	private final String fileName;
	private final BooleanProperty isOpen = new SimpleBooleanProperty(false);

	public StoryFileModel (String fileName) throws SQLException {
		this.fileName = fileName;
		this.db = new SQLiteFile(new File(fileName).toPath().toString());

		// 例外発生なら、コードはここまで到達しない
		this.isOpen.set(true);
	}

	public ResultSet executeQuery (String sql) throws SQLException {
		return this.db.executeQuery(sql);
	}

	public void updateQuery (String sql) throws SQLException {
		this.db.updateQuery(sql);
	}

	public String getFileName () {
		return this.fileName;
	}

	public BooleanProperty isOpenProperty () {
		return this.isOpen;
	}

}
