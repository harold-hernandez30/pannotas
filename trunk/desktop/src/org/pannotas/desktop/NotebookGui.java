package org.pannotas.desktop;
import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.JButton;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

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

public class NotebookGui extends javax.swing.JDialog implements PNLinkListener {
	private JMenuBar jMenuBar1;
	private JButton jButton1;
	private PNTextPane noteEditor;
	private JScrollPane noteScrollEdit;
	private JTabbedPane notesTabs;
	private JTree notesTree;
	private JSplitPane jSplitPane1;
	private JToolBar jToolBar1;
	private JMenu jMenu1;
	private JPanel statusBar;
	private JLabel statusText;

	private RepositorySqlite rep;
	private DefaultMutableTreeNode nodeRoot;
	private DefaultMutableTreeNode nodeCurrent;
	private DefaultTreeModel treeModel;
	private TitleNode activeNote; 
	
	public static void main(String[] args) {
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				NotebookGui inst = new NotebookGui(frame);
				inst.setVisible(true);
			}
		});
		*/
		JFrame frame = new JFrame();
		NotebookGui inst = new NotebookGui(frame);
		inst.setVisible(true);
	}
	
	private void initRepository() {
		rep = new RepositorySqlite();
		rep.open("pannotas.db");
		
		//All repositories should have a main page called notebook
		if (rep.readPage("Notebook")==null) {
			rep.writePage("Notebook", "Welcome to PanNotas!\n");
		}
		
		nodeRoot = new DefaultMutableTreeNode(new TitleNode("Notebook","Notebook")); 
		nodeCurrent = nodeRoot;
		rescanNode(nodeCurrent);
	}
		
	private void rescanNode(DefaultMutableTreeNode node) {
		node.removeAllChildren();
		String page = ((TitleNode)nodeCurrent.getUserObject()).page;
		
		String[] children = rep.getPageChildren(page);
		for (int i=0; i<children.length; i++) {
			TitleNode newChild = new TitleNode(children[i],children[i]);
			node.add(new DefaultMutableTreeNode(newChild));
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
		
		treeModel = new DefaultTreeModel(nodeRoot);
		notesTree = new JTree(treeModel);
		
		jSplitPane1.add(notesTree, JSplitPane.LEFT);
		notesTabs = new JTabbedPane();
		jSplitPane1.add(notesTabs, JSplitPane.RIGHT);
		noteScrollEdit = new JScrollPane();
		notesTabs.addTab("Note", null, noteScrollEdit, null);
		jMenuBar1 = new JMenuBar();
		setJMenuBar(jMenuBar1);
		jMenu1 = new JMenu();
		jMenuBar1.add(jMenu1);
		jMenu1.setText("jMenu1");
		
		statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusText = new JLabel("Status");
		statusBar.add(statusText, BorderLayout.CENTER);
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));

		noteEditor = new PNTextPane(rep);
		noteEditor.setStatusText(statusText);
		noteScrollEdit.setViewportView(noteEditor);
		noteEditor.addLinkListener(this);
		
		getContentPane().add(statusBar,BorderLayout.SOUTH);
		pack();
		this.setSize(700, 500);
		//noteEditor.add
		setNote((TitleNode)nodeRoot.getUserObject());		
	}

	private void setNote(TitleNode node) {
		activeNote = node;
		noteEditor.setRepositoryPage(activeNote.page);
		notesTabs.setTitleAt(0,activeNote.displayedTitle);
	}

	@Override
	public void clickedHttp(String http) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedPage(String page) {
		// TODO Auto-generated method stub
		statusText.setText("Clicked on page "+page);

		if (rep.isPage(page)==false) {
			rep.writePage(page, "");
		}
		
		rescanNode(nodeCurrent);
		for (int i=0; i<nodeCurrent.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodeCurrent.getChildAt(i); 
			TitleNode tn = (TitleNode) node.getUserObject();
			if (tn.page.equals(page)) {
				nodeCurrent = node;
				rescanNode(nodeCurrent);
				break;
			}
		}
		treeModel.reload();
		notesTree.expandPath(new TreePath(nodeCurrent.getPath()));
		notesTree.repaint();
		setNote(new TitleNode(page,page));
	}
}
