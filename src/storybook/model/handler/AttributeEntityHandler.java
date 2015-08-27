/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package storybook.model.handler;

import javax.swing.ListCellRenderer;
import storybook.model.dao.AttributeDAOImpl;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.Attribute;
import storybook.ui.MainFrame;
import storybook.ui.combo.AttributeListCellRenderer;
import storybook.ui.table.SbColumnFactory;

/**
 *
 * @author favdb
 */
public class AttributeEntityHandler extends AbstractEntityHandler {

	public AttributeEntityHandler(MainFrame mainFrame) {
		super(mainFrame, SbColumnFactory.getInstance().getGenderColumns());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T> getEntityClass() {
		return (Class<T>) Attribute.class;
	}

	@Override
	public Entity createNewEntity() {
		Attribute attribute=new Attribute();
		return(attribute);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T> getDAOClass() {
		return (Class<T>) AttributeDAOImpl.class;
	}
	
	@Override
	public ListCellRenderer getListCellRenderer() {
		return new AttributeListCellRenderer();
	}

}
