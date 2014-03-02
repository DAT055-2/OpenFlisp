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
 * Base class for all perspective.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
public abstract class Perspective {
	
	/**
	 * The perspective dock controller.
	 */
	protected DockController controller;
	
	/**
	 * The perspective dock station.
	 */
	protected SplitDockStation station;
	
	/**
	 * The perspective dock grid.
	 */
	protected SplitDockGrid	dockGrid;
	
	/**
	 * The perspective theme.
	 */
	protected DockTheme	theme;
	
	/**
	 * Identifier for this perspective used to swap between Perspectives.
	 */
	protected String identifier;
	
	/**
	 * Create a new Assembler Perspective.
	 * 
	 * @param theme		theme for the perspective
	 */
	public Perspective(DockTheme theme) {
		this.theme = theme;
		this.controller = new DockController();
		this.controller.setTheme(theme);
		this.station = new SplitDockStation();
		this.controller.add(station);
		this.dockGrid = new SplitDockGrid();
	}
	
	/**
	 * Gets the perspective identifier.
	 * 
	 * @return the perspective identifier.
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Gets the SplitDockStation for this Perspective.
	 * 
	 * @return the SplitDockStation for this Perspective
	 */
	public SplitDockStation getStation() {
		return this.station;
	}
}
