package org.pannotas;

public interface ResourceListener {
	public void resourceChanged(String resource);
	public void resourceDeleted(String resource);
	public void resourceAdded(String resource);
}
