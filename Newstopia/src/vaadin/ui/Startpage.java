package vaadin.ui;

import vaadin.main.window.NewsItemDisplayer;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Startpage extends Panel {

	private Label article;
	private NewsItemDisplayer app;

	public Startpage(final NewsItemDisplayer app) {
		this.app = app;
		// String test = string;
		addStyleName("start");

		setCaption("Welcome");
		setSizeFull();

		/* Use a FormLayout as main layout for this Panel */
		VerticalLayout Layout = new VerticalLayout();
		setContent(Layout);

		/* Create UI components */
		article = new Label("Home to the best articles available",
				Label.CONTENT_XHTML);

		/* Add all the created components to the form */
		addComponent(article);
	}

}
