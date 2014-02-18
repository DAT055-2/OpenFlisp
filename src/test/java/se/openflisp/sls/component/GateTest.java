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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import se.openflisp.sls.ComponentTest;
import se.openflisp.sls.Component;
import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.Signal;
import se.openflisp.sls.event.ComponentEventDelegator;
import org.mockito.Mockito;

public abstract class GateTest extends ComponentTest {

	public Gate gate;
	public String gateName = "Test Gate 1"; // For classes extending GateTest

	@Before
	public void setup() {
		super.setup();
		gate = getInstance(id);
	}

	@Test
	public void testGettingOutput() {
		Output output = gate.getOutput();
		assertEquals(Gate.OUTPUT, output.getIdentifier());
	}
	
	@Override
	protected abstract Gate getInstance(String identifier);

	@Override
	protected abstract Gate getInstance(String identifier, ComponentEventDelegator delegator);

	public void helpEvaluatingOutput(Signal.State[] states, Signal.State expectedState, Gate gateToHelp) {
		Map<Input, Signal.State> inputMocks = new HashMap<Input, Signal.State>();
		for(Signal.State state : states) {
			inputMocks.put(Mockito.mock(Input.class), state);
		}
		Gate gateWithNewInputs = addInputMockToInputs(inputMocks, gateToHelp);
		assertThat(gateWithNewInputs.getInputs().size(), is(states.length));
		assertEquals(expectedState, gateWithNewInputs.evaluateOutput());
	}

	public Gate addInputMockToInputs(Map<Input, Signal.State> inputMocks, Gate gateToHelp) {
		Map<String, Input> newMap = new HashMap<String, Input>();

		int	i = 0; 
		for(Map.Entry<Input, Signal.State> entry : inputMocks.entrySet()) {
			Input tempMock = entry.getKey();
			Mockito.when(tempMock.getState()).thenReturn(entry.getValue());
			newMap.put("identifier " + i, tempMock);
			i++;
		}

		try {
			Field field = Component.class.getDeclaredField("inputs");
			field.setAccessible(true);
			field.set(gateToHelp, newMap);
		} catch (NoSuchFieldException e) {
			System.out.println(e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println(e.getMessage());
		}
		return gateToHelp;
	}

}
