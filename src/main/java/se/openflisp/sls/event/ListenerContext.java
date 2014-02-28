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

/**
 * Identifier for in what context the listener is used. The different contexts
 * will be used as rank for prioritization and threading.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 * @see EventDelegator
 */
public enum ListenerContext {
	/**
	 * The listener is part of the model and should be updated first in the
	 * same thread as the thread that triggered the model change.
	 */
	MODEL,
	
	/**
	 * The listener is part of the Swing GUI and should have higher priority than
	 * default but be run in the Swing event thread.
	 */
	SWING,
	
	/**
	 * Lowest priority and run in the same thread that triggered the model change.
	 */
	DEFAULT
}
