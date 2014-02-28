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

import javax.swing.SwingUtilities;

import se.openflisp.sls.Component;
import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.Signal;

/**
 * Delegator for component events in the simulation.
 * 
 * Prioritizes {@link ListenerContext#MODEL} listeners before {@link ListenerContext#SWING} 
 * and {@link ListenerContext#DEFAULT} listeners. Will run the {@link ListenerContext#SWING} in 
 * the Swing event thread.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 */
public class ComponentEventDelegator extends EventDelegator<ComponentListener> implements ComponentListener {

	/**
	 * {@inheritDoc}
	 */
  	@Override
	public void onSignalChange(final Component component, final Signal signal) {
		for (ComponentListener listener : this.getModelListeners()) {
			listener.onSignalChange(component, signal);
		}
		for (final ComponentListener listener : this.getSwingListeners()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					listener.onSignalChange(component, signal);
				}
			});
		}
		for (ComponentListener listener : this.getNormalListeners()) {
			listener.onSignalChange(component, signal);
		}
	}

  	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSignalConnection(final Input input, final Output output) {
		for (ComponentListener listener : this.getModelListeners()) {
			listener.onSignalConnection(input, output);
		}
		for (final ComponentListener listener : this.getSwingListeners()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					listener.onSignalConnection(input, output);
				}
			});
		}
		for (ComponentListener listener : this.getNormalListeners()) {
			listener.onSignalConnection(input, output);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSignalDisconnection(final Input input, final Output output) {
		for (ComponentListener listener : this.getModelListeners()) {
			listener.onSignalDisconnection(input, output);
		}
		for (final ComponentListener listener : this.getSwingListeners()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					listener.onSignalDisconnection(input, output);
				}
			});
		}
		for (ComponentListener listener : this.getNormalListeners()) {
			listener.onSignalDisconnection(input, output);
		}
	}
}
