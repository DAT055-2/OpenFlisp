package se.openflisp.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import se.openflisp.gui.perspectives.SlsPerspective;

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

	private JMenuItem clearBoard, quitApplication, openHelp;
	
	public OpenFlispMenu(OpenFlispFrame frame) {
		this.frame = frame;
		this.initilize();
	}

	protected void initilize()	{
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
		} else if (e.getSource() == this.clearBoard)	{
			SlsPerspective sls = (SlsPerspective) this.frame.getPerspectives().getPerspective(SlsPerspective.IDENTIFIER);
			sls.getSimulationBoard().clearBoard();
		}
	}
}
