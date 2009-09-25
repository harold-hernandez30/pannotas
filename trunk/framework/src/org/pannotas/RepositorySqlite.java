package org.pannotas;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;
import java.util.regex.*;

public class RepositorySqlite implements RepositoryInterface {
	private Connection connection;
	private Statement st;
	private long lastUpdateTime = 0;
	
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
	public void appendPhrase(String page, String text) throws SQLException{
		long t = getNewUpdateTime();
		// TODO Network sync

		String s = readPage(page);		
		if (s == null) sqlSetPage(page, text, t);
		else sqlSetPage(page, s + text, t);
		
	}

	@Override
	public void bindPage(String page, ParagraphBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindParagrah(String page, int paragraph, ParagraphBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindRepository(RepositoryBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearRepository() throws SQLException{
		st.execute("delete from pages");
		st.execute("delete from objects");
	}

	@Override
	public void copyPhrase(String sourcePage, int sourceParagraph,int sourceStart, int length, 
			String targetPage, int targetParagraph, int targetStart) throws SQLException {
		long t = getNewUpdateTime();
		// TODO Network sync

		String sourceString = readPage(sourcePage);
		if (sourceString == null) return;		
		int sourceLoc = getParagraphStart(sourceString, sourceParagraph);
		if (sourceLoc == -1) return; 
		sourceLoc = sourceLoc + sourceStart;
		if (sourceLoc >= sourceString.length() ) return;
		String text = sourceString.substring(sourceLoc, sourceLoc + length);
		
		StringBuffer targetString = new StringBuffer(readPage(targetPage));
		if (targetString == null) return;		
		int targetLoc = getParagraphStart(targetString, targetParagraph);
		if (targetLoc == -1) return; 
		if (targetLoc + targetStart >= targetString.length()) {
			targetString.append(text);
		}
		else {
			targetString.insert(targetLoc + targetStart, text);
		}
		
		sqlSetPage(targetPage, targetString.toString(), t);

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
	public void deletePage(String page) throws SQLException{
		// TODO Network sync

		PreparedStatement prep = connection.prepareStatement("delete from pages where page=?");
		prep.setString(1, page);
		prep.execute();
	}

	@Override
	public void deletePhrase(String page, int paragraph, int start,	int length) throws SQLException {
		long t = getNewUpdateTime();
		// TODO Network sync

		StringBuffer s = new StringBuffer(readPage(page));
		int loc = getParagraphStart(s, paragraph);
		
		if (loc == -1) return;
		loc = loc + start;
		if (loc >= s.length()) return;
		s.delete(loc, loc + length);
		
		sqlSetPage(page, s.toString(), t);		
	}

	@Override
	public String[] getAllPageTitles() throws SQLException{
		st.execute("select page from pages");
		ResultSet rs = st.getResultSet();
		if (rs != null ) {
			java.util.ArrayList<String> p = new java.util.ArrayList<String>();
			while (rs.next()) {
				p.add(rs.getString(1));
			}
			return p.toArray(new String[0]);
		}
		
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
	public PageInfo getPageInfo(String page) throws SQLException{
		PreparedStatement prep = connection.prepareStatement("select page, data, created, updated, size from pages " +
				"where page=?");
		prep.setString(1, page);
		prep.execute();
		ResultSet rs = prep.getResultSet();
		if (rs == null)	return null;
		else if (rs.next()==false) return null;
		else {
			PageInfo info = new PageInfo();
			info.page = rs.getString(1);
			info.text = rs.getString(2);
			info.created = rs.getLong(3);
			info.updated = rs.getLong(4);
			info.size = rs.getInt(5);
			return info;
		}
	}

	@Override
	public void insertPhrase(String page, int paragraph, int start,	String text) throws SQLException {
		long t = getNewUpdateTime();
		// TODO Network sync

		StringBuffer s = new StringBuffer(readPage(page));
		int loc = getParagraphStart(s, paragraph);
		
		if (loc == -1) 	s.append(text);
		else s.insert(loc + start, text);
		
		sqlSetPage(page, s.toString(), t);
	}

	@Override
	public String readPage(String page) throws SQLException{
				
		return sqlGetPage(page);
	}

	@Override
	public void releasePageBind(String page, ParagraphBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void releaseParagraphBind(String page, int paragraph, ParagraphBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void releaseRepositoryBind(RepositoryBind bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writePage(String page, String text) throws SQLException{
		long t = getNewUpdateTime();
		//TODO Networking sync
		
		sqlSetPage(page, text, t);
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

	protected boolean sqlPageExists(String page) throws SQLException {
		st.execute("select * from pages where page='" + page + "'");
		ResultSet rs = st.getResultSet();
		if ( rs.next() ) return true;
		else return false;		
	}
	
	protected void sqlSetPage(String page, String text, long time) throws SQLException {
		if ( sqlPageExists(page) ) {
			PreparedStatement prep = connection.prepareStatement("update pages set data=?, updated=?, size=?" +
					" where page=?");
			prep.setString(1,text);
			prep.setLong(2, time);
			prep.setInt(3, text.length());
			prep.setString(4,page);
			prep.execute();
		}
		else {
			PreparedStatement prep = connection.prepareStatement("insert into pages values(?,?,?,?,?)");
			prep.setString(1,page);
			prep.setString(2,text);
			prep.setLong(3, time);
			prep.setLong(4, time);
			prep.setInt(5, text.length());
			prep.execute();
		}
	}

	protected String sqlGetPage(String page) throws SQLException {
		st.execute("select data from pages where page='" + page + "'");
		ResultSet rs = st.getResultSet();
		if ( rs.next() ) {
			return rs.getString(1);
		}
		return null;
	}

	protected long getNewUpdateTime() {
		Date d = new Date();
		long t = d.getTime();
		
		if (lastUpdateTime >= t) {
			t = lastUpdateTime + 1;
		}
		lastUpdateTime = t;
		return t;
	}
	
	public static int getParagraphStart(String text, int paragraph) {
		if (paragraph == 0) return 0;
		int loc = 0;
		for (int i=0; i<paragraph; i++) {
			loc = text.indexOf("\n",loc);
			if (loc == -1) return -1;
			loc = loc + 1;
		}
		return loc;
	}

	public static int getParagraphStart(StringBuffer text, int paragraph) {
		if (paragraph == 0) return 0;
		int loc = 0;
		for (int i=0; i<paragraph; i++) {
			loc = text.indexOf("\n",loc);
			if (loc == -1) return -1;
			loc = loc + 1;
		}
		return loc;
	}

	public static int getParagraphCount(String text, int end) {
		if (end == 0) return 0;
		int loc = 0;
		int paragraph = 0;
		while (loc < end) {
			loc = text.indexOf("\n",loc);
			if (loc == -1 || loc >= end) return paragraph;
			paragraph = paragraph + 1;
			loc = loc + 1;
		}
		return paragraph;
	}

	@Override
	public void changePhrase(String page, int paragraph, int start, int length, String text) throws SQLException {
		long t = getNewUpdateTime();
		// TODO Network sync

		StringBuffer s = new StringBuffer(readPage(page));
		if (s == null) return;
		int loc = getParagraphStart(s, paragraph);
		if (loc == -1) return;
		loc = loc + start;
		if (loc >= s.length() ) return;
		s.delete(loc, loc + length);
		s.insert(loc, text);
		
		sqlSetPage(page, s.toString(), t);
		
	}


	@Override
	public PhraseLocation[] findAll(Pattern search) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PhraseLocation[] findAll(Pattern search, String page) throws SQLException {
		ArrayList<PhraseLocation> results = new ArrayList<PhraseLocation>();
		
		String p = readPage(page);
		if (p == null) return null;
		Matcher match = search.matcher(p);
		while (match.find()) {
			PhraseLocation loc = new PhraseLocation();
			loc.page = page;
			loc.paragraph = getParagraphCount(p, match.start());
			loc.start = match.start() - getParagraphStart(p, loc.paragraph);
			loc.length = match.end() - match.start();
			results.add(loc);
		}
		
		return results.toArray(new PhraseLocation[0]);
	}

}
