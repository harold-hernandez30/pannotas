package org.pannotas;
import java.sql.SQLException;

/**
 * @author ciprian
 * 
 * Word separators: " ' , ; : | / \  & @ 
 *
 */
public interface RepositoryInterface {
	public void open(String dbName) throws Exception;
	public void close() throws SQLException;
	
	public boolean addClient(String clientName, int clientType) throws SQLException;
	public boolean deleteClient(String clientName) throws SQLException;
	public String[] getClientList() throws SQLException;
	public int addClientConnection(String clientName, java.util.Properties params) throws SQLException;
	public boolean deleteClientConnection(String clientName, int connectionID) throws SQLException;
	public java.util.Properties[] getConnectionParams(String clientName, int connectionID) throws SQLException;
	
	public String[] getAllPageTitles() throws SQLException;
	public PageInfo getPageInfo(String page) throws SQLException;
		
	public void clearRepository() throws SQLException;	
	
	public void bindRepository(RepositoryBind bind);
	public void releaseRepositoryBind(RepositoryBind bind);
	public void bindPage(String page, ParagraphBind bind);
	public void releasePageBind(String page, ParagraphBind bind);
	public void bindParagrah(String page, int paragraph, ParagraphBind bind);
	public void releaseParagraphBind(String page, int paragraph, ParagraphBind bind);
	
	public void writePage(String page, String text) throws SQLException;
	public String readPage(String page) throws SQLException;
	public void deletePage(String page) throws SQLException;
	
	public void insertPhrase(String page, int paragraph, int start, String text) throws SQLException;
	public void appendPhrase(String page, String text) throws SQLException;
	public void deletePhrase(String page, int paragraph, int start, int end) throws SQLException;
	public void changePhrase(String page, int paragraph, int start, int end, String text) throws SQLException;
	public void copyPhrase(String sourcePage, int sourceParagraph, int sourceStart, int length,
			String targetPage, int targetParagraph, int targetStart) throws SQLException;
	
	public PhraseLocation[] findAll(java.util.regex.Pattern search) throws SQLException;
	public PhraseLocation[] findAll(java.util.regex.Pattern search, String page) throws SQLException;
}
