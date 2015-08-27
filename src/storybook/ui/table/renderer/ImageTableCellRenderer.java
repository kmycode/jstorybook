/*
Storybook: Scene-based software for novelists and authors.
Copyright (C) 2008 - 2011 Martin Mustun

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

package storybook.ui.table.renderer;

import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import storybook.SbConstants.ClientPropertyName;
import storybook.model.BookModel;
import jstorybook.model.entity.Person;
import storybook.ui.MainFrame;

@SuppressWarnings("serial")
public class ImageTableCellRenderer extends DefaultTableCellRenderer {

	private static HashMap<String, ImageIcon> cacheMap = new HashMap<String, ImageIcon>();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		MainFrame mainFrame = (MainFrame) table
				.getClientProperty(ClientPropertyName.MAIN_FRAME.toString());
		JLabel lbText = (JLabel) super.getTableCellRendererComponent(table,
				null, isSelected, hasFocus, row, column);
		if (value == null
				|| ( ! (value instanceof String))) {
			return lbText;
		}
		String filePath = Person.getImageIconPath(mainFrame.getDbFile()) + (String) value;
		ImageIcon icon = cacheMap.get(filePath);
		if (icon == null) {
			icon = new ImageIcon(filePath);
			icon = new ImageIcon(icon.getImage().getScaledInstance(30, 30,
																   Image.SCALE_SMOOTH));
			cacheMap.put(filePath, icon);
		}
		lbText.setText("");
		lbText.setIcon(icon);
		return lbText;
	}
}
