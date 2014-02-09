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
package se.openflisp.gui;

import se.openflisp.gui.swing.OpenFlispFrame;

/**	
 * The main application for OpenFlisp
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 * 
 */
public class OpenFlispApplication {
	/* Application frame and settings */
	private OpenFlispFrame frame;
	
	
	public OpenFlispApplication() {
		this.frame = null;	
	}
	
	public void initialize() {
		
		/* Create and show main frame */
		this.frame = new OpenFlispFrame();
		this.frame.setVisible(true);
		this.frame.setLocationRelativeTo(null);
		
		/* Add frame icon */
		// TODO	Frame icon
				
		/* Load settings */
		// TODO Settings
	}

	public static void main(String[] args) {
		OpenFlispApplication application = new OpenFlispApplication();
		application.initialize();
	}

}
