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
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import bibliothek.gui.DockController;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;

/**	
 * Create OpenFlisp Perspectives
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
public class OpenFlispPerspectives implements ItemListener {
	// Define perspectives 
	JPanel perspective;
	final static String SLSPERSPECTIVE = "Syncronous logic simulation";
    final static String ASMPERSPECTIVE = "DigiFLISP Simulation";
	
    /**
	 * Gets a contentpane on which a combobox and the choosen perspective
	 * is added.
	 * 
	 * @param a content pane
	 */
	public void addComponentToPane(Container pane) {
		//Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { SLSPERSPECTIVE, ASMPERSPECTIVE };
        
        //Create combobox for perspectives
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
         
        // Create slsPerspective
        DockController slsController = new DockController();
        SplitDockStation slsStation = new SplitDockStation();
        slsController.add(slsStation);
        
        SplitDockGrid slsGrid = new SplitDockGrid();
        slsGrid.addDockable(0, 0, 2, 1, new DefaultDockable("Komponenter"));
        slsGrid.addDockable(0, 1, 1 ,1, new DefaultDockable("Kopplingsarea"));
        slsStation.dropTree( slsGrid.toTree());
        
        // Create asmPerspective
        DockController asmController = new DockController();
        SplitDockStation asmStation = new SplitDockStation();
        asmController.add(asmStation);
        
        SplitDockGrid asmGrid = new SplitDockGrid();
        asmGrid.addDockable(0, 0, 2, 1, new DefaultDockable("Editor"));
        asmGrid.addDockable(0, 1, 1 ,1, new DefaultDockable("Simulator"));
        asmStation.dropTree( asmGrid.toTree());
        //TODO DRY 
        
        //Create the panel that contains the "cards".
        perspective = new JPanel(new CardLayout());
        perspective.add(slsStation,SLSPERSPECTIVE);
        perspective.add(asmStation,ASMPERSPECTIVE);
                 
        //Populate the combobox
        pane.add(comboBoxPane, BorderLayout.LINE_START);
        pane.add(perspective, BorderLayout.CENTER);
        
        //make the new content in container visible
        pane.revalidate();
        pane.repaint();

	}
	
	/**
	 * Listener for changes in the combobox, if a change does occur
	 * the cardlayout switches to another perspective.
	 */
	public void itemStateChanged(ItemEvent evt) {
	        CardLayout cl = (CardLayout)(perspective.getLayout());
	        cl.show(perspective, (String)evt.getItem());
	}
}
