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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D.Float;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.Signal;
import se.openflisp.sls.component.NandGate;
import se.openflisp.sls.component.NorGate;
import se.openflisp.sls.component.NotGate;
import se.openflisp.sls.component.NxorGate;
import se.openflisp.sls.event.ComponentAdapter;
import se.openflisp.sls.event.ListenerContext;

/**	
 * A view for the signal on a ComponentView.
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SignalView extends JButton {

	/**
	 * Signal model to display.
	 */
	public final Signal signal;
	
	/**
	 * The ComponentView that this SignalView is within.
	 */
	private final ComponentView componentView;
	
	/**
	 * Shape of the clickable JButton signal.
	 */
	private Shape shape;
	
	/**
	 * Size of the circle used to display Signal states.
	 */
	protected static int arcLength = ComponentView.componentSize / 5;

	
	/**
	 * Size of the entire SignalView including circle and stick.
	 */
	public static Dimension btnSize = new Dimension(ComponentView.componentSize/2,arcLength); 


	/**
	 * Creates a new SignalView.
	 * 
	 * @param componentView		the parent ComponentView that created the SignalView
	 * @param signal			the signal model to display
	 */
	public SignalView(ComponentView componentView, Signal signal) {
		this.componentView = componentView;
		this.setPreferredSize(btnSize);
		this.signal = signal;
		this.setContentAreaFilled(false);
		this.signal.getOwner().getEventDelegator().addListener(ListenerContext.SWING, new ComponentAdapter() {
			@Override
			public void onSignalChange(se.openflisp.sls.Component component, Signal signal) {
				if (signal == SignalView.this.signal) {
					SignalView.this.repaint();
					SignalView.this.revalidate();
				}
			}
		});
	}

	/**
	 * Gets the parent ComponentView that this SignalView is in.
	 * 
	 * @return the parent ComponentView
	 */
	public ComponentView getComponentView() {
		return this.componentView;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(int x, int y) {
		if (this.shape == null || !this.shape.getBounds().equals(this.getBounds())) {
			if (this.signal instanceof Input) {
				this.shape = new Float(0, 0, arcLength - 1, arcLength - 1);
			} else {
				this.shape = new Float(btnSize.width - (arcLength + 1), 0, arcLength - 1 , arcLength - 1);
			}
		}
		return this.shape.contains(x, y);
	}

	/**
	 * Gets the current position of the Signal circle in the context of another AWT Component.
	 * 
	 * @param context		in what context the position should be given in
	 * @return a Point containing the x,y-coordinates for the Signal circle
	 */
	public Point getPosition(Component context) {
		if (this.signal instanceof Output) {
			return SwingUtilities.convertPoint(
				this.getParent(),
				this.getX() + (btnSize.width),
				this.getY() + (arcLength) / 2,
				context
			);
		} else {
			return SwingUtilities.convertPoint(
				this.getParent(),
				this.getLocation().x,
				this.getLocation().y + (SignalView.arcLength) / 2,
				context
			);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (signal.getState() == Signal.State.HIGH) {
			g.setColor(Color.BLUE);
		} else if (signal.getState() == Signal.State.FLOATING) {
			g.setColor(Color.RED);
		} else {
			g.setColor(getBackground());
		}
		if (signal instanceof Input) {
			g.fillOval(0,0, arcLength-1, arcLength-1);
		} else {
			g.fillOval((btnSize.width - (arcLength+1)),0, arcLength-1, arcLength-1);
		}
		
		g2.setColor(getForeground());
		g2.setColor(getForeground());
		g2.setStroke(new BasicStroke(4));

		if (signal instanceof Input) {
			g2.setColor(Color.GRAY);
			g2.drawLine(arcLength, btnSize.height/2, btnSize.width, btnSize.height/2);
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.BLACK);
			g2.drawOval(0,0,arcLength-1,arcLength-1);
		}	else {
			if (signal.getOwner() instanceof NandGate || signal.getOwner() instanceof NotGate ||
					signal.getOwner() instanceof NxorGate || signal.getOwner() instanceof NorGate) {
				g2.setColor(Color.GRAY);
				g2.drawLine(0, btnSize.height/2, btnSize.width - (arcLength+1), btnSize.height/2);
				g2.setColor(Color.BLACK);
				g2.fillOval(-2,0,arcLength-1,arcLength-1);
			} else {
				g2.setColor(Color.GRAY);
				g2.drawLine(0, btnSize.height/2, btnSize.width - (arcLength+1), btnSize.height/2);
			}
			g2.setStroke(new BasicStroke(1));
			g2.drawOval(btnSize.width - (arcLength+1),0,arcLength-1,arcLength-1);
		}
	}
}
