package com.moka.core;

/**
 * JMoka Log manager. This static class allows to write easy logs with tags, also provides a way of
 * saving log to a file.
 */
public final class JMokaLog {
	private static String sharedTag = "JMokaClient";

	/**
	 * Logs a message with a given tag.
	 * @param tag	a specific tag for this log.
	 * @param m		the log message.
	 */
	public static void o(String tag, String m) {
		System.out.println("[" + tag + "] " + m);
	}

	public static void setSharedTag(String sharedTag) {
		JMokaLog.sharedTag = sharedTag;
	}

	public static String getSharedTag() {
		return sharedTag;
	}
	
	public static void saveLogFile() {

	}

	/**
	 * Logs a message using the shared tag.
	 * @param m		the log message.
	 */
	public static void o(String m) {
		System.out.println("[" + sharedTag + "] " + m);
	}

	public static void o(int id) {
		o(Integer.toString(id));
	}

	public static void o(float id) {
		o(Double.toString(id));
	}
	
	public static void o(double id) {
		o(Double.toString(id));
	}
}
