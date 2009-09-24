package org.pannotas;

import java.sql.*;
import java.util.Properties;

public class RepositorySqlite implements RepositoryInterface {
	private Connection connection;
	private Statement st;
	
	public RepositorySqlite() {		
	}
	
	
	@Override
	public boolean addClient(String clientName, int clientType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int addClientConnection(String clientName, Properties params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void appendPhrase(String page, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindPage(String page, PhraseBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindPhrase(String page, int paragraph, int wordStart,
			int wordEnd, PhraseBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindRepository(RepositoryBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearRepository() {
		// TODO Auto-generated method stub

	}

	@Override
	public void copyPhrase(String sourcePage, int sourceParagraph,
			int sourceWordStart, int sourceWordEnd, String targetPage,
			int targetParagrah, int targetWordStart) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean deleteClient(String clientName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteClientConnection(String clientName, int connectionID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deletePage(String page) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletePhrase(String page, int paragraph, int wordStart,
			int wordEnd) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getAllPageTitles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getClientList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties[] getConnectionParams(String clientName, int connectionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageInfo getPageInfo(String page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertPhrase(String page, int paragraph, int wordStart,
			String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public String readPage(String page, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releasePageBind(String page, PhraseBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void releasePhraseBind(String page, int paragraph, int wordStart,
			int wordEnd, PhraseBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void releaseRepositoryBind(RepositoryBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writePage(String page, String text) {
		// TODO Auto-generated method stub

	}


	@Override
	public void close() throws SQLException{
		connection.close();
		st = null;
		connection = null;
	}


	@Override
	public void open(String dbName) throws Exception {
		Class.forName("org.sqlite.JDBC");
		
		connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		st = connection.createStatement();
	
	    st.execute("PRAGMA page_size=16384");
	    st.execute("PRAGMA journal_mode=OFF");
	    st.execute("PRAGMA synchronous = OFF");
	    st.execute("PRAGMA temp_store = MEMORY");
	    st.execute("PRAGMA cache_size = 10000"); 		
		
	    //if the tables does not exist then create it
		st.execute("select * from sqlite_master where type='table' and name='pages'");
		ResultSet rs = st.getResultSet();
		if (rs.next() == false) { 
			st.execute("create table pages (page text, data text, created integer, updated integer, size integer)");
		}
		rs.close();
		
	    //if the tables does not exist then create it
		st.execute("select * from sqlite_master where type='table' and name='objects'");
		rs = st.getResultSet();
		if (rs.next() == false) { 
			st.execute("create table objects (object text, data blob, created integer, updated integer, size integer)");
		}
		rs.close();					

	}

}
