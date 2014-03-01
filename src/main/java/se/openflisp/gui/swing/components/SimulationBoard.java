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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import se.openflisp.sls.event.CircuitListener;
import se.openflisp.sls.event.ListenerContext;
import se.openflisp.sls.simulation.Circuit2D;
import se.openflisp.sls.Component;
import	se.openflisp.gui.swing.components.ComponentView;
import se.openflisp.gui.util.KeyEventDelegator;

/**	
 * The Board for simulating gates 
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SimulationBoard extends JPanel {

	// For drag and drop support
	private DropTarget dropTarget;

	// The circuit we are simulating
	public Circuit2D circuit;

	// In order to match component with componentViews
	private Map<Component, ComponentView> components;

	// A panel containing the components
	private JPanel componentLayer;

	// A panel containing the background
	private JPanel backgroundPanel;

	// A panel containing wires
	private WirePanel wirePanel;
	
	/**
	 * Creates the simulation board
	 */
	public SimulationBoard() {

		// Handle drop events
		this.dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetListener() {
			@Override
			public void drop(DropTargetDropEvent dropEvent) {
				// Try to convert the transferable to a string and send it to ComponentFactory for creation
				try {
					Transferable tr = dropEvent.getTransferable();
					String identifier;
					identifier = (String)tr.getTransferData(DataFlavor.stringFlavor);
					GateView view = ComponentFactory.createGateFromIdentifier(identifier);

					if (view != null) {
						// Add the components to circuit and move it
						SimulationBoard.this.circuit.addComponent(view.getComponent());
						SimulationBoard.this.circuit.setComponentLocation(view.getComponent(), new Point(dropEvent.getLocation().x, dropEvent.getLocation().y));
					}
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void dropActionChanged(DropTargetDragEvent arg0) {
			}

			@Override
			public void dragEnter(DropTargetDragEvent arg0) {
			}

			@Override
			public void dragExit(DropTargetEvent arg0) {
			}

			@Override
			public void dragOver(DropTargetDragEvent arg0) {
			}

		}, true, null);

		// We need absolute positioning
		this.setLayout(null);

		// For drag and drop support
		this.setDropTarget(dropTarget);

		// Create the circuit and start simulation
		this.circuit = new Circuit2D();
		this.circuit.getSimulation().start();

		// Instantiate the componentLayer and set opaque
		this.componentLayer = new JPanel();
		this.componentLayer.setLayout(null);
		this.componentLayer.setOpaque(false);

		this.wirePanel = new WirePanel();
		this.wirePanel.setLayout(null);
		this.wirePanel.setOpaque(false);

		this.backgroundPanel = new BackgroundPanel();
		this.backgroundPanel.setOpaque(true);

		// This will add the panels to our layeredPane in order to make a transparent components
		this.components = new HashMap<Component, ComponentView>();
		this.add(backgroundPanel, new Integer(0), 0);
		this.add(wirePanel, new Integer(1), 0);
		this.add(componentLayer, new Integer(2),0);		

		this.componentLayer.setFocusable(true);
		this.componentLayer.requestFocusInWindow();

		// Set a listener on the circuit
		this.circuit.getEventDelegator().addListener(ListenerContext.SWING, circtuitHandler);
		
		this.addMouseListener(this.deselectionHandler);
		
		KeyEventDelegator.addKeyAction(
			this, 
			KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), 
			this.componentDeletionHandler
		);
	}

	/**
	 * Adds a component to the simulation board
	 * @param component		the component to be added
	 */
	public void addComponent(ComponentView component) {
		component.setOpaque(false);
		this.componentLayer.add(component);
		this.components.put(component.getComponent(), component);
		
		component.addMouseListener(this.componentMovementHandler);
		component.addMouseMotionListener(this.componentMovementHandler);
	}
	
	public void removeComponent(ComponentView component) {
		this.components.remove(component.getComponent());
		this.componentLayer.remove(component);
	}

	/**
	 * We need to override the painting
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		this.backgroundPanel.setBounds(0,0,this.getWidth(),this.getHeight());
		this.componentLayer.setBounds(0,0,this.getWidth(),this.getHeight());
		this.wirePanel.setBounds(0,0,this.getWidth(),this.getHeight());
		super.paintComponent(g2);	
	}

	/**
	 * Listener for the circuit
	 */
	protected final CircuitListener circtuitHandler = new CircuitListener() {
		/**
		 * Will add a new component to the circuit
		 * @param Component		the component to be added
		 */
		@Override
		public void onComponentAdded(Component component) {
			addComponent(ComponentFactory.createGateFromComponent(component));
			SimulationBoard.this.wirePanel.handleComponentAdded(SimulationBoard.this.components.get(component));
			SimulationBoard.this.repaint();
			SimulationBoard.this.revalidate();
		}

		@Override
		public void onComponentRemoved(Component component) {
			SimulationBoard.this.wirePanel.handleComponentRemoved(SimulationBoard.this.components.get(component));
			SimulationBoard.this.removeComponent(SimulationBoard.this.components.get(component));
			SimulationBoard.this.repaint();
			SimulationBoard.this.revalidate();
		}

		/**
		 * Repaints the component when it has moved
		 * 
		 */
		@Override
		public void onComponentMoved(Component component, Point from, Point to) {
			SimulationBoard.this.components.get(component).setBounds(to.x,to.y,ComponentView.componentSize*2,ComponentView.componentSize);
			SimulationBoard.this.wirePanel.handleComponentMoved(SimulationBoard.this.components.get(component));
		}
	};

	/**
	 * 	Creates a grid in the background layer
	 */
	public class BackgroundPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0xCC, 0xCC, 0xCC));
			g2.drawRect(0, 0, getWidth()-1, getHeight());
			g2.setColor(new Color(0xCD, 0xCD, 0xCD));
			paintGrid(g2,this.getWidth(),this.getHeight());    		
		}

		public void paintGrid(Graphics g, int gridWidth, int gridHeight) {
			for(int i=1; i<gridWidth; i=i+10)
			{
				g.drawLine(i, 0,      i,      gridHeight);          
			}      

			for(int i=1; i<gridHeight; i=i+10)
			{      
				g.drawLine(0, i, gridWidth, i);          
			} 
		}
	}
	
	private final MouseAdapter deselectionHandler = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent evt) {
			System.out.println("Clicked on empty space. Deselect all components and wires!");
			for (Entry<Component, ComponentView> entry : SimulationBoard.this.components.entrySet()) {
				entry.getValue().deselect();
			}
			SimulationBoard.this.wirePanel.deselectAllWires();
		}
	};

	private final MouseAdapter componentMovementHandler = new MouseAdapter()  {
		private Point point;
		private ComponentView draggedComponent;
		
		@Override
		public void mouseDragged(MouseEvent evt) {
			if (this.draggedComponent != null) {
				Point componentLocation = SimulationBoard.this.circuit.getComponentLocation(
					this.draggedComponent.getComponent()
				);
				Point point = new Point(
						componentLocation.x + (evt.getX() - this.point.x), 
						componentLocation.y + (evt.getY() - this.point.y)
				);
				SimulationBoard.this.circuit.setComponentLocation(
					this.draggedComponent.getComponent(), 
					point
				);
				
			}
		}
		
		@Override
		public void mousePressed(MouseEvent evt) {
			if (evt.getComponent().getComponentAt(evt.getPoint()) instanceof JLabel) {
				System.out.println("Component pressed: " + evt);
				
				this.draggedComponent = (ComponentView) evt.getComponent();
				this.draggedComponent.select();
				this.point = evt.getPoint();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent evt) {
			this.draggedComponent = null;
		}
	};
	
	private final Action componentDeletionHandler = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent evt) {
			for (Entry<Component, ComponentView> entry : SimulationBoard.this.components.entrySet()) {
				if (entry.getValue().isSelected()) {
					System.out.println("Removing " + entry.getKey());
					SimulationBoard.this.circuit.removeComponent(entry.getKey());
				}
			}
		}
	};
	
}