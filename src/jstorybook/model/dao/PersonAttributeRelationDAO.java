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
import jstorybook.model.entity.PersonAttributeRelation;

/**
 * 登場人物と属性の関係のDAO
 *
 * @author KMY
 */
public class PersonAttributeRelationDAO extends DAO<PersonAttributeRelation> {

	@Override
	protected String getTableName () {
		return "personattribute";
	}

	public PersonAttributeRelation getModelById (long personId, long groupId, long attributeId) {
		for (PersonAttributeRelation model : this.modelList.get()) {
			if (model.personIdProperty().get() == personId && model.groupIdProperty().get() == groupId && model.attributeIdProperty().
					get() == attributeId) {
				return model;
			}
		}
		return null;
	}

	// -------------------------------------------------------
	// データベースに対する操作
	@Override
	protected PersonAttributeRelation loadModel (ResultSet rs) throws SQLException {
		PersonAttributeRelation model = new PersonAttributeRelation();
		model.idProperty().set(rs.getLong("id"));
		model.personIdProperty().set(rs.getLong("personid"));
		model.groupIdProperty().set(rs.getLong("groupid"));
		model.attributeIdProperty().set(rs.getLong("attributeid"));
		model.noteProperty().set(rs.getString("note"));
		return model;
	}

	@Override
	protected void saveModel (PersonAttributeRelation model) throws SQLException {

		if (model.idProperty().get() > this.lastIdSaved) {

			// 最新の最大IDを保存（modelのidとは限らない）
			this.getStoryFileModel().
					updateQuery("update idtable set value = " + this.lastId + " where key = '" + this.getTableName() + "';");

			// 行を追加
			this.getStoryFileModel().updateQuery(
					"insert into `" + this.getTableName() + "`(id,personid,groupid,attributeid,note) values(" + model.
					idProperty().get() + ",0,0,0,'');");
		}

		// 保存
		StringBuilder query = new StringBuilder("update `" + this.getTableName() + "` set ");
		query.append(SQLiteUtil.updateQueryColumn("personid", model.personIdProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("groupid", model.groupIdProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("attributeid", model.attributeIdProperty().get(), false));
		query.append(SQLiteUtil.updateQueryColumn("note", model.noteProperty().get(), true));
		query.append("where id = " + model.idProperty().get() + ";");
		this.getStoryFileModel().updateQuery(query.toString());
	}

	@Override
	protected void createTable () throws SQLException {
		this.getStoryFileModel().updateQuery(
				"CREATE TABLE personattribute (id INTEGER PRIMARY KEY NOT NULL, note TEXT, personid INTEGER NOT NULL, groupid INTEGER NOT NULL, attributeid INTEGER NOT NULL);");
	}

}
