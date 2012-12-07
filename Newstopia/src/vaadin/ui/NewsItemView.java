package vaadin.ui;

import java.util.ArrayList;

import vaadin.main.window.NewsItemDisplayer;

import aalto.media.newsml.NewsItem;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class NewsItemView extends Panel {

    private Label article;
    private Panel holder;
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
        for(int i = 0; i<list.size();i++) {
        	holder = new NewsItemView(this.app,(NewsItem)(list.get(i)));
        	addComponent(holder);
        }	
        /* Create UI components */
        //article = new Label(names,Label.CONTENT_XHTML);
        /* Add all the created components to the form */
        //addComponent(holder);
    }

    public NewsItemView(final NewsItemDisplayer app, NewsItem item) {
        this.app = app;
        String text = newsArticle(item);
        article = new Label(text,Label.CONTENT_XHTML);

        /* Add all the created components to the form */
        addComponent(article);

        //TODO generate view if itemID is newsitem
    }
    
    public String newsArticle(NewsItem item)
    {
    	String article = "<i>"+item.getArticle()+"</i>";
    	String topic = "<b>"+item.getTopic()+"</b>";
    	return topic+"<br>"+article+"<br><br>";
    }
}
