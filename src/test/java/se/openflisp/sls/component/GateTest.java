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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import se.openflisp.sls.ComponentTest;
import se.openflisp.sls.Output;
import se.openflisp.sls.event.ComponentEventDelegator;

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
}
