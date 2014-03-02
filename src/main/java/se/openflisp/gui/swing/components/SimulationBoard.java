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
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.Timer;

import se.openflisp.sls.event.CircuitListener;
import se.openflisp.sls.event.ListenerContext;
import se.openflisp.sls.simulation.Circuit2D;
import se.openflisp.sls.Component;
import	se.openflisp.gui.swing.components.ComponentView;
import se.openflisp.gui.util.KeyEventDelegator;

/**	
 * The main JPanel which displays ComponentViews and their connections, signals and behavior.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SimulationBoard extends JPanel {

	/**
	 * Model of the current Circuit that is being shown.
	 */
	private Circuit2D circuit;

	/**
	 * Map over Component and their ComponentViews used to get the view for a certain model.
	 */
	private Map<Component, ComponentView> components;

	/**
	 * Internal panels used for showing the grid background and ComponentViews.
	 */
	private JPanel componentPanel, backgroundPanel;

	/**
	 * Internal panel used for showing the wires between ComponentViews.
	 */
	private WirePanel wirePanel;
	
	/**
	 * Creates a SimulationBoard.
	 */
	public SimulationBoard() {
		this.setLayout(null);

		this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this.dropHandler, true, null));

		this.componentPanel = new JPanel();
		this.componentPanel.setLayout(null);
		this.componentPanel.setOpaque(false);

		this.wirePanel = new WirePanel(this);
		this.wirePanel.setLayout(null);
		this.wirePanel.setOpaque(false);

		this.backgroundPanel = new BackgroundPanel();
		this.backgroundPanel.setOpaque(true);

		this.components = new HashMap<Component, ComponentView>();
		this.add(this.backgroundPanel, 0, 0);
		this.add(this.wirePanel, 1, 0);
		this.add(this.componentPanel, 2, 0);	
		
		this.addMouseListener(this.deselectionHandler);
		
		KeyEventDelegator.addKeyAction(
			this, 
			KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), 
			this.componentDeletionHandler
		);
		
		this.circuit = new Circuit2D();
		this.circuit.getSimulation().start();
		
		this.circuit.getEventDelegator().addListener(ListenerContext.SWING, this.circtuitHandler);		
	}

	/**
	 * Clears the current Circuit model and adds all Components from another Circuit.
	 * 
	 * @param circuit		circuit to change to
	 */
	public void switchCircuit(Circuit2D circuit) {
		this.clearBoard();
		
		this.circuit.getSimulation().interrupt();
		this.circuit = circuit;
		this.circuit.getSimulation().start();
		this.circuit.getEventDelegator().addListener(ListenerContext.SWING, circtuitHandler);
		
		for (Component component : SimulationBoard.this.circuit.getComponents()) {
			SimulationBoard.this.addComponent(ComponentFactory.createGateFromComponent(component));
		}
		
		SimulationBoard.this.repaint();
		SimulationBoard.this.revalidate();
		
		Timer timer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Component component : SimulationBoard.this.circuit.getComponents()) {
					SimulationBoard.this.wirePanel.handleComponentAdded(SimulationBoard.this.components.get(component));
				}
				
				SimulationBoard.this.repaint();
				SimulationBoard.this.revalidate();
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * Gets the model of the current Circuit that is being shown.
	 * 
	 * @return model of the current Circuit that is being shown
	 */
	public Circuit2D getCircuit() {
		return this.circuit;
	}
	
	/**
	 * Adds a component to the SimulationBoard.
	 * 
	 * @param component		the component to be added
	 */
	public void addComponent(ComponentView component) {
		component.setOpaque(false);
		Point position = this.circuit.getComponentLocation(component.getComponent());
		System.out.println("Adding component at: " + position);
		component.setBounds(
			position.x,
			position.y, 
			ComponentView.componentSize * 2, 
			ComponentView.componentSize
		);
		this.componentPanel.add(component);
		this.components.put(component.getComponent(), component);
		
		component.addMouseListener(this.componentMovementHandler);
		component.addMouseMotionListener(this.componentMovementHandler);
	}
	
	/**
	 * Removes a component from the SimulationBoard.
	 * 
	 * @param component		the component to be removed
	 */
	public void removeComponent(ComponentView component) {
		this.components.remove(component.getComponent());
		this.componentPanel.remove(component);
	}
	
	/**
	 * Removes all components from the SimulationBoard.
	 */
	public void clearBoard() {
		for (Entry<Component, ComponentView> entry : SimulationBoard.this.components.entrySet()) {
			SimulationBoard.this.circuit.removeComponent(entry.getKey());
		}
		SimulationBoard.this.repaint();
		SimulationBoard.this.revalidate();
	}

	/**
	 * Gets the ComponentView for a certain Component model
	 * 
	 * @param component		the component model to lookup
	 * @return the ComponentView that corresponds to a component model
	 */
	public ComponentView getComponentView(Component component) {
		return this.components.get(component);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		this.backgroundPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.componentPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.wirePanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		super.paintComponent(g2);	
	}
	
	/**
	 * Handles when a Component has been added, removed or moved in the Circuit model.
	 */
	private final CircuitListener circtuitHandler = new CircuitListener() {
		@Override
		public void onComponentAdded(Component component) {
			SimulationBoard.this.addComponent(ComponentFactory.createGateFromComponent(component));
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
		
		@Override
		public void onComponentMoved(Component component, Point from, Point to) {
			SimulationBoard.this.components.get(component).setBounds(to.x,to.y,ComponentView.componentSize*2,ComponentView.componentSize);
			SimulationBoard.this.wirePanel.handleComponentMoved(SimulationBoard.this.components.get(component));
		}
	};
	
	/**
	 * Handles the deselection that should happen when a user clicks on empty space.
	 */
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

	/**
	 * Handles the drag and drop of Components within the SimulatiomBoard.
	 */
	private final MouseAdapter componentMovementHandler = new MouseAdapter()  {
		private Point point;
		private ComponentView draggedComponent;
		
		@Override
		public void mouseDragged(MouseEvent evt) {
			if (this.draggedComponent != null && this.point != null) {
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
				this.point = evt.getPoint();
				
				if (this.draggedComponent.isSelected()) {
					this.draggedComponent.deselect();
				} else {
					this.draggedComponent.select();
				}
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent evt) {
			this.draggedComponent = null;
		}
	};
	
	/**
	 * Handles the deletion of selected Components.
	 */
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
	
	/**
	 * Handles the drop event that ComponentPanel generate when a new Component is dropped on the
	 * SimulationBoard.
	 */
	private final DropTargetListener dropHandler =  new DropTargetAdapter() {
		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
				Transferable tr = dtde.getTransferable();
				String identifier = (String) tr.getTransferData(DataFlavor.stringFlavor);
				GateView view = ComponentFactory.createGateFromIdentifier(identifier);
				if (view != null) {
					SimulationBoard.this.circuit.addComponent(view.getComponent());
					SimulationBoard.this.circuit.setComponentLocation(
						view.getComponent(), 
						new Point(
							dtde.getLocation().x, 
							dtde.getLocation().y
						)
					);
				}
			} catch (UnsupportedFlavorException e) {
			} catch (IOException e) {}
		}
	};
	
	/**
	 * A panel that draws a gray grid.
	 * 
	 * @author Daniel Svensson <daniel@dsit.se>
	 * @version 1.0
	 */
	public class BackgroundPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0xCC, 0xCC, 0xCC));
			g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight());
			g2.setColor(new Color(0xCD, 0xCD, 0xCD));
			paintGrid(g2, this.getWidth(), this.getHeight());
		}

		public void paintGrid(Graphics g, int gridWidth, int gridHeight) {
			for (int i = 1; i < gridWidth; i = i + 10) {
				g.drawLine(i, 0, i, gridHeight);          
			}      
			for (int i = 1; i < gridHeight; i = i + 10) {      
				g.drawLine(0, i, gridWidth, i);          
			} 
		}
	}
}