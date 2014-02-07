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
import se.openflisp.sls.exception.ComponentEvaluationException;

/**
 * Class representing a logical gate in a Sequential Logical Circuit.
 * A gate can have many inputs and only one output.
 * Extends the class Component.
 * 
 * @author Pär Svedberg <rockkuf@gmail.com>
 * @version 1.0
 */
public abstract class Gate extends Component {

	/**
	 * The single output for a logical gate.
	 */
	public static final String OUTPUT = "Q";
	
	/**
	 * {@inheritDoc}
	 */
	public Gate(String identifier) {
		super(identifier);
	}
	
	/**
	 * Gets the single output of the gate.
	 * 
	 * @return the gate's output via its superclass' method
	 */
	public Output getOutput() {
		return this.getOutput(Gate.OUTPUT);
	}
	
	/**
	 * Sets the output state of the gate.
	 * 
	 * @param state the state which the output should be set to
	 */
	protected void setState(Signal.State state) {
		this.setOutputState(Gate.OUTPUT, state);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void evaluate() throws ComponentEvaluationException;
}
