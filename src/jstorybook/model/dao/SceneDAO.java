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
import jstorybook.model.entity.Scene;

/**
 * シーンのDAO
 *
 * @author KMY
 */
public class SceneDAO extends DAO<Scene> {

	@Override
	protected String getTableName () {
		return "scene";
	}

	// -------------------------------------------------------
	// データベースに対する操作
	@Override
	protected Scene loadModel (ResultSet rs) throws SQLException {
		Scene model = new Scene();
		model.idProperty().set(rs.getLong("id"));
		model.orderProperty().set(rs.getLong("order"));
		model.nameProperty().set(rs.getString("name"));
		model.starttimeProperty().set(SQLiteUtil.getCalendar(rs.getString("starttime")));
		model.endtimeProperty().set(SQLiteUtil.getCalendar(rs.getString("endtime")));
		model.noteProperty().set(rs.getString("note"));
		model.textProperty().set(rs.getString("text"));
		return model;
	}

	@Override
	protected void saveModel (Scene model) throws SQLException {

		if (model.idProperty().get() > this.lastIdSaved) {

			// 最新の最大IDを保存（modelのidとは限らない）
			this.getStoryFileModel().
					updateQuery("update idtable set value = " + this.lastId + " where key = '" + this.getTableName() + "';");

			// 行を追加
			this.getStoryFileModel().updateQuery(
					"insert into `" + this.getTableName() + "`(id,name,`order`,starttime,endtime,note,`text`) values(" + model.
					idProperty().get() + ",'',0,'','','','');");
		}

		// 保存
		StringBuilder query = new StringBuilder("update `" + this.getTableName() + "` set ");
		query.append(SQLiteUtil.updateQueryColumn("name", model.nameProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("order", model.orderProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("starttime", SQLiteUtil.getString(model.starttimeProperty().get()), false));
		query.append(SQLiteUtil.updateQueryColumn("endtime", SQLiteUtil.getString(model.endtimeProperty().get()), false));
		query.append(SQLiteUtil.updateQueryColumn("note", model.noteProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("text", model.textProperty().get(), true));
		query.append("where id = " + model.idProperty().get() + ";");
		this.getStoryFileModel().updateQuery(query.toString());
	}

	@Override
	protected void createTable () throws SQLException {
		this.getStoryFileModel().updateQuery(
				"CREATE TABLE scene (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, [order] INTEGER NOT NULL DEFAULT (1), starttime TEXT, endtime TEXT, note TEXT, [text] TEXT)");
	}

}
