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

import java.awt.dnd.DragSource;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import se.openflisp.sls.Component;
import se.openflisp.sls.Signal;

/**	
 * Basic object for viewing components, will make more sense when we have additional components
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class ComponentView extends JPanel {
	
	private boolean selected = false;
	
	private final Component component;
	
	protected static int componentSize = 50;
	
	// We need this in order to make DragAndDrop
	public DragSource ds = DragSource.getDefaultDragSource();
	
	public ComponentView(Component component) {
		this.component = component;
	}
	
	public Component getComponent() {
		return this.component;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void select() {
		this.selected = true;
	}
	
	public void deselect() {
		this.selected = false;
	}
	
	public abstract JComponent getBodyComponent();
	
	public abstract Set<SignalView> getInputViews();
	
	public abstract Set<SignalView> getOutputViews();
	
	public abstract SignalView getSignalView(Signal signal);
	
}
