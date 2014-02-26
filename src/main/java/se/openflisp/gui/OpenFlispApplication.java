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

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import se.openflisp.gui.swing.OpenFlispFrame;

/**	
 * The main application for OpenFlisp
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
public class OpenFlispApplication {
	
	/**
	 * The main OpenFlispFrame
	 */
	private OpenFlispFrame frame;
	
	/**
	 * Instantiate the OpenFlispFrame, 
	 * make it visible and center it on screen
	 */
	public OpenFlispApplication() {
		this.frame = new OpenFlispFrame();
		this.frame.setVisible(true);
		this.frame.setLocationRelativeTo(null);
	}
	
	/**
	 * Initiate the frame, add a frame icon and load settings
	 */
	public void initialize() {
		
		// Add frame icon
		try {
			URL url;
			
			url = OpenFlispApplication.class.getClassLoader().getResource("Open_Flisp_icon.png");
			System.out.println(url);
			Image icon = ImageIO.read(url);
			frame.setIconImage(icon);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		// Load settings
		// TODO Load settings
	}

	/**
	 * {@inheritDoc}
	 */
	public static void main(String[] args) {
		
		// Make swing thread
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				OpenFlispApplication application = new OpenFlispApplication();
				application.initialize();
			}
		});
	}

}
