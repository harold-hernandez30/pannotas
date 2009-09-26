import java.sql.*;
import org.junit.*;
import org.junit.Assert.*;

public class TestSqlJdbc {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/*
	@Test
	public void test_Hsqldb() throws Exception {
		StringBuffer buf = new StringBuffer();
		
		for (int i=0; i<10; i++) buf.append("The brown fox is jumping over the slow dog. ");
		
		Class.forName("org.hsqldb.jdbcDriver");
		Class.forName("org.sqlite.JDBC");

		Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "sa", "");
		Statement st = conn.createStatement();
		
		st.execute("drop table tbl1 if exists;");
		st.execute("create cached table tbl1 (page varchar(1024), data LONGVARCHAR(1000000));");		
		
		for (int i=0; i<10; i++) {
			st.execute("insert into tbl1 values('p" + String.valueOf(i) + "','" + buf + "'); "); 
		}

		for (int k=0; k<1; k++) {
			for (int i=0; i<10; i++) {
				st.execute("update tbl1 set data='" + buf + "' where page='p" + String.valueOf(i) + "'; "); 
			}
		}
		//st.execute("CHECKPOINT DEFRAG");
		
		st.execute("SHUTDOWN");
		conn.close();
	}
	*/
	
	@Test
	public void test_Sqlite() throws Exception {
		StringBuffer buf = new StringBuffer();
		
		for (int i=0; i<10; i++) buf.append("The brown fox is jumping over the slow dog. ");
		
		Class.forName("org.sqlite.JDBC");

		Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		Statement st = conn.createStatement();

	    st.execute("PRAGMA page_size=16384");
	    st.execute("PRAGMA journal_mode=OFF");
	    st.execute("PRAGMA synchronous = OFF");
	    st.execute("PRAGMA temp_store = MEMORY");
	    st.execute("PRAGMA cache_size = 10000"); 		
		
		st.execute("drop table IF exists tbl1");
		st.execute("create table tbl1 (page varchar(1024), data LONGVARCHAR(1000000));");		
		
		for (int i=0; i<10; i++) {
			st.execute("insert into tbl1 values('p" + String.valueOf(i) + "','" + buf + "'); "); 
		}

		for (int k=0; k<1; k++) {
			for (int i=0; i<10; i++) {
				st.execute("update tbl1 set data='" + buf + "' where page='p" + String.valueOf(i) + "'; "); 
			}
		}
		//st.execute("CHECKPOINT DEFRAG");
		
		conn.close();
	}

}
