package org.geogebra.common.gui.inputfield;

import org.geogebra.common.util.StringUtil;

/**
 * @author gabor
 * 
 *         Just a collector class for static methods used both Web and Desktop
 *
 */
public abstract class MyTextField {

	/**
	 * Locates bracket positions in a given string with given caret position.
	 * 
	 * @param inputText
	 *            input
	 * @param caret
	 *            caret position
	 * @return array of bracket positions
	 */
	public static int[] getBracketPositions(String inputText, int caret) {
		String text = inputText;
		// position to the left of the caret if a bracket exists
		int bracketPos0 = -1;
		// position of matching bracket if it exists
		int bracketPos1 = -1;

		int searchDirection = 0;
		int searchEnd = 0;

		char bracketToMatch = ' ';
		char oppositeBracketToMatch = ' ';

		if (caret > 0 && caret <= text.length()) {

			// get the character just to the left of the caret
			char c = text.charAt(caret - 1);
			bracketPos0 = caret - 1;

			// check if we have a bracket next to the caret
			// and set the search parameters if we do
			switch (c) {
			case '(':
				searchDirection = +1;
				searchEnd = text.length();
				oppositeBracketToMatch = '(';
				bracketToMatch = ')';
				break;
			case '{':
				searchDirection = +1;
				searchEnd = text.length();
				oppositeBracketToMatch = '{';
				bracketToMatch = '}';
				break;
			case '[':
				searchDirection = +1;
				searchEnd = text.length();
				oppositeBracketToMatch = '[';
				bracketToMatch = ']';
				break;
			case ')':
				searchDirection = -1;
				searchEnd = -1;
				oppositeBracketToMatch = ')';
				bracketToMatch = '(';
				break;
			case '}':
				searchDirection = -1;
				searchEnd = -1;
				oppositeBracketToMatch = '}';
				bracketToMatch = '{';
				break;
			case ']':
				searchDirection = -1;
				searchEnd = -1;
				oppositeBracketToMatch = ']';
				bracketToMatch = '[';
				break;
			default:
				searchDirection = 0;
				bracketPos0 = -1;
				bracketPos1 = -1;
				break;
			}

		}

		// search the text for a matching bracket

		boolean textMode = false; // flag for quoted text
		if (searchDirection != 0) {
			int count = 0;
			for (int i = caret - 1; i != searchEnd; i += searchDirection) {
				if (text.charAt(i) == '\"') {
					textMode = !textMode;
				}
				if (!textMode && text.charAt(i) == bracketToMatch) {
					count++;
				} else if (!textMode
						&& text.charAt(i) == oppositeBracketToMatch) {
					count--;
				}

				if (count == 0) {
					bracketPos1 = i;
					break;
				}
			}
		}

		int[] result = { bracketPos0, bracketPos1 };

		return result;

	}

	/**
	 * @param c
	 *            character
	 * @return whether it's close bracket or whitespace
	 */
	public static boolean isCloseBracketOrWhitespace(char c) {
		// Character.isWhiteSpace not supported in GWT
		return StringUtil.isWhitespace(c) || c == ')' || c == ']' || c == '}';
	}

}
