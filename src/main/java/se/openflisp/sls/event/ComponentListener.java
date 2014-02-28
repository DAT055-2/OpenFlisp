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

import se.openflisp.sls.Component;
import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.Signal;

/**
 * Listener interface used to listen on events on a specific Component.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 * @see ComponentEventDelegator
 */
public interface ComponentListener {
	
	/**
	 * Called when one Component's signal change. If the signal is
	 * updated to the same value this will not be called.
	 * 
	 * @param component		the component where the event took place
	 * @param signal		the specific signal where the event took place
	 */
	public void onSignalChange(Component component, Signal signal);
	
	/**
	 * Called when two signals connect, which connects Components. Will be 
	 * called on both components which may be the same if its a feedback connection.
	 * 
	 * @param input			the input where the output was connected to
	 * @param output		the output where the input was connected to
	 */
	public void onSignalConnection(Input input, Output output);
	
	/**
	 * Called when two signals disconnect, which may disconnect two Components. Will be 
	 * called on both components which may be  if the connection was a feedback connection.
	 * 
	 * @param input		the input where the output was disconnected from
	 * @param output	the output where the input was disconnected from
	 */
	public void onSignalDisconnection(Input input, Output output);
}
