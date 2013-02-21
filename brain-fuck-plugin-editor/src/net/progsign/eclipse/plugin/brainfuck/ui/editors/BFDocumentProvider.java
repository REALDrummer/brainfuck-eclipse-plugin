package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * Brainfuck document provider.
 * 
 * @author Jacek Elczyk
 */
public class BFDocumentProvider extends FileDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if(document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(
					new BFPartitionScanner(), new String[] {
						BFPartitionScanner.BF_CODE
					});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
	
		return document;
	}
}
