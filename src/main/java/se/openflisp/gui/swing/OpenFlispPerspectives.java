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
package se.openflisp.gui.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import se.openflisp.gui.perspectives.AsmPerspective;
import se.openflisp.gui.perspectives.Perspective;
import se.openflisp.gui.perspectives.SlsPerspective;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
	
/**	
 * Create OpenFlisp Perspectives
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class OpenFlispPerspectives extends JPanel implements ItemListener {
	
	// Define perspectives 
	List<Perspective> perspectives;
	
	/**
	 * Set layout and initiate all perspectives
	 */
    public OpenFlispPerspectives() {
    	//Set to cardlayout in order to change layout
    	this.setLayout(new CardLayout());
    	
    	//Initialize list
    	perspectives = new ArrayList<Perspective>();
    	
    	//Initialize all the perspectives
    	perspectives.add(new SlsPerspective(new EclipseTheme()));
    	perspectives.add(new AsmPerspective(new EclipseTheme()));
    }
    
    /**
	 * Gets a contentpane on which a combobox and the choosen perspective
	 * is added.
	 * 
	 * @param a content pane
	 */
	public void addComponentToPane(Container pane) {
		//We create a JPanel for our JCombobox
		JPanel comboBoxPane = new JPanel(); //use FlowLayout
		
		//Create combobox items
		List<String> comboBoxItems = new ArrayList<String>();
		for (Perspective perspective : perspectives) {
			comboBoxItems.add(perspective.getIdentifier());
		}
        
        //Create combobox for perspectives
        @SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox comboBox = new JComboBox(comboBoxItems.toArray());
        
        //Disable editing of combobox
        comboBox.setEditable(false);
        
        //Add item listener so we can change layout
        comboBox.addItemListener(this);
        
        //Add the combobox to our JPanel
        comboBoxPane.add(comboBox);
        
        //Add all perspectives to our cardlayout
        for (Perspective perspective : perspectives) {
        	add(perspective.getStation(), perspective.getIdentifier());
        }
                 
        //Populate the combobox
        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(this, BorderLayout.CENTER);   
	}
	
	/**
	 * Listener for changes in the combo box, if a change does occur
	 * the card layout switches to another perspective.
	 */
	public void itemStateChanged(ItemEvent evt) {
			//Get this layout
	        CardLayout cl = (CardLayout)(this.getLayout());
	        
	        //Change to the choosen layout
	        cl.show(this, (String)evt.getItem());
	}
}
