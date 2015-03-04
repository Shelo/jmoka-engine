package com.moka.exceptions;

import com.moka.core.Moka;

public final class JMokaException extends RuntimeException {
	public JMokaException(String string) {
		super(string);
	}

	public static void raise(String s) {
		Moka.stop();
		throw new JMokaException(s);
	}
}
