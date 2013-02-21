package net.progsign.eclipse.plugin.brainfuck;


import java.util.ArrayList;
import java.util.List;

import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginConstants;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * Brainfuck launch configuration (UI contribution class)
 * @author Jacek Elczyk
 *
 */
public class BFLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
	private static final String DEFAULT_RUNNER = "net.progsign.eclipse.plugin.brainfuck.runtime.BFRunner"; 

	@Override
	public void launch(ILaunchConfiguration config, String mode, ILaunch launch,
			IProgressMonitor monitor) throws CoreException {
//		if(monitor == null) {
//			monitor = new NullProgressMonitor();
//		}
//		monitor.beginTask(BFPluginMessages.UI_MONITOR_begin_task, 1);
		IVMInstall defaultVM = JavaRuntime.getDefaultVMInstall();
		if(defaultVM != null) {
			IVMRunner vmRunner = defaultVM.getVMRunner(mode);
			String classPath = BFPluginActivator.getDefault().getPluginClassPath();
			if(classPath != null) {
				// taking focus on the ConsoleView (Eclipse built-in console)
				if(PlatformUI.getWorkbench().getWorkbenchWindowCount() > 0) {
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
						public void run() {
							try {
								IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
								if(window == null) {
									window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
								}
								window.getActivePage().showView(IConsoleConstants.ID_CONSOLE_VIEW);
							} catch(PartInitException pie) {
								pie.printStackTrace();
							}
						}
					});
				}
				VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(BFLaunchConfigurationDelegate.DEFAULT_RUNNER, new String[]{classPath});
				vmConfig.setProgramArguments(createProgramArgumentsFrom(config));
				vmRunner.run(vmConfig, launch, monitor);
			}
		}
//		monitor.done();
	}
	
	/**
	 * Convenience method for converting configuration parameters to array of Strings.
	 * @param config launch configuration
	 * @return array of String parameters
	 */
	public String[] createProgramArgumentsFrom(ILaunchConfiguration config) {
		List<String> args = new ArrayList<String>();
		try {
			String sourcePath = config.getAttribute(BFPluginConstants.ATTR_SOURCE_ABSOLUTE_PATH, (String)null);
			if(sourcePath == null) {
				return null;
			}
			args.add("-f " + sourcePath);
			args.add("-s " + config.getAttribute(BFPluginConstants.ATTR_STACK_SIZE, BFPluginConstants.DEFAULT_STACK_SIZE));
			String input = config.getAttribute(BFPluginConstants.ATTR_INPUT_FILE, (String)null);
			if(input != null) {
				args.add("-i " + input);
			}
		} catch(CoreException ce) {
			ce.printStackTrace();
			return null;
		}
		return args.toArray(new String[args.size()]);
	}
}
