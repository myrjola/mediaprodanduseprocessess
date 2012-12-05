package com.vaadin.demo.tutorial.addressbook.ui;

import com.vaadin.demo.tutorial.addressbook.AddressBookApplication;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SearchView extends Panel {

    private Label article;
    private AddressBookApplication app;

    public SearchView(final AddressBookApplication app) {
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
