package se.openflisp.gui.swing.components;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import se.openflisp.gui.util.KeyEventDelegator;
import se.openflisp.sls.Component;
import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.component.NotGate;

@SuppressWarnings("serial")
public class WirePanel extends JPanel {

	private final SimulationBoard simulationBoard;
	
	private Map<SignalView, List<WireView>> activeWires = new HashMap<SignalView, List<WireView>>();
	
	public WirePanel(SimulationBoard simulationBoard) {
		this.simulationBoard = simulationBoard;
		KeyEventDelegator.addKeyAction(
			this, 
			KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), 
			this.wireDeletionHandler
		);
	}
	
	protected void addWire(WireView wire) {
		wire.setBounds(0, 0, 1000, 1000);
		this.add(wire);
		this.repaint();
		this.revalidate();
	}
	
	protected void removeWire(WireView wire) {
		if (this.activeWires.containsKey(wire.getStart())) {
			this.activeWires.get(wire.getStart()).remove(wire);
		}
		if (this.activeWires.containsKey(wire.getEnd())) {
			this.activeWires.get(wire.getEnd()).remove(wire);
		}
		this.remove(wire);
		this.repaint();
		this.revalidate();
	}
	
	public void handleComponentAdded(ComponentView component) {
		for (SignalView signal : component.getInputViews()) {
			if (signal.signal.isConnected()) {
				WireView wire = new WireView(this, signal);
				
				Output output = ((Input) signal.signal).getConnection();
				
				ComponentView componentView = this.simulationBoard.getComponentView(output.getOwner());
				SignalView end = componentView.getSignalView(output);
				wire.attatchEnd(end);
				
				if (!this.activeWires.containsKey(signal)) {
					this.activeWires.put(signal, new LinkedList<WireView>());
				}
				this.activeWires.get(signal).add(wire);
				if (!this.activeWires.containsKey(end)) {
					this.activeWires.put(end, new LinkedList<WireView>());
				}
				this.activeWires.get(end).add(wire);
				this.addWire(wire);
			}
			signal.addMouseListener(this.wireCreationHandler);
			signal.addMouseMotionListener(this.wireCreationHandler);
		}
		for (SignalView signal : component.getOutputViews()) {
			signal.addMouseListener(this.wireCreationHandler);
			signal.addMouseMotionListener(this.wireCreationHandler);
		}
		this.handleComponentMoved(component);
	}
	
	public void handleComponentRemoved(ComponentView component) {
		Set<WireView> wires = new HashSet<WireView>();
		for (SignalView signal : component.getInputViews()) {
			if (this.activeWires.containsKey(signal)) {
				for (WireView wire : this.activeWires.get(signal)) {
					wires.add(wire);
				}
			}
		}
		for (SignalView signal : component.getOutputViews()) {
			if (this.activeWires.containsKey(signal)) {
				for (WireView wire : this.activeWires.get(signal)) {
					wires.add(wire);
				}
			}
		}
		for (Entry<SignalView, List<WireView>> entry : this.activeWires.entrySet()) {
			Iterator<WireView> it = entry.getValue().iterator();
			while (it.hasNext()) {
				WireView w = it.next();
				if (wires.contains(w)) {
					this.remove(w);
					it.remove();
					
				}
			}
		}
		this.repaint();
		this.revalidate();
	}
	
	public void handleComponentMoved(ComponentView component) {
		for (SignalView signal : component.getInputViews()) {
			if (this.activeWires.containsKey(signal)) {
				for (WireView wire : this.activeWires.get(signal)) {
					wire.updatePositions();
				}
			}
		}
		for (SignalView signal : component.getOutputViews()) {
			if (this.activeWires.containsKey(signal)) {
				for (WireView wire : this.activeWires.get(signal)) {
					wire.updatePositions();
				}
			}
		}
	}
	
	public void deselectAllWires() {
		for (List<WireView> wires : WirePanel.this.activeWires.values()) {
			for (WireView wire: wires) {
				wire.deselect();
			}
		}
	}
	
	private final Action wireDeletionHandler = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent evt) {
			for (Entry<SignalView, List<WireView>> entry : WirePanel.this.activeWires.entrySet()) {
				Iterator<WireView> it = entry.getValue().iterator();
				while (it.hasNext()) {
					WireView wire = it.next();
					if (wire.getEnd() != null && wire.isSelected()) {
						try {
							wire.getStart().signal.disconnect(wire.getEnd().signal);
							WirePanel.this.remove(wire);
							it.remove();
						} catch (IllegalArgumentException e) {
							System.out.println("Can not disconnect!" + e.getMessage());
						}
					}
				}
			}
		}
	};
	
	private final MouseAdapter wireCreationHandler = new MouseAdapter() {
		private WireView draggedWire;
		private SignalView lastSignalEntered;
		
		@Override
		public void mousePressed(MouseEvent evt) {
			if (this.draggedWire != null) {
				System.out.println("Detected press before release");
				return;
			}
			if (evt.getComponent() == null || !(evt.getComponent() instanceof SignalView)) {
				System.out.println("Didnt press on SignalView");
				return;
			}
			SignalView start = (SignalView) evt.getComponent();
			
			if (start.signal instanceof Input && WirePanel.this.activeWires.containsKey(start)) {
				System.out.println("Input already connected!");
				// TODO disconnect the last one?
				return;
			}
			
			this.draggedWire = new WireView(WirePanel.this, start);
			WirePanel.this.addWire(this.draggedWire);
			this.lastSignalEntered = null;
			System.out.println("Spawning new wire from " + start.signal);
		}
		
		@Override
		public void mouseDragged(MouseEvent evt) {
			if (this.draggedWire == null) {
				System.out.println("Dragged before pressed!");
				return;
			}
			Point point = SwingUtilities.convertPoint(
				(JComponent) evt.getSource(),
				evt.getX(),
				evt.getY(), 
				WirePanel.this
			);		
			this.draggedWire.setTemporaryDrawPoint(point);
			WirePanel.this.revalidate();
		}
		
		@Override
		public void mouseEntered(MouseEvent evt) {
			if (evt.getComponent() != null && evt.getComponent() instanceof SignalView) {
				this.lastSignalEntered = (SignalView) evt.getComponent();
				System.out.println("Entered: " + this.lastSignalEntered.signal);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent evt) {
			if (this.draggedWire == null) {
				System.out.println("Released before pressed!");
				return;
			}
			SignalView start = this.draggedWire.getStart();
			SignalView end = this.lastSignalEntered;
			
			if (end == null || !(evt.getComponent() instanceof SignalView)) {
				System.out.println("Didnt release on a Signal");
				WirePanel.this.removeWire(this.draggedWire);
				this.draggedWire = null;
				this.lastSignalEntered = null;
				return;
			}
			System.out.println("Ending wire on? " + end.signal);
			
			try {
				if (end.signal instanceof Input && end.signal.isConnected()) {
					System.out.println("Signal is full!");
					Component component = end.signal.getOwner();
					if (!(component instanceof NotGate) && component.getInputs().size() < 4) {
						Input newInput = component.getInput(Integer.toString(
							component.getInputs().size() + 1
						));
						end = end.getComponentView().createSignalView(newInput);
					}
				}
				
				
				if (end != null && start.signal.connect(end.signal)) {
					this.draggedWire.attatchEnd(end);
					
					if (!WirePanel.this.activeWires.containsKey(end)) {
						WirePanel.this.activeWires.put(end, new LinkedList<WireView>());
					}
					if (!WirePanel.this.activeWires.containsKey(start)) {
						WirePanel.this.activeWires.put(start, new LinkedList<WireView>());
					}
					WirePanel.this.activeWires.get(start).add(this.draggedWire);
					WirePanel.this.activeWires.get(end).add(this.draggedWire);
					
					final SignalView endFinal = end;
					
					Timer timer = new Timer(200, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							WirePanel.this.handleComponentMoved(endFinal.getComponentView());
						}
					});
					timer.setRepeats(false);
					timer.start();
				} else {
					WirePanel.this.removeWire(this.draggedWire);
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Connection could not be made: "+ e.getMessage());
				WirePanel.this.removeWire(this.draggedWire);
			} finally {
				this.draggedWire = null;
				this.lastSignalEntered = null;
			}
		}
	};
}