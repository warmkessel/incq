package com.incq.exception;

import javax.servlet.ServletException;

public class IncqServletException extends ServletException {
	/**
	 * 
	 */
	private Exception exception = null;
	private String message = "";
	private static final long serialVersionUID = -3787182437398967448L;

	public IncqServletException() {

	}
	public IncqServletException(String m) {
		this(null, m);
	}
	public IncqServletException(Exception e) {
		this(e, e.getMessage());
	}
	public IncqServletException(Exception e, String m) {
		exception = e;
		message = m;
	}
	public Exception getBaseException() {
		return exception;
	}
	public String getMessage() {
		StringBuffer theReturn = new StringBuffer(super.getMessage());
		theReturn.insert(0, message);
		return theReturn.toString();
	}
}
