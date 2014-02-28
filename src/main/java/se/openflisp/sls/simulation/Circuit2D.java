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
package se.openflisp.sls.simulation;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import se.openflisp.sls.Component;
import se.openflisp.sls.event.CircuitEventDelegator;

/**
 * An extension of a logical Circuit which maps Components to certain positions in a 2D-grid.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 * @see Circuit2DBuilder
 */
public class Circuit2D extends Circuit {

	/**
	 * Locations (x, y) for all Components in the Circuit.
	 */
	private Map<Component, Point> locations = new HashMap<Component, Point>();
	
	/**
	 * Creates a Circuit with knowledge of its components locations.
	 */
	public Circuit2D() {
		super();
	}
	
	/**
	 * Creates a Circuit with knowledge of its components locations.
	 * 
	 * @param delegator		the circuit event delegator
	 */
	public Circuit2D(CircuitEventDelegator delegator) {
		super(delegator);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(Component component) {
		this.addComponent(component, new Point(0, 0));
	}
	
	/**
	 * Adds a Component at a specific point in a 2D-grid. Will also recursively
	 * add the components connections. If the connections don't have a location in
	 * this Circuit a random location will be selected.
	 * 
	 * @param component		base component that should be added
	 * @param location		point where the component should be placed
	 */
	public void addComponent(Component component, Point location) {
		if (this.getComponentLocation(component) == null) {
			super.addComponent(component);
			this.setComponentLocation(component, location);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(Component component) {
		super.removeComponent(component);
		this.locations.remove(component);
	}
	
	/**
	 * Sets the location for a specific Component that exists in the Circuit.
	 * 
	 * @param component		component to change location of
	 * @param location		location to move the component to
	 */
	public void setComponentLocation(Component component, Point location) {
		if (component == null || !this.contains(component)) {
			throw new IllegalArgumentException("Can't set position of a component that doesn't exist");
		} else if (location == null) {
			throw new IllegalArgumentException("Can't set position to null");
		}
		Point oldLocation = this.locations.put(component, location);
		this.getEventDelegator().onComponentMoved(component, oldLocation, location);
	}
	
	/**
	 * Gets the x,y-coordinates for a Component.
	 * 
	 * @param component		component
	 * @return the x,y-coordinates or null if component doesn't exist in the Circuit
	 */
	public Point getComponentLocation(Component component) {
		return this.locations.get(component);
	}
}
