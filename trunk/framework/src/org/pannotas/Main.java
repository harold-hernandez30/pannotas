package org.pannotas;

/**
 * @author ciprian
 *
 */
public class Main {
	RepositorySqlite rep = new RepositorySqlite();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.run();
	}
	
	public void run() throws Exception {
		System.out.println("Hello PanNotas!");
		rep.open("test.db");
		rep.close();
	}

}
