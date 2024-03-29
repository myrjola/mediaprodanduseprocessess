package vaadin.ui;

import java.util.ArrayList;

import vaadin.main.window.NewsItemDisplayer;

import aalto.media.newsml.NewsItem;
import aalto.media.newsml.PackageGenerator;
import aalto.media.newsml.PackageItem;

import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
public class NavigationTree extends Tree {
	public static PackageGenerator pg;

	public NavigationTree(NewsItemDisplayer app) {
		pg = new PackageGenerator("webapps/Newstopia/WEB-INF/public/pictureitems");
		for (PackageItem pi : pg.generatePackages()) {

			addItem(pi);
			ArrayList<NewsItem> newsitems = pi.newsItems;

			/* Add newsitems as root items in the tree. */
			for (NewsItem ni : newsitems) {

				// Add the item as a regular item.
				addItem(ni);
				// Set it to be a child.
				setParent(ni, pi);
				// Make the moons look like leaves.
				setChildrenAllowed(ni, false);
			}
			// Expand the subtree.
			expandItemsRecursively(pi);

		}
		setSelectable(true);
		setNullSelectionAllowed(false);
		
		// Make application handle item click events
		addListener((ItemClickListener) app);

	}
}
