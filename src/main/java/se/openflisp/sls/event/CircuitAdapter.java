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
 * Abstract adapter for CircuitListener.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 * @see CircuitEventDelegator
 */
public abstract class CircuitAdapter implements CircuitListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onComponentAdded(Component component) {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onComponentRemoved(Component component) {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onComponentMoved(Component component, Point from, Point to) {}
}
