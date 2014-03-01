package se.openflisp.gui.util;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class KeyEventDelegator {

	public static Map<KeyStroke, List<Action>> actions = new HashMap<KeyStroke, List<Action>>();
	
	public static void addKeyAction(JPanel panel, KeyStroke keyStroke, Action action) {
		if (!KeyEventDelegator.actions.containsKey(keyStroke)) {
			KeyEventDelegator.actions.put(keyStroke, new LinkedList<Action>());
		}
		KeyEventDelegator.actions.get(keyStroke).add(action);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, keyStroke);
		panel.getActionMap().put(keyStroke, new ActionDelegator(keyStroke));
	}
	
	@SuppressWarnings("serial")
	private static class ActionDelegator extends AbstractAction {
		
		private KeyStroke keyStroke;
		
		public ActionDelegator(KeyStroke keyStroke) {
			this.keyStroke = keyStroke;
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
			for (Action action : KeyEventDelegator.actions.get(this.keyStroke)) {
				action.actionPerformed(evt);
			}
		}
	}
}
