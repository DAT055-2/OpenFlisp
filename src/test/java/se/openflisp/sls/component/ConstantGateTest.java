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

import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.openflisp.sls.Signal;
import se.openflisp.sls.event.ComponentEventDelegator;

public class ConstantGateTest extends GateTest {

	private ConstantGate constantGate;
	private ComponentEventDelegator delegator;
	private Signal.State defaultState = Signal.State.FLOATING;

	@Override
	protected ConstantGate getInstance(String identifier) {
		return new ConstantGate(identifier, defaultState);
	}

	@Override
	protected ConstantGate getInstance(String identifier, ComponentEventDelegator delegator) {
		return new ConstantGate(identifier, defaultState, delegator);
	}

	@Before
	public void setup() {
		super.setup();
		delegator = new ComponentEventDelegator();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstantGateConstructor1() {
		ConstantGate tempGate = new ConstantGate(id, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstantGateConstructor2() {
		ConstantGate tempGate = new ConstantGate(id, null, delegator);
	}

	@Test
	public void testGetConstantStateAndEvaluateOutputHIGH() {
		constantGate = new ConstantGate(id, Signal.State.HIGH, delegator);
		assertEquals(Signal.State.HIGH, constantGate.getConstantState());
		assertEquals(Signal.State.HIGH, constantGate.evaluateOutput());
	}

	@Test
	public void testGetConstantStateAndEvaluateOutputLOW() {
		constantGate = new ConstantGate(id, Signal.State.LOW, delegator);
		assertEquals(Signal.State.LOW, constantGate.getConstantState());
		assertEquals(Signal.State.LOW, constantGate.evaluateOutput());
	}

	@Test
	public void testGetConstantStateAndEvaluateOutputFLOATING() {
		constantGate = new ConstantGate(id, Signal.State.FLOATING, delegator);
		assertEquals(Signal.State.FLOATING, constantGate.getConstantState());
		assertEquals(Signal.State.FLOATING, constantGate.evaluateOutput());
	}

}
