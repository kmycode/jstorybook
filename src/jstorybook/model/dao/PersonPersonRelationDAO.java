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
import jstorybook.model.entity.PersonPersonRelation;

/**
 * 登場人物の関係のDAO
 *
 * @author KMY
 */
public class PersonPersonRelationDAO extends EntityRelationDAO<PersonPersonRelation> {

	@Override
	protected String getTableName () {
		return "personperson";
	}

	@Override
	protected PersonPersonRelation loadModel (ResultSet rs) throws SQLException {
		PersonPersonRelation model = new PersonPersonRelation();
		return super.loadModel(rs, model);
	}

	public void readPersonDAO (PersonDAO dao) {
		for (PersonPersonRelation model : this.modelList.get()) {
			model.entity1Property().set(dao.getModelById(model.entity1IdProperty().get()));
			model.entity2Property().set(dao.getModelById(model.entity2IdProperty().get()));
		}
	}

	@Override
	protected void saveModel (PersonPersonRelation model) throws SQLException {

		if (model.idProperty().get() > this.lastIdSaved) {

			// 最新の最大IDを保存（modelのidとは限らない）
			this.getStoryFileModel().
					updateQuery("update idtable set value = " + this.lastId + " where key = 'personperson';");

			// 行を追加
			this.getStoryFileModel().updateQuery(
					"insert into personperson(id,entity1,entity2,note) values(" + model.
					idProperty().get() + ",0,0,'');");
		}

		// 保存
		this.getStoryFileModel().updateQuery("update personperson set entity1 = " + model.entity1IdProperty().get()
				+ ", entity2 = " + model.entity2IdProperty().get() + ",note = '" + model.noteProperty().get() + "' where id = "
				+ model.idProperty().get() + ";");
	}

	@Override
	protected PersonPersonRelation newEntityRelationInstance () {
		return new PersonPersonRelation();
	}

}
