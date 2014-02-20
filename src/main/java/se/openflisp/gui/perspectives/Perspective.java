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

import bibliothek.gui.DockController;
import bibliothek.gui.DockTheme;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;

/**	
 * Model class for a perspective
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
public abstract class Perspective {
	protected DockController controller;
	protected SplitDockStation station;
	protected SplitDockGrid	dockGrid;
	protected DockTheme	theme;
	protected String	identifier;
	
	/**
	 * Create a new perspective given a theme, also creates a DockController
	 * and a DockStation
	 * @param theme		theme to use for this perspective
	 */
	public Perspective(DockTheme theme) {
		this.theme = theme;
		
		// Create Perspective
		controller = new DockController();
		controller.setTheme(theme);

		station = new SplitDockStation();
		controller.add(station);
		
		dockGrid = new SplitDockGrid();
	}
	
	/**
	 * Gets the perspective identifier
	 * @return	the indentifier for this perspective
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Gets the Dockstation for this perspective
	 * @return the dockstation for this perspective
	 */
	public SplitDockStation getStation() {
		return this.station;
	}
}
