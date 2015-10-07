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
package jstorybook.view.control.editor;

import java.util.Calendar;
import java.util.TimeZone;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

/**
 * 日付と時刻のコントロール
 *
 * @author KMY
 */
public class DateTimeControl extends VBox {

	private final DateControl dateControl = new DateControl();
	private final TimeControl timeControl = new TimeControl();
	private final ObjectProperty<Calendar> calendar = new SimpleObjectProperty<>();

	public DateTimeControl () {
		// 日付変更時
		this.dateControl.calendarProperty().addListener((obj) -> {
			if (this.isCalendarSet()) {
				Calendar cal = this.calendar.get();
				Calendar dst = this.dateControl.calendarProperty().get();
				cal.set(Calendar.YEAR, dst.get(Calendar.YEAR));
				cal.set(Calendar.DAY_OF_YEAR, dst.get(Calendar.DAY_OF_YEAR));
			}
		});
		// 時刻変更時
		this.timeControl.hourProperty().addListener((obj) -> {
			if (this.isCalendarSet()) {
				this.calendar.get().set(Calendar.HOUR_OF_DAY, ((IntegerProperty) obj).get());
			}
		});
		this.timeControl.minuteProperty().addListener((obj) -> {
			if (this.isCalendarSet()) {
				this.calendar.get().set(Calendar.MINUTE, ((IntegerProperty) obj).get());
			}
		});
		this.timeControl.secondProperty().addListener((obj) -> {
			if (this.isCalendarSet()) {
				this.calendar.get().set(Calendar.SECOND, ((IntegerProperty) obj).get());
			}
		});
		// カレンダーオブジェクト変更時
		this.calendar.addListener((obj) -> {
			if (this.isCalendarSet()) {
				Calendar cal = this.calendar.get();
				cal.setTimeZone(TimeZone.getDefault());
				this.timeControl.hourProperty().set(cal.get(Calendar.HOUR_OF_DAY));
				this.timeControl.minuteProperty().set(cal.get(Calendar.MINUTE));
				this.timeControl.secondProperty().set(cal.get(Calendar.SECOND));
				cal.setTimeZone(TimeZone.getTimeZone("UTC"));
				this.dateControl.setCalendar(cal);
			}
		});

		this.getChildren().addAll(this.dateControl, this.timeControl);
	}

	private boolean isCalendarSet () {
		return this.calendar.get() != null;
	}

	public ObjectProperty<Calendar> calendarProperty () {
		return this.calendar;
	}

}
