package vaadin.main.window;

import java.util.ArrayList;

import vaadin.data.PersonContainer;
import vaadin.data.SearchFilter;
import vaadin.ui.NavigationTree;
import vaadin.ui.NewsItemView;
import vaadin.ui.Startpage;

import aalto.media.newsml.NewsItem;
import aalto.media.newsml.PackageItem;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
public class NewsItemDisplayer extends Application implements ClickListener,
		ValueChangeListener, ItemClickListener {

	private NavigationTree tree = new NavigationTree(this);

	// private Button newContact = new Button("Add contact");
	// private Button search = new Button("Search");
	// private Button share = new Button("Share");
	// private Button help = new Button("Help");
	private HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel();

	// Lazyly created ui references
	// private ListView listView = null;
	private NewsItemView searchView = null;
	private Startpage start = null;
	// private PersonList personList = null;
	// private PersonForm personForm = null;
	// private HelpWindow helpWindow = null;
	// private SharingOptions sharingOptions = null;

	private PersonContainer dataSource = PersonContainer.createWithTestData();

	@Override
	public void init() {
		buildMainLayout();
		setMainComponent(getStartpage());
	}

	private void buildMainLayout() {
		setMainWindow(new Window("Awesome Media articles"));

		setTheme("runo");

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		layout.addComponent(createToolbar());
		layout.addComponent(horizontalSplit);
		layout.setExpandRatio(horizontalSplit, 1);

		horizontalSplit.setSplitPosition(200, Sizeable.UNITS_PIXELS);
		horizontalSplit.setFirstComponent(tree);

		getMainWindow().setContent(layout);
	}

	//
	private HorizontalLayout createToolbar() {
		HorizontalLayout lo = new HorizontalLayout();
		lo.setMargin(true);
		lo.setSpacing(true);
		lo.setStyleName("toolbar");
		lo.setWidth("100%");
		Label logo = new Label("LOGO");
		lo.addComponent(logo);
		lo.setComponentAlignment(logo, Alignment.MIDDLE_RIGHT);
		lo.setExpandRatio(logo, 1);
		return lo;
	}

	private void setMainComponent(Component c) {
		horizontalSplit.setSecondComponent(c);
	}

	/*
	 * View getters exist so we can lazily generate the views, resulting in
	 * faster application startup time.
	 */
	// private ListView getListView() {
	// if (listView == null) {
	// personList = new PersonList(this);
	// personForm = new PersonForm(this);
	// listView = new ListView(personList, personForm);
	// }
	// return listView;
	// }

	private NewsItemView getSearchView(PackageItem item) {
		searchView = new NewsItemView(this, item);
		return searchView;
	}


	private NewsItemView getNewsView(NewsItem item) {
		
		searchView = new NewsItemView(this, item);
		return searchView;
	}

	
	private Startpage getStartpage() {
		if (start == null) {
			start = new Startpage(this);
		}
		return start;
	}

	// private HelpWindow getHelpWindow() {
	// if (helpWindow == null) {
	// helpWindow = new HelpWindow();
	// }
	// return helpWindow;
	// }
	//
	// private SharingOptions getSharingOptions() {
	// if (sharingOptions == null) {
	// sharingOptions = new SharingOptions();
	// }
	// return sharingOptions;
	// }

	public PersonContainer getDataSource() {
		return dataSource;
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();

	}

	// private void showHelpWindow() {
	// getMainWindow().addWindow(getHelpWindow());
	// }

	// private void showShareWindow() {
	// getMainWindow().addWindow(getSharingOptions());
	// }

	// private void showListView() {
	// setMainComponent(getListView());
	// }

	private void showSearchView(PackageItem item) {
		setMainComponent(getSearchView(item));
	}
	
	private void showNewsView(NewsItem item) {
		setMainComponent(getNewsView(item));
	}

	public void valueChange(ValueChangeEvent event) {

	}

	public void itemClick(ItemClickEvent event) {
		if (event.getSource() == tree) {
			Object itemId = event.getItemId();
			if (itemId != null) {
				if (itemId instanceof PackageItem) {
					showSearchView(((PackageItem) itemId));
				}
				else if (itemId instanceof NewsItem) {
					showNewsView(((NewsItem) itemId));
				}
			}
		}
	}

	//
	// private void addNewContanct() {
	// showListView();
	// personForm.addContact();
	// }

	public void search(SearchFilter searchFilter) {
		// clear previous filters
		getDataSource().removeAllContainerFilters();
		// filter contacts with given filter
		getDataSource().addContainerFilter(searchFilter.getPropertyId(),
				searchFilter.getTerm(), true, false);
		// showSearchView();

		getMainWindow().showNotification(
				"Searched for " + searchFilter.getPropertyId() + "=*"
						+ searchFilter.getTerm() + "*, found "
						+ getDataSource().size() + " item(s).",
				Notification.TYPE_TRAY_NOTIFICATION);
	}
}
