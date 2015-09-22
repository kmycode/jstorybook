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
package jstorybook.model.story.sync;

import java.sql.SQLException;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jstorybook.common.contract.StorySettingName;
import jstorybook.model.dao.DAO;
import jstorybook.model.entity.StorySetting;
import jstorybook.model.story.StoryModel;
import jstorybook.view.dialog.ExceptionDialog;

/**
 * ストーリーを保存するための非同期クラス
 *
 * @author KMY
 */
public class StorySaveSync extends Task<Object> {

	private final StoryModel storyModel;

	private final LongProperty goal = new SimpleLongProperty(0);
	private final LongProperty step = new SimpleLongProperty(0);
	private final BooleanProperty countAllModelFinish = new SimpleBooleanProperty(false);
	private final BooleanProperty finish = new SimpleBooleanProperty(false);

	private long lastStep = 0;

	public StorySaveSync (StoryModel storyModel) {
		this.storyModel = storyModel;
	}

	@Override
	protected Object call () throws Exception {
		List<DAO> daoList = this.storyModel.getDAOList();
		if (this.isCancelled()) {
			return null;
		}

		// 必要な作業量を計算
		int goalNum = 0;
		for (DAO dao : daoList) {
			goalNum += dao.getModelCount();
			if (this.isCancelled()) {
				return null;
			}
		}
		this.goal.set(goalNum);
		this.countAllModelFinish.set(true);
		if (this.isCancelled()) {
			return null;
		}

		// エンティティ数を記録
		StorySetting entityCount = this.storyModel.getSetting(StorySettingName.ENTITY_COUNT);

		// 保存処理
		this.lastStep = 0;
		InvalidationListener listener = (obj) -> {
			LongProperty num = ((LongProperty) obj);
			this.step.set(num.get() + this.lastStep);
		};
		for (DAO dao : daoList) {
			try {
				dao.saveStepProperty().addListener(listener);
				dao.saveList();
				dao.saveStepProperty().removeListener(listener);

				this.lastStep = this.step.get();
				entityCount.intValueProperty().set(this.lastStep);

				if (this.isCancelled()) {
					return null;
				}
			} catch (SQLException e) {
				Platform.runLater(() -> {
					ExceptionDialog.showAndWait(e);
				});
				e.printStackTrace();
			}
		}

		this.step.set(this.goal.get());
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

	public static class StorySaveService extends Service<Object> {

		private final StoryModel storyModel;
		private final LongProperty goal = new SimpleLongProperty(0);
		private final LongProperty step = new SimpleLongProperty(0);
		private final DoubleProperty progress = new SimpleDoubleProperty(0);
		private final BooleanProperty countAllModelFinish = new SimpleBooleanProperty(false);
		private final BooleanProperty finish = new SimpleBooleanProperty(false);

		public StorySaveService (StoryModel storyModel) {
			this.storyModel = storyModel;
			this.step.addListener((obj) -> {
				if (this.goal.get() > 0) {
					this.progress.set((double) this.step.get() / (double) this.goal.get());
				}
			});
		}

		@Override
		protected Task<Object> createTask () {
			StorySaveSync task = new StorySaveSync(this.storyModel);
			this.goal.bind(task.goal);
			this.step.bind(task.step);
			this.countAllModelFinish.bind(task.countAllModelFinish);
			this.finish.bind(task.finish);
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
