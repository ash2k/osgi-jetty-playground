package com.ash2k.example.osgi_wadl_resource;

import java.util.List;

import com.sun.jersey.api.wadl.config.WadlGeneratorConfig;
import com.sun.jersey.api.wadl.config.WadlGeneratorDescription;
import com.sun.jersey.server.wadl.generators.resourcedoc.WadlGeneratorResourceDocSupport;

/**
 * @author Mikhail Mazursky
 * @since 2012-09-29
 */
public class OsgiWadlGeneratorConfig extends WadlGeneratorConfig {

	public static final String	RESOURCEDOC_FILE	= "resourcedoc.xml";

	@Override
	public List<WadlGeneratorDescription> configure() {
		return generator(WadlGeneratorResourceDocSupport.class).prop("resourceDocStream", RESOURCEDOC_FILE).descriptions();
	}
}
