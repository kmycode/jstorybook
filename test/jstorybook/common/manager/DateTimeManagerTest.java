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
public class DateTimeManagerTest {

	public DateTimeManagerTest () {
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
	 * Test of dateToString method, of class DateTimeManager.
	 */
	@Test
	public void testDateToString () {
		System.out.println("dateToString");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		DateTimeManager instance = DateTimeManager.getInstance();
		String expResult = "2000/01/01";
		String result = instance.dateToString(cal);
		assertEquals(expResult, result);
	}

	/**
	 * Test of dateTimeToString method, of class DateTimeManager.
	 */
	@Test
	public void testDateTimeToString () {
		System.out.println("dateTimeToString");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		DateTimeManager instance = DateTimeManager.getInstance();
		String expResult = "2000/01/01 00:00:00";
		String result = instance.dateTimeToString(cal);
		assertEquals(expResult, result);
	}

	/**
	 * Test of stringToDate method, of class DateTimeManager.
	 */
	@Test
	public void testStringToDate () throws Exception {
		System.out.println("stringToDate");
		String str = "2000/01/01";
		DateTimeManager instance = DateTimeManager.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Calendar expResult = cal;
		Calendar result = instance.stringToDate(str);
		assertEquals(str, instance.dateToString(result));
	}

}
