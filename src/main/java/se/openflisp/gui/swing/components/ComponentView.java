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

import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import se.openflisp.sls.Component;
import se.openflisp.sls.Signal;

/**	
 * A view for a logical gate Component.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class ComponentView extends JPanel {
	
	/**
	 * Component model that is being displayed.
	 */
	private final Component component;
	
	/**
	 * If the Component is selected, for deletion or movement.
	 */
	private boolean selected = false;
	
	/**
	 * Constant for how big the Component should be, in pixels.
	 */
	protected static int componentSize = 50;
	
	/**
	 * Creates a ComponentView from a Component model.
	 * 
	 * @param component		component model to display
	 */
	public ComponentView(Component component) {
		this.component = component;
	}
	
	/**
	 * Gets the Component model for the view.
	 * 
	 * @return the Component model
	 */
	public Component getComponent() {
		return this.component;
	}
	
	/**
	 * Checks if the Component is selected.
	 * 
	 * @return true if the component is selected, false otherwise
	 */
	public boolean isSelected() {
		return this.selected;
	}
	
	/**
	 * Selects this component. If already selected nothing happens.
	 */
	public void select() {
		this.selected = true;
	}
	
	/**
	 * Deselects this component. If not selected nothing happens.
	 */
	public void deselect() {
		this.selected = false;
	}
	
	/**
	 * Gets this components major body component. Which takes up most space and should
	 * react to eventual dragging events.
	 * 
	 * @return major internal JComponent
	 */
	public abstract JComponent getBodyComponent();
	
	/**
	 * Gets all SignalViews for inputs on this Component.
	 * 
	 * @return unmodifiable set of SignalViews for the inputs
	 */
	public abstract Set<SignalView> getInputViews();
	
	/**
	 * Gets all SignalViews for outputs on this Component.
	 * 
	 * @return unmodifiable set of SignalViews for the outputs
	 */
	public abstract Set<SignalView> getOutputViews();
	
	/**
	 * Gets the corresponding SignalView for a given Signal model.
	 * 
	 * @param signal	signal model 
	 * @return a SignalView corresponding to a Signal model
	 */
	public abstract SignalView getSignalView(Signal signal);
	
	/**
	 * Creates a new SignalView for a given Signal model.
	 * 
	 * @param signal	signal model
	 * @return a new SignalView corresponding to a Signal model
	 */
	public abstract SignalView createSignalView(Signal signal);
}
