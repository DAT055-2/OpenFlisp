package se.openflisp.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import se.openflisp.gui.perspectives.SlsPerspective;
import se.openflisp.sls.io.CircuitLegacyReader;
import se.openflisp.sls.simulation.Circuit2D;

/**
 * Creates the menubar and its items
 * 
 * @author Johan Strand <straaand@gmail.com>
 * @version 1.0
 *
 */
public class OpenFlispMenu implements ActionListener {
	
	private final OpenFlispFrame frame;
	
	private JMenuBar menuBar;

	private JMenuItem importBoard, clearBoard, quitApplication, openHelp;
	
	public OpenFlispMenu(OpenFlispFrame frame) {
		this.frame = frame;
		this.initilize();
	}

	protected void initilize()	{
		this.importBoard = new JMenuItem("Importera koppling");
		this.importBoard.addActionListener(this);
		this.importBoard.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_O, ActionEvent.CTRL_MASK
		));
		
		this.clearBoard = new JMenuItem("Rensa kopplingsbordet");
		this.clearBoard.addActionListener(this);
		this.clearBoard.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_BACK_SPACE, ActionEvent.CTRL_MASK
		));
		
		this.quitApplication = new JMenuItem("Avsluta");
		this.quitApplication.addActionListener(this);
		this.quitApplication.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_Q, ActionEvent.META_MASK
		));
		
		this.openHelp = new JMenuItem("Om OpenFlisp");
		this.openHelp.addActionListener(this);
		this.openHelp.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_H, ActionEvent.CTRL_MASK
		));
		
		JMenu archiveMenu = new JMenu("Arkiv");	
		archiveMenu.add(this.importBoard);
		archiveMenu.add(this.clearBoard);
		archiveMenu.add(this.quitApplication);
		
		JMenu helpMenu = new JMenu("Hjälp");
		helpMenu.add(this.openHelp);
		
		this.menuBar = new JMenuBar();
		this.menuBar.add(archiveMenu);
		this.menuBar.add(helpMenu);
		
		this.frame.setJMenuBar(this.menuBar);
	}
	
	public void actionPerformed(ActionEvent e)	{
		if (e.getSource() == this.quitApplication)	{
			System.exit(0);
		} else if (e.getSource() == this.openHelp)	{
			JOptionPane.showMessageDialog(
				null,
				"Detta är en prototyp. \nVersion: 2014-01-31\nAv: Johan & Fiona","OpenFlisp",
				JOptionPane.INFORMATION_MESSAGE
			);
		} else if (e.getSource() == this.clearBoard) {
			SlsPerspective sls = (SlsPerspective) this.frame.getPerspectives().getPerspective(SlsPerspective.IDENTIFIER);
			sls.getSimulationBoard().clearBoard();
		} else if (e.getSource() == this.importBoard) {
			SlsPerspective sls = (SlsPerspective) this.frame.getPerspectives().getPerspective(SlsPerspective.IDENTIFIER);
			CircuitLegacyReader reader = null;
			try {
				JFileChooser fc = new JFileChooser();
	            if (fc.showOpenDialog(this.frame) == JFileChooser.APPROVE_OPTION) {
	            	
	            	System.out.println(fc.getSelectedFile());
	            	
					reader = new CircuitLegacyReader(new FileReader(fc.getSelectedFile()));
					
					Circuit2D circuit = reader.readCircuit();
					
					System.out.println("Found " + circuit.getComponents().size() + " components.");
					
					sls.getSimulationBoard().switchCircuit(circuit);
	            }
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(
					null,
					"Kunde inte hitta den valda filen", "Filen kunde inte hittas",
					JOptionPane.ERROR_MESSAGE
				);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(
					null,
					e1.getMessage(), "Läsfel",
					JOptionPane.ERROR_MESSAGE
				);
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(
					null,
					"Filen kunde inte läsas, " + e1.getMessage(), "Felaktigt filformat",
					JOptionPane.ERROR_MESSAGE
				);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e1) {}
			}
			
		}
	}
}
