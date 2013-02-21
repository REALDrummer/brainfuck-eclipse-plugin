package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Basic whitespace detector.
 * 
 * @author Jacek Elczyk
 */
public class BFWhitespaceDetector implements IWhitespaceDetector {

	@Override
	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
