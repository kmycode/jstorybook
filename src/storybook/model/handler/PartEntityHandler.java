/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2008 - 2012 Martin Mustun

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package storybook.model.handler;

import java.sql.Timestamp;
import java.util.Calendar;
import org.hibernate.Session;
import storybook.model.BookModel;
import storybook.model.dao.PartDAOImpl;
import storybook.model.entity.AbstractEntity;
import storybook.model.entity.Part;
import storybook.toolkit.I18N;
import storybook.ui.MainFrame;
import storybook.ui.table.SbColumnFactory;

/**
 * @author martin
 *
 */
public class PartEntityHandler extends AbstractEntityHandler {

	public PartEntityHandler(MainFrame mainFrame) {
		super(mainFrame, SbColumnFactory.getInstance().getPartColumns());
	}

	@Override
	public AbstractEntity createNewEntity() {
		BookModel model = mainFrame.getBookModel();
		Session session = model.beginTransaction();
		PartDAOImpl dao = new PartDAOImpl(session);
		Integer nextNumber = dao.getNextPartNumber();
		model.commit();

		Part part = new Part();
		part.setNumber(nextNumber);
		part.setName(I18N.getMsg("msg.common.part") + " " + nextNumber);
		{
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			part.setCreationTime(new Timestamp(today.getTime().getTime()));
		}
		part.setObjectiveTime(null);
		part.setDoneTime(null);
		part.setObjectiveChars(0);
		return part;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T> getDAOClass() {
		return (Class<T>) PartDAOImpl.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T> getEntityClass() {
		return (Class<T>) Part.class;
	}
}
