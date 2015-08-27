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

package storybook.toolkit.swing.label;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JLabel;
import storybook.toolkit.DateUtil;

/**
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class DateLabel extends JLabel {

	private Date date;

	public DateLabel() {
		super();
		setOpaque(true);
	}

	public void setDate(Date date) {
		this.date = date;
		DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT,
				Locale.getDefault());
		setText(DateUtil.simpleDateToString(this.date));
		setOpaque(true);
	}

	public Date getDate() {
		return date;
	}
}
