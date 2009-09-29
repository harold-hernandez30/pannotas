package org.pannotas;

public class PageInfo {
	public String page;
	public String text;
	public long created;
	public long updated;
	public int size;
	
	public PageInfo(String vPage, String vText, long vCreated, long vUpdated, int vSize) {
		page = vPage;
		text = vText;
		created = vCreated;
		updated = vUpdated;
		size = vSize;
	}
}
