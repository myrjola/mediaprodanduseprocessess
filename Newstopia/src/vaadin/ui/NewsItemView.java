package vaadin.ui;

import vaadin.main.window.NewsItemDisplayer;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class NewsItemView extends Panel {

    private Label article;
    private NewsItemDisplayer app;

    public NewsItemView(final NewsItemDisplayer app) {
        this.app = app;
        addStyleName("view");

        setCaption("Articles be here");
        setSizeFull();

        /* Use a FormLayout as main layout for this Panel */
        VerticalLayout Layout = new VerticalLayout();
        setContent(Layout);

        /* Create UI components */
        article = new Label("Article text goes here?",Label.CONTENT_XHTML);

        /* Add all the created components to the form */
        addComponent(article);
        //addComponent(fieldToSearch);
        //addComponent(saveSearch);
        //addComponent(searchName);
        //addComponent(search);
    }

}
