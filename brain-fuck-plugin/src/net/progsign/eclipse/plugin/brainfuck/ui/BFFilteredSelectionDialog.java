package net.progsign.eclipse.plugin.brainfuck.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.progsign.eclipse.plugin.brainfuck.BFPluginActivator;
import net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginMessages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.ide.IDE.SharedImages;

public class BFFilteredSelectionDialog extends FilteredItemsSelectionDialog {
	/** This dialog settings ID */
	private static final String DIALOG_SETTINGS = "BFFilteredSelectionDialogSettings";

	/** List of files found in the root project */
	private List<IFile> resources;
	
	/**
	 * The constructor
	 * @param shell parent container
	 * @param project initial (root) project
	 */
	public BFFilteredSelectionDialog(Shell shell, IProject project) {
		super(shell, false);
		if(project == null) {
			throw new NullPointerException("project must not be null");
		}
		this.resources = new ArrayList<IFile>();

		setTitle(BFPluginMessages.UI_FILTERED_SELECTION_DIALOG_title);
		setSelectionHistory(new ResourceSelectionHistory());
		setListLabelProvider(new JavaElementLabelProvider());
		setDetailsLabelProvider(new StaticTextLabelProvider(project.getName()));
		setInitialPattern("**");
		
		loadResources(project);
	}
	
	/**
	 * Initializes the list. Traverses through the entire container looking for files
	 * @param root root container
	 */
	private void loadResources(IContainer root) {
		try {
			IResource[] members = root.members(IResource.NONE|IResource.DEPTH_INFINITE);
			for(IResource member : members) {
				if(member instanceof IFile) {
					resources.add((IFile) member);
				} else
				if(member instanceof IContainer){
					loadResources((IContainer)member);
				}
			}
		} catch(CoreException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ItemsFilter() {

			@Override
			public boolean matchItem(Object item) {
				return matches(((IFile)item).getName());
			}

			@Override
			public boolean isConsistentItem(Object item) {
				return true;
			}
		};
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider,
			ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
					throws CoreException {
		progressMonitor.beginTask(BFPluginMessages.UI_FILTERED_SELECTION_DIALOG_search, resources.size());
		for (Iterator<?> iter = resources.iterator(); iter.hasNext();) {
			contentProvider.add(iter.next(), itemsFilter);
			progressMonitor.worked(1);
		}
		progressMonitor.done();
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = BFPluginActivator.getDefault()
				.getDialogSettings().getSection(DIALOG_SETTINGS);
		if (settings == null) {
			settings = BFPluginActivator.getDefault().getDialogSettings()
					.addNewSection(DIALOG_SETTINGS);
		}
		return settings;
	}

	@Override
	public String getElementName(Object item) {
		return ((IFile)item).getName();
	}

	@Override
	protected Comparator<IFile> getItemsComparator() {
		return new Comparator<IFile>() {
			public int compare(IFile e0, IFile e1) {
				return e0.getName().compareTo(e1.getName());
			}
		};
	}

	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	/**
	 * Support class for providing labels for the selection dialog window
	 * @author Jacek Elczyk
	 */
	protected class StaticTextLabelProvider extends LabelProvider {
		private Image icon = PlatformUI.getWorkbench().getSharedImages().getImage(SharedImages.IMG_OBJ_PROJECT);
		private String text;
		
		/**
		 * The constructor
		 * @param text the label will be prefixed with this text followed by "://"
		 */
		protected StaticTextLabelProvider(String text) {
			this.text = text;
		}

		@Override
		public Image getImage(Object element) {
			return icon;
		}

		@Override
		public String getText(Object element) {
			return text + "://" + ((IFile)element).getProjectRelativePath();
		}
	}
	
	/**
	 * Support class for storing dialog window history
	 * @author Jacek Elczyk
	 */
	private class ResourceSelectionHistory extends SelectionHistory {
		@Override
		protected Object restoreItemFromMemento(IMemento element) {
			return null;
		}

		@Override
		protected void storeItemToMemento(Object item, IMemento element) {
		}
	}
}
