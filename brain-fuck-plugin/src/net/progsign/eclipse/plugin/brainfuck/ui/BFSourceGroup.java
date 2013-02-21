package net.progsign.eclipse.plugin.brainfuck.ui;

import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginConstants;
import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginMessages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.window.Window;
import org.eclipse.pde.ui.launcher.AbstractLauncherTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Source setting group in BFMainTab
 * @author Jacek Elczyk
 * @see BFMainTab
 */
public class BFSourceGroup {
	private AbstractLauncherTab bfTab;
	private String sourcePath;
	private Text projectNameText;
	private Text sourceFileText;
	private Button sourceFileBtn;
	
	public BFSourceGroup(AbstractLauncherTab tab) {
		this.bfTab = tab;
	}

	/**
	 * @see BFMainTab#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		GridData gridLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(gridLayoutData);
		group.setLayout(new GridLayout(3, false));
		group.setText(BFPluginMessages.UI_SOURCE_GROUP_title);
		
		// Project select controls
		Label label = new Label(group, SWT.NONE);
		label.setText(BFPluginMessages.UI_SOURCE_GROUP_project_lbl);
		projectNameText = new Text(group, SWT.BORDER);
		projectNameText.setLayoutData(gridLayoutData);
		projectNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String text = projectNameText.getText();
				if(text.length() != 0) {
					projectSelectionChanged();
					bfTab.updateLaunchConfigurationDialog();
				}
			}
		});
		Button button = new Button(group, SWT.PUSH);
		button.setText(BFPluginMessages.UI_SOURCE_GROUP_browse_btn);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BFProjectSelectionDialog dialog = new BFProjectSelectionDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell());

				if(dialog.open() == Window.OK) {
					IProject p = (IProject)dialog.getFirstResult();
					projectNameText.setText(p.getName());
				}
			}
		});
		
		// Source select controls
		label = new Label(group, SWT.NONE);
		label.setText(BFPluginMessages.UI_SOURCE_GROUP_source_lbl);
		sourceFileText = new Text(group, SWT.BORDER);
		sourceFileText.setLayoutData(gridLayoutData);
		sourceFileText.setEditable(false);
		sourceFileText.setEnabled(false);
		sourceFileBtn = new Button(group, SWT.PUSH);
		sourceFileBtn.setText(BFPluginMessages.UI_SOURCE_GROUP_browse_btn);
		sourceFileBtn.setEnabled(false);
		sourceFileBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BFFilteredSelectionDialog dialog = new BFFilteredSelectionDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						ResourcesPlugin.getWorkspace().getRoot().getProject(projectNameText.getText()));
				if(dialog.open() == Window.OK) {
					IFile file = (IFile)dialog.getFirstResult();
					sourcePath = file.getRawLocation().toString();
					sourceFileText.setText(file.getProjectRelativePath().toString());
					bfTab.updateLaunchConfigurationDialog();
				}
			}
		});
	}
	
	/**
	 * @see BFMainTab#initializeFrom(ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration config) throws CoreException {
		projectNameText.setText(config.getAttribute(BFPluginConstants.ATTR_PROJECT_NAME, ""));
		sourceFileText.setText(config.getAttribute(BFPluginConstants.ATTR_SOURCE_FILE, ""));
		sourcePath = config.getAttribute(BFPluginConstants.ATTR_SOURCE_ABSOLUTE_PATH, (String)null);
	}
	
	/**
	 * @see BFMainTab#performApply(ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(BFPluginConstants.ATTR_PROJECT_NAME, projectNameText.getText());
		config.setAttribute(BFPluginConstants.ATTR_SOURCE_FILE, sourceFileText.getText());
		config.setAttribute(BFPluginConstants.ATTR_SOURCE_ABSOLUTE_PATH, sourcePath);
	}
	
	/**
	 * @see BFMainTab#setDefaults(ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(BFPluginConstants.ATTR_PROJECT_NAME, (String)null);
		config.setAttribute(BFPluginConstants.ATTR_SOURCE_FILE, (String)null);
		config.setAttribute(BFPluginConstants.ATTR_SOURCE_ABSOLUTE_PATH, (String)null);
	}
	
	/**
	 * Callback method invoked every time project selection is updated
	 */
	private void projectSelectionChanged() {
		boolean valid = isValidProjectName(projectNameText.getText());
		sourcePath = "";
		sourceFileText.setText("");
		sourceFileText.setEnabled(valid);
		sourceFileBtn.setEnabled(valid);
	}
	
	/**
	 * Project name validation
	 * @param name project name
	 * @return true if project with the given name exists in current workspace, false otherwise
	 */
	protected boolean isValidProjectName(String name) {
		IProject[] projects = BFProjectSelectionDialog.getAccesibleProjects();
		for(IProject project : projects) {
			if(project.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Not used
	 * @return null
	 * @see BFMainTab#validateTab()
	 */
	public String validate() {
		return null;
	}
}
