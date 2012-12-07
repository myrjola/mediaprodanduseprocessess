package vaadin.ui;

import vaadin.main.window.NewsItemDisplayer;

import aalto.media.newsml.PackageGenerator;
import aalto.media.newsml.PackageItem;

import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {
	public static PackageGenerator pg;
	public static Object PACKAGE1;
    public static final Object PACKAGE2 = "Package Item 2";

    public NavigationTree(NewsItemDisplayer app) {
    	pg = new PackageGenerator("/public/pictureitems");
    	PACKAGE1 = pg.generatePackage();
        addItem(PACKAGE1);
        addItem(PACKAGE2);

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
