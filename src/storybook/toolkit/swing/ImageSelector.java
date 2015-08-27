
package storybook.toolkit.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import storybook.ui.interfaces.IRefreshable;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import jstorybook.model.entity.Person;
import storybook.ui.MainFrame;

@SuppressWarnings("serial")
public class ImageSelector extends JPanel implements IRefreshable,
														MouseListener {

	public static final String COMP_NAME_BT_PALETTE = "bt:palette";
	private static File currentDirectory = null;

	private String path;
	private boolean isPathChanged;
	private JLabel lbShowIcon;
	private JButton btIconChooser;
	private IconButton btPalette;
	private JButton btColorChooser;
	private IconButton btClearColor;
	private Color[] colors;
	private boolean allowNoColor;
	private Color startColor;
	private String title;
	private MainFrame mainFrame;

	public ImageSelector (MainFrame mainFrame) {
		this(mainFrame, "");
	}

	public ImageSelector (MainFrame mainFrame, String title) {
		this.title = title;
		this.mainFrame = mainFrame;
		initGUI();
	}

	private void initGUI() {
		MigLayout layout = new MigLayout("insets 0");
		setLayout(layout);

		this.lbShowIcon = new JLabel();
		this.add(lbShowIcon);

		// button to clear the color
		btClearColor = new IconButton("icon.small.delete", getClearImageAction());
		btClearColor.setSize20x20();
		this.add(btClearColor);

		this.btIconChooser = new JButton();
		this.btIconChooser.setAction(this.getShowImageFileChooserAction());
		this.add(btIconChooser);
	}

	@Override
	public void refresh() {
		removeAll();
		initGUI();
	}

	public void setIcon (ImageIcon icon) {
		icon = new ImageIcon(icon.getImage().getScaledInstance(100, -1,
															   Image.SCALE_SMOOTH));
		this.lbShowIcon.setIcon(icon);
	}

	public Icon getIcon () {
		return this.lbShowIcon.getIcon();
	}

	private void setPath (String path) {
		this.path = path;
		ImageIcon icon = new ImageIcon(this.path);
		ImageSelector.this.setIcon(icon);
	}

	// db.image/c__users_kmy_test.png
	public void setImgName (String filename) {
		this.setPath(Person.getImageIconPath(this.mainFrame.getDbFile()) + filename);
	}

	// ファイル選択ダイアログで選択されたパスを取得
	public String getPath () {
		return this.path;
	}

	// 現在選択されているファイルの、専用画像フォルダへ保存された状態のパスを取得
	// db.image/c__users_kmy_test.png
	public String getPathWithImgFolder () {
		String path = this.path;
		path = path.replace(File.separatorChar, '_');
		path = path.replace(':', '_');
		return Person.getImageIconPath(this.mainFrame.getDbFile()) + path;
	}

	// ファイルの専用画像フォルダへのコピーをともなう、ファイル名取得
	// c__users_kmy_test.png
	public String getNameWithCopy () {
		if (this.isPathChanged) {
			File dir = new File(Person.getImageIconPath(this.mainFrame.getDbFile()));
			if ( ! dir.exists()) {
				dir.mkdirs();
			}
			try {
				FileUtils.copyFile(new File(this.path), new File(getPathWithImgFolder()));
			} catch (IOException ex) {
				System.err.println("storybook.toolkit.swing.getPathWithCopy(String) " + ex.toString());
				System.err.println("dir=" + Person.getImageIconPath(this.mainFrame.getDbFile()));
			}
			return new File(this.getPathWithImgFolder()).getName();
		}
		else {
			return new File(this.path).getName();
		}
	}

	private AbstractAction getShowImageFileChooserAction () {
		return new AbstractAction(title) {
			public void actionPerformed (ActionEvent evt) {
				JFileChooser filechooser = new JFileChooser();
				if (currentDirectory != null) {
					filechooser.setCurrentDirectory(currentDirectory);
				}
				int selected = filechooser.showOpenDialog(ImageSelector.this.getParent());
				if (selected == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					ImageSelector.this.setPath(file.getPath());
					ImageSelector.this.isPathChanged = true;
					ImageSelector.currentDirectory = filechooser.getCurrentDirectory();
				}
			}
		};
	}

	private AbstractAction getClearImageAction () {
		return new AbstractAction("") {
			public void actionPerformed (ActionEvent evt) {
				ImageSelector.this.setPath("");
				ImageSelector.this.isPathChanged = true;
			}
		};
	}

	@Override
	public void mouseClicked (MouseEvent e) {
	}

	@Override
	public void mousePressed (MouseEvent e) {
	}

	@Override
	public void mouseReleased (MouseEvent e) {
	}

	@Override
	public void mouseEntered (MouseEvent e) {
	}

	@Override
	public void mouseExited (MouseEvent e) {
	}
}
