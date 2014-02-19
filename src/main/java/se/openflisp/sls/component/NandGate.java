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

import se.openflisp.sls.*;
import se.openflisp.sls.event.ComponentEventDelegator;
import se.openflisp.sls.util.SignalCollection;

/**
 * Class representing a logical NAND-gate.
 * 
 * @author Pär Svedberg <rockkuf@gmail.com>
 * @version 1.0
 */

public class NandGate extends Gate {
	
	/**
	 * {@inheritDoc}
	 */
	public NandGate(String identifier) {
		super(identifier);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public NandGate(String identifier, ComponentEventDelegator delegator) {
		super(identifier, delegator);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Signal.State evaluateOutput() {
		if(this.getInputs().size() < 2) {
			return Signal.State.FLOATING;
		}
		if(SignalCollection.containsState(this.getInputs(), Signal.State.LOW)) {
			return Signal.State.HIGH;
		}
		if(SignalCollection.containsState(this.getInputs(), Signal.State.FLOATING)) {
			return Signal.State.FLOATING;
		}
		return Signal.State.LOW;
	}

}
