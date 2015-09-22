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
import jstorybook.model.entity.EntityRelation;

/**
 * エンティティ同士の関係をとりもつエンティティを扱うDAO
 *
 * @author KMY
 */
public abstract class EntityRelationDAO<T extends EntityRelation> extends DAO<T> {

	public List<Long> getRelatedIdList (long modelId) {
		List<Long> result = new ArrayList<>();
		List<T> list = this.modelListProperty().get();
		for (T rl : list) {
			if (rl.entity1IdProperty().get() == modelId) {
				result.add(rl.entity2IdProperty().get());
			}
			else if (rl.entity2IdProperty().get() == modelId) {
				result.add(rl.entity1IdProperty().get());
			}
		}
		return result;
	}

	public List<Long> getRelatedIdList (long modelId, boolean isSecond) {
		List<Long> result = new ArrayList<>();
		List<T> list = this.modelListProperty().get();
		for (T rl : list) {
			if (!isSecond) {
				if (rl.entity1IdProperty().get() == modelId) {
					result.add(rl.entity2IdProperty().get());
				}
			}
			else {
				if (rl.entity2IdProperty().get() == modelId) {
					result.add(rl.entity1IdProperty().get());
				}
			}
		}
		return result;
	}

	public void setRelatedIdList (long modelId, List<Long> relatedIdList) {
		// 削除
		List<T> list = this.modelListProperty().get();
		List<T> deleteList = new ArrayList<>();
		for (T rl : list) {
			boolean hit = false;
			boolean tryCheck = false;
			if (rl.entity1IdProperty().get() == modelId) {
				tryCheck = true;
				for (long relatedId : relatedIdList) {
					if (relatedId == rl.entity2IdProperty().get()) {
						hit = true;
						break;
					}
				}
			}
			else if (rl.entity2IdProperty().get() == modelId) {
				tryCheck = true;
				for (long relatedId : relatedIdList) {
					if (relatedId == rl.entity1IdProperty().get()) {
						hit = true;
						break;
					}
				}
			}
			if (tryCheck && !hit) {
				deleteList.add(rl);
			}
		}
		for (T rl : deleteList) {
			super.deleteModel(rl);
		}
		// 追加
		List<T> addList = new ArrayList<>();
		for (long relatedId : relatedIdList) {
			boolean hit = false;
			for (T rl : this.modelList.get()) {
				if ((rl.entity1IdProperty().get() == modelId && rl.entity2IdProperty().get() == relatedId) || (rl.entity2IdProperty().
						get() == modelId && rl.entity1IdProperty().get() == relatedId)) {
					hit = true;
					break;
				}
			}
			if (!hit) {
				T newModel = this.newEntityRelationInstance();
				newModel.entity1IdProperty().set(modelId);
				newModel.entity2IdProperty().set(relatedId);
				addList.add(newModel);
			}
		}
		for (T rl : addList) {
			super.addModel(rl);
		}
	}

	public void setRelatedIdList (long modelId, List<Long> relatedIdList, boolean isSecond) {
		// 削除
		List<T> list = this.modelListProperty().get();
		List<T> deleteList = new ArrayList<>();
		for (T rl : list) {
			boolean hit = false;
			boolean tryCheck = false;
			if (!isSecond) {
				if (rl.entity1IdProperty().get() == modelId) {
					tryCheck = true;
					for (long relatedId : relatedIdList) {
						if (relatedId == rl.entity2IdProperty().get()) {
							hit = true;
							break;
						}
					}
				}
			}
			else {
				if (rl.entity2IdProperty().get() == modelId) {
					tryCheck = true;
					for (long relatedId : relatedIdList) {
						if (relatedId == rl.entity1IdProperty().get()) {
							hit = true;
							break;
						}
					}
				}
			}
			if (tryCheck && !hit) {
				deleteList.add(rl);
			}
		}
		for (T rl : deleteList) {
			super.deleteModel(rl);
		}
		// 追加
		List<T> addList = new ArrayList<>();
		for (long relatedId : relatedIdList) {
			boolean hit = false;
			for (T rl : this.modelList.get()) {
				if (!isSecond) {
					if (rl.entity1IdProperty().get() == modelId && rl.entity2IdProperty().get() == relatedId) {
						hit = true;
						break;
					}
				}
				else {
					if (rl.entity2IdProperty().get() == modelId && rl.entity1IdProperty().get() == relatedId) {
						hit = true;
						break;
					}
				}
			}
			if (!hit) {
				T newModel = this.newEntityRelationInstance();
				if (!isSecond) {
					newModel.entity1IdProperty().set(modelId);
					newModel.entity2IdProperty().set(relatedId);
				}
				else {
					newModel.entity2IdProperty().set(modelId);
					newModel.entity1IdProperty().set(relatedId);
				}
				addList.add(newModel);
			}
		}
		for (T rl : addList) {
			super.addModel(rl);
		}
	}

	abstract protected T newEntityRelationInstance ();

	@Override
	protected T loadModel (ResultSet rs) throws SQLException {
		T model = this.newEntityRelationInstance();
		model.idProperty().set(rs.getLong("id"));
		model.entity1IdProperty().set(rs.getInt("entity1"));
		model.entity2IdProperty().set(rs.getInt("entity2"));
		model.noteProperty().set(rs.getString("note"));
		return model;
	}

	@Override
	protected void saveModel (T model) throws SQLException {

		if (model.idProperty().get() > this.lastIdSaved) {

			// 最新の最大IDを保存（modelのidとは限らない）
			this.getStoryFileModel().
					updateQuery("update idtable set value = " + this.lastId + " where key = '" + this.getTableName() + "';");

			// 行を追加
			this.getStoryFileModel().updateQuery(
					"insert into `" + this.getTableName() + "`(id,entity1,entity2,note) values(" + model.
					idProperty().get() + ",0,0,'');");
		}

		// 保存
		this.getStoryFileModel().updateQuery("update `" + this.getTableName() + "` set entity1 = " + model.entity1IdProperty().get()
				+ ", entity2 = " + model.entity2IdProperty().get() + ",note = '" + model.noteProperty().get() + "' where id = "
				+ model.idProperty().get() + ";");
	}

}
