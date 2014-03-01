package se.openflisp.gui.swing.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import java.awt.event.MouseEvent;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import se.openflisp.sls.Input;
import se.openflisp.sls.Output;


@SuppressWarnings("serial")
public class WirePanel extends JPanel implements PropertyChangeListener{
	private Map<WireView, List<SignalView>> wires;
	private SimulationBoard simulationBoard;

	public WirePanel(SimulationBoard parent) {
		this.wires = new HashMap<WireView, List<SignalView>>();
		this.simulationBoard = parent;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("drag"))
			drawSignal(evt);
		else if (evt.getPropertyName().equals("released"))
			connectSignal(evt);
	}


	public void connectSignal(PropertyChangeEvent evt) {
		Point location = SwingUtilities.convertPoint((Component)evt.getSource(),((MouseEvent)evt.getNewValue()).getX(), ((MouseEvent)evt.getNewValue()).getY(), simulationBoard);

		if (!(SwingUtilities.getDeepestComponentAt(simulationBoard, location.x,location.y) instanceof SignalView)) {
			for(WireView wire : wires.keySet() ) {
				List<SignalView> signalList = wires.get(wire);
				for (SignalView signalView : signalList){
					if(  signalView.equals(evt.getSource())) {
						remove(wire);
						repaint();
						revalidate();
						wires.remove(wire);
						return;
					}
				}
			}
		} else {
			WireView wire = null;
			SignalView signalFrom = (SignalView)evt.getSource();
			SignalView signalTo	= (SignalView)SwingUtilities.getDeepestComponentAt(simulationBoard, location.x,location.y);

			// Get Wire
			for(WireView w : wires.keySet() ) {
				List<SignalView> signalList = wires.get(w);
				for (SignalView signalView : signalList){
					if(  signalView.equals(evt.getSource())) {
						wire = w;
						break;
					}
				}
			}
			if (wire == null)
				return;

			try {
				signalFrom.component.getOutput(signalFrom.signal.getIdentifier()).connect(signalTo.signal);
				if (signalTo.signal instanceof Input)
					wire.input = signalTo.signal;
				else
					wire.output = signalTo.signal;
			}	catch (IllegalArgumentException e) {
				System.out.println("connection refused!" + e.getMessage());

				remove(wire);

				repaint();
				revalidate();
				wires.remove(wire);
				return;
			}
		}
	}


public void ComponentMoved(se.openflisp.sls.Component component, Point from, Point to) {
	for (Input input : component.getInputs()) {
		for(WireView wire : wires.keySet()) {
			List<SignalView> signalList = wires.get(wire);
			for( SignalView inputWire : signalList) {
				if(input.equals(inputWire.signal)) {
					wire.reset();
					//wire.drawBetweenSignals(inputWire.signal, signalList.get(1));
				}
			}
		}
	}

	for (Output output : component.getOutputs()) {
		for(WireView wire : wires.keySet()) {
			List<SignalView> signalList = wires.get(wire);
			for( SignalView outputWire : signalList) {
				if(output.equals(outputWire.signal)) {
					//wire.drawBetweenSignals(outputWire.signal, signalList.get(0));
				}
			}
		}
	}
}

public void drawSignal(PropertyChangeEvent evt) {
	WireView wire = null;
	SignalView signalView = (SignalView)evt.getSource();

	// Get the correct x-y coordinates for simulationBoard
	Point signalLocation = SwingUtilities.convertPoint((Component)evt.getSource(),signalView.getLocation().x + SignalView.btnSize.width,signalView.getLocation().y - (SignalView.btnSize.height + SignalView.arcLength/2) , simulationBoard);
	Point dragLocation = SwingUtilities.convertPoint((Component)evt.getSource(),((Integer)evt.getOldValue()).intValue(),((Integer)evt.getNewValue()).intValue() , simulationBoard);		

	// Check if we already have a wire at the source
	for(WireView oldWire : wires.keySet()) {
		List<SignalView> signalList = wires.get(oldWire);
		for(SignalView oldSignal : signalList) {
			if( oldSignal.equals(signalView)) {
				wire = oldWire;

				//If we find a wire at the given point it might be connected, if so, we need to disconnect it
				if (signalList.get(0).signal.isConnected()) {
					try {
						wire.output.disconnect(wire.input);
					} catch (Exception e) {
					}
				}
				continue;
			}
		}
	}
	// If we didnt have a wire we create one
	if (wire == null) {
		wire = new WireView();
		if (signalView.signal instanceof Input)
			wire.input = signalView.signal;
		else
			wire.output = signalView.signal;

		ArrayList<SignalView> signalList = new ArrayList<SignalView>();
		signalList.add((SignalView)evt.getSource());
		wires.put(wire, signalList);
		wire.setBounds(0, 0, 1000, 1000);
		WirePanel.this.add(wire);
		wire.moveCurve(signalLocation.x, signalLocation.y);
	}

	// Reset wire and paint it to the current mouse cursor position
	wire.reset();
	wire.moveCurve(signalLocation.x, signalLocation.y);
	wire.drawCurveTo(dragLocation);
	repaint();
	revalidate();
}

@Override
public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	super.paintComponent(g2);
}
}