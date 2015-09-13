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
package jstorybook.viewtool.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author KMY
 */
public class LocalDateCalendarConverter {

	private final ObjectProperty<LocalDate> localDate = new SimpleObjectProperty<>();
	private final ObjectProperty<Calendar> calendar = new SimpleObjectProperty<>();
	private boolean inListener = false;

	public LocalDateCalendarConverter () {
		this.localDate.addListener((obj) -> {
			System.out.println("localdate -");
			if (!LocalDateCalendarConverter.this.inListener) {
				LocalDateCalendarConverter.this.inListener = true;
				LocalDate ld = ((ObjectProperty<LocalDate>) obj).get();
				if (ld != null) {
					Date date = Date.from(ld.atTime(0, 0).toInstant(ZoneOffset.UTC));
					Calendar cal = Calendar.getInstance();
					cal.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
					cal.setTime(date);
					this.calendar.set(cal);
					System.out.println("localdate");
				}
				LocalDateCalendarConverter.this.inListener = false;
			}
		});
		this.calendar.addListener((obj) -> {
			System.out.println("calendar -");
			if (!LocalDateCalendarConverter.this.inListener) {
				LocalDateCalendarConverter.this.inListener = true;
				Calendar cal = ((ObjectProperty<Calendar>) obj).get();
				if (cal != null) {
					LocalDate ld = LocalDateTime.ofInstant(cal.getTime().toInstant(), ZoneOffset.UTC).toLocalDate();
					this.localDate.set(ld);
					System.out.println("calendar");
				}
				LocalDateCalendarConverter.this.inListener = false;
			}
		});
	}

	public ObjectProperty<LocalDate> localDateProperty () {
		return this.localDate;
	}

	public ObjectProperty<Calendar> calendarProperty () {
		return this.calendar;
	}

}
