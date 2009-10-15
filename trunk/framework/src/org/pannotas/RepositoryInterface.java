package org.pannotas;

/**
 * @author ciprian
 * 
 *
 */
public interface RepositoryInterface {
	public void open(String dbName) ;
	public void close();
	
	public boolean addClient(String clientName, int clientType);
	public boolean deleteClient(String clientName);
	public String[] getClientList();
	public int addClientConnection(String clientName, java.util.Properties params);
	public boolean deleteClientConnection(String clientName, int connectionID);
	public java.util.Properties[] getConnectionParams(String clientName, int connectionID);
	
	public String[] getAllPageTitles();
	public String[] getAllSystemPageTitles();
	public PageInfo getPageInfo(String page);
	public String[] getPageChildren(String page);
		
	public void clearRepository();
	public void flush();
	
	public void addRepositoryListener(RepositoryListener bind);
	public void removeRepositoryListener(RepositoryListener bind);
	public void addPageListener(String page, ParagraphListener bind);
	public void removePageListener(String page, ParagraphListener bind);
	public void addParagrahListener(String page, int paragraph, ParagraphListener bind);
	public void removeParagraphListener(String page, int paragraph, ParagraphListener bind);
	public void addResourceListener(String resource, ResourceListener bind);
	public void removeResourceListener(String resource, ResourceListener bind);
	
	public void writePage(String page, String text);
	public String readPage(String page);
	public void deletePage(String page);
	public boolean isPage(String page);
	
	public void insertPhrase(String page, int paragraph, int start, String text);
	public void appendPhrase(String page, String text);
	public void deletePhrase(String page, int paragraph, int start, int end);
	public void changePhrase(String page, int paragraph, int start, int end, String text);
	public void copyPhrase(String sourcePage, int sourceParagraph, int sourceStart, int length,
			String targetPage, int targetParagraph, int targetStart);
	
	public void writeResource(String resource, String mimeType, byte[] data);
	public byte[] readResource(String resource);
	public String readResourceType(String resource);
	public void deleteResource(String resource);
	public String[] getAllResourceNames();
		
	public PhraseLocation[] findAll(java.util.regex.Pattern search);
	public PhraseLocation[] findAll(java.util.regex.Pattern search, String page);
	public PhraseLocation findNext(java.util.regex.Pattern search, PhraseLocation lastFind);
	public PhraseLocation findNext(java.util.regex.Pattern search, String page, PhraseLocation lastFind);
}
