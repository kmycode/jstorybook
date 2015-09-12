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
package jstorybook.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.paint.Color;
import jstorybook.viewtool.messenger.ExceptionMessage;
import jstorybook.viewtool.messenger.Messenger;

/**
 * SQLiteを扱う上でのユーティリティクラス
 *
 * @author KMY
 */
public class SQLiteUtil {

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private SQLiteUtil () {
	}

	public static Calendar getCalendar (String str) {
		if (str == null) {
			return null;
		}
		Date date = null;

		try {
			date = new java.util.Date(SQLiteUtil.DATE_FORMAT.parse(str).getTime());
		} catch (java.text.ParseException e) {
			Messenger.getInstance().send(new ExceptionMessage(e));
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static String getString (Calendar cal) {
		return SQLiteUtil.DATE_FORMAT.format(cal.getTime());
	}

	public static Color getColor (int val) {
		// 255で割ると色が１段階分減っちゃうけど、256にするなら複雑なロジック必要になりそうだから後回し
		double red = (val & 0xff0000 >> 16) / 255.0d;
		double green = (val & 0xff00 >> 8) / 255.0d;
		double blue = (val & 0xff) / 255.0d;
		double opacity = (val & 0xff000000 >> 24) / 255.0d;
		return new Color(red, green, blue, opacity);
	}

	public static int getInteger (Color col) {
		byte red = (byte) (col.getRed() * 255);
		byte green = (byte) (col.getGreen() * 255);
		byte blue = (byte) (col.getBlue() * 255);
		byte opacity = (byte) (col.getOpacity() * 255);
		return ((int) red << 16) | ((int) green << 8) | ((int) blue) | ((int) opacity << 24);
	}

}
