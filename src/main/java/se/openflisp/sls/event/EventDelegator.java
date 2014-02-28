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

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Delegator for events in the simulation.
 * 
 * Handles the group of listeners that should be notified when a specific event happens. Extending classes
 * is responsible for calling the listeners that have been added. The extending class should also pay respect
 * to the ListenerContext of the listener to decide priority and if the listener needs to be run in another thread.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 * @see CircuitEventDelegator
 * @see ComponentEventDelegator
 * 
 * @param <T>	the listener interface that the delegator should handle
 */
public abstract class EventDelegator<T> {

	/**
	 * Map over all listeners arranged after their ListenerContext.
	 */
	private Map<ListenerContext, Set<T>> listeners = new ConcurrentHashMap<ListenerContext, Set<T>>();
	
	/**
	 * Adds a listener that should be notified when a event happens.
	 * 
	 * Will use {@link ListenerContext#DEFAULT} context as default.
	 * 
	 * @param listener		listener that should be notified when a event happens
	 * @return true if the listener was added, false otherwise
	 */
	public boolean addListener(T listener) {
		if (listener == null) {
			return false;
		}
		return this.addListener(ListenerContext.DEFAULT, listener);
	}
	
	/**
	 * Adds a listener that should be notified when a event happens.
	 * 
	 * @param context		in which context the listener should be called in
	 * @param listener		listener that should be notified when a event happens	
	 * @return true if the listener was added, false otherwise
	 */
	public boolean addListener(ListenerContext context, T listener) {
		if (listener == null || context == null) {
			return false;
		}
		if (!this.listeners.containsKey(context)) {
			this.listeners.put(context, Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>()));
		}
		return this.listeners.get(context).add(listener);
	}
	
	/**
	 * Removes a listener from all contexts where it have been added.
	 * 
	 * @param listener		listener to be removed
	 * @return true if the listener was removed at any context, otherwise false
	 */
	public boolean removeListener(T listener) {
		boolean changed = false;
		for (Entry<ListenerContext, Set<T>> entry : this.listeners.entrySet()) {
			if (entry.getValue().remove(listener)) {
				changed = true;
			}
		}
		return changed;
	}
	
	/**
	 * Removes a listener from one specific context.
	 * 
	 * @param context		which context to remove the listener from
	 * @param listener		listener to be removed
	 * @return true if the listener was removed, otherwise false
	 */
	public boolean removeListener(ListenerContext context, T listener) {
		Set<T> listeners = this.listeners.get(context);
		if (listeners != null) {
			return listeners.remove(listener);
		}
		return false;
	}
	
	/**
	 * Gets all listeners that has the specified context.
	 * 
	 * @param context		which context the listeners should have
	 * @return unmodifiable list of listeners with the specified context
	 */
	public Set<T> getListeners(ListenerContext context) {
		if (!this.listeners.containsKey(context)) {
			this.listeners.put(context, Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>()));
		}
		return Collections.unmodifiableSet(this.listeners.get(context));
	}
	
	/**
	 * Gets all listeners that has {@link ListenerContext#MODEL} context.
	 * 
	 * @return list of all {@link ListenerContext#MODEL} context listeners
	 */
	public Set<T> getModelListeners() {
		return this.getListeners(ListenerContext.MODEL);
	}
	
	/**
	 * Gets all listeners that has {@link ListenerContext#SWING} context.
	 * 
	 * @return list of all {@link ListenerContext#SWING} context listeners
	 */
	public Set<T> getSwingListeners() {
		return this.getListeners(ListenerContext.SWING);
	}
	
	/**
	 * Gets all listeners that don't have the {@link ListenerContext#MODEL} or {@link ListenerContext#SWING} context.
	 * 
	 * @return list of listeners that don't have the {@link ListenerContext#MODEL}
	 * 		    or {@link ListenerContext#SWING} context.
	 */
	public Set<T> getNormalListeners() {
		return this.getListeners(ListenerContext.DEFAULT);
	}
}
