package net.progsign.eclipse.plugin.brainfuck.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Plug-in messages bundle
 * @author Jacek Elczyk
 */
public final class BFPluginMessages extends NLS {
	private static final String BUNDLE_NAME = "net.progsign.eclipse.plugin.brainfuck.ui.internal.BFPluginMessages";
	
	public static String UI_MAIN_TAB_name;
	public static String UI_FILTERED_SELECTION_DIALOG_title;
	public static String UI_FILTERED_SELECTION_DIALOG_search;
	public static String UI_PROJECT_SELECTION_DIALOG_title;
	public static String UI_PROJECT_SELECTION_DIALOG_message;
	public static String UI_PARAMS_GROUP_title;
	public static String UI_PARAMS_GROUP_stack_size_lbl;
	public static String UI_PARAMS_GROUP_input_path_lbl;
	public static String UI_SOURCE_GROUP_title;
	public static String UI_SOURCE_GROUP_project_lbl;
	public static String UI_SOURCE_GROUP_source_lbl;
	public static String UI_SOURCE_GROUP_browse_btn;
	public static String UI_PARAMS_GROUP_input_title;
	public static String UI_PARAMS_GROUP_input_system_btn;
	public static String UI_PARAMS_GROUP_input_file_btn;
	public static String UI_MONITOR_begin_task;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, BFPluginMessages.class);
	}
	
	private BFPluginMessages() {
		// private constructor
	}
}
