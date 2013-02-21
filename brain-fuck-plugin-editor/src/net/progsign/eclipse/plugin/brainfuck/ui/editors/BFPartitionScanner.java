package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * Brainfuck partition scanner. Uses only one rule to partition document into
 * brainfuck code blocks and everything else. The actual highlighting is done
 * by <code>BFCodeScanner</code>. This is less resource draining method than,
 * for instance, partitioning every Brainfuck command individually, as all
 * Brainfuck commands are one character long.
 * 
 * @author Jacek Elczyk
 * @see BFCodeRule
 * @see BFCodeScanner
 */
public class BFPartitionScanner extends RuleBasedPartitionScanner {
	public static final String BF_CODE = "__bf_code";
	
	public BFPartitionScanner() {
		IToken code = new Token(BF_CODE);
		
		setPredicateRules(new IPredicateRule[] { new BFCodeRule(code) });
	}
}
