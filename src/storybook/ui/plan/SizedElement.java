package storybook.ui.plan;

import jstorybook.model.entity.Chapter;
import jstorybook.model.entity.Part;
import jstorybook.model.entity.Scene;

public class SizedElement {
	Object element;
	int size;
	
	public Object getElement() {
		return element;
	}
	/**
	 * @param element
	 */
	public void setElement(Object element) {
		this.element = element;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public String toString() {
		if (element instanceof Part) {
			Part part = (Part)element;
			return (part.getName() + "    (" + size + "/" + part.getObjectiveChars() +")");
		} else if (element instanceof Chapter) {
			Chapter chapter = (Chapter)element;
			return chapter.getTitle() + "    (" + size + "/" + chapter.getObjectiveChars() +")";
		} else if (element instanceof Scene) {
			Scene scene = (Scene)element;
			return scene.getTitle() + "    (" + size + ")";
		} else if (element instanceof String) {
			return (String)element;
		}
		return "";
	}
}
