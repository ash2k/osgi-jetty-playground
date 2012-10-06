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
				bind(SomeResource.class);

				Map<String, String> params = new HashMap<String, String>();
				params.put(ResourceConfig.PROPERTY_WADL_GENERATOR_CONFIG,
						OsgiWadlGeneratorConfig.class.getCanonicalName());

				// WHEN DEPLOYING TO ROOT

				// generates The resource 'resourcedoc.xml' does not exist.
				serve("/*").with(GuiceContainer.class, params);

				// generates The resource 'resourcedoc.xml' does not exist.
				// serve("*").with(GuiceContainer.class, params);

				// this do not work - WHY?
				// serve("*").with(GuiceContainer.class);

				// this works
				// serve("/*").with(GuiceContainer.class);

				// WHEN DEPLOYING TO NON-ROOT

				// generates The resource 'resourcedoc.xml' does not exist.
				// serve("/*").with(GuiceContainer.class, params);

				// generates The resource 'resourcedoc.xml' does not exist.
				// serve("*").with(GuiceContainer.class, params);

				// this do not work - WHY?
				// serve("*").with(GuiceContainer.class);

				// this works
				// serve("/*").with(GuiceContainer.class);
			}
		});
		expose(GuiceFilter.class);
	}
}
