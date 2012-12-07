package vaadin.ui;

import vaadin.main.window.NewsItemDisplayer;

import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {
    public static final Object SHOW_ALL = "Package Item 1";
    public static final Object SEARCH = "Package Item 2";

    public NavigationTree(NewsItemDisplayer app) {
        addItem(SHOW_ALL);
        addItem(SEARCH);

        /*
         * We want items to be selectable but do not want the user to be able to
         * de-select an item.
         */
        setSelectable(true);
        setNullSelectionAllowed(false);

        // Make application handle item click events
        addListener((ItemClickListener) app);

    }
}
