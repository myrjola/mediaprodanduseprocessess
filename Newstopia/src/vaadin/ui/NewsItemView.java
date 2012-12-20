package vaadin.ui;

import java.io.File;
import java.util.ArrayList;

import vaadin.main.window.NewsItemDisplayer;

import aalto.media.newsml.NewsItem;
import aalto.media.newsml.PackageItem;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class NewsItemView extends Panel {

    private Label article;
    private Panel holder;
    private NewsItemDisplayer app;
    private ArrayList list;

    public NewsItemView(final NewsItemDisplayer app, PackageItem item) {
        this.app = app;
        this.list = item.getNewsItems();
        addStyleName("view");
        
        setCaption("<b>"+item.getHeadline()+"</b>");
        setSizeFull();

        /* Use a VerticalLayout as main layout for this Panel */
        VerticalLayout Layout = new VerticalLayout();
        setContent(Layout);
        for(int i = 0; i<list.size();i++) {
        	holder = new NewsItemView(this.app,(NewsItem)(list.get(i)));
        	addComponent(holder);
        }	
    }

    public NewsItemView(final NewsItemDisplayer app, NewsItem item) {
        this.app = app;
        String path = "webapps/Newstopia/WEB-INF/public"+item.getImagePath().substring(6);
        Embedded img = new Embedded(null,new FileResource(new File(path),app));
        addComponent(img);
        String text = newsArticle(item);
        article = new Label(text,Label.CONTENT_XHTML);
        setCaption(item.getTopic());
        addComponent(article);
    }
    
    public String newsArticle(NewsItem item)
    {
    	String article = "<i>"+item.getArticle()+"</i>";
    	return article+"<br><br>";
    }
}
