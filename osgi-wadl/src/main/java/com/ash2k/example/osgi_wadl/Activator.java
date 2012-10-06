package com.ash2k.example.osgi_wadl;

import java.util.Dictionary;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.logging.Handler;
import java.util.logging.LogManager;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.osgi.boot.OSGiWebappConstants;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;

/**
 * @author Mikhail Mazursky
 * @since 2012-09-29
 */
public class Activator implements BundleActivator {

	private static final String MODULE = "com.ash2k.example.osgi_wadl_resource.WadlModule";
	private static final Logger log = LoggerFactory.getLogger(Activator.class);

	public void start(BundleContext context) throws Exception {
		initLoggerBridge();
		log.info("Starting bundle");
		Module wadlModule = null;
		for (Bundle bundle : context.getBundles()) {
			if (!bundle.getSymbolicName().startsWith("com.ash2k.")) {
				continue;
			}
			Class<?> moduleClass = null;
			try {
				moduleClass = bundle.loadClass(MODULE);
			} catch (final ClassNotFoundException e) {
				log.trace("Module {} not found in bundle {}", MODULE, bundle);
				continue;
			}
			log.info("Module {} found in bundle {}", MODULE, bundle);
			wadlModule = moduleClass.asSubclass(Module.class).newInstance();
			break;
		}
		if (wadlModule == null) {
			throw new RuntimeException("Module " + MODULE + " not found");
		}
		final Injector injector = Guice.createInjector(wadlModule);
		final WebAppContext contextHandler = new WebAppContext();
		contextHandler.addFilter(
				new FilterHolder(injector.getInstance(GuiceFilter.class)),
				"/*", EnumSet.allOf(DispatcherType.class));
		contextHandler.addServlet(EmptyServlet.class, "/");

		log.info("Registering service");

		final Dictionary<String, Object> dic = new Hashtable<String, Object>();
		dic.put(OSGiWebappConstants.JETTY_WAR_FOLDER_PATH, "WEB-INF");
		dic.put(OSGiWebappConstants.RFC66_WEB_CONTEXTPATH, "/");
		context.registerService(ContextHandler.class, contextHandler, dic);

		log.info("Service registered, bundle started");
	}

	public void stop(BundleContext context) throws Exception {
		log.info("Bundle stopped");
	}

	/**
	 * Jersey uses java.util.logging - bridge to slf4j
	 */
	private static void initLoggerBridge() {
		final java.util.logging.Logger rootLogger = LogManager.getLogManager()
				.getLogger("");
		for (final Handler handler : rootLogger.getHandlers()) {
			rootLogger.removeHandler(handler);
		}
		SLF4JBridgeHandler.install();
		java.util.logging.Logger.getLogger(Activator.class.getName()).info("JUL bridge installed");
	}

	@SuppressWarnings("serial")
	private static class EmptyServlet extends HttpServlet {

	}
}
