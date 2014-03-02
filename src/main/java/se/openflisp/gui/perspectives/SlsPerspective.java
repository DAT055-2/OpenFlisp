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

import se.openflisp.gui.swing.components.ComponentPanel;
import se.openflisp.gui.swing.components.SimulationBoard;
import bibliothek.gui.DockTheme;
import bibliothek.gui.dock.DefaultDockable;

/**	
 * Perspective for sequential logic simulation.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
public class SlsPerspective extends Perspective {
	
	/**
	 * The active simulation board for this perspective.
	 */
	private final SimulationBoard simulationBoard;
	
	/**
	 * Identifier for the sequential logic simulation perspective.
	 */
	public static final String IDENTIFIER = "Sequential Logical simulation";
	
	/**
	 * Create a new Sequential Logical Simulation perspective.
	 * 
	 * @param theme		theme for the perspective
	 */
	public SlsPerspective (DockTheme theme) {
		super(theme);
		this.identifier = IDENTIFIER;
		this.simulationBoard = new SimulationBoard();
		dockGrid.addDockable(0, 0, 2, 1, new DefaultDockable(this.simulationBoard));
		dockGrid.addDockable(0, 0, 1 ,1, new DefaultDockable(new ComponentPanel()));
		station.dropTree( dockGrid.toTree());
	}
	
	/**
	 * Gets the active simulation board for this perspective.
	 * 
	 * @return the active simulation board for this perspective
	 */
	public SimulationBoard getSimulationBoard() {
		return this.simulationBoard;
	}
}
