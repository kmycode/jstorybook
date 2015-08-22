/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2008 - 2012 Martin Mustun

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package storybook.toolkit.completer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 * 複数の文字列を組み合わせて１つの文字列を作る
 *
 * @author martin
 *
 */
public class AbbrCompleter extends AbstractCompleter {

	private ArrayList<JTextComponent> sourceCompList = new ArrayList<JTextComponent>();
	private ArrayList<String> compNameList = new ArrayList<String>();
	private ArrayList<Integer> compNameSizeList = new ArrayList<Integer>();

	public AbbrCompleter (String firstName, String... names) {
		this.compNameList.add(firstName);
		for (String name : names) {
			this.compNameList.add(name);
		}
	}

	@Override
	public String getCompletedText() {
		try {
			int compListSize = this.sourceCompList.size();
			int sizeListSize = this.compNameSizeList.size();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < compListSize; i ++) {
				JTextComponent sourceComp = this.sourceCompList.get(i);

				if (sourceComp.getText().length() >= 1) {
					int strlen = 2;			// フォークした時に 2 と書かれていたので、これが初期値
					if (i < sizeListSize) {
						strlen = this.compNameSizeList.get(i);
					}
					if (strlen > 0 && strlen <= sourceComp.getText().length()) {
						builder.append(sourceComp.getText(0, strlen));
					} else {
						builder.append(sourceComp.getText());
					}
				}
			}
			return builder.toString();
		} catch (BadLocationException e) {
			// ignore
		}
		return "";
	}

	@Deprecated
	public JTextComponent getSourceComp1 () {
		this.compArrayCheck();
		return this.sourceCompList.get(0);
	}

	@Deprecated
	public void setSourceComp1(JTextComponent sourceComp1) {
		this.compArrayCheck();
		this.sourceCompList.set(0, sourceComp1);
	}

	@Deprecated
	public JTextComponent getSourceComp2() {
		this.compArrayCheck();
		return this.sourceCompList.get(1);
	}

	@Deprecated
	public void setSourceComp2(JTextComponent sourceComp2) {
		this.compArrayCheck();
		this.sourceCompList.set(1, sourceComp2);
	}

	public void setCompNameSize (int firstSize, int... sizes) {
		if (sizes.length + 1 != this.compNameList.size()) {
			throw new IllegalArgumentException(
					"storybook.toolkit.completer.AbbrCompleter#setCompNameSize(int, int...)");
		}
		this.compNameSizeList.add(firstSize);
		for (int size : sizes) {
			this.compNameSizeList.add(size);
		}
	}

	public void setSourceComp (JTextComponent firstComponent, JTextComponent... components) {
		if (components.length + 1 != this.compNameList.size()) {
			throw new IllegalArgumentException(
					"storybook.toolkit.completer.AbbrCompleter#setSourceComp(JTextComponent, JTextComponent...)");
		}
		this.sourceCompList.add(firstComponent);
		for (JTextComponent component : components) {
			this.sourceCompList.add(component);
		}
	}

	private void compArrayCheck () {
		int sourceCompSize = this.sourceCompList.size();
		for (int i = 0; i < 2 - sourceCompSize; i ++) {
			this.sourceCompList.add(null);
		}
	}

	private void nameArrayCheck () {
		int compNameSize = this.compNameList.size();
		for (int i = 0; i < 2 - compNameSize; i ++) {
			this.compNameList.add(null);
		}
	}

	@Deprecated
	public String getCompName1 () {
		this.nameArrayCheck();
		return this.compNameList.get(0);
	}

	@Deprecated
	public String getCompName2() {
		this.nameArrayCheck();
		return this.compNameList.get(1);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		comp.setText(getCompletedText());
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
