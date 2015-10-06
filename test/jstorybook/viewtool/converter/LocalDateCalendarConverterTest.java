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
import java.time.Month;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author KMY
 */
public class LocalDateCalendarConverterTest {

	public LocalDateCalendarConverterTest () {
	}

	@BeforeClass
	public static void setUpClass () {
	}

	@AfterClass
	public static void tearDownClass () {
	}

	@Before
	public void setUp () {
	}

	@After
	public void tearDown () {
	}

	/**
	 * Test of localDateProperty method, of class LocalDateCalendarConverter.
	 */
	@Test
	public void testLocalDateProperty () {
		System.out.println("localDateProperty");
		LocalDateCalendarConverter instance = new LocalDateCalendarConverter();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		instance.calendarProperty().set(cal);
		LocalDate expResult = LocalDate.of(2000, Month.JANUARY, 1);
		expResult.atTime(0, 0, 0, 0);
		LocalDate result = instance.localDateProperty().get();
		assertEquals(expResult, result);
	}

	/**
	 * Test of calendarProperty method, of class LocalDateCalendarConverter.
	 */
	@Test
	public void testCalendarProperty () {
		System.out.println("calendarProperty");
		LocalDateCalendarConverter instance = new LocalDateCalendarConverter();
		LocalDate ld = LocalDate.of(2000, Month.JANUARY, 1);
		ld.atTime(0, 0, 0, 0);
		instance.localDateProperty().set(ld);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Calendar expResult = cal;
		Calendar result = instance.calendarProperty().get();
		assertEquals(expResult, result);
	}

}
