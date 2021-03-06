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
import javafx.scene.paint.Color;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.SQLiteUtil;
import jstorybook.model.entity.Sex;

/**
 * 性のDAO
 *
 * @author KMY
 */
public class SexDAO extends DAO<Sex> {

	@Override
	protected String getTableName () {
		return "sex";
	}

	// -------------------------------------------------------
	// データベースに対する操作
	@Override
	protected Sex loadModel (ResultSet rs) throws SQLException {
		Sex model = new Sex();
		model.idProperty().set(rs.getLong("id"));
		model.orderProperty().set(rs.getLong("order"));
		model.nameProperty().set(rs.getString("name"));
		model.colorProperty().set(SQLiteUtil.getColor(rs.getInt("color")));
		model.noteProperty().set(rs.getString("note"));
		return model;
	}

	@Override
	protected void saveModel (Sex model) throws SQLException {

		if (model.idProperty().get() > this.lastIdSaved) {

			// 最新の最大IDを保存（modelのidとは限らない）
			this.getStoryFileModel().
					updateQuery("update idtable set value = " + this.lastId + " where key = '" + this.getTableName() + "';");

			// 行を追加
			this.getStoryFileModel().updateQuery(
					"insert into `" + this.getTableName() + "`(id,name,`order`,note,color) values(" + model.
					idProperty().get() + ",'',0,'',0);");
		}

		// 保存
		StringBuilder query = new StringBuilder("update `" + this.getTableName() + "` set ");
		query.append(SQLiteUtil.updateQueryColumn("name", model.nameProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("order", model.orderProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("color", SQLiteUtil.getInteger(model.colorProperty().get()), false));
		query.append(SQLiteUtil.updateQueryColumn("note", model.noteProperty().get(), true));
		query.append("where id = " + model.idProperty().get() + ";");
		this.getStoryFileModel().updateQuery(query.toString());
	}

	@Override
	protected void createTable () throws SQLException {
		this.getStoryFileModel().updateQuery(
				"CREATE TABLE sex (id INTEGER PRIMARY KEY NOT NULL, note TEXT, `order` INTEGER NOT NULL, name TEXT NOT NULL, color INTEGER NOT NULL);");

		// デフォルトのデータ
		Sex male = new Sex();
		Sex female = new Sex();
		male.nameProperty().set(ResourceManager.getMessage("msg.person.sex.male"));
		female.nameProperty().set(ResourceManager.getMessage("msg.person.sex.female"));
		male.colorProperty().set(Color.BLUE);
		female.colorProperty().set(Color.RED);
		this.addModel(male);
		this.addModel(female);
		this.saveList();
	}

}
