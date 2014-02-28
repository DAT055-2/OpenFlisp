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
import static org.mockito.Mockito.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class Circuit2DTest extends CircuitTest {

	public Circuit2D circuit2D;
	public Point point1, point2;
	
	@Before
	public void setup() {
		super.setup();
		circuit = circuit2D = new Circuit2D(delegator);
		point1 = Mockito.mock(Point.class);
		point2 = Mockito.mock(Point.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreatingWithNullDelegator() {
		new Circuit2D(null);
	}
	
	@Test
	public void testGettingPosition() {
		circuit2D.addComponent(component1, point1);
		assertEquals(point1, circuit2D.getComponentLocation(component1));
	}
	
	@Test
	public void testGettingPositionOfNonExistantComponent() {
		assertNull(circuit2D.getComponentLocation(component1));
	}
	
	@Test
	public void testSettingValidPosition() {
		circuit2D.addComponent(component1);
		circuit2D.setComponentLocation(component1, point1);
		assertEquals(point1, circuit2D.getComponentLocation(component1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSettingNullPosition() {
		circuit2D.setComponentLocation(component1, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSettingNullComponent() {
		circuit2D.setComponentLocation(null, point1);
	}
	
	@Test
	public void testDelegationMoveOnNewComponentWithoutPosition() {
		circuit.addComponent(component1);
		verify(delegator).onComponentMoved(eq(component1), eq((Point) null), any(Point.class));
	}
	
	@Test
	public void testDelegationMoveOnNewComponentWithPosition() {
		circuit2D.addComponent(component1, point1);
		verify(delegator).onComponentMoved(component1, null, point1);
	}
	
	@Test
	public void testDelegationMoveOnComponentMove() {
		circuit2D.addComponent(component1, point1);
		circuit2D.setComponentLocation(component1, point2);
		verify(delegator).onComponentMoved(component1, point1, point2);
	}
	
	@Test
	public void testRemovesPositionOnRemove() {
		circuit2D.addComponent(component1, point1);
		circuit2D.removeComponent(component1);
		assertNull(circuit2D.getComponentLocation(component1));
	}
	
	@Test
	public void testRemovePositionDoesntRemoveAll() {
		circuit2D.addComponent(component1, point1);
		circuit2D.addComponent(component2, point2);
		circuit2D.removeComponent(component1);
		assertNull(circuit2D.getComponentLocation(component1));
		assertSame(circuit2D.getComponentLocation(component2), point2);
	}
}
