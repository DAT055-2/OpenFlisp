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
	
	private GeneralPath wire = new GeneralPath();
	
	private final SignalView start;
	
	private final WirePanel panel;
	
	private SignalView end;
	
	private boolean selected = false;
	
	public WireView(WirePanel panel, SignalView start) {
		this.panel = panel;
		this.start = start;
		this.setContentAreaFilled(false);
		this.addMouseListener(this.wireSelectionHandler);
	}
	
	public SignalView getStart() {
		return this.start;
	}
	
	public SignalView getEnd() {
		return this.end;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void deselect() {
		this.selected = false;
		this.repaint();
		this.revalidate();
	}
	
	public void select() {
		this.selected = true;
		this.repaint();
		this.revalidate();
	}

	public void attatchEnd(SignalView end) {
		if (this.start != this.end && this.end == null) {
			this.end = end;
			this.moveEndPoint(this.end.getPosition(this.panel));
		}
	}
	
	public void setTemporaryDrawPoint(Point point) {
		if (this.end == null) {
			this.moveEndPoint(point);
		}
	}
	
	public void updatePositions() {
		if (this.end != null) {
			this.moveEndPoint(this.end.getPosition(this.panel));
		}
	}
	
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
	
	@Override
	public void paintComponent(Graphics g) {
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
	
	@Override
	public boolean contains(int x, int y) {
		return this.wire.intersects(x - 5, y - 5, 10, 10);
	}
	
	private final MouseAdapter wireSelectionHandler = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent evt) {
			WireView.this.selected = true;
		}
	};
}
