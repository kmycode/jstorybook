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
package jstorybook.view.pane.chart;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import jstorybook.common.manager.ResourceManager;
import jstorybook.common.util.GUIUtil;
import jstorybook.view.pane.MyPane;
import jstorybook.viewmodel.ViewModelList;
import jstorybook.viewmodel.pane.chart.PersonUsingChartViewModel;
import jstorybook.viewtool.messenger.CurrentStoryModelGetMessage;
import jstorybook.viewtool.messenger.Messenger;
import jstorybook.viewtool.messenger.general.ResetMessage;
import jstorybook.viewtool.messenger.pane.chart.Data2DSendMessage;

/**
 * 登場人物の使用頻度チャート
 *
 * @author KMY
 */
public class PersonUsingChartPane extends MyPane {

	private final BarChart<String, Number> chart;
	private XYChart.Series<String, Number> chartSeries = new XYChart.Series<>();
	private final Messenger mainMessenger;
	private final Messenger messenger = new Messenger();
	private final ViewModelList viewModelList = new ViewModelList(new PersonUsingChartViewModel());

	public PersonUsingChartPane (Messenger messenger) {
		super(ResourceManager.getMessage("msg.personusing"));

		this.mainMessenger = messenger;
		this.messenger.apply(Data2DSendMessage.class, this, (ev) -> this.addData((Data2DSendMessage<String, Number>) ev));
		this.messenger.apply(ResetMessage.class, this, (ev) -> this.reset());
		this.messenger.relay(CurrentStoryModelGetMessage.class, this, this.mainMessenger);
		this.viewModelList.storeMessenger(this.messenger);

		// チャートの設定
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel(ResourceManager.getMessage("msg.person"));
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel(ResourceManager.getMessage("msg.personusing.usenum"));
		this.chart = new BarChart<>(xAxis, yAxis);
		this.chart.setTitle(ResourceManager.getMessage("msg.personusing"));

		this.chart.getData().add(this.chartSeries);
		this.chartSeries.setName(ResourceManager.getMessage("msg.personusing.usenum"));
		GUIUtil.bindFontStyle(this.chart);
		this.setContent(this.chart);

		// チャートを作成
		this.viewModelList.executeCommand("load");

		// コンテキストメニュー
		MenuItem reloadMenu = GUIUtil.createMenuItem(this.viewModelList, "load");
		reloadMenu.setText(ResourceManager.getMessage("msg.reload"));
		ContextMenu contextMenu = new ContextMenu(reloadMenu);
		this.setContextMenu(contextMenu);
	}

	private void reset () {
		this.chartSeries.getData().clear();
		this.chart.getData().clear();
		this.chartSeries = new XYChart.Series<>();
		this.chart.getData().add(this.chartSeries);
	}

	private void addData (Data2DSendMessage<String, Number> message) {
		this.chartSeries.getData().add(new XYChart.Data<String, Number>(message.getXData(), message.getYData()));
	}

}
