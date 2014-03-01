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
import java.awt.geom.GeneralPath;

import javax.swing.JButton;

import se.openflisp.sls.Input;
import se.openflisp.sls.Output;
import se.openflisp.sls.Signal;

/**	
 * A view for wires
 * 
 * @author Daniel Svensson <daniel@dsit.se>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WireView extends JButton {	
	private GeneralPath wire = new GeneralPath();
	public Signal input;
	public Signal output;

	WireView(){
		setContentAreaFilled(false);
	}

	/**
	 * Set this curve
	 */
	public void drawCurveTo(Point point){	 
		wire.quadTo( wire.getCurrentPoint().getX() + ((point.x - wire.getCurrentPoint().getX())/2), wire.getCurrentPoint().getY() + ((point.y - wire.getCurrentPoint().getY())), point.x, point.y);
		repaint();
		revalidate();
	}

	/**
	 * Move this curve
	 */
	public void moveCurve(int x, int y) {
		wire.moveTo(x, y);
	}

	/**
	 * Reset wire
	 */
	public void reset() {
		wire.reset();
	}

	/**
	 * Paint wire between signals
	 */
	public void drawBetweenSignals(Input input, Output output) {


	}


	/**
	 * Custom paint method so our button looks like a signal
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (getModel().isArmed() ) {
			g2.setColor(Color.BLUE);
		} else {
			g2.setColor(Color.RED);
		}

		g2.draw(wire);
	}

	/**
	 * Paints the border around the signal
	 */
	protected void paintBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(6));
		g2.setColor(Color.BLACK);
		g2.draw(wire);

		if(output != null && output.getState() == Signal.State.HIGH){
			g2.setColor(Color.BLUE);
		}
		else{
			g2.setColor(Color.WHITE);
		}
		g2.setStroke(new BasicStroke(4));
		g2.draw(wire);
	}

	/**
	 * Will decide if the given x and y - values are within our button
	 * @param	x	the x value
	 * @param	y	the y value
	 */
	public boolean contains(int x, int y) {
		return wire.intersects(x-5,y-5,10,10);
	}
}