package org.geogebra.web.web.gui;

import org.geogebra.common.util.Unicode;
import org.geogebra.web.html5.gui.textbox.GTextBox;
import org.geogebra.web.html5.main.AppW;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

// Later this shall inherit from MyTextField instead of TextBox

public class AngleTextFieldW extends GTextBox implements KeyUpHandler {

	AppW app;

	public AngleTextFieldW(int columns, AppW app) {
		super();
		this.app = app;
		setVisibleLength(columns);
		this.addKeyUpHandler(this);
	}

	public void onKeyUp(KeyUpEvent e) {

		boolean modifierKeyPressed = app.isMacOS() ? e.isControlKeyDown() : e
		        .isAltKeyDown();

		// we don't want to act when AltGr is down
		// as it is used eg for entering {[}] is some locales
		// NB e.isAltGraphDown() doesn't work
		if (e.isAltKeyDown() && e.isControlKeyDown()) {
			modifierKeyPressed = false;
		}

		// Application.debug(e+"");

		String insertString = "";

		// switch
		// (KeyEvent.getKeyText(e.getKeyCode()).toLowerCase(Locale.US).charAt(0))
		// {
		switch (Character.toChars(e.getNativeEvent().getCharCode())[0]) {
		case 'o':
			insertString = Unicode.DEGREE;
			break;
		case 'p':
			insertString = Unicode.PI_STRING;
			break;
		}

		if (modifierKeyPressed && !insertString.equals("")) {
			int start = getCursorPos();
			int end = start + getSelectionLength();
			// clear selection if there is one
			if (start != end) {
				int pos = getCursorPos();
				String oldText = getText();
				StringBuilder sb = new StringBuilder();
				sb.append(oldText.substring(0, start));
				sb.append(oldText.substring(end));
				setText(sb.toString());
				if (pos < sb.length())
					setCursorPos(pos);
			}

			String oldText = getText();

			// don't insert more than one degree sign or pi *in total*
			if (oldText.indexOf(Unicode.DEGREE_CHAR) == -1
					&& oldText.indexOf(Unicode.pi) == -1) {
				int pos = oldText.length(); // getCaretPosition();
				StringBuilder sb = new StringBuilder();
				sb.append(oldText.substring(0, pos));
				sb.append(insertString);
				sb.append(oldText.substring(pos));
				setText(sb.toString());
				setCursorPos(pos + insertString.length());
			}

			// e.consume();
		}
	}

}
