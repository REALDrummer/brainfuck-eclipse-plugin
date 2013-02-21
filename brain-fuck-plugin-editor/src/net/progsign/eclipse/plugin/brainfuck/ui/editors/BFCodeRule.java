package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * A predicate rule for finding Brainfuck code partitions in documents.
 * 
 * @author Jacek Elczyk
 * @see BFPartitionScanner
 */
public class BFCodeRule implements IPredicateRule {
	private static final String DEFAULT_COMMANDS = ",.+-[]<>";
	private Set<Character> commands;
	protected IToken fToken;
	
	public BFCodeRule(IToken token) {
		this(DEFAULT_COMMANDS.toCharArray(), token);
	}
	
	public BFCodeRule(char[] commands, IToken token) {
		this.commands = new HashSet<Character>();
		for(char cmd : commands) {
			this.commands.add(cmd);
		}
		fToken = token;
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		boolean match = false;
		
		int ch = scanner.read();
		if(isValidCommand(ch)) {
			match = true;
			// continue to end of command block
			while((ch = scanner.read()) != ICharacterScanner.EOF) {
				if(!isValidCommand(ch)) {
					scanner.unread();
					break;
				}
			}
		}
		if(match) {
			return fToken;
		}
		scanner.unread();
		return Token.UNDEFINED;
	}

	@Override
	public IToken getSuccessToken() {
		return fToken;
	}
	
	/**
	 * Checks if the argument is valid command.
	 * @param command a character
	 * @return true if character is a Brainfuck command
	 */
	public boolean isValidCommand(int command) {
		return commands.contains((char)command);
	}
}
