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
package jstorybook.common.manager;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jstorybook.view.dialog.ExceptionDialog;

/**
 *
 * @author KMY
 */
public class ResourceManager {

	private static ResourceManager defaultInstance = new ResourceManager();
	private ResourceBundle resourceBundle;

	private ResourceManager () {
		this.changeLocale(LocaleManager.getInstance().localeProperty().get());
		LocaleManager.getInstance().localeProperty().addListener((obj) -> {
			ObjectProperty<Locale> newObj = (ObjectProperty<Locale>) obj;
			ResourceManager.this.changeLocale(newObj.get());
		});
	}

	private void changeLocale (Locale locale) {
		this.resourceBundle = ResourceBundle.getBundle("jstorybook.resource.message.messages", locale);
	}

	public static String getMessage (String resourceKey, Object... args) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(Locale.getDefault());
		
		String pattern = "";
		try {
			pattern = defaultInstance.resourceBundle.getString(resourceKey);
		} catch (Exception e) {
			if (resourceKey.indexOf("msg.exception") == 0) {
				ExceptionDialog.showExpenditure(e);
			}
			else {
				ExceptionDialog.showAndWait(e);
			}
		}

		formatter.applyPattern(pattern);
		return formatter.format(args);
	}

	public static String getCss (String resourceKey) {
		return ResourceManager.class.getResource("/jstorybook/resource/stylesheet/" + resourceKey).toExternalForm();
	}

	public static String getIconResource (String resourceKey) {
		return ResourceManager.class.getResource("/jstorybook/resource/icon/" + resourceKey).toExternalForm();
	}

	public static ImageView getIconNode (String resourceKey) {
		return new ImageView(new Image(ResourceManager.class.getResource("/jstorybook/resource/icon/" + resourceKey).toExternalForm()));
	}

	public static ImageView getMiniIconNode (String resourceKey) {
		Image image = new Image(ResourceManager.class.getResource("/jstorybook/resource/icon/" + resourceKey).toExternalForm(), 16, 16,
								false, true);
		ImageView imageView = new ImageView(image);
		return imageView;
	}

}
