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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**	
 * Panel for displaying all gates that can be used in the SimulationBoard.
 * 
 * @author Oskar Selberg <oskar.selberg@gmail.com>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ComponentPanel extends JPanel {
	
	/**
	 * Map over all known components to their identifier, needed in ComponentFactory to create a new
	 * Component when dragged into SimulationBoard.
	 */
	private Map<ComponentView, String> componentIdentifiers = new HashMap<ComponentView, String>();
	
	/**
	 * How many components in each column (y-axis).
	 */
	public static final int COLUMN_SIZE = 4;
	
	/**
	 * Creates a new ComponentPanel and fills it with all known components
	 */
	public ComponentPanel() {
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1.0;
		layout.setConstraints(this, constraints);
		
		String[] gates = {
			"AndGate", "NotGate", "ConstantOneGate", "ConstantZeroGate", "NandGate", 
			"OrGate", "XorGate", "NorGate", "NxorGate"
		};
		int x = 0, y = 0;
		for (String gateIdentifier : gates) {
			GateView gate = ComponentFactory.createGateFromIdentifier(gateIdentifier);
			this.componentIdentifiers.put(gate, gateIdentifier);
			gate.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));
			constraints.gridx = x;
			constraints.gridy = y++;
			this.add(gate, constraints);
			if (y >= COLUMN_SIZE) {
				x++;
				y = 0;
			}
		}
		
		DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(
			this, 
			DnDConstants.ACTION_COPY_OR_MOVE, 
			this.dragHandler
		);
	}
	
	/**
	 * Listens for attempts to initiate drag and drop. If drag and drop happens a new ComponentView is created
	 * and the drag is started. 
	 */
	private final DragGestureListener dragHandler = new DragGestureListener() {
		
		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			JComponent component = (JComponent) ComponentPanel.this.getComponentAt(dge.getDragOrigin());
			if (component instanceof GateView) {
				String gateIdentifier = ComponentPanel.this.componentIdentifiers.get(component);
				if (gateIdentifier != null) {
					DragSource.getDefaultDragSource();
					dge.startDrag(DragSource.DefaultMoveDrop, new StringSelection(gateIdentifier));
				}
			}
		}
	};
}
