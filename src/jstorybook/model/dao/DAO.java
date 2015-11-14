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
package jstorybook.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jstorybook.model.entity.Entity;
import jstorybook.model.entity.ISortableEntity;
import jstorybook.model.story.StoryFileModel;

/**
 * DAOクラスの抽象クラス
 *
 * @author KMY
 */
public abstract class DAO<E extends Entity> {

	private StoryFileModel storyFileModel;
	protected final ObjectProperty<ObservableList<E>> modelList = new SimpleObjectProperty<>();
	protected final List<Long> removeIdList = new ArrayList<>();
	protected final LongProperty saveStep = new SimpleLongProperty(0);
	protected int lastId = 0;				// 最大ID
	protected int lastIdSaved = 0;		// 最後に保存した時の最大ID

	public void setStoryFileModel (StoryFileModel storyFileModel) throws SQLException {
		this.setStoryFileModel(storyFileModel, false);
	}

	public void setStoryFileModel (StoryFileModel storyFileModel, boolean isCreate) throws SQLException {
		this.reset();
		this.storyFileModel = storyFileModel;
		if (isCreate) {
			this.getStoryFileModel().updateQuery("insert into idtable(key,value) values ('" + this.getTableName() + "',0);");
			this.createTable();
		}
		this.storyFileModelSet();
	}

	protected void reset () {
		this.storyFileModel = null;
		if (this.modelList.get() != null) {
			this.modelList.get().clear();
		}
		else {
			this.modelList.set(FXCollections.observableArrayList());
		}
		this.removeIdList.clear();
		this.lastId = 0;
		this.lastIdSaved = 0;
	}

	abstract protected String getTableName ();

	abstract protected E loadModel (ResultSet rs) throws SQLException;

	abstract protected void saveModel (E model) throws SQLException;

	abstract protected void createTable () throws SQLException;

	protected void removeModel (long id) throws SQLException {
		this.getStoryFileModel().updateQuery("delete from `" + this.getTableName() + "` where id = " + id + ";");
	}

	public void addModel (E model) {
		if (!model.isCreating()) {
			throw new RuntimeException("This model already created.");
		}
		model.idProperty().set(++this.lastId);
		if (model instanceof ISortableEntity) {
			((ISortableEntity) model).orderProperty().set(model.idProperty().get());
		}
		this.modelList.get().add(model);
	}

	public void deleteModel (E model) {
		int i = 0;
		for (E m : this.modelList.get()) {
			if (m.idProperty() == model.idProperty()) {
				this.modelList.get().remove(i);
				this.removeIdList.add(model.idProperty().get());
				break;
			}
			i++;
		}
	}

	public void loadList () throws SQLException {

		this.saveStep.set(0);
		int saveCount = 0;

		// IDの最大値を取得
		ResultSet rs = this.getStoryFileModel().executeQuery("select * from idtable where key = '" + this.getTableName()
				+ "';");
		if (rs.next()) {
			this.lastIdSaved = this.lastId = rs.getInt("value");
		}
		else {
			throw new SQLException("idtable doesn't have the key: " + this.getTableName());
		}

		// リストを作成
		ObservableList<E> result = FXCollections.observableArrayList();
		rs = this.getStoryFileModel().executeQuery("select * from `" + this.getTableName() + "`;");
		while (rs.next()) {
			E entity = this.loadModel(rs);
			entity.save();		// 変更フラグをオフにする
			result.add(entity);
			this.saveStep.set(++saveCount);
		}

		// ソート・順番振り直し
		Collections.sort(result);

		this.modelList.set(result);
	}

	// ソートして、順番を一から振り直す
	public void resetOrder () {
		List<E> result = this.modelList.get();

		Collections.sort(result);

		if (result.size() > 0 && result.get(0) instanceof ISortableEntity) {
			int count = 0;
			for (Entity entity : result) {
				((ISortableEntity) entity).orderProperty().set(++count);
			}
		}
	}

	public void saveList () throws SQLException {

		this.saveStep.set(0);
		int saveCount = 0;

		// 保存処理
		ObservableList<E> list = this.modelList.get();
		for (E model : list) {
			if (model.isChanged()) {
				model.save();
				this.saveModel(model);
			}
			this.saveStep.set(++saveCount);
		}

		// 削除処理
		for (long delid : this.removeIdList) {
			this.removeModel(delid);
		}

		this.lastIdSaved = this.lastId;
	}

	public E getModelById (long id) {
		for (E model : this.modelList.get()) {
			if (model.idProperty().get() == id) {
				return model;
			}
		}
		return null;
	}

	// ここでのsetは受動態。StoryFileModelがセットされた時に呼び出される
	// おもにStoryFileModelを使った処理を行う
	protected void storyFileModelSet () throws SQLException {
		this.loadList();
	}

	public ObjectProperty<ObservableList<E>> modelListProperty () {
		return this.modelList;
	}

	public int getModelCount () {
		return this.modelList.get().size();
	}

	public LongProperty saveStepProperty () {
		return this.saveStep;
	}

	protected StoryFileModel getStoryFileModel () {
		return this.storyFileModel;
	}

	// DAOの定義されていないテーブルを作成する
	public static void createStorySystemTable (StoryFileModel model) throws SQLException {
		model.updateQuery("CREATE TABLE idtable (key TEXT PRIMARY KEY NOT NULL, value INTEGER NOT NULL);");
	}

}
