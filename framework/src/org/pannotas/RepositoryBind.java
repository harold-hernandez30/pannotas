package org.pannotas;

public interface RepositoryBind {
	public void pageChanged(String page);
	public void pageDeleted(String page);
	public void pageAdded(String page);
}
