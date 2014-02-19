package se.openflisp.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Creates the menubar and its items
 * 
 * @author Johan Strand <straaand@gmail.com>
 * @version 1.0
 *
 */

public class OpenFlispMenu implements ActionListener {
	JMenuBar menubar;
	JMenu menu2;
	JMenuItem menu_item1, menu_close, menu_about;
	OpenFlispFrame frame;
	
	/*
	 * Add the menu to the OpenFlispFrame
	 */
	public void addMenuToFrame(OpenFlispFrame frame)	{
		this.frame = frame;
		menubar = new JMenuBar();
		
		JMenu menu = new JMenu("Arkiv");	//the arkiv menu
		menubar.add(menu); 
		frame.setJMenuBar(menubar);
		
		/* First item in in "Arkiv" */
		menu_item1 = new JMenuItem("Logik", KeyEvent.VK_T); 
		menu_item1.addActionListener(this);
		menu_item1.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.ALT_MASK)); //open with alt-1
		menu.add(menu_item1);
		
		/* Close the application from the menu */
		menu.addSeparator();
		menu_close = new JMenuItem("Avsluta");
		menu_close.addActionListener(this);
		menu.add(menu_close);
		menu_close.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_2, ActionEvent.ALT_MASK)); //close with alt-2
		
		/* The second menu */
		menu2 = new JMenu("Hjälp");
		menu_about = new JMenuItem("Om OpenFlisp");
		menu2.add(menu_about);
		menu_about.addActionListener(this);
		menubar.add(menu2);
		
		
	}
	
	public void actionPerformed(ActionEvent e)	{
		//close the application
		if (e.getSource() == menu_close)	{
			System.exit(0);
		}
		//show the Help menu
		if (e.getSource() == menu_about)	{
			JOptionPane.showMessageDialog(null,"Detta är en prototyp. \nVersion: 2014-01-31\nAv: Johan & Fiona","OpenFlisp", JOptionPane.INFORMATION_MESSAGE);
		}
		//open the logic simulation
		if (e.getSource() == menu_item1)	{
			OpenFlispPerspectives view = new OpenFlispPerspectives();
			view.addComponentToPane(frame.getContentPane());
		}

	}

}
