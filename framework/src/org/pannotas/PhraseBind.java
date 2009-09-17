package org.pannotas;

public interface PhraseBind {
	public void phraseInserted(String page, int paragraph, int wordStart, String text);
	public void phraseChanged(String page, int paragraph, int wordStart, int wordEnd, String text);
	public void phraseDeleted(String page, int paragraph, int wordStart, int wordEnd);

}
