package org.pannotas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;
import java.util.regex.*;

import android.content.Context;
import android.util.Log;


/**
 * @author ciprian
 *
 */
public class RepositorySqlite implements RepositoryInterface {
	final static int PLATFORM_ANDROID = 1;
	final static int PLATFORM_PC = 2;
	final static String DATABASE_NAME = "pannotasdb";
	
	private int platform = 0; 
	private java.sql.Connection jdbcConnection;
	private android.database.sqlite.SQLiteDatabase androidConnection;

	private long lastUpdateTime = 0;
	
	public RepositorySqlite() {
		//Check for android
		if (System.getProperty("java.vm.name").equals("Dalvik")) {		
		    try {
		       Class.forName("android.database.sqlite.SQLiteDatabase", false, null);
		       platform = PLATFORM_ANDROID;
		    }
		    catch (ClassNotFoundException e) {}
		}
		
		//check for PC (JDBC has to be present)
		if (platform ==0) {
		    try {
		    		Class.forName("org.sqlite.JDBC");
		    		platform = PLATFORM_PC;
			    }
			    catch (ClassNotFoundException e) {}
		}
		
		if (platform == 0 ) {
			throw new RuntimeException("Cannot find either the ANDROID nor the JDBC sqlite.");
		}
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
		long t = getNewUpdateTime();
		// TODO Network sync

		String s = readPage(page);		
		if (s == null) sqlSetPage(page, text, t);
		else sqlSetPage(page, s + text, t);
		
	}

	@Override
	public void addPageListener(String page, ParagraphListener bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addParagrahListener(String page, int paragraph, ParagraphListener bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRepositoryListener(RepositoryListener bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearRepository() {
		sqlExecute("delete from pages");
		sqlExecute("delete from objects");
	}

	@Override
	public void copyPhrase(String sourcePage, int sourceParagraph,int sourceStart, int length, 
			String targetPage, int targetParagraph, int targetStart) {
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
	public void deletePage(String page) {
		// TODO Network sync

		switch (platform) {
		case PLATFORM_ANDROID:
			androidConnection.execSQL("delete from pages where page=?", new String[] {page});
			break;
		case PLATFORM_PC:
			try {
				java.sql.PreparedStatement prep = jdbcConnection.prepareStatement("delete from pages where page=?");
				prep.setString(1, page);
				prep.execute();
			} catch (SQLException e) {throw new RuntimeException(e); }
			break;			
		}
		
			
	}

	@Override
	public void deletePhrase(String page, int paragraph, int start,	int length) {
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
	public String[] getAllPageTitles() {
		java.util.ArrayList<String> p;
		
		switch (platform) {
		case PLATFORM_ANDROID:
			android.database.Cursor cur = androidConnection.query("pages",new String[] {"page"},null,null,null,null,null);
			if (cur.getCount() < 1) return null;
			p = new java.util.ArrayList<String>();
			while (cur.moveToNext()) {
				p.add(cur.getString(0));
			}
			return p.toArray(new String[0]);
			
		case PLATFORM_PC:
			try {
				java.sql.Statement st = jdbcConnection.createStatement();
				st.execute("select page from pages");
				java.sql.ResultSet rs = st.getResultSet();
				if (rs != null ) {
					p = new java.util.ArrayList<String>();
					while (rs.next()) {
						p.add(rs.getString(1));
					}
					return p.toArray(new String[0]);
				}
			} catch (SQLException e) {throw new RuntimeException(e); }
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
	public PageInfo getPageInfo(String page) {
		switch (platform) {
		case PLATFORM_ANDROID:
			android.database.Cursor cur = androidConnection.query("pages",new String[] {"page","data","created","updated","size"},
				"page=?", new String[] {page}, null, null, null);
				if (cur.getCount() < 1) return null;
				cur.moveToFirst();
				return new PageInfo(cur.getString(0), cur.getString(1),cur.getLong(2),cur.getLong(3),cur.getInt(4));			
		case PLATFORM_PC:
			try {
				java.sql.PreparedStatement prep = jdbcConnection.prepareStatement("select page, data, created, updated, size from pages " +
						"where page=?");
				prep.setString(1, page);
				prep.execute();
				java.sql.ResultSet rs = prep.getResultSet();
				if (rs == null) 	return null;
				else if (rs.next()==false) return null;
				else {
					return new PageInfo(rs.getString(1), rs.getString(2),rs.getLong(3),rs.getLong(4),rs.getInt(5));
				}
			} catch (SQLException e) {throw new RuntimeException(e); }
		}
		return null;
	}

	@Override
	public void insertPhrase(String page, int paragraph, int start,	String text) {
		long t = getNewUpdateTime();
		// TODO Network sync

		StringBuffer s = new StringBuffer(readPage(page));
		int loc = getParagraphStart(s, paragraph);
		
		if (loc == -1) 	s.append(text);
		else s.insert(loc + start, text);
		
		sqlSetPage(page, s.toString(), t);
	}

	@Override
	public String readPage(String page) {
				
		return sqlGetPage(page);
	}

	@Override
	public void removePageListener(String page, ParagraphListener bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeParagraphListener(String page, int paragraph, ParagraphListener bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRepositoryListener(RepositoryListener bind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writePage(String page, String text) {
		long t = getNewUpdateTime();
		//TODO Networking sync
		
		sqlSetPage(page, text, t);
	}


	@Override
	public void close() {
		switch (platform) {
		case PLATFORM_ANDROID:
			androidConnection.close();
			androidConnection = null;
			break;
		case PLATFORM_PC:
			try {
				jdbcConnection.close();
				jdbcConnection = null;
			} catch (SQLException e) {throw new RuntimeException(e); }
			break;			
		}
				
	}


	@Override
	public void open(String dbName) {
		if (dbName == null) {
			dbName = DATABASE_NAME;
		}
		
		switch (platform) {
		case PLATFORM_ANDROID:			
			androidConnection = android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(dbName, null);
			break;
		case PLATFORM_PC:
			try {
				jdbcConnection = java.sql.DriverManager.getConnection("jdbc:sqlite:" + dbName);
			} catch (SQLException e) {throw new RuntimeException(e); }
			break;			
		}
			
	    sqlExecute("PRAGMA page_size=16384");
	    sqlExecute("PRAGMA journal_mode=OFF"); 
	    sqlExecute("PRAGMA synchronous = OFF");
	    sqlExecute("PRAGMA temp_store = MEMORY");
	    sqlExecute("PRAGMA cache_size = 10000"); 		
		
	    //create the tables and ignore the sql errors if they exist
	    try {
	    	sqlExecute("create table pages (page text, data text, created integer, updated integer, size integer)");
	    	sqlExecute("create table objects (object text, data blob, created integer, updated integer, size integer)");
	    }
	    catch (Throwable e) {}
		
	}

	public void openAndroid(android.content.Context context, String dbName) {
		if (dbName == null) {
			dbName = DATABASE_NAME;
		}
		androidConnection = context.openOrCreateDatabase(dbName, 0, null);

	    sqlExecute("PRAGMA page_size=16384");
	    //sqlExecute("PRAGMA journal_mode=OFF"); does not seem to work on Android
	    sqlExecute("PRAGMA synchronous = OFF");
	    sqlExecute("PRAGMA temp_store = MEMORY");
	    sqlExecute("PRAGMA cache_size = 10000"); 		
		
	    //create the tables and ignore the sql errors if they exist
	    try {
	    	sqlExecute("create table pages (page text, data text, created integer, updated integer, size integer)");
	    	sqlExecute("create table objects (object text, data blob, created integer, updated integer, size integer)");
	    }
	    catch (Throwable e) {}
	}
	
	protected boolean sqlPageExists(String page) {
		switch (platform) {
		case PLATFORM_ANDROID:
			try {
				android.database.Cursor cur = androidConnection.query("pages",new String[] {"page"},"page=?",new String[]{page},null,null,null);
				if (cur.getCount() < 1) return false;			
				//pages should always be unique
				assert cur.getCount() == 1;
			}
			catch (Throwable e) { 
					e.printStackTrace();
					System.out.println(e.toString());
			}
			return true;
		case PLATFORM_PC:
			try {
				java.sql.PreparedStatement prep = jdbcConnection.prepareStatement("select page from pages where page=?");
				prep.setString(1, page);
				prep.execute();
				java.sql.ResultSet rs = prep.getResultSet();
				if ( rs.next() ) return true;
				return false;
			} catch (SQLException e) {throw new RuntimeException(e); }
		}
		return false;
	}
	
	protected void sqlSetPage(String page, String text, long time) {
		switch (platform) {
		case PLATFORM_ANDROID:
			if ( sqlPageExists(page) ) {
				androidConnection.execSQL("update pages set data=?, updated=?, size=? where page=?", 
						new Object[] {text,time,text.length(),page});
			}
			else {
				androidConnection.execSQL("insert into pages values(?,?,?,?,?)", 
						new Object[] {page,text,time,time,text.length()});				
			}
			break;
		case PLATFORM_PC:
			try {
				if ( sqlPageExists(page) ) {
					java.sql.PreparedStatement prep = jdbcConnection.prepareStatement("update pages set data=?, updated=?, size=?" +
							" where page=?");
					prep.setString(1,text);
					prep.setLong(2, time);
					prep.setInt(3, text.length());
					prep.setString(4,page);
					prep.execute();
				}
				else {
					java.sql.PreparedStatement prep = jdbcConnection.prepareStatement("insert into pages values(?,?,?,?,?)");
					prep.setString(1,page);
					prep.setString(2,text);
					prep.setLong(3, time);
					prep.setLong(4, time);
					prep.setInt(5, text.length());
					prep.execute();
				}
			} catch (SQLException e) {throw new RuntimeException(e); }
			break;
		}
				
	}

	protected String sqlGetPage(String page) {
		switch (platform) {
		case PLATFORM_ANDROID:
			android.database.Cursor cur = androidConnection.query("pages",new String[] {"data"},"page=?",new String[]{page},null,null,null);
			if (cur.getCount() < 1) return null;			
			//pages should always be unique
			assert(cur.getCount() == 1);
			cur.moveToFirst();
			return cur.getString(0);
		case PLATFORM_PC:
			try {
				java.sql.PreparedStatement prep = jdbcConnection.prepareStatement("select data from pages where page=?");
				prep.setString(1, page);
				prep.execute();
				java.sql.ResultSet rs = prep.getResultSet();
				if ( rs.next() ) {
					return rs.getString(1);
				}
				return null;
			} catch (SQLException e) {throw new RuntimeException(e); }
		}
		return null;
	}

	
	/**
	 * It will execute an SQL statement
	 * @param sql statement to execute 
	 */
	protected void sqlExecute(String sql) {
		switch (platform) {
		case PLATFORM_ANDROID:
			androidConnection.execSQL(sql);
			break;
		case PLATFORM_PC:
			try {
				java.sql.Statement st = jdbcConnection.createStatement();
				st.execute(sql);
			} catch (SQLException e) {throw new RuntimeException(e); }
			break;			
		}
		
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
	public void changePhrase(String page, int paragraph, int start, int length, String text) {
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
	public PhraseLocation[] findAll(Pattern search, String page) {
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


	@Override
	public void addResourceListener(String resource, ResourceListener bind) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteResource(String resource) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public PhraseLocation findNext(Pattern search, PhraseLocation lastFind) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PhraseLocation findNext(Pattern search, String page,
			PhraseLocation lastFind) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String[] getAllResourceNames() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String[] getAllSystemPageTitles() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public byte[] readResource(String resource) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String readResourceType(String resource) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void removeResourceListener(String resource, ResourceListener bind) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void writeResource(String resource, String mimeType, byte[] data) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isPage(String page) {
		if (readPage(page)==null) return false;
		return true;
	}


	@Override
	public String[] getPageChildren(String page) {
		ArrayList<String> results = new ArrayList<String>();
		Pattern search = Pattern.compile("\\[\\[(.+)\\]\\]"); 
		
		String p = readPage(page);
		if (p == null) return null;
		Matcher match = search.matcher(p);
		while (match.find()) {
			results.add(match.group(1));
		}
		
		return results.toArray(new String[0]);
	}

}
