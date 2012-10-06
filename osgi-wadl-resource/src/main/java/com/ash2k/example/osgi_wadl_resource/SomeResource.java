package com.ash2k.example.osgi_wadl_resource;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Hello resource.
 * 
 * @author Mikhail Mazursky
 * @since 2012-09-29
 */
@Path("res")
@Singleton
public class SomeResource {

	/**
	 * Hello, world!
	 * 
	 * @return Greeting to the world
	 */
	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "hello";
	}
}
