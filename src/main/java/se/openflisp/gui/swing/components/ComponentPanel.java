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

import javax.swing.JPanel;

import se.openflisp.sls.Component;
import se.openflisp.sls.Signal;
import se.openflisp.sls.component.AndGate;
import se.openflisp.sls.component.ConstantGate;
import se.openflisp.sls.component.NandGate;
import se.openflisp.sls.component.NorGate;
import se.openflisp.sls.component.NotGate;
import se.openflisp.sls.component.NxorGate;
import se.openflisp.sls.component.OrGate;
import se.openflisp.sls.component.XorGate;

/**	
 * Component for showing all components and enabling drag and drop creation.
 * 
 * @author oskar selberg <oskar.selberg@gmail.com>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ComponentPanel extends JPanel {
	//Layout for the panel
	private GridBagLayout layout;
	
	//Constraints for the layout
	private GridBagConstraints constraints;
	
	//All components to show
	private ComponentView andGate;
	private ComponentView notGate;
	private ComponentView constantGateLOW;
	private ComponentView constantGateHIGH;
	private ComponentView nandGate;
	private ComponentView orGate;
	private ComponentView norGate;
	private ComponentView xorGate;
	private ComponentView nxorGate;
	
	//Component for gate initialization
	private Component gate;
	
	/**
	 * Creates the component panel, from which you can drag-out components to the SimulationBoard
	 */
	public ComponentPanel() {
		//Initiate variables
		constraints = new GridBagConstraints();
        layout = new GridBagLayout();
        
        //set default constraints
        constraints.weighty = 1.0;	//add space between the components
        
        //add the constraints to the layout
		layout.setConstraints(this, constraints);
		
		//add the layout to this component
		this.setLayout(layout);
		
		//Initiate the ComponentViews
		gate = new ConstantGate("ConstantOneGate", Signal.State.LOW);
		constantGateLOW = new GateView(gate);
		
		gate = new ConstantGate("ConstantZeroGate", Signal.State.HIGH);
		constantGateHIGH = new GateView(gate);
		
		gate = new AndGate("Andgate");
		gate.getInput("input");
		gate.getInput("input2");
		andGate = new GateView(gate);
		
		gate = new NotGate("Notgate");
		gate.getInput("input");
		gate.getInput("input2");
		notGate = new GateView(gate);
		
		gate = new NandGate("NandGate");
		gate.getInput("input");
		gate.getInput("input2");
		nandGate = new GateView(gate);
		
		gate = new OrGate("OrGate");
		gate.getInput("input");
		gate.getInput("input2");
		orGate = new GateView(gate);
		
		gate = new NorGate("NorGate");
		gate.getInput("input");
		gate.getInput("input2");
		norGate = new GateView(gate);	
		
		gate = new XorGate("XorGate");
		gate.getInput("input");
		gate.getInput("input2");
		xorGate = new GateView(gate);
		
		gate = new NxorGate("NxorGate");
		gate.getInput("input");
		gate.getInput("input2");
		nxorGate = new GateView(gate);
		
		//set the size of the components
		andGate.setMaximumSize(new Dimension(ComponentView.componentSize,2));
		notGate.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));
		constantGateLOW.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));
		constantGateHIGH.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));
		nandGate.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));
		orGate.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));
		norGate.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));	
		xorGate.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));	
		nxorGate.setMaximumSize(new Dimension(ComponentView.componentSize,ComponentView.componentSize/2));	

		
		//Add the ConstantGate (Signal.State.HIGH) to CompontPanel
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add( constantGateHIGH, constraints );
		
		//Add the ConstantGate (Signal.State.LOW) to CompontPanel
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add( constantGateLOW, constraints );
		
		//Add the NotGate to CompontPanel
		constraints.gridx = 0;
		constraints.gridy = 2;
		this.add( notGate, constraints );
		
		//Add the AndGate to CompontPanel
		constraints.gridx = 0;
		constraints.gridy = 3;
		this.add( andGate, constraints );
	
		//Add the OrGate to CompontPanel
		constraints.gridx = 0;
		constraints.gridy = 4;
		this.add( orGate, constraints );

		//Add the NandGate to CompontPanel
		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add( nandGate, constraints );		
		
		//Add the NorGate to CompontPanel
		constraints.gridx = 1;
		constraints.gridy = 1;
		this.add( norGate, constraints );	

		//Add the XorGate to CompontPanel
		constraints.gridx = 1;
		constraints.gridy = 2;
		this.add(xorGate, constraints );	
		
		//Add the NxorGate to CompontPanel
		constraints.gridx = 1;
		constraints.gridy = 3;
		this.add(nxorGate, constraints );	
		
	
		/**
		 * Enable drag and drop listener, by sending a string to the receiver
		 */
		this.notGate.ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new DragGestureListener() {
			
			@Override
			public void dragGestureRecognized(DragGestureEvent event) {
				try {
					GateView view = (GateView)getComponentAt(event.getDragOrigin());
					if (view.component instanceof AndGate)
						event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("AndGate"));
					else if (view.component instanceof NotGate)
						event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("NotGate"));
					else if (view.component instanceof ConstantGate) {
						if ( ((ConstantGate)view.component).getConstantState() == Signal.State.HIGH)
							event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("ConstantOneGate"));
						else
							event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("ConstantZeroGate"));	
					} 
					else if (view.component instanceof NandGate)
						event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("NandGate"));
					else if (view.component instanceof OrGate)
						event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("OrGate"));
					else if (view.component instanceof NorGate)
						event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("NorGate"));
					else if (view.component instanceof XorGate)
						event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("XorGate"));
					else if (view.component instanceof NxorGate)
						event.startDrag(ComponentPanel.this.notGate.ds.DefaultMoveDrop, new StringSelection("NxorGate"));
				} catch(Exception e) {
					e.printStackTrace();	
				}
			}
		});
	}
}