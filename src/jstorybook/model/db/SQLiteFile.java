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
package jstorybook.model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jstorybook.view.dialog.ExceptionDialog;

/**
 * SQLiteのファイル
 *
 * @author KMY
 */
public class SQLiteFile {

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet rs = null;

	public SQLiteFile (String fileName) throws SQLException {
		String sql = "";

		// クラスのロードエラーは通常出るエラーではなさそうなので、ExceptionDialogで生ログ出してもよさそう
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			ExceptionDialog.showAndWait(e);
		}

		this.connection = DriverManager
				.getConnection("jdbc:sqlite:" + fileName);
		this.statement = this.connection.createStatement();
		this.statement.setQueryTimeout(30);
	}

	public ResultSet executeQuery (String sql) throws SQLException {
		this.resourceClose();
		this.rs = this.statement.executeQuery(sql);
		return this.rs;
	}

	public void updateQuery (String sql) throws SQLException {
		this.resourceClose();
		this.statement.executeUpdate(sql);
	}

	private void resourceClose () throws SQLException {
		if (this.rs != null) {
			rs.close();
		}
	}

	public void close () {
		try {
			if (this.statement != null) {
				this.statement.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (SQLException e) {
			ExceptionDialog.showAndWait(e);
		}
	}

}
