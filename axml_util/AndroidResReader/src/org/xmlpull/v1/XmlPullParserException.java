package org.xmlpull.v1;

import java.io.PrintStream;

public class XmlPullParserException extends Exception {
	protected Throwable detail;
	protected int row = -1;
	protected int column = -1;

	public XmlPullParserException(String s) {
		super(s);
	}

	public XmlPullParserException(String msg, XmlPullParser parser,
			Throwable chain) {
		super(((msg == null) ? "" : new StringBuilder(String.valueOf(msg))
				.append(" ").toString())
				+ ((parser == null) ? "" : new StringBuilder("(position:")
						.append(parser.getPositionDescription()).append(") ")
						.toString())
				+ ((chain == null) ? "" : new StringBuilder("caused by: ")
						.append(chain).toString()));

		if (parser != null) {
			this.row = parser.getLineNumber();
			this.column = parser.getColumnNumber();
		}
		this.detail = chain;
	}

	public Throwable getDetail() {
		return this.detail;
	}

	public int getLineNumber() {
		return this.row;
	}

	public int getColumnNumber() {
		return this.column;
	}

	public void printStackTrace() {
		if (this.detail == null)
			super.printStackTrace();
		else
			synchronized (System.err) {
				System.err.println(super.getMessage()
						+ "; nested exception is:");
				this.detail.printStackTrace();
			}
	}
}
