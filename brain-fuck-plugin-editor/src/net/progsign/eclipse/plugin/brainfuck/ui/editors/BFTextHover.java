package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import net.progsign.eclipse.plugin.brainfuck.ui.editors.internal.BFCommandTip;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;

/**
 * Custom text hover code tips.
 * 
 * @author Jacek Elczyk
 */
public class BFTextHover implements ITextHover {
	@Override
	public IRegion getHoverRegion(ITextViewer viewer, int offset) {
		Point selection = viewer.getSelectedRange();
		if(selection.x <= offset && offset < selection.x + selection.y) {
			return new Region(selection.x, selection.y);
		}
		return new Region(offset, 0);
	}
	
	@Override
	public String getHoverInfo(ITextViewer viewer, IRegion region) {
		if(region != null && region.getLength()==1 || region.getLength()==0) {
			try {
				char command = viewer.getDocument().getChar(region.getOffset());
				return BFCommandTip.getCommandTip(command);
			} catch(BadLocationException ble) {
			}
		}
		return null;
	}
}
