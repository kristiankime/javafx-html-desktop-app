package com.artclod.deskweb.support;

import javafx.application.Platform;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

public class JavaFXWebApplicationUtil {

	public static class ShudownManager {
		private final Server server;

		public ShudownManager(Server server) {
			this.server = server;
		}

		public void exit() throws Exception {
			server.stop();
			Platform.exit();
		}
	}

	public static Server setupResourceServer(int webPort, Class<?> resourceClass) throws Exception {
		Server server = new Server(webPort);
		SelectChannelConnector connector0 = new SelectChannelConnector();
		connector0.setPort(webPort);
		connector0.setMaxIdleTime(30000);
		connector0.setRequestHeaderSize(8192);
		server.setConnectors(new Connector[] { connector0 });
		server.setHandler(new JarResourceHandler(resourceClass));
		server.start();
		return server;
	}

}