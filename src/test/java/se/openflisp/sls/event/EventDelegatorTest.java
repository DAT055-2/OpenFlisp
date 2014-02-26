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
package se.openflisp.sls.event;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.openflisp.sls.Component;
import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.Signal;

public abstract class EventDelegatorTest<T> {
	
	public EventDelegator<T> eventDelegator;
	public T listener;
	public Component component;
	public Signal signal;
	public Input input;
	public Output output;
	private List<T> listeners;
	private static final int NR_OF_DEFAULT_LISTENERS = 9;
	private static final int NR_OF_SWING_LISTENERS = 3;
	private static final int NR_OF_MODEL_LISTENERS = 7;

	@Before
	public void setup() {
		listener = Mockito.mock(getListenerClass());
		eventDelegator = getDelegatorInstance();
	}

	@Test
	public void testNoListenersAtStart() {
		assertThat(eventDelegator.getListeners(ListenerContext.DEFAULT).size(), is(0));
		assertThat(eventDelegator.getListeners(ListenerContext.SWING).size(), is(0));
		assertThat(eventDelegator.getListeners(ListenerContext.MODEL).size(), is(0));
		assertThat(eventDelegator.getModelListeners().size(), is(0));
		assertThat(eventDelegator.getSwingListeners().size(), is(0));
		assertThat(eventDelegator.getNormalListeners().size(), is(0));
	}

	@Test
	public void testGettingSameListeners_DEFAULT() {
		assertTrue(eventDelegator.addListener(ListenerContext.DEFAULT, listener));
		assertSame(listener, eventDelegator.getListeners(ListenerContext.DEFAULT).get(0));
		assertSame(eventDelegator.getListeners(ListenerContext.DEFAULT).get(0),
			eventDelegator.getNormalListeners().get(0));
	}

	@Test
	public void testGettingSameListenerList_DEFAULT() {
		eventDelegator.addListener(ListenerContext.DEFAULT, listener);
		eventDelegator.addListener(ListenerContext.DEFAULT, listener);
		assertEquals(eventDelegator.getNormalListeners(), eventDelegator.getNormalListeners());
	}

	@Test
	public void testGettingSameListeners_MODEL() {
		assertTrue(eventDelegator.addListener(ListenerContext.MODEL, listener));
		assertSame(listener, eventDelegator.getModelListeners().get(0));
		assertSame(eventDelegator.getListeners(ListenerContext.MODEL).get(0),
			eventDelegator.getModelListeners().get(0));
	}
	
	@Test
	public void testGettingSameListeners_SWING() {
		assertTrue(eventDelegator.addListener(ListenerContext.SWING, listener));
		assertSame(listener, eventDelegator.getSwingListeners().get(0));
		assertSame(eventDelegator.getListeners(ListenerContext.SWING).get(0),
			eventDelegator.getSwingListeners().get(0));
	}

	@Test
	public void testGettingListenersMany() {
		assertTrue(eventDelegator.addListener(listener));
		assertThat(eventDelegator.getNormalListeners().size(), is(1));

		assertThat(eventDelegator.getNormalListeners().size(), is(1));

		assertTrue(eventDelegator.addListener(listener));
		assertThat(eventDelegator.getNormalListeners().size(), is(2));
	}

	@Test
	public void testGettingModelListenersBeforeAdding() {
		assertThat(eventDelegator.getModelListeners().size(), is(0));
	}

	@Test
	public void testGettingModelListeners2() {
		assertTrue(eventDelegator.addListener(ListenerContext.MODEL, listener));
		List<T> listenerList = eventDelegator.getModelListeners();
		assertThat(listenerList.size(), is(1));
		assertSame(listenerList.get(0), listener);
	}

	@Test
	public void testGettingModelListenersMany() {
		assertTrue(eventDelegator.addListener(ListenerContext.MODEL, listener));
		assertThat(eventDelegator.getModelListeners().size(), is(1));

		assertThat(eventDelegator.getModelListeners().size(), is(1));

		assertTrue(eventDelegator.addListener(ListenerContext.MODEL, listener));
		assertThat(eventDelegator.getModelListeners().size(), is(2));
	}

	@Test
	public void testGettingNormalListenersMany() {
		assertTrue(eventDelegator.addListener(listener));
		assertThat(eventDelegator.getNormalListeners().size(), is(1));

		assertThat(eventDelegator.getNormalListeners().size(), is(1));

		assertTrue(eventDelegator.addListener(listener));
		assertThat(eventDelegator.getNormalListeners().size(), is(2));
	}

	@Test
	public void testGettingSwingListenersMany() {
		assertTrue(eventDelegator.addListener(ListenerContext.SWING, listener));
		assertThat(eventDelegator.getSwingListeners().size(), is(1));

		assertThat(eventDelegator.getSwingListeners().size(), is(1));

		assertTrue(eventDelegator.addListener(ListenerContext.SWING, listener));
		assertThat(eventDelegator.getSwingListeners().size(), is(2));
	}

	@Test
	public void testAddingSeveralListeners() {
		assertTrue(eventDelegator.addListener(listener));
		assertThat(eventDelegator.getModelListeners().size(), is(0));
		assertThat(eventDelegator.getModelListeners().size(), is(0));
		assertThat(eventDelegator.getNormalListeners().size(), is(1));
		assertTrue(eventDelegator.addListener(ListenerContext.MODEL, listener));
		assertThat(eventDelegator.getModelListeners().size(), is(1));
		assertThat(eventDelegator.getModelListeners().size(), is(1));

		assertTrue(eventDelegator.addListener(ListenerContext.DEFAULT, listener));
		assertThat(eventDelegator.getModelListeners().size(), is(1));
		assertThat(eventDelegator.getNormalListeners().size(), is(2));
		assertThat(eventDelegator.getModelListeners().size(), is(1));

		assertTrue(eventDelegator.addListener(ListenerContext.SWING, listener));
		assertThat(eventDelegator.getSwingListeners().size(), is(1));
		assertThat(eventDelegator.getNormalListeners().size(), is(2));
		assertThat(eventDelegator.getModelListeners().size(), is(1));
	}

	@Test
	public void testAddingNullListener() {
		assertFalse(eventDelegator.addListener(null));
		assertThat(eventDelegator.getNormalListeners().size(), is(0));
		assertThat(eventDelegator.getModelListeners().size(), is(0));
		assertThat(eventDelegator.getSwingListeners().size(), is(0));
		assertFalse(eventDelegator.addListener(null, listener));
		assertThat(eventDelegator.getNormalListeners().size(), is(0));
		assertThat(eventDelegator.getModelListeners().size(), is(0));
		assertThat(eventDelegator.getSwingListeners().size(), is(0));
	}

	@Test
	public void testAddListeners() {
		EventDelegator delegator = getDelegatorInstance();
		addListeners(delegator, getListenerClass());
		assertThat(delegator.getModelListeners().size(), is(NR_OF_MODEL_LISTENERS));
		assertThat(delegator.getSwingListeners().size(), is(NR_OF_SWING_LISTENERS));
		assertThat(delegator.getNormalListeners().size(), is(NR_OF_DEFAULT_LISTENERS));
	}

	public abstract EventDelegator<T> getDelegatorInstance();
	public abstract Class<T> getListenerClass();

	/**
	 * Creates an anonymous class.
	 * Used for testing that listener methods
	 * are run in correct thread. Swing listener's methods should
	 * run in Event Dispatch Thread and the other should not.
	 *
	 * @param isSwingListener boolean to indicate that the listener
	 * should be a swing listener.
	 */
	 public abstract T createListener(boolean isSwingListener);

	/**
	 * Adds listeners to a delegator.
	 * Uses Constants NR_OF_DEFAULT_LISTENERS,
	 * NR_OF_SWING_LISTENERS and NR_OF_MODEL_LISTENERS
	 */
	public void addListeners(EventDelegator delegator, Class<T> listenerClass) {
		listeners = new ArrayList<T>();
		T tempListener;

		int nrOfListeners = NR_OF_DEFAULT_LISTENERS
			+ NR_OF_SWING_LISTENERS
			+ NR_OF_MODEL_LISTENERS;

		for(int i = 0; i < nrOfListeners; i++) {
			tempListener = Mockito.mock(listenerClass);
			listeners.add(tempListener);
		}
		
		int i = 0;
		for(T l : listeners) {
			if (i < NR_OF_DEFAULT_LISTENERS) {
				delegator.addListener(ListenerContext.DEFAULT, listeners.get(i));
			} else if (i < NR_OF_DEFAULT_LISTENERS + NR_OF_SWING_LISTENERS) {
				delegator.addListener(ListenerContext.SWING, listeners.get(i));
			} else {
				delegator.addListener(ListenerContext.MODEL, listeners.get(i));
			}
			i++;
		}
	}

}
