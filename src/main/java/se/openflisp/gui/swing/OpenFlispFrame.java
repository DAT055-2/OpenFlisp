/*
 * Copyright (C) 2014- See AUTHORS file.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.openflisp.gui.swing;


import java.awt.BorderLayout;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;

import se.openflisp.gui.swing.OpenFlispPerspectives;

/**	
 * The main frame for OpenFlisp, will handle different perspectives
 * and a menubar.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class OpenFlispFrame extends JFrame {
	
	private OpenFlispPerspectives perspectives;
	private OpenFlispMenu menu;
	
	/**
	 * Creates the main Frame, menubar and perspectives
	 */
	public OpenFlispFrame() {
		
		// Set program title
		this.setTitle("OpenFlisp");
		
		// Maximize window
		this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		
		// Set minimum size of window
		this.setMinimumSize(new Dimension(640,480));
		
		// Exit application when it is closed
		this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		
		// Add border Layout
		this.setLayout(new BorderLayout());
		
		//Initiate perspectives 
		this.perspectives = new OpenFlispPerspectives();
		
		// Add this contentpane to our view
		this.perspectives.addComponentToPane(this.getContentPane());
		
		// Add the menubar to this frame
		this.menu = new OpenFlispMenu();
		this.menu.addMenuToFrame(this);
		
		this.pack();
		this.setVisible(true);
	}
}	