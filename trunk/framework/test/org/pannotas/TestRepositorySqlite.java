package org.pannotas;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRepositorySqlite {
	private static RepositorySqlite rep;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rep = new RepositorySqlite();	
		rep.open("pannotas.db");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		rep.close();
		rep = null;
	}

	@Test
	public void testAddClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddClientConnection() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendPhrase() {
		fail("Not yet implemented");
	}

	@Test
	public void testBindPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testBindPhrase() {
		fail("Not yet implemented");
	}

	@Test
	public void testBindRepository() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearRepository() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopyPhrase() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteClientConnection() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeletePage() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeletePhrase() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllPageTitles() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClientList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetConnectionParams() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPageInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertPhrase() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testReleasePageBind() {
		fail("Not yet implemented");
	}

	@Test
	public void testReleasePhraseBind() {
		fail("Not yet implemented");
	}

	@Test
	public void testReleaseRepositoryBind() {
		fail("Not yet implemented");
	}

	@Test
	public void testWritePage() {
		fail("Not yet implemented");
	}

	@Test
	public void testOpenAndClose() throws Exception{
		RepositorySqlite r = new RepositorySqlite();
		r.open("PanNotas.db");
		r.close();
	}

}
