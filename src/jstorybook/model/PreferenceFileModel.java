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
package jstorybook.model;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
import jstorybook.common.contract.PreferenceKey;
import jstorybook.model.db.SQLiteFile;
import jstorybook.model.sync.PreferenceLoadSync;
import jstorybook.model.sync.PreferenceSaveSync;
import jstorybook.viewtool.messenger.IUseMessenger;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.dialog.ProgressDialogShowMessage;

/**
 * 設定ファイル保存先のモデル
 *
 * @author KMY
 */
public class PreferenceFileModel implements IUseMessenger {

	private final SQLiteFile db;
	private final BooleanProperty isOpen = new SimpleBooleanProperty(false);
	private String dirName = System.getProperty("user.home") + "/.jstorybook";
	private String fileName = dirName + "/preference.db";
	private Messenger messenger = Messenger.getInstance();

	public PreferenceFileModel () throws SQLException, IOException {

		// ディレクトリ存在確認
		File dir = new File(dirName);
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				throw new IOException("~/.jstorybook directory create failed.");
			}
		}
		else if (!dir.isDirectory()) {
			throw new IOException("~/.jstorybook is not a directory.");
		}

		// ファイル存在確認、存在しなければ初期化
		boolean initFlag = false;
		if (!new File(fileName).exists()) {
			initFlag = true;
		}

		// ファイルを開く
		this.db = new SQLiteFile(new File(fileName).toPath().toString());

		// 初期化するなら初期化
		if (initFlag) {
			this.init();
		}

		// 例外発生なら、コードはここまで到達しない
		this.isOpen.set(true);
	}

	// 設定ファイルをリセット
	public void reset() {
		new File(this.fileName).delete();
	}

	private void init () throws SQLException {
		this.updateQuery("create table preference (key text, value);");
	}

	public void loadSettings () throws SQLException {
		this.loadSettings(PreferenceKey.getList());
	}

	public void loadSettingsSync () {
		this.loadSettingsSync(PreferenceKey.getList());
	}

	public void saveSettings () throws SQLException {
		this.saveSettings(PreferenceKey.getList());
	}

	public void saveSettingsSync () {
		this.saveSettingsSync(PreferenceKey.getList());
	}

	public void loadSettingsSync (List<PreferenceKey> keys) {

		PreferenceLoadSync.PreferenceLoadService service = new PreferenceLoadSync.PreferenceLoadService(this.db, keys);

		// 進捗状況を表示
		this.messenger.send(new ProgressDialogShowMessage(service.myProgressProperty()));

		// 保存処理（非同期）
		service.stepProperty().addListener((obj) -> {
			// getバグ
			service.stepProperty().get();
		});
		service.start();
	}

	@Deprecated
	public void loadSettings (List<PreferenceKey> keys) throws SQLException {
		ResultSet rs = this.executeQuery("select * from preference");
		while (rs.next()) {
			String keyName = rs.getString("key");
			PreferenceKey key = null;
			for (PreferenceKey keytmp : keys) {
				if (keytmp.getName().equals(keyName)) {
					key = keytmp;
					break;
				}
			}

			if (key != null) {
				switch (key.getType()) {
				case INTEGER:
					key.setValue(rs.getInt("value"));
					break;
				case DOUBLE:
					key.setValue(rs.getDouble("value"));
					break;
				case BOOLEAN:
					key.setValue(rs.getBoolean("value"));
					break;
				case STRING:
					key.setValue(rs.getString("value"));
					break;
				case ORIENTATION:
					key.setValue(rs.getBoolean("value") ? Orientation.VERTICAL : Orientation.HORIZONTAL);
					break;
				}
			}
		}
	}

	public void saveSettingsSync (List<PreferenceKey> keys) {

		PreferenceSaveSync.PreferenceSaveService service = new PreferenceSaveSync.PreferenceSaveService(this.db, keys);

		// 進捗状況を表示
		this.messenger.send(new ProgressDialogShowMessage(service.myProgressProperty()));

		// 保存処理（非同期）
		service.stepProperty().addListener((obj) -> {
			// getバグ
			service.stepProperty().get();
		});
		service.start();
	}

	@Deprecated
	public void saveSettings (List<PreferenceKey> keys) throws SQLException {

		// すでに保存されているキーの一覧を取得
		ResultSet rs = this.executeQuery("select key from preference");
		List<String> savedKeyNameList = new ArrayList<>();
		while (rs.next()) {
			savedKeyNameList.add(rs.getString("key"));
		}

		for (PreferenceKey key : keys) {
			String value = "";
			switch (key.getType()) {
			case INTEGER:
				value = key.getInteger().toString();
				break;
			case DOUBLE:
				value = key.getDouble().toString();
				break;
			case BOOLEAN:
				value = "'" + key.getBoolean().toString() + "'";
				break;
			case STRING:
				value = "'" + key.getString() + "'";
				break;
			case ORIENTATION:
				value = "'" + (key.getValue() == Orientation.VERTICAL ? "1" : "0") + "'";
				break;
			}

			// すでに保存されているものを上書きする形であるか確認
			if (savedKeyNameList.indexOf(key.getName()) >= 0) {
				this.updateQuery("update preference set value=" + value + " where key = '" + key.getName() + "';");
			}
			// そうでなければその場で作る
			else {
				this.updateQuery("insert into preference(key,value) values ('" + key.getName() + "'," + value + ");");
			}
		}
	}

	private ResultSet executeQuery (String sql) throws SQLException {
		return this.db.executeQuery(sql);
	}

	private void updateQuery (String sql) throws SQLException {
		this.db.updateQuery(sql);
	}

	public BooleanProperty isOpenProperty () {
		return this.isOpen;
	}

	@Override
	public void setMessenger (Messenger messenger) {
		this.messenger = messenger;
	}

}
