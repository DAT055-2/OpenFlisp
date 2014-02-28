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
package se.openflisp.sls.event;

import java.awt.Point;

import se.openflisp.sls.Component;

/**
 * Listener interface used to listen on events from a Circuit that contains
 * a set of Components.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 * @see CircuitEventDelegator
 */
public interface CircuitListener {

	/**
	 * Called when a new Component is added to a Circuit. Will be followed
	 * by a {@link #onComponentMoved(Component, Point, Point)} if the Circuit has knowledge
	 * of it's location.
	 * 
	 * @param component		component that have been added
	 */
	public void onComponentAdded(Component component);
	
	/**
	 * Called when a Component is removed from a Circuit.
	 * 
	 * @param component		component that have been removed
	 */
	public void onComponentRemoved(Component component);
	
	/**
	 * Called when a Component is moved within a Circuit or is first placed in a
	 * Circuit.
	 * 
	 * @param component		component that have been moved
	 * @param from			where the component was before the move, or null if just added
	 * @param to			where the component is after the move
	 */
	public void onComponentMoved(Component component, Point from, Point to);
}
