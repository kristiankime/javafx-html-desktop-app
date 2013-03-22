package com.artclod.deskweb;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import org.eclipse.jetty.server.Server;

import com.artclod.deskweb.support.AlertToSystemErr;
import com.artclod.deskweb.support.JavaFXWebApplicationUtil;

public class DesktopWebApplication extends Application {
	private static final String HTTP_LOCALHOST = "http://localhost:";
	private static final int WEB_PORT = 8765;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// This starts up a Jetty web server that will serve up resources loaded via the specified class at the specified port on localhost
		final Server server = JavaFXWebApplicationUtil.setupResourceServer(WEB_PORT, DesktopWebApplication.class);

		WebView webView = new WebView();
		final WebEngine webEngine = webView.getEngine();

		// The WebEngine needs to have behavior set for the alert javascript call
		webEngine.setOnAlert(new AlertToSystemErr());

		// This call will load a resource html page as the starting page
		webEngine.load(HTTP_LOCALHOST + WEB_PORT + "/com/artclod/platformer/platformer.html");

		// JavaFX can make Java objects accessible to javascript but it must be done after the page is loaded
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					JSObject window = (JSObject) webEngine.executeScript("window");
					window.setMember("app", new JavaFXWebApplicationUtil.ShudownManager(server));
				}
			}
		});

		Scene scene = new Scene(webView);
		stage.setScene(scene);
		stage.setTitle("Desktop Web Application");
		stage.show();
	}

}