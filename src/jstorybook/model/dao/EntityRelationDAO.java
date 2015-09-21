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

	protected T loadModel (ResultSet rs, T model) throws SQLException {
		model.entity1IdProperty().set(rs.getInt("entity1"));
		model.entity2IdProperty().set(rs.getInt("entity2"));
		return model;
	}

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

	abstract protected T newEntityRelationInstance ();

}
