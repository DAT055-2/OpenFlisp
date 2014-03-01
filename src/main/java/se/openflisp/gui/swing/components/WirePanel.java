package se.openflisp.gui.swing.components;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import se.openflisp.sls.Input;

@SuppressWarnings("serial")
public class WirePanel extends JPanel {

	private Map<SignalView, WireView> activeWires = new HashMap<SignalView, WireView>();
	
	private WireView draggedWire;
	
	private SignalView lastSignalEntered;

	protected void addWire(WireView wire) {
		wire.setBounds(0, 0, 1000, 1000);
		this.add(wire);
		this.repaint();
		this.revalidate();
	}
	
	protected void removeWire(WireView wire) {
		this.remove(wire);
		this.repaint();
		this.revalidate();
	}
	
	
	public void handleComponentAdded(ComponentView component) {
		for (SignalView signal : component.getInputViews()) {
			signal.addMouseListener(this.wireCreationHandler);
			signal.addMouseMotionListener(this.wireCreationHandler);
		}
		for (SignalView signal : component.getOutputViews()) {
			signal.addMouseListener(this.wireCreationHandler);
			signal.addMouseMotionListener(this.wireCreationHandler);
		}
	}
	
	public void handleComponentRemoved(ComponentView component) {
		
	}
	
	public void handleComponentMoved(ComponentView component) {
		for (SignalView signal : component.getInputViews()) {
			if (this.activeWires.containsKey(signal)) {
				this.activeWires.get(signal).updatePositions();
			}
		}
		for (SignalView signal : component.getOutputViews()) {
			if (this.activeWires.containsKey(signal)) {
				this.activeWires.get(signal).updatePositions();
			}
		}
	}
	
	private final MouseAdapter wireCreationHandler = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent evt) {
			if (WirePanel.this.draggedWire != null) {
				System.out.println("Detected press before release");
				return;
			}
			if (evt.getComponent() == null || !(evt.getComponent() instanceof SignalView)) {
				System.out.println("Didnt press on SignalView");
				return;
			}
			SignalView start = (SignalView) evt.getComponent();
			
			if (start.signal instanceof Input && WirePanel.this.activeWires.containsKey(start)) {
				// TODO disconnect the last one?
				return;
			}
			
			WirePanel.this.draggedWire = new WireView(WirePanel.this, start);
			WirePanel.this.addWire(WirePanel.this.draggedWire);
			WirePanel.this.lastSignalEntered = null;
			System.out.println("Spawning new wire from " + start.signal);
		}
		
		@Override
		public void mouseDragged(MouseEvent evt) {
			if (WirePanel.this.draggedWire == null) {
				System.out.println("Dragged before pressed!");
				return;
			}
			Point point = SwingUtilities.convertPoint(
				(Component) evt.getSource(),
				evt.getX(),
				evt.getY(), 
				WirePanel.this
			);		
			WirePanel.this.draggedWire.setTemporaryDrawPoint(point);
			WirePanel.this.revalidate();
		}
		
		@Override
		public void mouseEntered(MouseEvent evt) {
			if (evt.getComponent() != null && evt.getComponent() instanceof SignalView) {
				WirePanel.this.lastSignalEntered = (SignalView) evt.getComponent();
				System.out.println("Entered: " + WirePanel.this.lastSignalEntered.signal);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent evt) {
			if (WirePanel.this.draggedWire == null) {
				System.out.println("Released before pressed!");
				return;
			}
			SignalView start = WirePanel.this.draggedWire.getStart();
			SignalView end = WirePanel.this.lastSignalEntered;
			if (end == null || !(evt.getComponent() instanceof SignalView)) {
				System.out.println("Didnt release on a Signal");
				WirePanel.this.removeWire(WirePanel.this.draggedWire);
				WirePanel.this.draggedWire = null;
				WirePanel.this.lastSignalEntered = null;
				return;
			}
			System.out.println("Ending wire on? " + end.signal);
			
			try {
				start.signal.connect(end.signal);
				WirePanel.this.draggedWire.attatchEnd(end);
				
				WirePanel.this.activeWires.put(end, WirePanel.this.draggedWire);
				WirePanel.this.activeWires.put(start, WirePanel.this.draggedWire);
			} catch (IllegalArgumentException e) {
				System.out.println("Connection could not be made: "+ e.getMessage());
				WirePanel.this.removeWire(WirePanel.this.draggedWire);
			} finally {
				WirePanel.this.draggedWire = null;
				WirePanel.this.lastSignalEntered = null;
			}
		}
	};
}