package net.progsign.eclipse.plugin.brainfuck;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The plug-in activator (main class)
 * @author Jacek Elczyk
 *
 */
public class BFPluginActivator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String BRAINFUCK_PLUGIN_ID = "net.progsign.brainfuck"; //$NON-NLS-1$

	// The shared instance
	private static BFPluginActivator plugin;
	
	/**
	 * The constructor
	 */
	public BFPluginActivator() {
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	/**
	 * Returns runtime class path for this plug-in.
	 * @return class path as string
	 */
	public String getPluginClassPath() {
		String classPath = null;
		try {
			Bundle bundle = Platform.getBundle(BFPluginActivator.BRAINFUCK_PLUGIN_ID);
			classPath = FileLocator.toFileURL(bundle.getEntry("/")).toString().substring("file:/".length());
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return classPath;
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static BFPluginActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file from the given plug-in relative path.
	 * Used for loading plug-in bundled images.
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(BRAINFUCK_PLUGIN_ID, path);
	}
	
	/**
	 * Log info message
	 * @param message
	 */
	public static void logInfo(String message) {
		BFPluginActivator.getDefault().getLog().log(createStatus(IStatus.INFO, IStatus.OK, message, null));
	}

	/**
	 * Log error message
	 * @param message
	 */
	public static void logError(String message) {
		BFPluginActivator.getDefault().getLog().log(createStatus(IStatus.ERROR, IStatus.OK, message, null));
	}
	
	/**
	 * Log error message with details about the thrown exception
	 * @param message
	 * @param exception thrown exception
	 */
	public static void logException(String message, Throwable exception) {
		BFPluginActivator.getDefault().getLog().log(createStatus(IStatus.ERROR, IStatus.OK, message, exception));
	}
	
	/**
	 * Helper method to create instance of IStatus interface for logger methods
	 * @param severity
	 * @param code
	 * @param message
	 * @param exception
	 * @see Status#Status(int, String, int, String, Throwable)
	 */
	private static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, BFPluginActivator.BRAINFUCK_PLUGIN_ID, code, message, exception);
	}
}
