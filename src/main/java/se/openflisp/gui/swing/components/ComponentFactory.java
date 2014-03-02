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
package se.openflisp.gui.swing.components;

import se.openflisp.sls.Component;
import se.openflisp.sls.component.AndGate;
import se.openflisp.sls.component.ConstantGate;
import se.openflisp.sls.component.Gate;
import se.openflisp.sls.component.NandGate;
import se.openflisp.sls.component.NorGate;
import se.openflisp.sls.component.NotGate;
import se.openflisp.sls.component.NxorGate;
import se.openflisp.sls.component.OrGate;
import se.openflisp.sls.component.XorGate;
import se.openflisp.sls.Signal;

/**	
 * Factory for creating new ComponentViews from Components or Component identifiers.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
public class ComponentFactory {
	
	/**
	 * Internal counter of how many gates that have been created. Used to give "unique" identifiers
	 * for all created Gates.
	 */
	private static int gateCount;
	
	/**
	 * Creates a new Gate initiates a minimum number of inputs and returns a new GateView
	 * for the wanted gate.
	 * 
	 * @param identifier	the identifier for the gate
	 * @return	a new GateView corresponding to the identifier
	 */
	public static GateView createGateFromIdentifier(String identifier) {
		if (identifier.equals("AndGate")) {
			AndGate andGate = new AndGate(Integer.toString(gateCount++));
			andGate.initiateInputs(2);
			return new GateView(andGate);
		} else if(identifier.equals("NotGate")) { 
			NotGate notGate = new NotGate(Integer.toString(gateCount++));
			notGate.initiateInputs(1);
			return new GateView(notGate);
		} else if(identifier.equals("ConstantOneGate")) { 
			ConstantGate constantGate = new ConstantGate(Integer.toString(gateCount++), Signal.State.HIGH);
			return new GateView(constantGate);
		} else if(identifier.equals("ConstantZeroGate")) { 
			ConstantGate constantGate = new ConstantGate(Integer.toString(gateCount++), Signal.State.LOW);
			return new GateView(constantGate);
		} else if(identifier.equals("NandGate")) { 
			NandGate nandGate = new NandGate(Integer.toString(gateCount++));
			nandGate.initiateInputs(2);
			return new GateView(nandGate);
		} else if(identifier.equals("OrGate")) { 
			OrGate orGate = new OrGate(Integer.toString(gateCount++));
			orGate.initiateInputs(2);
			return new GateView(orGate);
		} else if(identifier.equals("XorGate")) { 
			XorGate xorGate = new XorGate(Integer.toString(gateCount++));
			xorGate.initiateInputs(2);
			return new GateView(xorGate);
		} else if(identifier.equals("NorGate")) { 
			NorGate norGate = new NorGate(Integer.toString(gateCount++));
			norGate.initiateInputs(2);
			return new GateView(norGate);
		} else if(identifier.equals("NxorGate")) { 
			NxorGate nxorGate = new NxorGate(Integer.toString(gateCount++));
			nxorGate.initiateInputs(2);
			return new GateView(nxorGate);
		} else {
			return null;
		}
	}
	
	/**
	 * Creates a new GateView from an already existing Component.
	 * 
	 * @param component		component to generate GateView from
	 * @return	a new GateView corresponding to the Component
	 */
	public static GateView createGateFromComponent(Component component) {
		if (component instanceof Gate)  {
			return new GateView(component);
		} else {
			return null;
		}
	}
}
