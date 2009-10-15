package org.pannotas.desktop;

import java.awt.Color;
import java.util.Stack;

import javax.swing.text.*;
import javax.swing.text.html.HTML;

public class PNStyledDocument extends DefaultStyledDocument {

	private void rescanText() {
		Stack<PanStyle> styles = new Stack<PanStyle>();
		try {
			String s = getText(0, getLength());
			
			SimpleAttributeSet attrPlain = new SimpleAttributeSet();
			attrPlain.addAttribute(StyleConstants.FontFamily, "Times New Roman");
			attrPlain.addAttribute(StyleConstants.FontSize, 14);
	
			SimpleAttributeSet attrBold = new SimpleAttributeSet();
			attrBold.addAttribute(StyleConstants.FontFamily, "Times New Roman");
			attrBold.addAttribute(StyleConstants.FontSize, 14);
			attrBold.addAttribute(StyleConstants.CharacterConstants.Bold, true);
			attrBold.addAttribute(StyleConstants.Foreground, new Color(0,125,25));
			
			SimpleAttributeSet attrLink = new SimpleAttributeSet();
			attrLink.addAttribute(StyleConstants.FontFamily, "Times New Roman");
			attrLink.addAttribute(StyleConstants.FontSize, 14);
			//attrLink.addAttribute(StyleConstants.CharacterConstants.Underline, true);
			attrLink.addAttribute(StyleConstants.Foreground, new Color(0,0,125));
			
			PanStyle current = new PanStyle();
			
			current.start = 0;
			current.style = PanStyle.PLAIN;
			int pos = 0;
			
			while (pos < s.length()-2) {
				if ((s.substring(pos, pos+2)).equals("++")) {
					if (current.style==PanStyle.BOLD) {
						setCharacterAttributes(current.start, pos-current.start+2, attrBold, true);
						current.style=PanStyle.PLAIN;
						current.start=pos+2;
					}
					else  {
						setCharacterAttributes(current.start, pos-current.start, attrPlain, true);
						current.style=PanStyle.BOLD;						
						current.start=pos;
					}
				}

				if ((s.substring(pos, pos+2)).equals("[[")) {
					setCharacterAttributes(current.start, pos-current.start, attrPlain, true);
					current.style=PanStyle.LINK;						
					current.start=pos;
				}
				if ((s.substring(pos, pos+2)).equals("]]")) {
					setCharacterAttributes(current.start, pos-current.start+2, attrLink, true);
					SimpleAttributeSet attrSpecificLink = new SimpleAttributeSet();
					attrSpecificLink.addAttribute(HTML.Attribute.HREF, s.substring(current.start+2,pos));
					setCharacterAttributes(current.start, pos-current.start+2, attrSpecificLink, false);
					current.style=PanStyle.PLAIN;
					current.start=pos+2;
				}
				
				pos++;
			}
			
			if (current.style==PanStyle.PLAIN) {
				setCharacterAttributes(current.start, pos-current.start, attrPlain, true);
			}
			if (current.style==PanStyle.BOLD) {
				setCharacterAttributes(current.start, pos-current.start, attrBold, true);
			}
			if (current.style==PanStyle.LINK) {
				setCharacterAttributes(current.start, pos-current.start, attrLink, true);
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		// TODO Auto-generated method stub
		super.insertString(offs, str, a);
		
		rescanText();
		
	}

}

class PanStyle {
	public int start;
	public int style;
	public final static int PLAIN=0;
	public final static int BOLD=1;
	public final static int LINK=2;
}