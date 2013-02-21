package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.jface.text.*;

/**
 * Double click strategy for Brainfuck source code. Selects the whole code inside
 * a loop (between '[' and ']') or the closest character if caret is not inside a
 * loop.
 * 
 * @author Jacek Elczyk
 */
public class BFDoubleClickStrategy implements ITextDoubleClickStrategy {
	protected ITextViewer fText;

	@Override
	public void doubleClicked(ITextViewer part) {
		int pos = part.getSelectedRange().x;

		if (pos < 0)
			return;

		fText = part;

		if (!selectLoop(pos)) {
			selectRange(pos, pos);
		}
	}
	
	/**
	 * Tries to find enclosing square brackets (Brainfuck loop). If found, selects everything
	 * inside the loop and returns true. 
	 * @param caretPos initial position
	 * @return true if <code>caretPos</code> is inside a loop, false otherwise
	 */
	protected boolean selectLoop(int caretPos) {
		IDocument doc = fText.getDocument();
		int startPos, endPos;

		try {
			// caret not inside a loop or inside an empty loop
			if(doc.getChar(caretPos) == '[' || doc.getChar(caretPos) == ']') {
				return false;
			}
			if((startPos = startLoopPosition(doc, caretPos)) != -1) {
				if((endPos = endLoopPosition(doc, caretPos)) != doc.getLength()) {
					selectRange(startPos, endPos);
					return true;
				}
			}
		} catch (BadLocationException x) {
			// code syntax error
		}

		return false;
	}
	
	private int startLoopPosition(IDocument doc, int caretPos) throws BadLocationException {
		char c;
		int ilc = 0;	// inner loops counter
		while (caretPos >= 0) {
			c = doc.getChar(caretPos);
			if (c == ']') {
				++ilc;
			} else
			if (c == '[') {
				if(ilc > 0) --ilc;
				else {
					break;
				}
			}
			--caretPos;
		}
		return caretPos;
	}
	
	private int endLoopPosition(IDocument doc, int caretPos) throws BadLocationException {
		char c;
		int length = doc.getLength();
		int ilc = 0;	// inner loops counter
		while (caretPos < length) {
			c = doc.getChar(caretPos);
			if (c == '[') {
				++ilc;
			} else
			if (c == ']') {
				if (ilc > 0) --ilc;
				else break;
			}
			++caretPos;
		}
		return caretPos;
	}

	/**
	 * Selects a range narrowed by <code>startPos</code> and <code>endPos</code>.
	 * @param startPos start position
	 * @param stopPos end position
	 */
	private void selectRange(int startPos, int stopPos) {
		int offset = startPos + 1;
		int length = stopPos - offset;
		fText.setSelectedRange(offset, length);
	}
}