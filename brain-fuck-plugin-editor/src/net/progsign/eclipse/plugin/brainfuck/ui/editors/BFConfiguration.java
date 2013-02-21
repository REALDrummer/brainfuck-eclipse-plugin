package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * The Brainfuck editor source viewer configuration.
 * 
 * @author Jacek Elczyk
 */
public class BFConfiguration extends SourceViewerConfiguration {
	private BFColorManager colorManager;
	private BFCodeScanner codeScanner;
	private BFTextHover textHover;
	private ITextDoubleClickStrategy doubleClickStrategy;
	
	
	public BFConfiguration(BFColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			BFPartitionScanner.BF_CODE
		};
	}
	
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		if(textHover == null) {
			textHover = new BFTextHover();
		}
		return textHover;
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new BFDoubleClickStrategy();
		return doubleClickStrategy;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		// setting Brainfuck code partition highlighter
		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getBFCodeScanner());
		reconciler.setDamager(dr, BFPartitionScanner.BF_CODE);
		reconciler.setRepairer(dr, BFPartitionScanner.BF_CODE);

		// apply default damager/repairer for everything else (i.e comments)
		NonRuleBasedDamagerRepairer ndr =
				new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager.getColor(IBFColorConstants.BF_COMMENT)));
		reconciler.setDamager(ndr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(ndr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}
	
	/**
	 * A convenience method. Creates a new BFCodeScanner object if not yet existing
	 * and returns the reference to it.
	 * @return a reference to a BFCodeScanner
	 */
	protected BFCodeScanner getBFCodeScanner() {
		if(codeScanner == null) {
			codeScanner = new BFCodeScanner(colorManager);
		}
		return codeScanner; 
	}
}
