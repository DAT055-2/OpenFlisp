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
package se.openflisp.sls;

import se.openflisp.sls.event.ComponentEventDelegator;
import se.openflisp.sls.exception.ComponentEvaluationException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic model class for a Component in a Sequential Logical Circuit.
 * 
 * @author Anton Ekberg <anton.ekberg@gmail.com>
 * @version 1.0
 */
public abstract class Component {
	
	/**
	 * Component identifier used in collections of components and in debugging.
	 */
	private final String identifier;

	/**
	 * Delegates events that happen in the Component.
	 */
	private final ComponentEventDelegator eventDelegator;
	
	/**
	 * Map of the components inputs mapped to its pin identifiers.
	 */
	private Map<String, Input> inputs = new HashMap<String, Input>();
	
	/**
	 * Map of the components outputs mapped to its pin identifiers.
	 */
	private Map<String, Output> outputs = new HashMap<String, Output>();
	
	/**
	 * Constructs a new Component with a identifier.
	 * 
	 * @param identifier	the component identifier
	 */
	public Component(String identifier) {
		this(identifier, new ComponentEventDelegator());
	}
	
	/**
	 * Constructs a new Component with a identifier and delegator.
	 * 
	 * @param identifier	the component identifier
	 * @param delegator		the component event delegator
	 */
	public Component(String identifier, ComponentEventDelegator delegator) {
		if (identifier == null || identifier.isEmpty()) {
			throw new IllegalArgumentException("Identifier can not be null or empty.");
		}
		if (delegator == null) {
			throw new IllegalArgumentException("Delegator can not be null.");
		}

		this.identifier 	= identifier;
		this.eventDelegator = delegator;
	}
	
	/**
	 * Gets the component identifier.
	 * 
	 * @return the identifier for this component
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Gets the event delegator for the Component.
	 * 
	 * @return event delegator for the Component
	 */
	public ComponentEventDelegator getEventDelegator() {
		return this.eventDelegator;
	}
	
	/**
	 * Gets the input given a pin identifier.
	 * 
	 * If the input is not found, it will be created before returned.
	 * 
	 * @param identifier	a identifier for which pin the input is connected to
	 * @return the input which the identifier corresponds to
	 */
	public Input getInput(String identifier) {
		if (!this.inputs.containsKey(identifier)) {
			this.inputs.put(identifier, new Input(identifier, this));
		}
		return this.inputs.get(identifier);
	}
	
	/**
	 * Gets the output given a pin identifier.
	 * 
	 * If the output is not found, it will be created before returned.
	 * 
	 * @param identifier	a identifier for which pin the output is connected to
	 * @return the output which the identifier corresponds to
	 */
	public Output getOutput(String identifier) {
		if (!this.outputs.containsKey(identifier)) {
			this.outputs.put(identifier, new Output(identifier, this));
		}
		return this.outputs.get(identifier);
	}
	
	/**
	 * Gets an unmodifiable collection of all inputs in the component.
	 * 
	 * @return unmodifiable collection of all inputs
	 */
	public Collection<Input> getInputs() {
		return Collections.unmodifiableCollection(this.inputs.values());
	}
	
	/**
	 * Gets an unmodifiable collection of all outputs in the component.
	 * 
	 * @return unmodifiable collection of all outputs
	 */
	public Collection<Output> getOutputs() {
		return Collections.unmodifiableCollection(this.outputs.values());
	}
	
	/**
	 * Sets one of the components output to a certain state.
	 * 
	 * Is required since the children of this class is in another package which can not access the 
	 * protected {@link Signal#setState(se.openflisp.sls.Signal.State) setState} method of its Outputs.
	 * 
	 * @param identifier	a pin identifier which corresponding output should be set
	 * @param state 		which state the output should be set to
	 */
	protected void setOutputState(String identifier, Signal.State state) {
		this.getOutput(identifier).setState(state);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s[#%s]", this.getClass(), this.identifier);
	}
	
	/**
	 * Evaluates the components inputs and produces changes on its outputs.
	 */
	public abstract void evaluate() throws ComponentEvaluationException;
}
