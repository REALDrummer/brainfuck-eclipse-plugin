package net.progsign.eclipse.plugin.brainfuck.ui.editors.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.osgi.util.NLS;

public final class BFCommandTip extends NLS {
	private static final String BUNDLE_NAME = "net.progsign.eclipse.plugin.brainfuck.ui.editors.internal.BFCommandTip";
	private static final Map<Character, String> mapping;
	
	public static String COMMAND_TIP_plus;
	public static String COMMAND_TIP_minus;
	public static String COMMAND_TIP_lt;
	public static String COMMAND_TIP_gt;
	public static String COMMAND_TIP_coma;
	public static String COMMAND_TIP_dot;
	public static String COMMAND_TIP_loop_in;
	public static String COMMAND_TIP_loop_out;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, BFCommandTip.class);
		mapping = new HashMap<Character, String>();
		mapping.put('+', COMMAND_TIP_plus);
		mapping.put('-', COMMAND_TIP_minus);
		mapping.put('<', COMMAND_TIP_lt);
		mapping.put('>', COMMAND_TIP_gt);
		mapping.put(',', COMMAND_TIP_coma);
		mapping.put('.', COMMAND_TIP_dot);
		mapping.put('[', COMMAND_TIP_loop_in);
		mapping.put(']', COMMAND_TIP_loop_out);
	}
	
	public static String getCommandTip(char command) {
		return mapping.get(command);
	}
	
	private BFCommandTip() {
		// private constructor
	}
}
