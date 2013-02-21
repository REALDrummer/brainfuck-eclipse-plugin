package net.progsign.eclipse.plugin.brainfuck.ui.editors;

import org.eclipse.swt.graphics.RGB;

/**
 * Color constants for syntax highlighting.
 * 
 * @author Jacek Elczyk
 */
public interface IBFColorConstants {
	RGB BF_COMMENT = new RGB(128, 128, 128);
	RGB IO_COMMAND = new RGB(0, 0, 0);
	RGB INC_DEC_COMMAND = new RGB(160, 50, 50);
	RGB POINTER_COMMAND = new RGB(0, 128, 0);
	RGB JUMP_COMMAND = new RGB(0, 0, 128);
}
