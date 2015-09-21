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
import jstorybook.model.entity.PersonPersonRelation;

/**
 * 登場人物の関係のDAO
 *
 * @author KMY
 */
public class PersonPersonRelationDAO extends DAO<PersonPersonRelation> {

	@Override
	protected String getTableName () {
		return "personperson";
	}

	@Override
	protected PersonPersonRelation loadModel (ResultSet rs) throws SQLException {
		PersonPersonRelation model = new PersonPersonRelation();
		model.person1IdProperty().set(rs.getInt("person1"));
		model.person2IdProperty().set(rs.getInt("person2"));
		return model;
	}

	public void readPersonDAO (PersonDAO dao) {
		for (PersonPersonRelation model : this.modelList.get()) {
			model.person1Property().set(dao.getModelById(model.person1IdProperty().get()));
			model.person2Property().set(dao.getModelById(model.person2IdProperty().get()));
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
					"insert into personperson(id,person1,person2,note) values(" + model.
					idProperty().get() + ",0,0,'');");
		}

		// 保存
		this.getStoryFileModel().updateQuery("update personperson set person1 = " + model.person1IdProperty().get()
				+ ", person2 = " + model.person2IdProperty().get() + ",note = '" + model.noteProperty().get() + "' where id = "
				+ model.idProperty().get() + ";");
	}

	public List<Long> getRelatedIdList (long modelId) {
		List<Long> result = new ArrayList<>();
		List<PersonPersonRelation> list = this.modelListProperty().get();
		for (PersonPersonRelation rl : list) {
			if (rl.person1IdProperty().get() == modelId) {
				result.add(rl.person2IdProperty().get());
			}
			else if (rl.person2IdProperty().get() == modelId) {
				result.add(rl.person1IdProperty().get());
			}
		}
		return result;
	}

	public void setRelatedIdList (long modelId, List<Long> relatedIdList) {
		// 削除
		List<PersonPersonRelation> list = this.modelListProperty().get();
		List<PersonPersonRelation> deleteList = new ArrayList<>();
		for (PersonPersonRelation rl : list) {
			boolean hit = false;
			if (rl.person1IdProperty().get() == modelId) {
				for (long relatedId : relatedIdList) {
					if (relatedId == rl.person2IdProperty().get()) {
						hit = true;
						break;
					}
				}
			}
			else if (rl.person2IdProperty().get() == modelId) {
				for (long relatedId : relatedIdList) {
					if (relatedId == rl.person1IdProperty().get()) {
						hit = true;
						break;
					}
				}
			}
			if (!hit) {
				deleteList.add(rl);
			}
		}
		for (PersonPersonRelation rl : deleteList) {
			super.deleteModel(rl);
		}
		// 追加
		List<PersonPersonRelation> addList = new ArrayList<>();
		for (long relatedId : relatedIdList) {
			boolean hit = false;
			for (PersonPersonRelation rl : this.modelList.get()) {
				if ((rl.person1IdProperty().get() == modelId && rl.person2IdProperty().get() == relatedId) || (rl.person2IdProperty().
						get() == modelId && rl.person1IdProperty().get() == relatedId)) {
					hit = true;
					break;
				}
			}
			if (!hit) {
				PersonPersonRelation newModel = new PersonPersonRelation();
				newModel.person1IdProperty().set(modelId);
				newModel.person2IdProperty().set(relatedId);
				addList.add(newModel);
			}
		}
		for (PersonPersonRelation rl : addList) {
			super.addModel(rl);
		}
	}

}
