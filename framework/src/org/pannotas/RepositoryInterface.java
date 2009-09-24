package org.pannotas;
import java.sql.SQLException;

/**
 * @author ciprian
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
	public void bindPage(String page, PhraseBind bind);
	public void releasePageBind(String page, PhraseBind bind);
	public void bindPhrase(String page, int paragraph, int wordStart, int wordEnd, PhraseBind bind);
	public void releasePhraseBind(String page, int paragraph, int wordStart, int wordEnd, PhraseBind bind);
	
	public void writePage(String page, String text) throws SQLException;
	public String readPage(String page, String text) throws SQLException;
	public void deletePage(String page) throws SQLException;
	
	public void insertPhrase(String page, int paragraph, int wordStart, String text) throws SQLException;
	public void appendPhrase(String page, String text) throws SQLException;
	public void deletePhrase(String page, int paragraph, int wordStart, int wordEnd) throws SQLException;
	public void copyPhrase(String sourcePage, int sourceParagraph, int sourceWordStart, int sourceWordEnd,
			String targetPage, int targetParagrah, int targetWordStart) throws SQLException;
	
}
