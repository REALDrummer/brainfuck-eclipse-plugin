package net.progsign.eclipse.plugin.brainfuck.ui;

import java.util.ArrayList;
import java.util.List;

import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginMessages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * Project selection dialog window
 * @author Jacek Elczyk
 */
public class BFProjectSelectionDialog extends ElementListSelectionDialog {
	public BFProjectSelectionDialog(Shell shell) {
		super(shell, new JavaElementLabelProvider());
		
		setTitle(BFPluginMessages.UI_PROJECT_SELECTION_DIALOG_title);
		setMessage(BFPluginMessages.UI_PROJECT_SELECTION_DIALOG_message);
	
		loadResources();
	}
	
	/**
	 * Initializes the list
	 * @see ElementListSelectionDialog#setElements(Object[])
	 */
	private void loadResources() {
		setElements(getAccesibleProjects());
	}
	
	/**
	 * Creates and returns an array of availabe projects in current workspace
	 * @return projects in current workspace as an array of IProject
	 */
	public static IProject[] getAccesibleProjects() {
		List<IProject>projects = new ArrayList<IProject>();
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject project : allProjects) {
			if(project.isAccessible()) {
				projects.add(project);
			}
		}
		return projects.toArray(new IProject[projects.size()]);
	}
}
