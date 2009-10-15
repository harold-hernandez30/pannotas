package org.pannotas.desktop;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.*;

import org.pannotas.*;

public class PNTextPane extends JTextPane implements DocumentListener, MouseListener{
	private JLabel statusText;
	private RepositoryInterface repository;
	private PNStyledDocument  doc;
	private String repositoryPage;
	private PhraseLocation repositoryPhrase;
	private ArrayList<PNLinkListener> linkListenersMap;
	
	public PNTextPane(RepositoryInterface repository) {
		super();
		this.repository = repository;
		doc = new PNStyledDocument();
		this.setDocument(doc);
		doc.addDocumentListener(this);
		this.addMouseListener(this);
		linkListenersMap = new ArrayList<PNLinkListener>();
	}

	public void addLinkListener(PNLinkListener listener) {
		linkListenersMap.add(listener);
	}
	public void removeLinkListener(PNLinkListener listener) {
		linkListenersMap.remove(listener);
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		displayEditInfo(arg0);		
		
		//TODO: Do real processing of the changes
		repository.writePage(repositoryPage, getText());
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		displayEditInfo(arg0);		

		repository.writePage(repositoryPage, getText());
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		displayEditInfo(arg0);	

		repository.writePage(repositoryPage, getText());
	}

    private void displayEditInfo(DocumentEvent e) {
    	if (statusText == null) return;
    	
        Document document = e.getDocument();
        int changeLength = e.getLength();
        statusText.setText(e.getType().toString() + ": " + changeLength + " character" +
            ((changeLength == 1) ? " " : "s ") + "at " + e.getOffset() + "." + " Text length = " + document.getLength() + "." );
    }

	public void setStatusText(JLabel statusText) {
		this.statusText = statusText;
	}

	public JLabel getStatusText() {
		return statusText;
	}

	public void setRepository(RepositoryInterface repository) {
		this.repository = repository;
	}

	public RepositoryInterface getRepository() {
		return repository;
	}

	public void setRepositoryPage(String repositoryPage) {
		this.repositoryPage = repositoryPage;
		setText(repository.readPage(repositoryPage));
	}

	public String getRepositoryPage() {
		return repositoryPage;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		Point pt = new Point(e.getX(), e.getY());
	    int pos = viewToModel(pt);
	    if (pos >= 0) {
	        Element elem = doc.getCharacterElement(pos);
	        Object link = elem.getAttributes().getAttribute(HTML.Attribute.HREF);
	    	if (link != null) {
	    		for (int i=0; i<linkListenersMap.size(); i++) {
	    			linkListenersMap.get(i).clickedPage(link.toString());
	    		}	    		
	    	}
	    }
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
