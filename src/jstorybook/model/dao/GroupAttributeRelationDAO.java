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

import java.util.ArrayList;
import java.util.List;
import jstorybook.model.entity.GroupAttributeRelation;

/**
 * 集団と属性の関係のDAO
 *
 * @author KMY
 */
public class GroupAttributeRelationDAO extends EntityRelationDAO<GroupAttributeRelation> {

	@Override
	protected String getTableName () {
		return "groupattribute";
	}

	public void readGroupDAO (GroupDAO dao) {
		for (GroupAttributeRelation model : this.modelList.get()) {
			model.entity1Property().set(dao.getModelById(model.entity1IdProperty().get()));
		}
	}

	public void readAttributeDAO (AttributeDAO dao) {
		for (GroupAttributeRelation model : this.modelList.get()) {
			model.entity2Property().set(dao.getModelById(model.entity2IdProperty().get()));
		}
	}

	public List<Long> getAttributeIdList (long groupId) {
		List<Long> list = new ArrayList<>();
		for (GroupAttributeRelation model : this.modelList.get()) {
			if (model.entity1IdProperty().get() == groupId) {
				list.add(model.entity2IdProperty().get());
			}
		}
		return list;
	}

	@Override
	protected GroupAttributeRelation newEntityRelationInstance () {
		return new GroupAttributeRelation();
	}

}
