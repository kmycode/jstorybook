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
package jstorybook.viewtool.messenger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * メッセンジャ
 *
 * @author KMY
 */
public class Messenger {

	private static final Messenger defaultInstance = new Messenger();
	private final List<MessageReceiver> receiverList = new ArrayList<>();

	public static Messenger getInstance () {
		return Messenger.defaultInstance;
	}

	public void apply (Class<? extends Message> messageType, Object actionApplier, ActionListener listener) {
		this.receiverList.add(new MessageReceiver(messageType, actionApplier, listener));
	}

	public void send (Message message) {
		Class<? extends Message> messageType = message.getClass();
		for (MessageReceiver receiver : this.receiverList) {
			if (receiver.messageType == messageType) {
				ActionEvent ev = new ActionEvent(message, message.hashCode(), message.toString());
				receiver.listener.actionPerformed(ev);
			}
		}
	}

	public void remove (Object actionApplier) {
		List<MessageReceiver> removeList = new ArrayList<>();
		for (MessageReceiver receiver : this.receiverList) {
			if (receiver.actionApplier == actionApplier) {
				removeList.add(receiver);
			}
		}
		this.receiverList.removeAll(removeList);
	}

	public void remove (Object actionApplier, Message message) {
		List<MessageReceiver> removeList = new ArrayList<>();
		Class<? extends Message> messageType = message.getClass();
		for (MessageReceiver receiver : this.receiverList) {
			if (receiver.actionApplier == actionApplier && receiver.messageType == messageType) {
				removeList.add(receiver);
			}
		}
		this.receiverList.removeAll(removeList);
	}

	// -------------------------------------------------------
	// メッセージの受信に必要な情報を取りまとめたイミュータブルなクラス
	private static class MessageReceiver {

		public final Class<? extends Message> messageType;
		public final Object actionApplier;		// アクションを登録したオブジェクト
		public final ActionListener listener;

		public MessageReceiver (Class<? extends Message> messageType, Object actionApplier, ActionListener listener) {
			this.messageType = messageType;
			this.actionApplier = actionApplier;
			this.listener = listener;
		}
	}

}
