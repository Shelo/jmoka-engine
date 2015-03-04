package com.moka.core.xml;


import java.util.ArrayList;

public class ParametersParser {
	private static final ParametersParser INSTANCE = new ParametersParser();
	
	private static final int STATE_NONE		= 0;
	private static final int STATE_NUMBER	= 1;
	private static final int STATE_STRING	= 2;
	private static final int STATE_ENTITY	= 3;
	private static final int STATE_VALUE	= 4;

	private int state = STATE_NONE;
	private StringBuilder current = new StringBuilder();
	private ArrayList<String> parameters = new ArrayList<>();

	public String[] parse(String m) throws ParsingException {
		for(int i = 0; i < m.length(); i++) {
			char c = m.charAt(i);

			// TODO: take negative values!!!!.
			
			if(state == STATE_NONE) {
				if(Character.isDigit(c)) {
					// detected a number.
					state = STATE_NUMBER;
					current.append(c);
				} else if(c == '\'') {
					// detected start of string.
					state = STATE_STRING;
				} else if(c == '@') {
					state = STATE_ENTITY;
					current.append(c);
				} else if(c == '$') {
					state = STATE_VALUE;
					current.append(c);
				} else if(c == ' ' || c == '\n' || c == '\t') {
					// continue if those characters are found, they don't mean anything.
				} else if(c == ',' || !Character.isDigit(c)) {
					throw new ParsingException("Invalid string \"" + m + "\", near @" + i + ".");
				} else {
					// no parameters.
					return new String[] {""};
				}
			} else if(state == STATE_NUMBER) {
				if(Character.isDigit(c)) {
					current.append(c);
				} else if(c == '.') {
					current.append(c);
				}else if(c == ',') {
					flush();
				} else if(!Character.isDigit(c)) {
					throw new ParsingException("Invalid string \"" + m + "\", near @" + i + ".");
				}
			} else if(state == STATE_STRING) {
				if(c == '\'') {
					i++;
					flush();
				} else {
					current.append(c);
				}
			} else if(state == STATE_ENTITY) {
				if(Character.isLetter(c) || Character.isDigit(c) || c == '_') {
					current.append(c);
				} else if(c == ',') {
					flush();
				} else {
					throw new ParsingException("Invalid string \"" + m + "\", near @" + i + ".");
				}
			} else if(state == STATE_VALUE) {
				if(Character.isLetter(c) || Character.isDigit(c) || c == '_') {
					current.append(c);
				} else if(c == ',') {
					flush();
				} else {
					throw new ParsingException("Invalid string \"" + m + "\", near @" + i + ".");
				}
			}
		}

		if(state == STATE_STRING) {
			throw new ParsingException("Invalid string \"" + m + "\" near \"" + current.toString()
					+ "\", (missing semi-colon?).");
		} else if(state == STATE_NUMBER) {
			flush();
		} else if(state == STATE_ENTITY) {
			flush();
		}

		String[] p = new String[parameters.size()];
		p = parameters.toArray(p);
		reset();
		return p;
	}

	private void reset() {
		state = STATE_NONE;
		current = new StringBuilder();
		parameters = new ArrayList<>();
	}

	private void flush() {
		parameters.add(current.toString());
		current = new StringBuilder();
		state = STATE_NONE;
	}

	public static ParametersParser getInstance() {
		return INSTANCE;
	}
}
