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
import jstorybook.model.entity.Person;

/**
 * 登場人物のDAO
 *
 * @author KMY
 */
public class PersonDAO extends DAO<Person> {

	@Override
	protected String getTableName () {
		return "person";
	}

	// -------------------------------------------------------
	// データベースに対する操作
	@Override
	protected Person loadModel (ResultSet rs) throws SQLException {
		Person model = new Person();
		model.idProperty().set(rs.getLong("id"));
		model.orderProperty().set(rs.getLong("order"));
		model.firstNameProperty().set(rs.getString("firstname"));
		model.lastNameProperty().set(rs.getString("lastname"));
		model.sexIdProperty().set(rs.getLong("sex"));
		model.birthdayProperty().set(SQLiteUtil.getCalendar(rs.getString("birthday")));
		model.dayOfDeathProperty().set(SQLiteUtil.getCalendar(rs.getString("dayofdeath")));
		model.colorProperty().set(SQLiteUtil.getColor(rs.getInt("color")));
		model.noteProperty().set(rs.getString("note"));
		return model;
	}

	@Override
	protected void saveModel (Person model) throws SQLException {

		if (model.idProperty().get() > this.lastIdSaved) {

			// 最新の最大IDを保存（modelのidとは限らない）
			this.getStoryFileModel().
					updateQuery("update idtable set value = " + this.lastId + " where key = 'person';");

			// 行を追加
			this.getStoryFileModel().updateQuery(
					"insert into person(id,firstname,lastname,`order`,sex,birthday,dayofdeath,color,note) values(" + model.
					idProperty().get() + ",'','',0,0,'','',0,'');");
		}

		// 保存
		StringBuilder query = new StringBuilder("update person set ");
		query.append(SQLiteUtil.updateQueryColumn("firstname", model.firstNameProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("lastname", model.lastNameProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("order", model.orderProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("sex", model.sexIdProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("birthday", SQLiteUtil.getString(model.birthdayProperty().get()), false));
		query.append(SQLiteUtil.updateQueryColumn("dayofdeath", SQLiteUtil.getString(model.dayOfDeathProperty().get()), false));
		query.append(SQLiteUtil.updateQueryColumn("color", SQLiteUtil.getInteger(model.colorProperty().get()), false));
		query.append(SQLiteUtil.updateQueryColumn("note", model.noteProperty().get(), true));
		query.append("where id = " + model.idProperty().get() + ";");
		this.getStoryFileModel().updateQuery(query.toString());
	}

	@Override
	protected void createTable () throws SQLException {
		this.getStoryFileModel().updateQuery(
				"CREATE TABLE person (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, [order] INTEGER NOT NULL DEFAULT (1), "
				+ "firstname TEXT, lastname TEXT, sex INTEGER, birthday TEXT, dayofdeath TEXT, color INTEGER, note TEXT)");
	}

}
