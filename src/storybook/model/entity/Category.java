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

/**
 * Part generated by hbm2java
 *
 * @hibernate.class
 *   table="CATEGORY"
 */
public class Category extends AbstractEntity implements Comparable<Category> {

	private Integer sort;
	private String name;
	private Category sup;

	public Category() {
	}

	public Category(Integer sort, String name, Category sup) {
		this.sort = sort;
		this.name = name;
		this.sup = sup;
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
	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * @hibernate.property
	 */
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.property
	 */
    public Category getSup() {
        return this.sup;
    }

    public boolean hasSup() {
        return sup != null;
    }

    public void setSup(Category sup) {
		this.sup = sup;
    }

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		Category test = (Category) obj;
		boolean ret = true;
		ret = ret && equalsIntegerNullValue(sort, test.getSort());
		ret = ret && equalsStringNullValue(name, test.getName());
		return ret;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = hash * 31 + sort.hashCode();
		hash = hash * 31 + name.hashCode();
		return hash;
	}

	@Override
	public int compareTo(Category ca) {
		if (hasSup() && (ca.hasSup()) && (!(getSup().equals(ca.getSup())))) {
			return getSup().compareTo(ca.getSup());
		}
		return sort.compareTo(ca.sort);
	}
}
