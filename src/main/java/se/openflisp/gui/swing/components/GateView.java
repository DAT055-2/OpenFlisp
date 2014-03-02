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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.openflisp.sls.Component;
import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.Signal;
import se.openflisp.gui.swing.components.ComponentView;
import se.openflisp.sls.component.*;

/**	
 * GateView, a view for all Gate components.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GateView extends ComponentView {
	
	/**
	 * All SignalView objects corresponding to the Components Outputs.
	 */
	private Set<SignalView> outputSignals;
	
	/**
	 * All SignalView objects corresponding to the Components Inputs.
	 */
	private Set<SignalView> inputSignals;
	
	/**
	 * Internal panels containing SignalViews and a JLabel.
	 */
	private JPanel identifierPanel, inputPanel, outputPanel;
	
	/**
	 * Major part of this ComponentView which is displayed as a white box with
	 * an gate symbol in the middle.
	 */
	private JLabel identifier;

	/**
	 * Creates a GateView.
	 * 
	 * @param component		the component model to display
	 */
	public GateView(Component component) {
		super(component);
		
		this.setSize(new Dimension(componentSize * 4, componentSize));
		this.setPreferredSize(new Dimension(componentSize * 2, componentSize));
		this.setMaximumSize(new Dimension(componentSize * 2, componentSize));
		this.setMinimumSize(new Dimension(componentSize * 2, componentSize));
		
		this.setLayout(new BorderLayout());
		this.identifier = new JLabel("", JLabel.CENTER);
		this.identifierPanel = new JPanel(new FlowLayout());
		this.identifierPanel.add(identifier);
		
		if (component instanceof NotGate) {
			this.identifier.setText("1");
		} else if (component instanceof ConstantGate) {
			if (((ConstantGate) this.getComponent()).getConstantState() == Signal.State.HIGH) {
				this.identifier.setText("1");
			} else {
				this.identifier.setText("0");
			}
		} else if ((component instanceof OrGate) || (component instanceof NorGate)) {
			this.identifier.setText("\u22651");
		} else if ((component instanceof AndGate) || (component instanceof NandGate)) {
			this.identifier.setText("&");
		} else if ((component instanceof XorGate) || (component instanceof NxorGate)) {
			this.identifier.setText("=1");
		}
		
		this.identifier.setOpaque(true);
		this.identifier.setBorder(BorderFactory.createLineBorder(Color.black));
		this.identifier.setBackground(Color.WHITE);
		this.identifier.setPreferredSize(new Dimension(componentSize, componentSize));
		this.identifier.setMaximumSize(new Dimension(componentSize, componentSize));
		this.identifier.setMinimumSize(new Dimension(componentSize, componentSize));
		
		this.inputSignals = new HashSet<SignalView>();
		this.outputSignals = new HashSet<SignalView>();
		
		this.inputPanel = new JPanel();
		this.inputPanel.setLayout(new BoxLayout(this.inputPanel, BoxLayout.Y_AXIS));
		
		this.outputPanel = new JPanel();
		this.outputPanel.setLayout(new BoxLayout(this.outputPanel, BoxLayout.Y_AXIS));
		
		this.inputPanel.setPreferredSize(new Dimension(componentSize/2, componentSize));
		this.outputPanel.setPreferredSize(new Dimension(componentSize/2,componentSize));
		
		this.inputPanel.setOpaque(false);
		this.outputPanel.setOpaque(false);
		
		add(inputPanel, BorderLayout.WEST);
		add(identifier, BorderLayout.CENTER);
		add(outputPanel, BorderLayout.EAST);
		
		for(Output output : component.getOutputs()) {
			SignalView out = new SignalView(this, output);
			out.setMaximumSize(SignalView.btnSize);
			this.outputSignals.add(out);
			this.outputPanel.add( Box.createVerticalGlue() );
			this.outputPanel.add(out);
			this.outputPanel.add( Box.createVerticalGlue() );
		}
		
		for(Input input : component.getInputs()) {
			SignalView in = new SignalView(this, input);
			in.setMaximumSize(SignalView.btnSize);
			this.inputSignals.add(in);
			this.inputPanel.add( Box.createVerticalGlue() );
			
			this.inputPanel.add(in);
			this.inputPanel.add( Box.createVerticalGlue() );
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SignalView> getInputViews() {
		return new HashSet<SignalView>(this.inputSignals);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SignalView> getOutputViews() {
		return new HashSet<SignalView>(this.outputSignals);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent getBodyComponent() {
		return this.identifier;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select() {
		super.select();
		this.identifier.setBorder(BorderFactory.createLineBorder(Color.orange));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deselect() {
		super.deselect();
		this.identifier.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SignalView getSignalView(Signal signal) {
		for (SignalView view : this.getInputViews()) {
			if (view.signal == signal) {
				return view;
			}
		}
		for (SignalView view : this.getOutputViews()) {
			if (view.signal == signal) {
				return view;
			}
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SignalView createSignalView(Signal signal) {
		SignalView view = new SignalView(this, signal);
		view.setMaximumSize(SignalView.btnSize);
		if (signal instanceof Output) {
			this.outputSignals.add(view);
			this.outputPanel.add(Box.createVerticalGlue());
			this.outputPanel.add(view);
			this.outputPanel.add(Box.createVerticalGlue());
		} else {
			this.inputSignals.add(view);
			this.inputPanel.add(Box.createVerticalGlue());
			this.inputPanel.add(view);
			this.inputPanel.add(Box.createVerticalGlue());
		}
		this.revalidate();
		return view;
	}
}
