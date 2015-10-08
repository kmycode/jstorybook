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
package jstorybook.common.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * 日時処理に関連するクラス
 *
 * @author KMY
 */
public final class DateTimeManager {

	private static final DateTimeManager defaultInstance = new DateTimeManager();

	private ObjectProperty<DateFormat> dateFormat = new SimpleObjectProperty<>(new SimpleDateFormat("yyyy/MM/dd"));
	private ObjectProperty<DateFormat> dateTimeFormat = new SimpleObjectProperty<>(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));

	private DateTimeManager () {
	}

	public static DateTimeManager getInstance () {
		return DateTimeManager.defaultInstance;
	}

	public String dateToString (Calendar cal) {
		if (cal != null) {
			return this.dateFormat.get().format(cal.getTime());
		}
		else {
			return "";
		}
	}

	public String dateTimeToString (Calendar cal) {
		if (cal != null) {
			return this.dateTimeFormat.get().format(cal.getTime());
		}
		else {
			return "";
		}
	}

	public Calendar stringToDate (String str) throws ParseException {
		Date date = new java.util.Date(this.dateFormat.get().parse(str).getTime());
		Calendar cal_created = Calendar.getInstance();
		cal_created.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal_created.setTime(date);
		return cal_created;
	}

	public boolean isMatchDateFormat (String str) {
		String month = "(([1-9])|(0[1-9])|(1[0-2]))";
		String day = "(([1-9])|(0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))";
		String year = "(([0-9]{1,6})|(-[0-9]{1,6}))";
		return (str.matches("()|(" + year + "\\/" + month + "\\/" + day + ")"));
	}

	public String getDateFormatterDefaultText () {
		return "2000/01/01";
	}

}
