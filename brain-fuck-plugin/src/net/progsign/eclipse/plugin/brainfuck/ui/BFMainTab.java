package net.progsign.eclipse.plugin.brainfuck.ui;

import net.progsign.eclipse.plugin.brainfuck.BFPluginActivator;
import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginConstants;
import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginMessages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.pde.ui.launcher.AbstractLauncherTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Concrete implementation of the {@link AbstractLaunchConfigurationTab} class.
 * Provides the GUI for the BrainFuck launch configuration
 * @author Jacek Elczyk
 */
public class BFMainTab extends AbstractLauncherTab {
	protected BFSourceGroup bfSourceGroup;
	protected BFParamsGroup bfParamsGroup;
	
	public BFMainTab() {
		createSourceGroup();
		createParamsGroup();
	}
	
	
	/**
	 * Creates source selection group
	 */
	private void createSourceGroup() {
		bfSourceGroup = new BFSourceGroup(this);
	}
	
	/**
	 * Creates run parameters group
	 */
	private void createParamsGroup() {
		bfParamsGroup = new BFParamsGroup(this);
	}
	
	@Override
	public Image getImage() {
		return BFPluginActivator.getImageDescriptor(BFPluginConstants.IMG_MAIN_TAB_ICON).createImage();
	}

	@Override
	public String getName() {
		return BFPluginMessages.UI_MAIN_TAB_name;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		
		container.setLayout(new GridLayout(1, true));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		bfSourceGroup.createControl(container);
		bfParamsGroup.createControl(container);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		try {
			bfSourceGroup.initializeFrom(config);
			bfParamsGroup.initializeFrom(config);
		} catch(CoreException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		bfSourceGroup.performApply(config);
		bfParamsGroup.performApply(config);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		bfSourceGroup.setDefaults(config);
		bfParamsGroup.setDefaults(config);
	}

	@Override
	public void validateTab() {
		setMessage(null);
		
		String msg = bfSourceGroup.validate();
		if(msg != null) {
			setErrorMessage(msg);
			return;
		}
		msg = bfParamsGroup.validate();
		setErrorMessage(msg);
	}
}
