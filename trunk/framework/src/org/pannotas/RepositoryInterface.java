package org.pannotas;

/**
 * @author ciprian
 *
 */
public interface RepositoryInterface {
	public boolean addClient(String clientName, int clientType);
	public boolean deleteClient(String clientName);
	public String[] getClientList();
	public int addClientConnection(String clientName, java.util.Properties params);
	public boolean deleteClientConnection(String clientName, int connectionID);
	public java.util.Properties[] getConnectionParams(String clientName, int connectionID);
	
	public String[] getAllPageTitles();
	public PageInfo getPageInfo(String page);
	
	public void clearRepository();
	public void bindRepository(RepositoryBind bind);
	public void releaseRepositoryBind(RepositoryBind bind);
	public void bindPage(String page, PhraseBind bind);
	public void releasePageBind(String page, PhraseBind bind);
	public void bindPhrase(String page, int paragraph, int wordStart, int wordEnd, PhraseBind bind);
	public void releasePhraseBind(String page, int paragraph, int wordStart, int wordEnd, PhraseBind bind);
	
	public void writePage(String page, String text);
	public String readPage(String page, String text);
	public void deletePage(String page);
	
	public void insertPhrase(String page, int paragraph, int wordStart, String text);
	public void appendPhrase(String page, String text);
	public void deletePhrase(String page, int paragraph, int wordStart, int wordEnd);
	public void copyPhrase(String sourcePage, int sourceParagraph, int sourceWordStart, int sourceWordEnd,
			String targetPage, int targetParagrah, int targetWordStart);
	
}
