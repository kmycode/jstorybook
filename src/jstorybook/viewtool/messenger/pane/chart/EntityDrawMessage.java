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
package jstorybook.viewtool.messenger.pane.chart;

import javafx.event.EventHandler;
import jstorybook.viewtool.messenger.Message;

/**
 * エンティティを描画するメッセージ
 *
 * @author KMY
 */
public abstract class EntityDrawMessage extends Message {

	private final String name;
	private final EventHandler ev;
	private int releatedId;
	private boolean isReleated = false;
	private int drawId;
	private boolean isDrawn = false;

	public EntityDrawMessage (String name, EventHandler event) {
		this.name = name;
		this.ev = event;
	}

	public String getName () {
		return this.name;
	}

	public EventHandler getEvent () {
		return this.ev;
	}

	public void setReleatedId (int id) {
		this.releatedId = id;
		this.isReleated = true;
	}

	public int getReleatedId () {
		return this.releatedId;
	}

	public boolean isReleated () {
		return this.isReleated;
	}

	public void setDrawId (int id) {
		this.drawId = id;
		this.isDrawn = true;
	}

	public int getDrawId () {
		return this.drawId;
	}

	public boolean isDrawn () {
		return this.isDrawn;
	}

}
