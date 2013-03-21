package com.artclod.deskweb.support;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

public class JarResourceHandler extends ResourceHandler {
	private final Class<?> resourceClass;

	public JarResourceHandler(Class<?> resourceClass) {
		if (resourceClass == null)
			throw new NullPointerException("resourceClass was null");
		this.resourceClass = resourceClass;
	}

	public Resource getResource(String path) throws MalformedURLException {
		if (path == null || !path.startsWith("/"))
			throw new MalformedURLException(path);

		URL resource = resourceClass.getResource(path);

		try {
			return Resource.newResource(resource);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}