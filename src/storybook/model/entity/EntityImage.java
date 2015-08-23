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

package storybook.model.entity;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import storybook.toolkit.I18N;

/**
 * Gender generated by hbm2java
 * @hibernate.class
 *   table="GENDER"
 */
public class EntityImage extends AbstractEntity {

	private String path;
	private Icon image = null;

	public EntityImage () {
		this.path = "";
	}

	public EntityImage (String path) {
		this.setPath(path);
	}

	/**
	 * @hibernate.id
	 *   column="ID"
	 *   generator-class="increment"
	 *   unsaved-value="null"
	 */
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @hibernate.property
	 */
	public String getPath () {
		return this.path;
	}

	public void setPath (String path) {
		this.path = path;
		this.image = null;
	}

	public Icon getImage () {
		if (this.image == null &&  ! this.path.isEmpty()) {
			this.image = new ImageIcon(this.path);
		}
		return this.image;
	}

	@Override
	public String toString() {
		return getPath();
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		EntityImage test = (EntityImage) obj;
		boolean ret = true;
		ret = ret && equalsStringNullValue(path, test.getPath());
		return ret;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = hash * 31 + (this.path != null ? this.path.hashCode() : 0);
		return hash;
	}
}
