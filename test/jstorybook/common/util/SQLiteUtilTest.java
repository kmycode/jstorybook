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

import javafx.scene.paint.Color;
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
public class SQLiteUtilTest {

	public SQLiteUtilTest () {
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
	 * Test of getInteger method, of class SQLiteUtil.
	 */
	@Test
	public void testGetInteger () {
		Color col = Color.RED;
		int expResult = 0xffff0000;
		int result = SQLiteUtil.getInteger(col);
		System.out.println(String.format("RED   : %1$x", result));
		assertEquals(expResult, result);
	}

	@Test
	public void testGetInteger2 () {
		Color col = Color.LIME;
		int expResult = 0xff00ff00;
		int result = SQLiteUtil.getInteger(col);
		System.out.println(String.format("GREEN : %1$x", result));
		assertEquals(expResult, result);
	}

	@Test
	public void testGetInteger3 () {
		Color col = Color.BLUE;
		int expResult = 0xff0000ff;
		int result = SQLiteUtil.getInteger(col);
		System.out.println(String.format("BLUE  : %1$x", result));
		assertEquals(expResult, result);
	}

}
