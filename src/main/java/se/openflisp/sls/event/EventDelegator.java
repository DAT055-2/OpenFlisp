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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Base class for a event delegator.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 * 
 * @param <T>	the listener interface that the delegator should handle
 */
public abstract class EventDelegator<T> {

	/**
	 * Map over all listeners arranged after their ListenerContext.
	 */
	private Map<ListenerContext, List<T>> listeners = new HashMap<ListenerContext, List<T>>();
	
	/**
	 * Adds a listener that should be notified when a event happens.
	 * 
	 * Will use {@link ListenerContext#DEFAULT} context as default.
	 * 
	 * @param listener	listener that should be notified when a event happens
	 * @return true if the listener was added, false otherwise
	 */
	public boolean addListener(T listener) {
		return this.addListener(ListenerContext.DEFAULT, listener);
	}
	
	/**
	 * 
	 * @param listener	listener that should be notified when a event happens
	 * @param context	
	 * @return true if the listener was added, false otherwise
	 */
	public boolean addListener(ListenerContext context, T listener) {
		if (!this.listeners.containsKey(context)) {
			this.listeners.put(context, new LinkedList<T>());
		}
		return this.listeners.get(context).add(listener);
	}
	
	/**
	 * Gets all listeners that has the specified context.
	 * 
	 * @param context	which context the listeners should have
	 * @return unmodifiable list of listeners with the specified context
	 */
	public List<T> getListeners(ListenerContext context) {
		if (!this.listeners.containsKey(context)) {
			this.listeners.put(context, new LinkedList<T>());
		}
		return Collections.unmodifiableList(this.listeners.get(context));
	}
	
	/**
	 * Gets all listeners that has {@link ListenerContext#MODEL} context.
	 * 
	 * @return list of all {@link ListenerContext#MODEL} context listeners
	 */
	public List<T> getModelListeners() {
		return this.getListeners(ListenerContext.MODEL);
	}
	
	/**
	 * Gets all listeners that has {@link ListenerContext#SWING} context.
	 * 
	 * @return list of all {@link ListenerContext#SWING} context listeners
	 */
	public List<T> getSwingListeners() {
		return this.getListeners(ListenerContext.SWING);
	}
	
	/**
	 * Gets all listeners that don't have the {@link ListenerContext#MODEL} or {@link ListenerContext#SWING} context.
	 * 
	 * @return list of listeners that don't have the {@link ListenerContext#MODEL}
	 * 		    or {@link ListenerContext#SWING} context.
	 */
	public List<T> getNormalListeners() {
		return this.getListeners(ListenerContext.DEFAULT);
	}
}
