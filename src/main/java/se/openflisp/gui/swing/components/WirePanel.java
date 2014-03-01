package se.openflisp.gui.swing.components;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import se.openflisp.sls.Input;

@SuppressWarnings("serial")
public class WirePanel extends JPanel {

	private Map<SignalView, WireView> activeWires = new HashMap<SignalView, WireView>();
	
	public WirePanel() {
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), 
			KeyEvent.VK_BACK_SPACE
		);
		this.getActionMap().put(KeyEvent.VK_BACK_SPACE, this.wireDeletionHandler);
		this.addMouseListener(this.wireDeselectionHandler);
	}
	
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
		for (SignalView signal : component.getInputViews()) {
			if (this.activeWires.containsKey(signal)) {
				this.removeWire(this.activeWires.get(signal));
			}
		}
		for (SignalView signal : component.getOutputViews()) {
			if (this.activeWires.containsKey(signal)) {
				this.removeWire(this.activeWires.get(signal));
			}
		}
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
	
	public void deselectAllWires() {
		for (WireView wire : WirePanel.this.activeWires.values()) {
			wire.deselect();
		}
	}
	
	private final MouseAdapter wireDeselectionHandler = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent evt) {
			System.out.println("Clicked on empty space. Deselect all wires!");
			WirePanel.this.deselectAllWires();
		}
	};
	
	private final Action wireDeletionHandler = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent evt) {
			for (WireView wire : WirePanel.this.activeWires.values()) {
				if (wire.getEnd() != null && wire.isSelected()) {
					try {
						wire.getStart().signal.disconnect(wire.getEnd().signal);
						WirePanel.this.removeWire(wire);
					} catch (IllegalArgumentException e) {
						System.out.println("Can not disconnect!" + e.getMessage());
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
				(Component) evt.getSource(),
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
				start.signal.connect(end.signal);
				this.draggedWire.attatchEnd(end);
				
				WirePanel.this.activeWires.put(end, this.draggedWire);
				WirePanel.this.activeWires.put(start, this.draggedWire);
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