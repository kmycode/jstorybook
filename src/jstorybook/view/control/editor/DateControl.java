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

import java.text.ParseException;
import java.util.Calendar;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import jstorybook.common.manager.DateTimeManager;
import jstorybook.viewtool.converter.LocalDateCalendarConverter;

/**
 * 日付を入力するコントロール
 *
  * @author KMY
 */
public class DateControl extends DatePicker {

	private final ObjectProperty<Calendar> calendar = new SimpleObjectProperty<>();
	private final LocalDateCalendarConverter converter;

	public DateControl () {
		this.setPromptText("yyyy/MM/dd");

		converter = new LocalDateCalendarConverter();
		converter.localDateProperty().bindBidirectional(this.valueProperty());
		//this.calendar.bind(converter.calendarProperty());
		converter.calendarProperty().addListener((obj) -> {
			// GETバグ
			this.calendar.get();
			this.calendar.set(converter.calendarProperty().get());
		});

		// フォーマットにあってるかで色を変える
		this.getEditor().textProperty().addListener((obj) -> {
			StringProperty text = (StringProperty) obj;
			if (text.get().isEmpty()) {
				converter.calendarProperty().set(null);
				this.getEditor().setStyle("-fx-text-fill:black");
			}
			else {
				if (DateTimeManager.getInstance().isMatchDateFormat(text.get())) {
					try {
						Calendar cal = DateTimeManager.getInstance().stringToDate(text.get());
						if (cal.equals(converter.calendarProperty().get())) {
							converter.calendarProperty().set(cal);
							this.getEditor().setStyle("-fx-text-fill:black");
						}
					} catch (ParseException e) {
						// エラーが起きてもともと
						this.getEditor().setStyle("-fx-text-fill:red");
					}
				}
				else {
					this.getEditor().setStyle("-fx-text-fill:red");
				}
			}
		});
	}

	public ReadOnlyObjectProperty<Calendar> calendarProperty () {
		return this.calendar;
	}

	public void setCalendar (Calendar cal) {
		this.converter.calendarProperty().set(cal);
	}

}
