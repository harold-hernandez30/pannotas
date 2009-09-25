package org.pannotas;

public class PhraseLocation {
	public String page;
	public int paragraph;
	public int start;
	public int length;

	public PhraseLocation() {}
	public PhraseLocation (String vPage, int vParagraph, int vStart, int vLength) {
		page = vPage;
		paragraph = vParagraph;
		start = vStart;
		length = vLength;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PhraseLocation) {
			PhraseLocation that = (PhraseLocation)other;
			if (page.equals(that.page) && paragraph == that.paragraph && start == that.start && length == that.length) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {		
		return page.hashCode()*100+paragraph*start*length;
	}
	
}
