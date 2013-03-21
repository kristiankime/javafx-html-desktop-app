package com.artclod.deskweb.support;

import javafx.event.EventHandler;
import javafx.scene.web.WebEvent;

public class AlertToSystemErr implements EventHandler<WebEvent<String>> {
	@Override
	public void handle(WebEvent<String> event) {
		System.err.println(event.getData());
	}
}