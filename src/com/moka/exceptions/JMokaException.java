package com.moka.exceptions;

import com.moka.core.Moka;

public final class JMokaException extends RuntimeException {
	public JMokaException(String string) {
		super(string);
		Moka.stop();
	}
}
