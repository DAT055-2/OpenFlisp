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


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.Border;

import se.openflisp.gui.swing.OpenFlispPerspectives;

/**	
 * The main frame for OpenFlisp
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 * 
 */

public class OpenFlispFrame extends JFrame {
	private OpenFlispPerspectives perspectives;
	
	public OpenFlispFrame() {
		
		/* Some options */
		this.setTitle("OpenFlisp");
		
		//this.setIconImages(icons);
		this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(640,480));
		this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);

		/* Set Layout */
		OpenFlispPerspectives view = new OpenFlispPerspectives();
		view.addComponentToPane(this.getContentPane());
		
		this.pack();
		this.setVisible(true);
	}
	
	//Create a border to highlight active OpenFlispPerspective
	Border outline = BorderFactory.createLineBorder(Color.black);
	
}	