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
package jstorybook.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import jstorybook.common.util.SQLiteUtil;
import jstorybook.model.entity.Keyword;

/**
 * キーワードのDAO
 *
 * @author KMY
 */
public class KeywordDAO extends DAO<Keyword> {

	@Override
	protected String getTableName () {
		return "keyword";
	}

	// -------------------------------------------------------
	// データベースに対する操作
	@Override
	protected Keyword loadModel (ResultSet rs) throws SQLException {
		Keyword model = new Keyword();
		model.idProperty().set(rs.getLong("id"));
		model.orderProperty().set(rs.getLong("order"));
		model.nameProperty().set(rs.getString("name"));
		model.noteProperty().set(rs.getString("note"));
		return model;
	}

	@Override
	protected void saveModel (Keyword model) throws SQLException {

		if (model.idProperty().get() > this.lastIdSaved) {

			// 最新の最大IDを保存（modelのidとは限らない）
			this.getStoryFileModel().
					updateQuery("update idtable set value = " + this.lastId + " where key = '" + this.getTableName() + "';");

			// 行を追加
			this.getStoryFileModel().updateQuery(
					"insert into `" + this.getTableName() + "`(id,name,`order`,note) values(" + model.
					idProperty().get() + ",'',0,'');");
		}

		// 保存
		StringBuilder query = new StringBuilder("update `" + this.getTableName() + "` set ");
		query.append(SQLiteUtil.updateQueryColumn("name", model.nameProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("order", model.orderProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("note", model.noteProperty().get(), true));
		query.append("where id = " + model.idProperty().get() + ";");
		this.getStoryFileModel().updateQuery(query.toString());
	}

	@Override
	protected void createTable () throws SQLException {
		this.getStoryFileModel().updateQuery(
				"CREATE TABLE keyword (id INTEGER PRIMARY KEY NOT NULL, note TEXT, `order` INTEGER NOT NULL, name TEXT NOT NULL);");
	}

}
