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
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
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
		model.idProperty().set(rs.getInt("id"));
		model.firstNameProperty().set(rs.getString("firstname"));
		model.lastNameProperty().set(rs.getString("lastname"));
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
					"insert into person(id,firstname,lastname,birthday,dayofdeath,color,note) values(" + model.
					idProperty().get() + ",'','','','',0,'');");
		}

		// 保存
		this.getStoryFileModel().updateQuery("update person set firstname = '" + model.firstNameProperty().get()
				+ "',lastname = '" + model.lastNameProperty().get() + "',birthday = '" + SQLiteUtil.getString(
						model.birthdayProperty().get()) + "',dayofdeath = '" + SQLiteUtil.getString(model.
						dayOfDeathProperty().get()) + "',color = " + SQLiteUtil.getInteger(model.colorProperty().
						get()) + ",note = '" + model.noteProperty().get() + "' where id = " + model.idProperty().
				get() + ";");
	}

	// -------------------------------------------------------

	@Override
	protected void storyFileModelSet () throws SQLException {
		this.loadList();
	}

	public ObjectProperty<ObservableList<Person>> modelListProperty () {
		return this.modelList;
	}

}
