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

package storybook.toolkit.swing;

import java.awt.Font;

/**
 * @author martin
 * @deprecated storybook.toolkit.swing.FontManager
 */
@Deprecated
public class FontUtil {

	/**
	 * 太字フォントを返します
	 *
	 * @return 太字のフォント
	 */
	public static Font getBoldFont () {
		return FontManager.getInstance(FontManager.FontType.BOLD).fontProperty().get();
	}

	public static Font getDefaultFont () {
		return FontManager.getInstance().fontProperty().get();
	}

	public static void setDefaultFont(Font font) {
		if (font == null) {
			return;
		}
		SwingUtil.setUIFont(new javax.swing.plaf.FontUIResource(font.getName(),
				font.getStyle(), font.getSize()));
		FontManager.getInstance().setFont(font.getName(), font.getSize());
	}
}
