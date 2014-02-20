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
package se.openflisp.gui.perspectives;

import bibliothek.gui.DockTheme;
import bibliothek.gui.dock.DefaultDockable;

/**	
 * Create the sequential logic simulation perspective
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
public class SlsPerspective extends Perspective {
	/**
	 * Creates the Sequential logic simulation perspective
	 * @param theme		the theme to use with this perspective
	 */
	public SlsPerspective (DockTheme theme) {
		super(theme);
		
		// Set identifier
		this.identifier = "Sequential Logical simulation";
		
		//Set grid
		dockGrid.addDockable(0, 0, 2, 1, new DefaultDockable("Komponenter"));
		dockGrid.addDockable(0, 0, 1 ,1, new DefaultDockable("Kopplingsarea"));
		station.dropTree( dockGrid.toTree());
	}
}
