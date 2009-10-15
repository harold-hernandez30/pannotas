package org.pannotas.desktop;

import javax.swing.JTextPane;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.*;

import org.pannotas.RepositoryInterface;

public class PNTextPane extends JTextPane implements DocumentListener {
	private JLabel statusText;
	private RepositoryInterface repository;
	private StyledDocument  doc;
	
	public PNTextPane(RepositoryInterface repository) {
		super();
		this.repository = repository;
		doc = this.getStyledDocument();
		doc.addDocumentListener(this);
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		displayEditInfo(arg0);		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		displayEditInfo(arg0);		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		displayEditInfo(arg0);	
	}

    private void displayEditInfo(DocumentEvent e) {
        Document document = e.getDocument();
        int changeLength = e.getLength();
        getStatusText().setText(e.getType().toString() + ": " + changeLength + " character" +
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
}
