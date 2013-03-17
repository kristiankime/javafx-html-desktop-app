package com.artclod.deskweb;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import com.sun.webpane.webkit.JSObject;

public class DesktopWebApplication extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		WebView webView = new WebView();
		final WebEngine webEngine = webView.getEngine();
		
		// The WebEngine needs to have behavior set for the alert javascript call
		webEngine.setOnAlert(new AlertToSystemErr());
		
		// This call will load a resource html page as the starting "page"
		webEngine.load(DesktopWebApplication.class.getResource("/com/artclod/deskweb/main.html").toExternalForm());

		// JavaFX can make Java objects accessible to javascript but it must be done after the page is loaded  
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					JSObject window = (JSObject) webEngine.executeScript("window");
					window.setMember("app", new JavaApplication());
				}
			}
		});

		Scene scene = new Scene(webView);
		stage.setScene(scene);
		stage.setTitle("Desktop Web Application");
		stage.show();
	}

	public static class AlertToSystemErr implements EventHandler<WebEvent<String>> {
		@Override
		public void handle(WebEvent<String> event) {
			System.err.println(event.getData());
		}
	}

	public static class JavaApplication {
		public void exit() {
			Platform.exit();
		}
	}
}