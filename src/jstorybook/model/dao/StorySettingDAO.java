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
import jstorybook.model.entity.StorySetting;

/**
 * ストーリー設定モデルを取得するDAO
 *
 * @author KMY
 */
public class StorySettingDAO extends DAO<StorySetting> {

	@Override
	protected String getTableName () {
		return "setting";
	}

	@Override
	protected StorySetting loadModel (ResultSet rs) throws SQLException {
		StorySetting model = new StorySetting();
		model.idProperty().set(0);
		model.keyProperty().set(rs.getString("key"));
		model.valueProperty().set(rs.getString("value"));
		model.intValueProperty().set(rs.getInt("intvalue"));
		model.noteProperty().set("");
		return model;
	}

	@Override
	protected void saveModel (StorySetting model) throws SQLException {
		StringBuilder query = new StringBuilder("update `" + this.getTableName() + "` set ");
		query.append(SQLiteUtil.updateQueryColumn("value", model.valueProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("intvalue", model.intValueProperty().get(), true));
		query.append("where key = '" + model.keyProperty().get() + "';");
		this.getStoryFileModel().updateQuery(query.toString());
	}

	public StorySetting getSetting (String key) {
		for (StorySetting model : this.modelList.get()) {
			if (model.keyProperty().get().equals(key)) {
				return model;
			}
		}
		return null;
	}

	public void setSetting (String key, String value) {
		System.out.println(key + "/" + value + " - " + this.modelList.get().size());
		for (StorySetting model : this.modelList.get()) {
			if (model.keyProperty().get().equals(key)) {
				model.valueProperty().set(value);
			}
		}
	}

	public void setSetting (String key, int value) {
		for (StorySetting model : this.modelList.get()) {
			if (model.keyProperty().get().equals(key)) {
				model.intValueProperty().set(value);
			}
		}
	}

	@Override
	protected void createTable () throws SQLException {
		this.getStoryFileModel().updateQuery(
				"CREATE TABLE setting (key TEXT PRIMARY KEY NOT NULL, value TEXT, intValue INTEGER)");
		this.getStoryFileModel().updateQuery("insert into setting(key,value,intValue) values ('storyname','',0);");
		this.getStoryFileModel().updateQuery("insert into setting(key,value,intValue) values ('entitycount','',0);");
		this.getStoryFileModel().updateQuery("insert into setting(key,value,intValue) values ('fileversion','',1);");
		this.loadList();
	}

}
