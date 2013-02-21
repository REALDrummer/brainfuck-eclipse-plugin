package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IElementStateListener;

/**
 * Custom Brainfuck code editor
 * 
 * @author Jacek Elczyk
 */
public class BFEditor extends TextEditor {
	private BFColorManager colorManager;
	
	public BFEditor() {
		super();
		colorManager = new BFColorManager();
		setSourceViewerConfiguration(new BFConfiguration(colorManager));
		
		IDocumentProvider provider = new BFDocumentProvider();
		provider.addElementStateListener(new AbstractElementStateListener() {
			@Override
			public void elementDirtyStateChanged(Object element, boolean isDirty) {
				if(!isDirty) {
					validateAndMark();
				}
			}
		});
		setDocumentProvider(provider);
	}
	
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	protected void validateAndMark() {
		IFile file = (IFile) getEditorInput().getAdapter(IFile.class);
		if(file != null) {
			BFMarkingValidator.getDefault().validateAndMark(file);
		}
	}
	
	/**
	 * Private abstract adapter class implementing the IElementStateListener interface.
	 * 
	 * @author Jacek Elczyk
	 */
	private static abstract class AbstractElementStateListener implements IElementStateListener {		
		@Override
		public void elementMoved(Object originalElement, Object movedDlement) {}
		
		@Override
		public void elementDirtyStateChanged(Object element, boolean isDirty) {}
		
		@Override
		public void elementDeleted(Object element) {}
		
		@Override
		public void elementContentReplaced(Object element) {}
		
		@Override
		public void elementContentAboutToBeReplaced(Object element) {}
	}
}
