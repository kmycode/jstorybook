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
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jstorybook.common.util.SQLiteUtil;
import jstorybook.model.entity.Person;

/**
 * 登場人物のDAO
 *
 * @author KMY
 */
public class PersonDAO extends DAO {

	private final ObjectProperty<ObservableList<Person>> modelList = new SimpleObjectProperty<>();
	private final List<Long> removeIdList = new ArrayList<>();
	private int lastId = 0;				// 最大ID
	private int lastIdSaved = 0;		// 最後に保存した時の最大ID

	// -------------------------------------------------------
	// データベースに対する操作

	private Person loadModel (ResultSet rs) throws SQLException {
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

	private void saveModel (Person model) throws SQLException {

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
	
	private void removeModel (long id) throws SQLException {
		this.getStoryFileModel().updateQuery("delete from person where id = " + id + ";");
	}

	// -------------------------------------------------------
	// 変数に対する操作
	public void addModel (Person model) {
		model.idProperty().set(++this.lastId);
		this.modelList.get().add(model);
	}

	public void deleteModel (Person model) {
		this.modelList.get().remove(model);
		this.removeIdList.add(model.idProperty().get());
	}

	// -------------------------------------------------------
	// 操作

	public void loadList () throws SQLException {

		// IDの最大値を取得
		ResultSet rs = this.getStoryFileModel().executeQuery("select * from idtable where key = 'person';");
		if (rs.next()) {
			this.lastIdSaved = this.lastId = rs.getInt("value");
		}
		else {
			throw new SQLException();
		}

		// リストを作成
		ObservableList<Person> result = FXCollections.observableArrayList();
		rs = this.getStoryFileModel().executeQuery("select * from person;");
		while (rs.next()) {
			result.add(this.loadModel(rs));
		}
		this.modelList.set(result);
	}

	public void saveList () throws SQLException {

		// 保存処理
		ObservableList<Person> list = this.modelList.get();
		for (Person model : list) {
			this.saveModel(model);
		}

		// 削除処理
		for (long delid : this.removeIdList) {
			this.removeModel(delid);
		}

		this.lastIdSaved = this.lastId;
	}

	public Person getModelById (int id) {
		for (Person model : this.modelList.get()) {
			if (model.idProperty().get() == id) {
				return model;
			}
		}
		return null;
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
