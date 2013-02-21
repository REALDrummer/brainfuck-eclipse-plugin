package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.graphics.RGB;

/**
 * Brainfuck code syntax highlighter
 * 
 * @author Jacek Elczyk
 */
public class BFCodeScanner extends RuleBasedScanner {
	private BFColorManager colorManager;
	
	public BFCodeScanner(BFColorManager manager) {
		this.colorManager = manager;

		IRule[] rules = new IRule[5];
		rules[0] = createRule(new char[] {'+', '-'}, IBFColorConstants.INC_DEC_COMMAND);
		rules[1] = createRule(new char[] {'.', ','}, IBFColorConstants.IO_COMMAND);
		rules[2] = createRule(new char[] {'[', ']'}, IBFColorConstants.JUMP_COMMAND);
		rules[3] = createRule(new char[] {'<', '>'}, IBFColorConstants.POINTER_COMMAND);
		rules[4] = new WhitespaceRule(new BFWhitespaceDetector());
		setRules(rules);
	}
	
	/**
	 * Convenience method for creating new Brainfuck code rules.
	 * @param commands commands to be attached to the rule
	 * @param rgb highlight color as RGB
	 * @return new BFCodeRule
	 */
	protected BFCodeRule createRule(char[] commands, RGB rgb) {
		IToken token = new Token(new TextAttribute(colorManager.getColor(rgb)));
		return new BFCodeRule(commands, token);
	}
}
