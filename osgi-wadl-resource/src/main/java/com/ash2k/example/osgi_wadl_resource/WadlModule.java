package com.ash2k.example.osgi_wadl_resource;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.google.inject.PrivateModule;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * @author Mikhail Mazursky
 * @since 2012-09-29
 */
public class WadlModule extends PrivateModule {

	@Override
	protected void configure() {
		install(new JerseyServletModule() {
			@Override
			protected void configureServlets() {
				super.configureServlets();
				binder().requireExplicitBindings();
				bind(GuiceContainer.class);
				bind(GuiceFilter.class).in(Singleton.class);
				Map<String, String> params = new HashMap<String, String>();
				params.put(ResourceConfig.PROPERTY_WADL_GENERATOR_CONFIG,
						OsgiWadlGeneratorConfig.class.getCanonicalName());
				serve("*").with(GuiceContainer.class, params);

				bind(SomeResource.class);
			}
		});
		expose(GuiceFilter.class);
	}
}
