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

import javax.swing.SwingUtilities;

import se.openflisp.sls.Component;

/**
 * Delegator for circuit events in the simulation.
 * 
 * Prioritizes {@link ListenerContext#MODEL} listeners before {@link ListenerContext#SWING} 
 * and {@link ListenerContext#DEFAULT} listeners. Will run the {@link ListenerContext#SWING} in 
 * the Swing event thread.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 */
public class CircuitEventDelegator extends EventDelegator<CircuitListener> implements CircuitListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onComponentAdded(final Component component) {
		for (CircuitListener listener : this.getModelListeners()) {
			listener.onComponentAdded(component);
		}
		for (final CircuitListener listener : this.getSwingListeners()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					listener.onComponentAdded(component);
				}
			});
		}
		for (CircuitListener listener : this.getNormalListeners()) {
			listener.onComponentAdded(component);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onComponentRemoved(final Component component) {
		for (CircuitListener listener : this.getModelListeners()) {
			listener.onComponentRemoved(component);
		}
		for (final CircuitListener listener : this.getSwingListeners()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					listener.onComponentRemoved(component);
				}
			});
		}
		for (CircuitListener listener : this.getNormalListeners()) {
			listener.onComponentRemoved(component);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onComponentMoved(final Component component, final Point from, final Point to) {
		for (CircuitListener listener : this.getModelListeners()) {
			listener.onComponentMoved(component, from, to);
		}
		for (final CircuitListener listener : this.getSwingListeners()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					listener.onComponentMoved(component, from, to);
				}
			});
		}
		for (CircuitListener listener : this.getNormalListeners()) {
			listener.onComponentMoved(component, from, to);
		}

	}
}
