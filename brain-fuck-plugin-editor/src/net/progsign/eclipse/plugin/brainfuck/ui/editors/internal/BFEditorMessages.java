package net.progsign.eclipse.plugin.brainfuck.ui.editors.internal;

import org.eclipse.osgi.util.NLS;

public final class BFEditorMessages extends NLS {
	private static final String BUNDLE_NAME = "net.progsign.eclipse.plugin.brainfuck.ui.editors.internal.BFEditorMessages";
	
	public static String MARKERS_ERROR_loop;
	public static String MARKERS_ERROR_offset;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, BFEditorMessages.class);
	}
	
	private BFEditorMessages() {
		// private constructor
	}
}
