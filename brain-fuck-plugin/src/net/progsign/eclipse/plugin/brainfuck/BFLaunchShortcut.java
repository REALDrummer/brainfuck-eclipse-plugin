package net.progsign.eclipse.plugin.brainfuck;

import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginConstants;

import org.eclipse.core.resources.IFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

/**
 * Launch configuration shortcut (UI contribution class)
 * @author Jacek Elczyk
 *
 */
public class BFLaunchShortcut implements ILaunchShortcut {
	public static final String BRAINFUCK_CONFIG_TYPE_ID = "net.progsign.eclipse.plugin.brainfuck.bfLaunchConfiguration";

	@Override
	public void launch(ISelection selection, String mode) {
		if(selection instanceof IStructuredSelection && ((IStructuredSelection) selection).getFirstElement() instanceof IFile) {
			IFile file = (IFile)((IStructuredSelection) selection).getFirstElement();
			prepareAndLaunch(file, mode);
		} else {
			BFPluginActivator.logError("Launch shortcut invoked for invalid resource");
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		editor.getSite().getPage().saveEditor(editor, false);
		IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);
		if(file != null) {
			prepareAndLaunch(file, mode);
		} else {
			BFPluginActivator.logError("Launch shortcut invoked for invalid resource");
		}
	}
	
	/**
	 * A convenience method for returning Brainfuck launch configuration
	 * @return launch configuration associated to this plug-in
	 */
	public ILaunchConfigurationType getBrainfuckLaunchConfigType() {
		return DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType(BRAINFUCK_CONFIG_TYPE_ID);
	}
	
	/**
	 * Prepares and launches a new launch configuration
	 * @param input source code file
	 * @param mode launch mode
	 */
	private void prepareAndLaunch(IFile file, String mode) {
		IProject project = file.getProject();
		if(project != null) {
			try {
				String configName = DebugPlugin.getDefault().getLaunchManager().generateLaunchConfigurationName(project.getName());
				ILaunchConfigurationWorkingCopy config = getBrainfuckLaunchConfigType().newInstance(null, configName);
				config.setAttribute(BFPluginConstants.ATTR_PROJECT_NAME, file.getProject().getName());
				config.setAttribute(BFPluginConstants.ATTR_SOURCE_FILE, file.getProjectRelativePath().toString());
				config.setAttribute(BFPluginConstants.ATTR_SOURCE_ABSOLUTE_PATH, file.getRawLocation().toString());

				DebugUITools.launch(config, mode);
			} catch(CoreException ce) {
				BFPluginActivator.logException("Launching file " + file.getName() + " failed", ce);
			}
		}
	}
}
