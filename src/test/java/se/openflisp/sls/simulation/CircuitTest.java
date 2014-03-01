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
package se.openflisp.sls.simulation;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import se.openflisp.sls.Component;
import se.openflisp.sls.event.CircuitEventDelegator;

public class CircuitTest {

	public Circuit circuit;
	public CircuitEventDelegator delegator;
	public Component component1, component2, component3;
	
	@Before
	public void setup() {
		delegator = Mockito.mock(CircuitEventDelegator.class);
		circuit = new Circuit(delegator);
		component1 = mockComponent("1");
		component2 = mockComponent("2");
		component3 = mockComponent("3");
	}
	
	protected Component mockComponent(String identifier) {
		Component component = new Component(identifier) {
			public void evaluate() {
				throw new IllegalStateException();
			}
		};
		return component;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreatingWithNullDelegator() {
		new Circuit(null);
	}
	
	@Test
	public void testCircuitIsEmptyAtInit() {
		assertTrue(circuit.getComponents().isEmpty());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddingNullComponent() {
		circuit.addComponent(null);
	}
	
	@Test
	public void testAddingUnconnectedComponent() {
		circuit.addComponent(component1);
		assertThat(circuit.getComponents(), hasItem(component1));
	}

	@Test
	public void testAddingOutputConnectedComponent() {
		component1.getOutput("1").connect(component2.getInput("2"));
		circuit.addComponent(component1);
		assertThat(circuit.getComponents(), hasItems(component1, component2));
	}
	
	@Test
	public void testAddingInputConnectedComponent() {
		component1.getOutput("1").connect(component2.getInput("2"));
		circuit.addComponent(component2);
		assertThat(circuit.getComponents(), hasItems(component1, component2));
	}
	
	@Test
	public void testAddingBothInputAndOutputConnectedComponent() {
		component1.getOutput("1").connect(component2.getInput("2"));
		component3.getOutput("1").connect(component1.getInput("2"));
		circuit.addComponent(component1);
		assertThat(circuit.getComponents(), hasItems(component1, component2, component3));
	}
	
	@Test
	public void testConnectingOutputComponentAfterAdd() {
		circuit.addComponent(component1);
		component1.getOutput("1").connect(component2.getInput("2"));
		assertThat(circuit.getComponents(), hasItems(component1, component2));
	}
	
	@Test
	public void testConnectingInputComponentAfterAdd() {
		circuit.addComponent(component2);
		component1.getOutput("1").connect(component2.getInput("2"));
		assertThat(circuit.getComponents(), hasItems(component1, component2));
	}
	
	@Test
	public void testConnectingInputAndOutputComponentsAfterAdd() {
		circuit.addComponent(component1);
		component1.getOutput("1").connect(component2.getInput("2"));
		component3.getOutput("1").connect(component1.getInput("2"));
		assertThat(circuit.getComponents(), hasItems(component1, component2, component3));
	}
	
	@Test
	public void testDelegationSingleAddedComponent() {
		circuit.addComponent(component1);
		verify(delegator).onComponentAdded(component1);
	}
	
	@Test
	public void testDelegationConnectedComponent() {
		component1.getOutput("1").connect(component2.getInput("2"));
		circuit.addComponent(component1);
		verify(delegator).onComponentAdded(component2);
	}
	
	@Test
	public void testDelegationConnectedComponentAfterAdd() {
		circuit.addComponent(component1);
		component1.getOutput("1").connect(component2.getInput("2"));
		verify(delegator).onComponentAdded(component2);
	}
	
	@Test
	public void testRemoveAComponentAfterDirectAdd() {
		circuit.addComponent(component1);
		circuit.removeComponent(component1);
		assertThat(circuit.getComponents(), not(hasItems(component1)));
	}
	
	@Test
	public void testRemoveAComponentAfterIndirectAdd() {
		component1.getOutput("1").connect(component2.getInput("2"));
		circuit.addComponent(component1);
		circuit.removeComponent(component2);
		assertThat(circuit.getComponents(), hasItems(component1));
		assertThat(circuit.getComponents(), not(hasItems(component2)));
	}
	
	@Test
	public void testRemoveDisconnectsOneConnection() {
		component1.getOutput("1").connect(component2.getInput("2"));
		circuit.addComponent(component1);
		circuit.removeComponent(component2);
		assertFalse(component1.getOutput("1").isConnected());
		assertFalse(component2.getInput("2").isConnected());
	}
	
	@Test
	public void testRemoveDisconnectsManyConnections() {
		circuit.addComponent(component1);
		component1.getOutput("1").connect(component2.getInput("2"));
		component3.getOutput("1").connect(component1.getInput("2"));
		circuit.removeComponent(component1);
		assertFalse(component1.getOutput("1").isConnected());
		assertFalse(component1.getInput("2").isConnected());
		assertFalse(component3.getOutput("1").isConnected());
		assertFalse(component2.getInput("2").isConnected());
	}
	
	@Test
	public void testDelegationRemovedDirectComponent() {
		circuit.addComponent(component1);
		circuit.removeComponent(component1);
		verify(delegator).onComponentRemoved(component1);
	}
	
	@Test
	public void testDelegationRemovedIndirectComponent() {
		component1.getOutput("1").connect(component2.getInput("2"));
		circuit.addComponent(component1);
		circuit.removeComponent(component2);
		verify(delegator).onComponentRemoved(component2);
	}
	
	@Test
	public void testRemovingComponentWithoutConnection() {
		circuit.addComponent(component1);
		circuit.removeComponent(component1);
		assertThat(circuit.getComponents(), not(hasItems(component1)));
	}
}
