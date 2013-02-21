package net.progsign.eclipse.plugin.brainfuck.ui;

import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginConstants;
import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginMessages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.pde.ui.launcher.AbstractLauncherTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Run parameters settings group in BFMainTab
 * @author Jacek Elczyk
 * @see BFMainTab
 */
public class BFParamsGroup {
	private AbstractLauncherTab bfTab;
	private Text stackSizeText;
	private Text inputPathText;
	private Button inputSystemBtn;
	private Button inputFileBtn;
	private Button inputBrowseBtn;
	
	public BFParamsGroup(AbstractLauncherTab tab) {
		this.bfTab = tab;
	}
	
	/**
	 * @see BFMainTab#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		GridData gridLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(gridLayoutData);
		group.setLayout(new GridLayout(2, false));
		group.setText(BFPluginMessages.UI_PARAMS_GROUP_title);
		
		Label label = new Label(group, SWT.NONE);
		label.setText(BFPluginMessages.UI_PARAMS_GROUP_stack_size_lbl);
		stackSizeText = new Text(group, SWT.BORDER);
		gridLayoutData = new GridData(100, SWT.DEFAULT);
		stackSizeText.setLayoutData(gridLayoutData);
		stackSizeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				bfTab.updateLaunchConfigurationDialog();
			}
		});
		stackSizeText.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent event) {
				for(Character ch : event.text.toCharArray()) {
					if(!Character.isDigit(ch)) {
						event.doit = false;
						return;
					}
				}
			}
		});
		
		gridLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		gridLayoutData.horizontalSpan = 3;
		Group inputGrp = new Group(group, SWT.NONE);
		inputGrp.setLayoutData(gridLayoutData);
		inputGrp.setLayout(new GridLayout(3, false));
		inputGrp.setText(BFPluginMessages.UI_PARAMS_GROUP_input_title);
		inputSystemBtn = new Button(inputGrp, SWT.RADIO);
		inputSystemBtn.setText(BFPluginMessages.UI_PARAMS_GROUP_input_system_btn);
		inputSystemBtn.setLayoutData(gridLayoutData);
		inputSystemBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(inputSystemBtn.getSelection()) {
					inputMethodChanged();
					bfTab.updateLaunchConfigurationDialog();
				}
			}
		});
		inputFileBtn = new Button(inputGrp, SWT.RADIO);
		inputFileBtn.setText(BFPluginMessages.UI_PARAMS_GROUP_input_file_btn);
		inputFileBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(inputFileBtn.getSelection()) {
					inputMethodChanged();
				}
			}
		});

		inputPathText = new Text(inputGrp, SWT.BORDER);
		inputPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inputPathText.setEnabled(false);
		inputBrowseBtn = new Button(inputGrp, SWT.PUSH);
		inputBrowseBtn.setText(BFPluginMessages.UI_SOURCE_GROUP_browse_btn);
		inputBrowseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
				String path;
				if((path = dialog.open()) != null) {
					inputPathText.setText(path);
					bfTab.updateLaunchConfigurationDialog();
				}
			}
		});
		inputBrowseBtn.setEnabled(false);
	}
	
	/**
	 * @see BFMainTab#initializeFrom(ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration config) throws CoreException {
		stackSizeText.setText("" + config.getAttribute(BFPluginConstants.ATTR_STACK_SIZE, BFPluginConstants.DEFAULT_STACK_SIZE));
		String inputPath = config.getAttribute(BFPluginConstants.ATTR_INPUT_FILE, "");
		if(!inputPath.equals("")) {
			inputPathText.setText(inputPath);
			inputFileBtn.setSelection(true);
			inputMethodChanged();
		} else {
			inputSystemBtn.setSelection(true);
		}
	}
	
	/**
	 * @see BFMainTab#performApply(ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(BFPluginConstants.ATTR_STACK_SIZE, Integer.parseInt(stackSizeText.getText()));
		if(inputFileBtn.getSelection()) {
			config.setAttribute(BFPluginConstants.ATTR_INPUT_FILE, inputPathText.getText());
		} else {
			config.setAttribute(BFPluginConstants.ATTR_INPUT_FILE, (String)null);
		}
	}
	
	/**
	 * @see BFMainTab#setDefaults(ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(BFPluginConstants.ATTR_STACK_SIZE, BFPluginConstants.DEFAULT_STACK_SIZE);
		config.setAttribute(BFPluginConstants.ATTR_INPUT_FILE, (String)null);
	}
	
	/**
	 * Callback method called when user changes the input source (radio button) 
	 */
	private void inputMethodChanged() {
		updateInputGroupState(inputFileBtn.getSelection());
	}
	
	/**
	 * Enables or disables the controls from "input method" group
	 * @param enabled
	 */
	private void updateInputGroupState(boolean enabled) {
		inputPathText.setEnabled(enabled);
		inputBrowseBtn.setEnabled(enabled);
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
