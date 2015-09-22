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

import jstorybook.model.entity.ScenePlaceRelation;

/**
 * シーンと場所の関係のDAO
 *
 * @author KMY
 */
public class ScenePlaceRelationDAO extends EntityRelationDAO<ScenePlaceRelation> {

	@Override
	protected String getTableName () {
		return "sceneplace";
	}

	public void readPlaceDAO (PlaceDAO dao) {
		for (ScenePlaceRelation model : this.modelList.get()) {
			model.entity2Property().set(dao.getModelById(model.entity2IdProperty().get()));
		}
	}

	public void readSceneDAO (SceneDAO dao) {
		for (ScenePlaceRelation model : this.modelList.get()) {
			model.entity1Property().set(dao.getModelById(model.entity1IdProperty().get()));
		}
	}

	@Override
	protected ScenePlaceRelation newEntityRelationInstance () {
		return new ScenePlaceRelation();
	}

}
