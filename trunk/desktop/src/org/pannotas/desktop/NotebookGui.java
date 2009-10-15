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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.*;
import javax.swing.tree.DefaultMutableTreeNode;

import org.pannotas.RepositorySqlite;

class TitleNode {
	public String displayedTitle;
	public String page;
	public TitleNode(String displayedTitle, String page) {
		this.displayedTitle = displayedTitle;
		this.page = page;
	}
	public String toString() {
		return displayedTitle;
	}
}

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
	private DefaultMutableTreeNode notesRoot;
	private TitleNode activeNote; 
	
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
		
		//All repositories should have a main page called notebook
		if (rep.readPage("Notebook")==null) {
			rep.writePage("Notebook", "Welcome to PanNotas!\n");
		}
		
		notesRoot = new DefaultMutableTreeNode(new TitleNode("Notebook","Notebook")); 
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
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        
		jToolBar1 = new JToolBar();
		getContentPane().add(jToolBar1, BorderLayout.NORTH);
		jButton1 = new JButton();
		jToolBar1.add(jButton1);
		jButton1.setText("jButton1");
					
		jSplitPane1 = new JSplitPane();
		getContentPane().add(jSplitPane1, BorderLayout.CENTER);
		jSplitPane1.setDividerLocation(200);
		notesTree = new JTree(notesRoot);
		jSplitPane1.add(notesTree, JSplitPane.LEFT);
		notesTabs = new JTabbedPane();
		jSplitPane1.add(notesTabs, JSplitPane.RIGHT);
		noteScrollEdit = new JScrollPane();
		notesTabs.addTab("Note", null, noteScrollEdit, null);
		noteEditor = new JTextPane();
		noteScrollEdit.setViewportView(noteEditor);
		jMenuBar1 = new JMenuBar();
		setJMenuBar(jMenuBar1);
		jMenu1 = new JMenu();
		jMenuBar1.add(jMenu1);
		jMenu1.setText("jMenu1");
		pack();
		this.setSize(600, 400);
		//noteEditor.add
		//setNote((TitleNode)notesRoot.getUserObject());
	}

	private void setNote(TitleNode node) {
		activeNote = node;
		noteEditor.setText(rep.readPage(activeNote.page));
		notesTabs.setTitleAt(0, activeNote.displayedTitle);
	}
}
