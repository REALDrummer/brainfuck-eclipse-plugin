package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

import net.progsign.eclipse.plugin.brainfuck.ui.editors.internal.BFEditorMessages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;

public class BFMarkingValidator {
	private static final String PROBLEM_MARKER_ID = "net.progsign.eclipse.plugin.brainfuck.ui.editors.bfmarker";
	private static BFMarkingValidator _instance;
	private Stack<Integer> startLoopPosition;
	private Stack<Integer> endLoopPosition;

	private BFMarkingValidator() {
		startLoopPosition = new Stack<Integer>();
		endLoopPosition = new Stack<Integer>();
	}
	
	public static synchronized BFMarkingValidator getDefault() {
		if(_instance == null) {
			_instance = new BFMarkingValidator();
		}
		return _instance;
	}
	
	public void validateAndMark(IFile file) {
		removeErrorMarking(file);
		if(parse(file)) {
			mark(file);
		}
	}
	
	public void removeErrorMarking(IFile file) {
		 try {
             file.deleteMarkers(PROBLEM_MARKER_ID, true, IResource.DEPTH_ZERO);
		 } catch (final CoreException ce) {
             ce.printStackTrace();
		 }
	}
	
	private boolean parse(IFile file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(file.getContents()));
			int pos = 0;
			int ch = 0;
			while((ch = reader.read()) != -1) {
				if(ch == '[') {
					// push loop start position
					startLoopPosition.push(pos);
				} else
				if(ch == ']') {
					if(startLoopPosition.size() > 0) {
						// pop matching position 
						startLoopPosition.pop();
					} else {
						// no matching loop, push position on stack
						endLoopPosition.push(pos);
					}
				}
				pos++;
			}
			return true;
		} catch(CoreException ce) {
			ce.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if(reader != null) {
				try { reader.close(); } catch(IOException ioe) {};
			}
		}
		return false;
	}
	
	private void mark(IFile file) {
		int pos;
		try {
			while(!startLoopPosition.empty()) {
				pos = startLoopPosition.pop();
				IMarker marker = file.createMarker(PROBLEM_MARKER_ID);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				marker.setAttribute(IMarker.MESSAGE, BFEditorMessages.MARKERS_ERROR_loop);
				marker.setAttribute(IMarker.CHAR_START, pos);
				marker.setAttribute(IMarker.CHAR_END, pos+1);
				marker.setAttribute(IMarker.LOCATION, NLS.bind(BFEditorMessages.MARKERS_ERROR_offset, pos));
			}
			while(!endLoopPosition.empty()) {
				pos = endLoopPosition.pop();
				IMarker marker = file.createMarker(PROBLEM_MARKER_ID);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				marker.setAttribute(IMarker.MESSAGE, BFEditorMessages.MARKERS_ERROR_loop);
				marker.setAttribute(IMarker.CHAR_START, pos);
				marker.setAttribute(IMarker.CHAR_END, pos+1);
				marker.setAttribute(IMarker.LOCATION, NLS.bind(BFEditorMessages.MARKERS_ERROR_offset, pos));
			}
		} catch(CoreException ce) {
			ce.printStackTrace();
		}
	}
}
