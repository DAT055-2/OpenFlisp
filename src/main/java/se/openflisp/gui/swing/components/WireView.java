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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;

import javax.swing.JButton;

/**	
 * A view for wires
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WireView extends JButton {	
	
	/**
	 * 
	 */
	private GeneralPath wire = new GeneralPath();
	
	/**
	 * The SignalView where the Wire should start from.
	 */
	private final SignalView start;
	
	/**
	 * The SignalView where the Wire should end if it is connected.
	 */
	private SignalView end;
	
	/**
	 * The parent panel where the WireView is created.
	 */
	private final WirePanel panel;
	
	/**
	 * If the WireView is selected, used for deletion.
	 */
	private boolean selected = false;
	
	/**
	 * Creates a WireView.
	 * 
	 * @param panel		parent panel where the WireView is created
	 * @param start		starting point for this WireView
	 */
	public WireView(WirePanel panel, SignalView start) {
		this.panel = panel;
		this.start = start;
		this.setContentAreaFilled(false);
		this.addMouseListener(this.wireSelectionHandler);
	}
	
	/**
	 * Get the starting SignalView for this WireView.
	 * 
	 * @return starting SignalView
	 */
	public SignalView getStart() {
		return this.start;
	}
	
	/**
	 * Get the ending SignalView for this WireView.
	 * 
	 * @return ending SignalView or null if the wire is not connected
	 */
	public SignalView getEnd() {
		return this.end;
	}
	
	/**
	 * Checks if the WireView is selected.
	 * 
	 * @return true if the component is selected, false otherwise
	 */
	public boolean isSelected() {
		return this.selected;
	}
	
	/**
	 * Selects this component. If already selected nothing happens.
	 */
	public void select() {
		this.selected = true;
		this.repaint();
		this.revalidate();
	}
	
	/**
	 * Deselects this component. If not selected nothing happens.
	 */
	public void deselect() {
		this.selected = false;
		this.repaint();
		this.revalidate();
	}
	
	/**
	 * Attaches the end of this WireView to a SignalView and locks it in place.
	 * 
	 * @param end		SignalView to connect to
	 */
	public void attatchEnd(SignalView end) {
		if (this.start != this.end && this.end == null) {
			this.end = end;
			this.moveEndPoint(this.end.getPosition(this.panel));
		}
	}
	
	/**
	 * Draws the wire to a temporary point without the wire being connected.
	 * 
	 * @param point		where to draw the end of the wire.
	 */
	public void setTemporaryDrawPoint(Point point) {
		if (this.end == null) {
			this.moveEndPoint(point);
		}
	}
	
	/**
	 * Update the wires starting position and ending position from their positions.
	 */
	public void updatePositions() {
		if (this.end != null) {
			this.moveEndPoint(this.end.getPosition(this.panel));
		}
	}
	
	/**
	 * Helper function to redraw the wire to a certain point.
	 * 
	 * @param end	where to draw the end of the wire.
	 */
	protected void moveEndPoint(Point end) {
		Point start = this.start.getPosition(this.panel);
		System.out.println("WireView.redrawWire(" + start + "," + end + ")");
		this.wire.reset();
		this.wire.moveTo(start.x, start.y);
		this.wire.quadTo(
			(this.wire.getCurrentPoint().getX() + end.x) / 2,
			end.y,
			end.x,
			end.y
		);
		this.repaint();
		this.revalidate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(6));
		if (this.isSelected()) {
			g2.setColor(Color.orange);
		} else {
			g2.setColor(Color.black);
		}
		g2.draw(this.wire);
		switch (this.start.signal.getState()) {
			case HIGH:
				g2.setColor(Color.blue);
			break;
			case LOW:
				g2.setColor(Color.white);
			break;
			case FLOATING:
				g2.setColor(Color.red);
			break;
		}
		g2.setStroke(new BasicStroke(4));
		g2.draw(this.wire);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(int x, int y) {
		return this.wire.intersects(x - 5, y - 5, 10, 10);
	}
	
	/**
	 * Enables the wire to be selected on mouse press.
	 */
	private final MouseAdapter wireSelectionHandler = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent evt) {
			if (WireView.this.isSelected()) {
				WireView.this.deselect();
			} else {
				WireView.this.select();
			}
		}
	};
}
