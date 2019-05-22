package com.couponsystem.exceptions;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * DailyTaskException is an exception used to indicates exception which derived
 * from error which related to Daily Task Therad.
 */

public class DailyTaskException extends Exception {

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message pattern
	 * from Exception superclass.
	 */
	public DailyTaskException(String message) {
		super(message);
	}

}
