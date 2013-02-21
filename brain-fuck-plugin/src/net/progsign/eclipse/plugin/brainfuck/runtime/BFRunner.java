package net.progsign.eclipse.plugin.brainfuck.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Stack;

/**
 * The Brainfuck interpreter. 
 * @author Jacek Elczyk
 */
public class BFRunner {
	private static final int CR = 13;
	private static final int LF = 10;
	
	/** Default stack size. */
	public static final int DEFAULT_STACK_SIZE = 1024;

	/* Properties keys */
	public static final String PROP_INPUT = "INPUT";
	public static final String PROP_STACK = "STACK";
	public static final String PROP_SOURCE = "SOURCE";
	
	private Stack<Integer> jumpPosition;
	private Stack<Integer> inputQueue;
	private int[] stack;
	private int sp;			// stack pointer
	private int ip;			// instruction pointer

	/**
	 * The main method.
	 * @param args
	 */
	public static final void main(String[] args) {
		Properties props = new Properties();
		for(String arg : args) {
			String param = arg.substring(2).trim();
			if(arg.startsWith("-i")) { props.setProperty(PROP_INPUT, param); } else
			if(arg.startsWith("-s")) { props.setProperty(PROP_STACK, param); } else
			if(arg.startsWith("-f")) { props.setProperty(PROP_SOURCE, param); };
		}
		BFRunner.buildAndRun(props);
	}
	
	/**
	 * Helper method to build and run configuration from properties.
	 * @param props
	 */
	public static void buildAndRun(Properties props) {
		int stackSize = DEFAULT_STACK_SIZE;
		boolean ignoreCRLF = true;
		if(props.getProperty(PROP_STACK) != null) {
			stackSize = Integer.parseInt(props.getProperty(PROP_STACK));
		}
		if(props.getProperty(PROP_SOURCE) == null) {
			System.err.println("No source");
			return;
		}
		String code = loadCode(new File(props.getProperty(PROP_SOURCE)));
		InputStream input = System.in;
		if(props.getProperty(PROP_INPUT) != null) {
			try {
				input = new FileInputStream(new File(props.getProperty(PROP_INPUT)));
				ignoreCRLF = false;
			} catch(FileNotFoundException fnfe) {
				System.err.println("Unable to load input file " + fnfe.getMessage());
			}
		}
		
		new BFRunner(stackSize).execute(code, input, ignoreCRLF);
	}
	
	/**
	 * Helper method to load source code from file
	 * @param file source file
	 * @return source code as String
	 */
	public static String loadCode(File file) {
		StringBuilder code = new StringBuilder();
		try {
			Scanner s = new Scanner(file);
			while(s.hasNextLine()) {
				code.append(s.nextLine());
			}
			s.close();
		} catch(FileNotFoundException fnfe) {
			System.err.println("Unable to load source file " + fnfe.getMessage());
		}
		return code.toString();
	}
	
	/**
	 * Creates a new BrainFuck interpreter. Uses DEFAULT_STACK_SIZE as the initial stack size.
	 */
	public BFRunner() {
		this(DEFAULT_STACK_SIZE);
	}

	/**
	 * Creates a new BrainFuck interpreter with initial size of the stack.
	 * @param stackSize custom stack size
	 */
	public BFRunner(int stackSize) {
		stack = new int[stackSize];
		jumpPosition = new Stack<Integer>();
		inputQueue = new Stack<Integer>();
	}

	/**
	 * The BrainFuck code parser.
	 * @param program BrainFuck code
	 * @param input input stream
	 * @param ignoreCRLF tells if the interpreter is to skip "new line" characters from input stream
	 * @throws BFExecutionException
	 */
	public void execute(String program, InputStream input, boolean ignoreCRLF) {
		if(input == null) {
			throw new UnsupportedOperationException("Input stream must not be null in order to use the ',' command.");
		}
		reset();
		char[] code = program.toCharArray();
		char c = 0;
		try {
			for(ip=0; ip<code.length && stack[sp]>=0; ip++) {
				c = code[ip];
				switch(c) {
				case '+': ++stack[sp]; break;
				case '-': --stack[sp]; break;
				case '>': ++sp; break;
				case '<': --sp; break;
				case '.': System.out.print((char)stack[sp]); break;
				case ',': {
					stack[sp] = read(input, ignoreCRLF);
				} break;
				case '[': {
					if(stack[sp] > 0) {
						jumpPosition.push(ip);
					} else {
						ip = skipLoop(code, ip); // loop counter is 0, skip to next instruction
					}
				} break;
				case ']': {
					if(stack[sp] > 0) {
						ip = jumpPosition.peek();
					} else {
						jumpPosition.pop();
					}
				} break;
				default : {
					/* As per the specification, BrainFuck ignores any character that is not BF command.
					 * However some checking may be done by uncommenting the below code. 
					 */
					/*
					if(!Character.isWhitespace(c)) {
						throw new UnsupportedOperationException(
								String.format("Invalid character at position %d (%c)", cp, c));
					}
					 */
				}
				} // switch
			} // for
		} catch(IOException ioe) {
			throw new BFExecutionException(String.format("%s at position %d", ioe.getMessage(), ip));
		} catch(EmptyStackException ese) {
			throw new BFExecutionException(String.format("Invalid operation at position %d, operation: %c (invalid state)", ip, c));
		} catch(ArrayIndexOutOfBoundsException aioobe) {
			throw new BFExecutionException(String.format("Stack pointer out of range: %d (%d). Stack set too small.", sp, stack.length));
		}

	}
	
	/**
	 * Convenience method for skipping loop code when loop counter reaches 0.
	 * @param code execution code
	 * @param ip instruction pointer
	 * @return instruction pointer set to first instruction after the loop
	 */
	private int skipLoop(char[] code, int ip) {
		int ilc = -1; // inner loops counter
		do { // skipping to matching closing bracket
			if(code[ip] == '[') {
				ilc++;
			} else
			if(code[ip] == ']' && ilc > 0) {
				ilc--;
			}
		} while(!(code[++ip] == ']' && ilc == 0));
		return ip;
	}
	
	/**
	 * Reads single character from input stream. Ignores new line characters if flag "ignoreCRLF" is set.
	 * @param input input stream
	 * @param ignoreCRLF ignore "new line" character
	 * @return character read as int
	 * @throws IOException
	 */
	private int read(InputStream input, boolean ignoreCRLF) throws IOException {
		if(!inputQueue.isEmpty()) {
			return inputQueue.pop();
		}
		int b = input.read();
		b = b==-1 ? 0 : b; // converts EOF to 0
		if(b==CR && ignoreCRLF) {
			b = input.read();	// expected LF
			if(b==LF) {
				b = read(input, ignoreCRLF);
			} else {
				inputQueue.push(b);
			}
		}
		return b;
	}

	/**
	 * Resets all parameters.
	 */
	public void reset() {
		Arrays.fill(stack, 0);
		jumpPosition.clear();
		inputQueue.clear();
		ip = sp = 0;
	}

	/**
	 * Returns the interpreter status. Useful for debugging purposes.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder("[BFInterpreter: ");
		builder.append("stack=").append(stack.length);
		builder.append(", pointer=").append(sp);
		builder.append(", instruction pointer=").append(ip);
		builder.append("]");
		return builder.toString();
	}
}
