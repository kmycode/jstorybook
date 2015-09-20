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
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.StorySetting;

/**
 * ストーリー設定モデルを取得するDAO
 *
 * @author KMY
 */
public class StorySettingDAO extends DAO {

	private final ObjectProperty<ObservableList> modelList = new SimpleObjectProperty<>();

	public StorySetting getSetting (String key) throws SQLException {
		StorySetting model = null;
		ResultSet rs = this.getStoryFileModel().executeQuery("select * from setting where key='" + key + "';");
		if (rs.next()) {
			model = new StorySetting();
			model.keyProperty().set(rs.getString("key"));
			model.valueProperty().set(rs.getString("value"));
		}
		return model;
	}

	@Override
	protected void storyFileModelSet () throws SQLException {
		this.modelList.set(FXCollections.observableArrayList());
	}

	@Override
	protected String getTableName () {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Entity loadModel (ResultSet rs) throws SQLException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void saveModel (Entity model) throws SQLException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
