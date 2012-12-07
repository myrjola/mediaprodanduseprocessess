package vaadin.ui;

import java.util.ArrayList;

import vaadin.main.window.NewsItemDisplayer;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class NewsItemView extends Panel {

    private Label article;
    private NewsItemDisplayer app;
    private ArrayList list;

    public NewsItemView(final NewsItemDisplayer app, ArrayList list) {
        this.app = app;
        this.list = list;
        addStyleName("view");
        
        setCaption("Articles be here");
        setSizeFull();

        /* Use a FormLayout as main layout for this Panel */
        VerticalLayout Layout = new VerticalLayout();
        setContent(Layout);
        String names = null;
        for(int i = 0; i<list.size();i++) {
        	names = names + list.get(i).toString() + '\n';
        }
        	
        /* Create UI components */
        article = new Label(names,Label.CONTENT_XHTML);

        /* Add all the created components to the form */
        addComponent(article);
        //addComponent(fieldToSearch);
        //addComponent(saveSearch);
        //addComponent(searchName);
        //addComponent(search);
    }

}
