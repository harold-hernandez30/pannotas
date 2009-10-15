package org.pannotas;

public interface ParagraphListener {
	public void phraseInserted(String page, int paragraph, int start, String text);
	public void phraseChanged(String page, int paragraph, int start, int length, String text);
	public void phraseDeleted(String page, int paragraph, int start, int length);
	public void paragraphMoved(String page, int paragrah, int newParagrah);

}
