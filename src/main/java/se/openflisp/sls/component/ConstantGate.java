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
package se.openflisp.sls.component;

import se.openflisp.sls.Signal;
import se.openflisp.sls.annotation.SourceComponent;
import se.openflisp.sls.event.ComponentEventDelegator;

/**
 * A Gate that will output a constant value.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 */
@SourceComponent
public class ConstantGate extends Gate {

	/**
	 * State which the gate will constantly output.
	 */
	private final Signal.State state;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param state		state which the gate will constantly output
	 */
	public ConstantGate(String identifier, Signal.State state) {
		super(identifier);
		if (state == null) {
			throw new IllegalArgumentException("State can not be null.");
		}
		this.state = state;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param state		state which the gate will constantly output
	 */
	public ConstantGate(String identifier, Signal.State state, ComponentEventDelegator delegator) {
		super(identifier, delegator);
		if (state == null) {
			throw new IllegalArgumentException("State can not be null.");
		}
		this.state = state;
	}

	/**
	 * Gets the state which the gate will constantly output.
	 * 
	 * @return the state which the gate will constantly output
	 */
	public Signal.State getConstantState() {
		return this.state;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Signal.State evaluateOutput() {
		return this.state;
	}
}
