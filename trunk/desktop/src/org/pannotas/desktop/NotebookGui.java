package org.pannotas.desktop;
import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import org.pannotas.RepositorySqlite;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class NotebookGui extends javax.swing.JDialog {
	private JMenuBar jMenuBar1;
	private JButton jButton1;
	private JTextPane noteEditor;
	private JScrollPane noteScrollEdit;
	private JTabbedPane notesTabs;
	private JTree notesTree;
	private JSplitPane jSplitPane1;
	private JToolBar jToolBar1;
	private JMenu jMenu1;

	private RepositorySqlite rep;
	
	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				NotebookGui inst = new NotebookGui(frame);
				inst.setVisible(true);
			}
		});
	}
	
	private void initRepository() {
		rep = new RepositorySqlite();
		rep.open("pannotas.db");
		if (rep.readPage("Notebook")==null) {
			rep.writePage("Notebook", "Welcome to PanNotas!\n");
		}
	}
	
	
	public NotebookGui(JFrame frame) {
		super(frame);
		initRepository();
		initGUI();
	}
	
	private void initGUI() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	rep.close();
            	System.exit(0);
            }
        });

		
		try {
			{
				jToolBar1 = new JToolBar();
				getContentPane().add(jToolBar1, BorderLayout.NORTH);
				{
					jButton1 = new JButton();
					jToolBar1.add(jButton1);
					jButton1.setText("jButton1");
				}
			}
			{
				jSplitPane1 = new JSplitPane();
				getContentPane().add(jSplitPane1, BorderLayout.CENTER);
				jSplitPane1.setDividerLocation(200);
				{
					notesTree = new JTree();
					jSplitPane1.add(notesTree, JSplitPane.LEFT);
				}
				{
					notesTabs = new JTabbedPane();
					jSplitPane1.add(notesTabs, JSplitPane.RIGHT);
					{
						noteScrollEdit = new JScrollPane();
						notesTabs.addTab("Note", null, noteScrollEdit, null);
						{
							noteEditor = new JTextPane();
							noteScrollEdit.setViewportView(noteEditor);
						}
					}
				}
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("jMenu1");
				}
			}
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
