package src.com.example.mysteriousostrich;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class MysteriousostrichApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Mysteriousostrich Application");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
