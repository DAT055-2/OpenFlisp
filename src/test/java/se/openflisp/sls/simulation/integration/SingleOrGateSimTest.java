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
package se.openflisp.sls.simulation.integration;

import org.junit.Before;
import org.junit.Test;

import se.openflisp.sls.Signal;
import se.openflisp.sls.component.*;

/**
 * Tests the functions of an OrGate in a Circuit simulation.
 * 
 * @author Pär Svedberg <rockkuf@gmail.com>
 * @version 1.0
 */
public class SingleOrGateSimTest extends SingleGateSimTest {
	private OrGate orGate;
	private String orGateID;

	@Before
	public void setup() {
		super.setup();
		orGateID = "OrGate";
		orGate = new OrGate(orGateID);
	}
	
	/* LOW OUTPUT */
	
	@Test
	public void testOrGateOutputLOW_TwoLow() {
		ConstantGate[] inputGates = {constantLow, constantLow};
		helpSimulate(inputGates, Signal.State.LOW, orGate);
	}
	
	/* HIGH OUTPUT */
	
	@Test
	public void testOrGateOutputHIGH_TwoHigh() {
		ConstantGate[] inputGates = {constantHigh, constantHigh};
		helpSimulate(inputGates, Signal.State.HIGH, orGate);
	}
	
	@Test
	public void testOrGateOutputHIGH_LowORHigh() {
		ConstantGate[] inputGates = {constantLow, constantHigh};
		helpSimulate(inputGates, Signal.State.HIGH, orGate);
	}
	
	@Test
	public void testOrGateOutputHIGH_HighORLow() {
		ConstantGate[] inputGates = {constantHigh, constantLow};
		helpSimulate(inputGates, Signal.State.HIGH, orGate);
	}
	
	@Test
	public void testOrGateOutputHIGH_FloatORHigh() {
		ConstantGate[] inputGates = {constantFloating, constantHigh};
		helpSimulate(inputGates, Signal.State.HIGH, orGate);
	}
	
	@Test
	public void testOrGateOutputHIGH_HighORFloat() {
		ConstantGate[] inputGates = {constantHigh, constantFloating};
		helpSimulate(inputGates, Signal.State.HIGH, orGate);
	}
	
	/* FLOATING OUTPUT */
	
	@Test
	public void testOrGateOutputFLOATING_NoInputs() {
		helpSimulate(new Gate[0], Signal.State.FLOATING, orGate);
	}
	
	@Test
	public void testOrGateOutputFLOATING_OneHighInput() {
		ConstantGate[] inputGates = {constantHigh};
		helpSimulate(inputGates, Signal.State.FLOATING, orGate);
	}
	
	@Test
	public void testOrGateOutputFLOATING_OneLowInput() {
		ConstantGate[] inputGates = {constantLow};
		helpSimulate(inputGates, Signal.State.FLOATING, orGate);
	}
	
	@Test
	public void testOrGateOutputFLOATING_FloatORLow() {
		ConstantGate[] inputGates = {constantFloating, constantLow};
		helpSimulate(inputGates, Signal.State.FLOATING, orGate);
	}
	
	@Test
	public void testOrGateOutputFLOATING_LowORFloat() {
		ConstantGate[] inputGates = {constantLow, constantFloating};
		helpSimulate(inputGates, Signal.State.FLOATING, orGate);
	}
	
	@Test
	public void testOrGateOutputFLOATING_OneFloatingInput() {
		ConstantGate[] inputGates = {constantFloating};
		helpSimulate(inputGates, Signal.State.FLOATING, orGate);
	}
	
	@Test
	public void testOrGateOutputFLOATING_TwoFloatingInputs() {
		ConstantGate[] inputGates = {constantFloating, constantFloating};
		helpSimulate(inputGates, Signal.State.FLOATING, orGate);
	}
}
