package se.openflisp.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import se.openflisp.gui.swing.components.ComponentView;
import se.openflisp.gui.swing.components.GateView;
import se.openflisp.sls.Component;
import se.openflisp.sls.component.AndGate;
import se.openflisp.sls.component.ConstantGate;
import se.openflisp.sls.component.Gate;
import se.openflisp.sls.component.NandGate;
import se.openflisp.sls.component.NorGate;
import se.openflisp.sls.component.NotGate;
import se.openflisp.sls.component.OrGate;
import se.openflisp.sls.component.XorGate;
import se.openflisp.sls.simulation.Circuit2D;
import se.openflisp.sls.Signal;

/**
 * Test the functionality of the gates
 * 
 * @author Johan Strand
 * @version 1.0
 *
 */

public class GateSimulation extends JPanel implements ActionListener	{
	private JPanel leftPanel, rightPanel, mainPanel;
	
	ArrayList<JRadioButton> buttonList;//list for the radiobuttons
	
	// The circuit we are simulating
	private Circuit2D circuit;
	
	// In order to match component with componentViews
	private Map<Component, ComponentView> components;
	
	// A panel containing the components
	private JPanel componentLayer;
	
	// Two gates to simulate signal "High" and signal "Low"
	private ConstantGate ett = new ConstantGate("ett", Signal.State.HIGH);
	private ConstantGate noll = new ConstantGate("noll", Signal.State.LOW);

	// An array containing all the gates
	private ArrayList<Gate> gateArray;

	public GateSimulation()	{
		/* Set layout to this JPanel */	
		this.setLayout(new GridBagLayout());	//the mainpanel
		
		mainPanel = new JPanel(new BorderLayout());
		Border blackline = BorderFactory.createLineBorder(Color.black);
		mainPanel.setBorder(blackline);
		mainPanel.setPreferredSize(new Dimension(250,430));
		mainPanel.setBackground(Color.green);
		this.add(mainPanel, new GridBagConstraints());	//set to center of mainpanel
		
		leftPanel = new JPanel(); //left panel with text and radiobuttons for signal generation
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		rightPanel = new JPanel(); //right panel with the gates
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(leftPanel, BorderLayout.LINE_START);	//add the left panel to the left in grouppanel
		mainPanel.add(rightPanel, BorderLayout.CENTER);	//add the right panel to the right in grouppanel
		
		/* Create some extra space between border and components */
		rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		leftPanel.add(Box.createRigidArea(new Dimension(10, 100)));
		leftPanel.add(new JLabel(" Generera signal:"));
		
		/* Create circuit simulation */
		
		this.circuit = new Circuit2D();
		this.circuit.getSimulation().start();
		this.componentLayer = new JPanel();
		this.components = new HashMap<Component, ComponentView>();
		
		/* Create the gates and add them to gateArray */
		gateArray = new ArrayList<Gate>();
		
		gateArray.add(new NotGate("not"));
		gateArray.add(new AndGate("and"));
		gateArray.add(new NandGate("nand"));
		gateArray.add(new OrGate("or"));
		gateArray.add(new NorGate("nor"));
		gateArray.add(new XorGate("xor"));
		gateArray.add(new AndGate("and3"));
		
		/* Create view for gates and gate view for all gates in gateArray */
		for (Gate g : gateArray)	{
			g.getInput("input1");	//create input1 for gate
			noll.getOutput().connect(g.getInput("input1")); //connect signal low
			
			if (!g.getIdentifier().equals("not"))	{	//for all gates except the not-gate
				g.getInput("input2");
				noll.getOutput().connect(g.getInput("input2"));
				
				if (g.getIdentifier().equals("and3")) {	//for the 3 input and-gate
					g.getInput("input3");
					noll.getOutput().connect(g.getInput("input3"));
				}
			}
			this.circuit.addComponent(g);
			
			ComponentView view = new GateView(g);
			view.setOpaque(false);
			this.componentLayer.add(view);
			view.setBounds(200,200,200,100);
			this.components.put(g, view);
			rightPanel.add(view);
			rightPanel.add(Box.createRigidArea(new Dimension(5, 10)));	//some extra space between components
			
		}
		 /* Three radiobuttons for signals */
		buttonList = new ArrayList<JRadioButton>();
		for (int i = 0; i<3; i++)	{
			JRadioButton b = new JRadioButton("0");
			b.addActionListener(this);
			buttonList.add(b);
			leftPanel.add(b);
		}
	}
	
	/*
	 * Handles the three radiobuttons and sets high or low signal to the gates
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	
	public void actionPerformed (ActionEvent e) {
		JRadioButton b = (JRadioButton)e.getSource();
		/* Button 1 */
		if (b == buttonList.get(0))	{
			if (b.getText().equals("0"))	{
				b.setText("1");
				for (int i = 0; i < gateArray.size(); i++){
					connect(gateArray.get(i), "input1");
				}	
			}
			else {
				b.setText("0");
				for (int i = 0; i < gateArray.size(); i++){
					disconnect(gateArray.get(i), "input1");
				}
			}
		}
		
		/* Button 2 */
		if (b == buttonList.get(1))	{
			if (b.getText().equals("0"))	{
				b.setText("1");
				for (int i = 1; i < gateArray.size(); i++){
					connect(gateArray.get(i), "input2");
				}
			}
			else {
				b.setText("0");
				for (int i = 1; i < gateArray.size(); i++){
					disconnect(gateArray.get(i), "input2");
				}
			}	
		}
		
		/* Button 3
		 * Only for gates with three inputs. (In this case the three input andgate)
		 */
		if (b == buttonList.get(2))	{
			if (b.getText().equals("0")) {
				b.setText("1");
				for (int i = 1; i < gateArray.size(); i++)	{
					if (gateArray.get(i).getInputs().size() == 3)	{
						connect(gateArray.get(i), "input3");
					}
				}
			}
			else {
				b.setText("0");
				for (int i = 1; i < gateArray.size(); i++)	{
					if (gateArray.get(i).getInputs().size() == 3)	{
						disconnect(gateArray.get(i), "input3");
					}
				}
			}
		}
		
		rightPanel.repaint(); //updates the panel with the gates
	}
	
	/*
	 * Connects the input of a gate from signal low to signal high
	 * 
	 * @param gate 		The gate to connect
	 * @param input		Which input of the gate to connect
	 */
	private void connect(Gate gate, String input)	{
		noll.getOutput().disconnect(gate.getInput(input));
		ett.getOutput().connect(gate.getInput(input));
	}
	/*
	 * Disconnects the input of a gate. (Sets the signal to low)
	 * 
	 * @param gate 		The gate to connect
	 * @param input		Which input of the gate to disconnect
	 */
	private void disconnect(Gate g, String input)	{
		ett.getOutput().disconnect(g.getInput(input));
		noll.getOutput().connect(g.getInput(input));
		
	}
}