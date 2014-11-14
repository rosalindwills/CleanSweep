package com.se459.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.se459.modules.models.Vacuum;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IFloor;
import com.se459.util.log.Config;

public class HomeLayoutPanel extends JPanel {

	IFloor floor;
	Vacuum vacuum;

	public HomeLayoutPanel(IFloor fl, Vacuum vac) {
		floor = fl;
		vacuum = vac;
	}

	private void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Dimension dimension = getSize();

		int xMult = dimension.width / (floor.getMaxX() - floor.getMinX() + 1);
		int yMult = dimension.height / (floor.getMaxY() - floor.getMinY() + 1);

		for (int x = floor.getMinX(); x <= floor.getMaxX(); ++x) {
			for (int y = floor.getMinY(); y <= floor.getMaxY(); ++y) {

				ICell cell = floor.getCell(x, y);

				if (null != cell) {
					// known and traversed
					if (vacuum.getMemory().getAllKnownCells().contains(cell)
							&& (cell.isTraversed())) {
						g2d.setColor(getSurfaceColor(cell, cell.getDirtUnits()));
						Rectangle2D rect = new Rectangle2D.Float(x * xMult, y
								* yMult, xMult, yMult);
						g2d.fill(rect);
					}

					// known but not traversed
					if (vacuum.getMemory().getAllUnfinishedCells()
							.contains(cell)) {
						g2d.setStroke(new BasicStroke(2));
						g2d.setColor(Color.black);
						g2d.drawRect(x * xMult + xMult / 3, y * yMult + yMult
								/ 3, xMult / 3, yMult / 3);
					}

					// draw wall
					if (vacuum.getMemory().getAllKnownCells().contains(cell) && (cell.isTraversed()) ){
					g2d.setStroke(new BasicStroke(3));
					g2d.setColor(Color.black);
					if (cell.getPathNegX() != PathType.OPEN) {
						g2d.drawLine(x * xMult, y * yMult, x * xMult, y * yMult
								+ yMult);
					}
					if (cell.getPathPosX() != PathType.OPEN) {
						g2d.drawLine(x * xMult + xMult, y * yMult, x * xMult
								+ xMult, y * yMult + yMult);
					}
					if (cell.getPathPosY() != PathType.OPEN) {
						g2d.drawLine(x * xMult, y * yMult + yMult, x * xMult
								+ xMult, y * yMult + yMult);
					}
					if (cell.getPathNegY() != PathType.OPEN) {
						g2d.drawLine(x * xMult, y * yMult, x * xMult + xMult, y
								* yMult);
					}
				}

					// draw charging station
					if (cell.getIsChargingStation()) {
						g2d.setColor(Color.lightGray);
						g2d.fillOval(x * xMult, y * yMult, xMult, yMult);
					}
				}
			}

			if (vacuum.isOn()) {
				if (Config.getInstance().debugMode) {
					// draw current target cell
					g2d.setColor(Color.white);
					g2d.drawOval(vacuum.getDestinationX() * xMult
							+ (int) (xMult / 2.6), vacuum.getDestinationY()
							* yMult + (int) (yMult / 2.6), xMult / 4, yMult / 4);
				}

				// draw current cell (vacuum). green means vacuum is on
				g2d.setColor(Color.green);
				g2d.drawOval(vacuum.getX() * xMult + xMult / 4, vacuum.getY()
						* yMult + yMult / 4, xMult / 2, yMult / 2);
			} else {
				// draw the vacuum. red means vacuum is off
				g2d.setColor(Color.red);
				g2d.drawOval(vacuum.getX() * xMult + xMult / 4, vacuum.getY()
						* yMult + yMult / 4, xMult / 2, yMult / 2);

			}
		}

		if (Config.getInstance().debugMode) {
			// draw return path
			if (null != vacuum.getNavigationLogic().getReturnPath()) {
				g2d.setColor(Color.red);

				for (ICell c : vacuum.getNavigationLogic().getReturnPath()) {
					g2d.drawOval(c.getX() * xMult + xMult * 4 / 10, c.getY()
							* yMult + yMult * 4 / 10, xMult / 5, yMult / 5);

				}
			}

			// draw the current path
			if (!vacuum.getNavigationLogic().getIsReturning()
					&& null != vacuum.getNavigationLogic().getCurrentPath()) {
				g2d.setColor(Color.CYAN);

				for (ICell c : vacuum.getNavigationLogic().getCurrentPath()) {
					g2d.drawOval(c.getX() * xMult + xMult * 4 / 10, c.getY()
							* yMult + yMult * 4 / 10, xMult / 5, yMult / 5);

				}
			}
		}
	}

	private Color getSurfaceColor(ICell cell, int dirtUnits) {
		float r = 0;
		float g = 0;
		float b = 0;

		switch (cell.getSurfaceType()) {
		case BAREFLOOR:
			r = 1.0f;
			g = 0.5f;
			b = 0.25f;
			break;
		case LOWPILE:
			r = 0.25f;
			g = 0.5f;
			b = 1.0f;
			break;
		case HIGHPILE:
			r = 1.0f;
			g = 0.3f;
			b = 0.5f;
			break;
		default:
			break;
		}

		r *= Math.pow(0.8, dirtUnits);
		g *= Math.pow(0.8, dirtUnits);
		b *= Math.pow(0.8, dirtUnits);

		return new Color(r, g, b, 1.0f);
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		draw(g);
	}
}