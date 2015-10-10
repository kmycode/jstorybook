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
package jstorybook.model.sync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import jstorybook.common.contract.PreferenceKey;
import jstorybook.model.db.SQLiteFile;
import jstorybook.view.dialog.ExceptionDialog;

/**
 * 設定ファイルを読み込むための非同期クラス
 *
 * @author KMY
 */
public class PreferenceLoadSync extends Task<Object> {

	private final SQLiteFile db;
	private final List<PreferenceKey> keyList;

	private final LongProperty goal = new SimpleLongProperty(0);
	private final LongProperty step = new SimpleLongProperty(0);
	private final BooleanProperty countAllModelFinish = new SimpleBooleanProperty(false);
	private final BooleanProperty finish = new SimpleBooleanProperty(false);

	public PreferenceLoadSync (SQLiteFile db, List<PreferenceKey> keyList) {
		this.db = db;
		this.keyList = keyList;
	}

	@Override
	protected Object call () throws Exception {

		// 必要な作業量を計算
		int goalNum = this.keyList.size() + 1;
		this.goal.set(goalNum);
		this.countAllModelFinish.set(true);
		if (this.isCancelled()) {
			return null;
		}

		// 読み込み処理
		this.step.set(0);

		try {
			ResultSet rs = this.db.executeQuery("select * from preference");
			while (rs.next()) {
				String keyName = rs.getString("key");
				PreferenceKey key = null;
				for (PreferenceKey keytmp : this.keyList) {
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
				this.step.set(this.step.get() + 1);
			}
		} catch (SQLException e) {
			Platform.runLater(() -> {
				ExceptionDialog.showAndWait(e);
			});
			e.printStackTrace();
		}

		this.step.set(this.goal.get());
		this.succeeded();
		return new Object();
	}

	public LongProperty goalProperty () {
		return this.goal;
	}

	public LongProperty stepProperty () {
		return this.step;
	}

	public BooleanProperty finishProperty () {
		return this.finish;
	}

	public BooleanProperty countAllModelFinishProperty () {
		return this.countAllModelFinish;
	}

	public static class PreferenceLoadService extends Service<Object> {

		private final SQLiteFile db;
		private final List<PreferenceKey> keyList;
		private final LongProperty goal = new SimpleLongProperty(0);
		private final LongProperty step = new SimpleLongProperty(0);
		private final DoubleProperty progress = new SimpleDoubleProperty(0);
		private final BooleanProperty countAllModelFinish = new SimpleBooleanProperty(false);
		private final BooleanProperty finish = new SimpleBooleanProperty(false);

		public PreferenceLoadService (SQLiteFile db, List<PreferenceKey> keyList) {
			this.db = db;
			this.keyList = keyList;
			this.step.addListener((obj) -> {
				if (this.goal.get() > 0) {
					this.progress.set((double) this.step.get() / (double) this.goal.get());
				}
			});
		}

		@Override
		protected Task<Object> createTask () {
			PreferenceLoadSync task = new PreferenceLoadSync(this.db, this.keyList);
			this.goal.bind(task.goal);
			this.step.bind(task.step);
			this.countAllModelFinish.bind(task.countAllModelFinish);
			this.finish.bind(task.finish);
			task.setOnSucceeded((ev) -> this.succeeded());
			return task;
		}

		public DoubleProperty myProgressProperty () {
			return this.progress;
		}

		public LongProperty goalProperty () {
			return this.goal;
		}

		public LongProperty stepProperty () {
			return this.step;
		}

		public BooleanProperty finishProperty () {
			return this.finish;
		}

		public BooleanProperty countAllModelFinishProperty () {
			return this.countAllModelFinish;
		}

	}

}
