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
package jstorybook.view.control;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

enum DockingDirection {
	NONE,
	TOP,
	RIGHT,
	BOTTOM,
	LEFT,
	OVER;
}

/**
 * ドッキング可能なエリアで、ひとかたまりの長方形グループをあらわす
 * SplitPaneの特性で水平・垂直方向のどちらかにしか指定できないためｒｙ
 * ちなみにrootはVerticalであることを想定
 * itemは、DockableTabPaneとDockableAreaGroupPaneのどちらかを入れる
 *
 * @author KMY
 */
public class DockableAreaGroupPane extends SplitPane {

	private DockableAreaGroupPane parent;
	private DockableAreaGroupPane rootPane = null;		// 取得時は必ずgetRootPaneを使用
	private DockablePane rootParent;
	private static DockingBorder dockingBorder;
	private static DockingDirection dockingDirection = DockingDirection.NONE;
	private static DockableTabPane overTabPane;

	public DockableAreaGroupPane (DockableAreaGroupPane parent) {
		this.parent = parent;
		this.rootPane = parent.getRootPane();
		this.setOrientation(Orientation.VERTICAL);
	}

	public DockableAreaGroupPane (DockablePane parent) {
		this.rootParent = parent;
		DockableAreaGroupPane.dockingBorder = new DockingBorder(parent.getWindow());
		DockableAreaGroupPane.dockingBorder.setLocalPosition(0, 0);
		DockableAreaGroupPane.dockingBorder.setSize(100, 100);
		this.setOrientation(Orientation.VERTICAL);
	}

	// コンストラクタ内でのthisリークを防ぐ
	// rootPaneを利用するときは、必ずこのメソッドを経由すること
	DockableAreaGroupPane getRootPane () {
		if (this.rootPane == null) {
			this.rootPane = this;
		}
		return this.rootPane;
	}

	// -------------------------------------------------------

	public DockableTabPane add (int index) {
		DockableTabPane pane = new DockableTabPane(this);
		AnchorPane.setTopAnchor(pane, 10.0);
		AnchorPane.setLeftAnchor(pane, 10.0);
		AnchorPane.setRightAnchor(pane, 10.0);
		AnchorPane.setBottomAnchor(pane, 10.0);
		this.getItems().add(index, pane);
		return pane;
	}

	void moveTab (DockingDirection direction, DockableTab tab) {
		DockableTabPane pane = new DockableTabPane(this);
		if (direction != DockingDirection.NONE && direction != DockingDirection.OVER) {
			DockableTabPane tabPane = new DockableTabPane(this);
			tabPane.getTabs().add(tab);
			if (this.getOrientation() == Orientation.VERTICAL) {
				if (direction == DockingDirection.BOTTOM) {
					this.getItems().add(tabPane);
				}
				else if (direction == DockingDirection.TOP) {
					this.getItems().add(0, tabPane);
				}
			}
			else if (this.getOrientation() == Orientation.HORIZONTAL) {
				if (direction == DockingDirection.RIGHT) {
					this.getItems().add(tabPane);
				}
				else if (direction == DockingDirection.LEFT) {
					this.getItems().add(0, tabPane);
				}
			}
		}
		else if (direction == DockingDirection.OVER) {
			this.getOverTabPane().getTabs().add(tab);
		}
	}

	List<DockableTabPane> getTabPaneList () {
		ArrayList<DockableTabPane> result = new ArrayList<>();
		this.getTabPaneList(result);
		return result;
	}

	private void getTabPaneList (List<DockableTabPane> result) {
		for (Node node : this.getItems()) {
			if (node instanceof DockableTabPane) {
				result.add((DockableTabPane) node);
			}
			else if (node instanceof DockableAreaGroupPane) {
				((DockableAreaGroupPane) node).getTabPaneList(result);
			}
		}
	}

	public void remove (TabPane e) {
		this.getChildren().remove(e);
	}

	public void remove (int index) {
		this.getChildren().remove(index);
	}

	// タブを持たないタブパネルやグループパネルを見つけたら消す
	public void removeEmptyTabPane () {
		List<Node> removeNodeList = new ArrayList<>();
		for (Node node : this.getItems()) {
			if (node instanceof DockableTabPane) {
				DockableTabPane tabPane = (DockableTabPane) node;
				if (tabPane.getTabs().size() <= 0) {
					removeNodeList.add(tabPane);
				}
			}
			else if (node instanceof DockableAreaGroupPane) {
				DockableAreaGroupPane groupPane = (DockableAreaGroupPane) node;
				if (groupPane.getItems().size() <= 0) {
					removeNodeList.add(groupPane);
				}
			}
		}
		this.getItems().removeAll(removeNodeList);
	}

	// -------------------------------------------------------
	void startShowDockingBorder (MouseEvent ev) {
		this.getRootPane().showDockingBorder(ev);
	}

	private void showDockingBorder (MouseEvent ev) {
		double windowX = this.rootParent.getWindow().getX();
		double windowY = this.rootParent.getWindow().getY();
		double mouseX = ev.getScreenX();
		double mouseY = ev.getScreenY();
		double localMouseX = ev.getX();
		double localMouseY = ev.getY();
		Point2D position = this.getPosition();
		Rectangle2D size = this.getSize();
		double paneX = windowX + position.getX();
		double paneY = windowY + position.getY();
		double paneW = size.getWidth();
		double paneH = size.getHeight();

		// Right
		if (paneX + paneW - 60 < mouseX) {
			DockableAreaGroupPane.dockingBorder.setPosition(paneX + paneW - 50, paneY + 30);
			DockableAreaGroupPane.dockingBorder.setSize(50, paneH);
			DockableAreaGroupPane.dockingBorder.show(this.getRootPane().rootParent.getWindow());
			DockableAreaGroupPane.dockingDirection = DockingDirection.RIGHT;
		}
		// Bottom
		else if (paneY + paneH - 60 < mouseY) {
			DockableAreaGroupPane.dockingBorder.setPosition(paneX + 10, paneY + paneH - 30);
			DockableAreaGroupPane.dockingBorder.setSize(paneW, 50);
			DockableAreaGroupPane.dockingBorder.show(this.getRootPane().rootParent.getWindow());
			DockableAreaGroupPane.dockingDirection = DockingDirection.BOTTOM;
			/*
						System.out.println("x:" + paneX + "  y:" + paneY + "  w:" + paneW + "  h:" + paneH + "  mx:" + mouseX
					+ "  my:"
					+ mouseY);
			 */
		}
		// Top
		else if (paneY + 60 > mouseY) {
			DockableAreaGroupPane.dockingBorder.setPosition(paneX + 10, paneY + 30);
			DockableAreaGroupPane.dockingBorder.setSize(paneW, 50);
			DockableAreaGroupPane.dockingBorder.show(this.getRootPane().rootParent.getWindow());
			DockableAreaGroupPane.dockingDirection = DockingDirection.TOP;
		}
		// Left
		else if (paneX + 60 > mouseX) {
			DockableAreaGroupPane.dockingBorder.setPosition(paneX + 10, paneY + 30);
			DockableAreaGroupPane.dockingBorder.setSize(50, paneH);
			DockableAreaGroupPane.dockingBorder.show(this.getRootPane().rootParent.getWindow());
			DockableAreaGroupPane.dockingDirection = DockingDirection.LEFT;
		}
		// Over
		else {
			DockableAreaGroupPane.dockingDirection = DockingDirection.OVER;
			List<DockableTabPane> tabPaneList = this.getTabPaneList();
			for (DockableTabPane tabPane : tabPaneList) {
				Rectangle rect = tabPane.getLocalRectangle();
				if (localMouseX > rect.x && localMouseX < rect.x + rect.width && localMouseY > rect.y && localMouseY
						< rect.y + rect.height) {
					DockableAreaGroupPane.dockingBorder.setPosition(rect.x + windowX + 10, rect.y + windowY + 30);
					DockableAreaGroupPane.dockingBorder.setSize(rect.width, rect.height);
					DockableAreaGroupPane.dockingBorder.show(this.getRootPane().rootParent.getWindow());
					DockableAreaGroupPane.overTabPane = tabPane;
					break;
				}
			}
		}
	}

	DockingDirection startHideDockingBorder () {
		DockingDirection result = DockableAreaGroupPane.dockingDirection;
		this.getRootPane().hideDockingBorder();
		return result;
	}

	private void hideDockingBorder () {
		DockableAreaGroupPane.dockingBorder.hide();
		DockableAreaGroupPane.dockingDirection = DockingDirection.NONE;
	}

	DockableTabPane getOverTabPane () {
		return DockableAreaGroupPane.overTabPane;
	}

	Point2D getPosition () {
		int x = 0;
		int y = 0;
		DockableAreaGroupPane pane = this;
		DockableAreaGroupPane parentPane = this.parent;
		while (pane != pane.getRootPane()) {
			for (Node node : parentPane.getItems()) {
				if (node != this) {
					if (node instanceof Control) {
						if (parentPane.getOrientation() == Orientation.VERTICAL) {
							y += ((Control) node).getHeight();
						}
						else {
							x += ((Control) node).getWidth();
						}
					}
				}
				else {
					break;
				}
			}
		}
		return new Point2D(x, y);
	}

	private Rectangle2D getSize () {
		double width = this.getWidth();
		double height = this.getHeight();
		return new Rectangle2D(0, 0, width, height);
	}

}
