package net.progsign.eclipse.plugin.brainfuck;

import net.progsign.eclipse.plugin.brainfuck.ui.BFMainTab;


import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * Launch configuration tab group (UI contribution class)
 * @author Jacek Elczyk
 */
public class BFTabGroup extends AbstractLaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
			new BFMainTab()
		};
		setTabs(tabs);
	}
}
