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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import se.openflisp.sls.event.ComponentEventDelegator;
import org.mockito.Mockito;

public abstract class ComponentTest {

	private Component component;
	public String id;
	public ComponentEventDelegator delegatorMock;
	public Input inputMock1; //All Components have at least 1 input
	public Output outputMock1; //and at least 1 output

	@Before
	public void setup() {
		id = "identifier";
		component = getInstance(id);
		delegatorMock = Mockito.mock(ComponentEventDelegator.class);

		//Mockings for classes extending ComponentTest
		inputMock1 = Mockito.mock(Input.class);
		outputMock1 = Mockito.mock(Output.class);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructingWithNullIdentifier() {
		getInstance(null);
	}

	@Test
	public void testGettingIdentifier() {
		assertEquals(id,component.getIdentifier());
	}

	@Test
	public void gettingEventDelegatorNotNull() {
		assertNotNull(component.getEventDelegator());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructingWithDelegatorWithNullIdentifier() {
		getInstance(null, delegatorMock);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructingWithNullDelegator() {
		getInstance(id, null);
	}

	@Test
	public void creatingComponentWithDelegator() {
		Component tempComponent = getInstance(id, delegatorMock);
		assertSame(delegatorMock, tempComponent.getEventDelegator());
		assertEquals(id, tempComponent.getIdentifier());
	}

	@Test
	public void testGettingInputNotNull() {
		assertNotNull(component.getInput(id));
	}

	@Test
	public void testGettingInputReturnsSameObject() {
		Input tempInput1 = component.getInput(id);
		Input tempInput2 = component.getInput(id);
		assertSame(tempInput1, tempInput2);
	}

	@Test
	public void testGettingInputAddsToCollection() {
		assertThat(component.getInputs().size(), is(0));
		Input tempInput1 = component.getInput(id);
		assertThat(component.getInputs().size(), is(1));
		Input tempInput2 = component.getInput(id);
		assertThat(component.getInputs().size(), is(1));
		Input tempInput3 = component.getInput("identifier2");
		assertThat(component.getInputs().size(), is(2));
	}

	@Test
	public void testGettingDifferentInputsForDifferentId() {
		Input tempInput1 = component.getInput(id);
		Input tempInput2 = component.getInput("identifier2");
		assertNotSame(tempInput1, tempInput2);
	}

	@Test
	public void testGettingOutputNotNull() {
		assertNotNull(component.getOutput(id));
	}

	@Test
	public void testGettingOutputReturnSameObject() {
		Output tempOutput1 = component.getOutput(id);
		Output tempOutput2 = component.getOutput(id);
		assertSame(tempOutput1, tempOutput2);
	}

	@Test
	public void testGettingOutputAddsToCollection() {
		assertThat(component.getOutputs().size(), is(0));
		Output tempOutput1 = component.getOutput(id);
		assertThat(component.getOutputs().size(), is(1));
		Output tempOutput2 = component.getOutput(id);
		assertThat(component.getOutputs().size(), is(1));
		Output tempOutput3 = component.getOutput("identifier2");
		assertThat(component.getOutputs().size(), is(2));
	}

	@Test
	public void testGettingDifferentOutputsForDifferentId() {
		Output tempOutput1 = component.getOutput(id);
		Output tempOutput2 = component.getOutput("identifier2");
		assertNotSame(tempOutput1, tempOutput2);
	}

	/* Does not work... setOutputState() is protected.
	@Test
	public void testSettingOutputState() {
		component.getInput(id);
		component.setOutputState(id, Signal.State.LOW);
	}*/

	protected abstract Component getInstance(String identifier);

	protected abstract Component getInstance(String identifier, ComponentEventDelegator delegator);
}
