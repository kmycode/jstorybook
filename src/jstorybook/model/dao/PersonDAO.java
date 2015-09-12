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
import jstorybook.model.entity.StorySetting;

/**
 * 登場人物のDAO
 *
 * @author KMY
 */
public class PersonDAO extends DAO {

	private final ObjectProperty<ObservableList<Person>> modelList = new SimpleObjectProperty<>();

	private Person getModel (ResultSet rs) throws SQLException {
		Person model = new Person();
		model.firstNameProperty().set(rs.getString("firstname"));
		model.lastNameProperty().set(rs.getString("lastname"));
		model.birthdayProperty().set(SQLiteUtil.getCalendar(rs.getString("birthday")));
		model.dayOfDeathProperty().set(SQLiteUtil.getCalendar(rs.getString("dayofdeath")));
		model.colorProperty().set(SQLiteUtil.getColor(rs.getInt("color")));
		model.notesProperty().set(rs.getString("notes"));
		return model;
	}

	private void setModel (Person model) throws SQLException {
		this.getStoryFileModel().updateQuery("update person set firstname = '" + model.firstNameProperty().get()
				+ "',lastname = '" + model.lastNameProperty().get() + "',birthday = '" + SQLiteUtil.getString(
						model.birthdayProperty().get()) + "',dayofdeath = '" + SQLiteUtil.getString(model.
						dayOfDeathProperty().get()) + "',color = " + SQLiteUtil.getInteger(model.colorProperty().
						get()) + ",notes = '" + model.notesProperty().get() + "' where id = " + model.idProperty().
				get() + ";");
	}

	private Person insertModel () throws SQLException {
		int newid = 0;
		ResultSet rs = this.getStoryFileModel().executeQuery("select max(id) from person;");
		if (rs.next()) {
			newid = rs.getInt(0) + 1;
		}
		else {
			throw new SQLException();
		}
		this.getStoryFileModel().updateQuery(
				"insert into person(id,firstname,lastname,birthday,dayofdeath,color,notes) values(" + newid
				+ "'','','','',0,''");
		Person model = new Person();
		model.idProperty().set(newid);
		return model;
	}

	public List<Person> getList () throws SQLException {
		List<Person> result = new ArrayList<>();
		ResultSet rs = this.getStoryFileModel().executeQuery("select * from person;");
		while (rs.next()) {
			result.add(this.getModel(rs));
		}
		return result;
	}

	public Person getModelById (int id) throws SQLException {
		ResultSet rs = this.getStoryFileModel().executeQuery("select * from person where id=" + id + ";");
		if (rs.next()) {
			return this.getModel(rs);
		}
		return null;
	}

	// -------------------------------------------------------

	@Override
	protected void storyFileModelSet () throws SQLException {
		this.modelList.set(FXCollections.observableArrayList());
		this.modelList.get().addAll(this.getList());
	}

	public ObjectProperty<ObservableList<Person>> modelListProperty () {
		return this.modelList;
	}

}
