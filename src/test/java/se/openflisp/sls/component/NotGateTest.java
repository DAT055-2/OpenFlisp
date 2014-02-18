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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import se.openflisp.sls.Input;
import se.openflisp.sls.Signal;
import se.openflisp.sls.Component;
import se.openflisp.sls.component.Gate;
import se.openflisp.sls.component.GateTest;
import se.openflisp.sls.event.ComponentEventDelegator;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

public class NotGateTest extends GateTest {

	private Gate notGate;

	@Override
	protected Gate getInstance(String identifier) {
		return new NotGate(identifier);
	}

	@Override
	protected Gate getInstance(String identifier, ComponentEventDelegator delegator) {
		return new NotGate(identifier, delegator);
	}

	@Before
	public void before() {
		super.setup();
		notGate = new NotGate(gateName);
	}

	@Test
	public void testEvaluatingOutputHIGH() {
		addInputMock1ToInputs(Signal.State.LOW);
		assertEquals(Signal.State.HIGH, notGate.evaluateOutput());
	}

	@Test
	public void testEvaluatingOutputLOW() {
		addInputMock1ToInputs(Signal.State.HIGH);
		assertEquals(Signal.State.LOW, notGate.evaluateOutput());
	}

	@Test
	public void testEvaluatingOutputFLOATING() {
		addInputMock1ToInputs(Signal.State.FLOATING);
		assertEquals(Signal.State.FLOATING, notGate.evaluateOutput());
	}

	@Test
	public void testWithMoreThanOneInput() {
		Mockito.when(inputMock1.getState()).thenReturn(Signal.State.LOW);
		Input inputMock2 = Mockito.mock(Input.class);
		Mockito.when(inputMock2.getState()).thenReturn(Signal.State.LOW);
		
		Map<String, Input> hashMap = new HashMap<String, Input>();
		hashMap.put(id,inputMock1);
		hashMap.put("identifier2",inputMock2);
		try {
			Field field = Component.class.getDeclaredField("inputs");
			field.setAccessible(true);
			field.set(notGate, hashMap);
		} catch (NoSuchFieldException e) {
			System.out.println(e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println(e.getMessage());
		}
		assertThat(notGate.getInputs().size(), is(2));
		assertEquals(Signal.State.FLOATING, notGate.evaluateOutput());
	}

	@Test
	public void testWithNoInput() {
		assertThat(notGate.getInputs().size(), is(0));
		assertEquals(Signal.State.FLOATING, notGate.evaluateOutput());
		assertThat(notGate.getInputs().size(), is(0));
	}

	@Test
	public void testEvaluatingChangingOutputs() {
		addInputMock1ToInputs(Signal.State.LOW);
		assertEquals(Signal.State.HIGH, notGate.evaluateOutput());

		addInputMock1ToInputs(Signal.State.HIGH);
		assertEquals(Signal.State.LOW, notGate.evaluateOutput());

		addInputMock1ToInputs(Signal.State.FLOATING);
		assertEquals(Signal.State.FLOATING, notGate.evaluateOutput());

		addInputMock1ToInputs(Signal.State.LOW);
		assertEquals(Signal.State.HIGH, notGate.evaluateOutput());
	}

	public void addInputMock1ToInputs(Signal.State state) {
		Mockito.when(inputMock1.getState()).thenReturn(state);

		Map<String, Input> hashMap = new HashMap<String, Input>();
		hashMap.put(id, inputMock1);
		try {
			Field field = Component.class.getDeclaredField("inputs");
			field.setAccessible(true);
			field.set(notGate, hashMap);
		} catch (NoSuchFieldException e) {
			System.out.println(e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println(e.getMessage());
		}
	}
}
