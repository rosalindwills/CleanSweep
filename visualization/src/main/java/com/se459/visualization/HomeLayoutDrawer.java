package com.se459.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.xml.sax.SAXException;

import com.se459.modules.models.MemoryLog;
import com.se459.modules.models.Observer;
import com.se459.modules.models.Vacuum;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IFloor;
import com.se459.sensor.interfaces.IHomeLayout;
import com.se459.sensor.interfaces.ISensor;
import com.se459.sensor.models.SensorSimulator;

class HomeLayoutPanel extends JPanel {

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
					if (vacuum.getMemory().getAllKnownCells().contains(cell) && (cell.isTraversed())) {
						g2d.setColor(getSurfaceColor(cell,
								cell.getDirtUnits()));
						Rectangle2D rect = new Rectangle2D.Float(x * xMult,
								y * yMult, xMult, yMult);
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

					// draw charging station
					if (cell.getIsChargingStation()) {
						g2d.setColor(Color.lightGray);
						g2d.fillOval(x * xMult, y * yMult, xMult, yMult);
					}
				}
			}

			if (vacuum.isOn()) {
				// draw current target cell
				g2d.setColor(Color.white);
				g2d.drawOval(vacuum.getDestinationX() * xMult
						+ (int) (xMult / 2.6), vacuum.getDestinationY() * yMult
						+ (int) (yMult / 2.6), xMult / 4, yMult / 4);

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

		// draw return path
		if (null != vacuum.getNavigationLogic().getReturnPath()) {
			g2d.setColor(Color.red);
			
			for (ICell c : vacuum.getNavigationLogic().getReturnPath()) {
				g2d.drawOval(c.getX() * xMult + xMult * 4 / 10, c.getY()
						* yMult + yMult * 4 / 10, xMult / 5, yMult / 5);

			}
		}
		
		// draw the current path
		if (!vacuum.getNavigationLogic().getIsReturning() && null != vacuum.getNavigationLogic().getCurrentPath()) {
			g2d.setColor(Color.CYAN);
			
			for (ICell c : vacuum.getNavigationLogic().getCurrentPath()) {
				g2d.drawOval(c.getX() * xMult + xMult * 4 / 10, c.getY()
						* yMult + yMult * 4 / 10, xMult / 5, yMult / 5);

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

public class HomeLayoutDrawer extends JFrame implements Observer {

	ISensor sim = SensorSimulator.getInstance();
	IHomeLayout layout;
	Vacuum vacuum;

	Thread drawingThread;
	Thread vacuumThread;

	static boolean windowOpen = true;
	HomeLayoutPanel layoutPanel;
	JPanel statusPanel;
	static Thread thread;
	static int maximumWindowlWidth = 600;
	static int statusPanelHeight = 50;
	static int maximumLayoutPanelHeight = 500;
	static int maximumCellSize = 100;
	static int minimumCellSize = 5;

	private static int actualLayoutPanelWidth;
	private static int actualLayoutPanelHeight;

	static int padding = 50;

	private MemoryLog memoryLog = new MemoryLog();

	public HomeLayoutDrawer() {
		try {

			((SensorSimulator) sim).importXml("classes" + File.separator
					+ "homeLayout1.xml");

			layout = ((SensorSimulator) sim).getHomeLayout();
			vacuum = Vacuum.getInstance(sim, 1, 0, 0);

		} catch (SAXException | IOException e) {
			throw new RuntimeException(e);
		}

		initUI();
		vacuum.registerObserver(this);
		vacuumThread = new Thread(vacuum);
		vacuumThread.start();
	}

	private void initUI() {

		setTitle("CleanSweep");

		int rows = layout.getFloor(1).getMaxX() - layout.getFloor(1).getMinX()
				+ 1;
		int cols = layout.getFloor(1).getMaxY() - layout.getFloor(1).getMinY()
				+ 1;

		// given a maximum size of window, call getCellSize to calculate
		// a proper size for cell based on floor plan.
		int actualCellSize = calculateCellSize(rows, cols, maximumWindowlWidth,
				maximumLayoutPanelHeight, maximumCellSize, minimumCellSize);
		actualLayoutPanelWidth = actualCellSize * rows;
		actualLayoutPanelHeight = actualCellSize * cols;
		setSize(actualLayoutPanelWidth + padding, actualLayoutPanelHeight
				+ statusPanelHeight + padding);

		FlowLayout uiLayout = new FlowLayout();
		uiLayout.setHgap(0);
		uiLayout.setVgap(0);
		setLayout(uiLayout);

		statusPanel = new JPanel();
		statusPanel.add(new JLabel("Initializing ..."));
		statusPanel.setPreferredSize(new Dimension(actualLayoutPanelWidth,
				statusPanelHeight));

		layoutPanel = new HomeLayoutPanel(layout.getFloor(1), vacuum);
		layoutPanel.setPreferredSize(new Dimension(actualLayoutPanelWidth,
				actualLayoutPanelHeight));

		add(statusPanel);
		add(layoutPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		this.setVisible(true);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				HomeLayoutDrawer.windowOpen = false;
			}
		});

	}

	public static void main(String[] args) {

		new HomeLayoutDrawer();

	}

	private int calculateCellSize(int cols, int rows, int maximumWidth,
			int maximumHeight, int maximumCellSize, int minimumCellSize) {

		for (int cellSizeCandidate = maximumCellSize; cellSizeCandidate > minimumCellSize; cellSizeCandidate--) {
			if (cols * cellSizeCandidate <= maximumWidth
					&& rows * cellSizeCandidate <= maximumHeight) {
				actualLayoutPanelWidth = cols * cellSizeCandidate;
				actualLayoutPanelHeight = rows * cellSizeCandidate;
				return cellSizeCandidate;
			}
		}

		return minimumCellSize;
	}

	private void outputCurrentLayoutPanel() {
		int w = this.getWidth();
		int h = this.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		this.print(g);
		memoryLog.append(bi);
	}

	public void update() {

		if (layoutPanel.vacuum.isOn()) {
			statusPanel.removeAll();
			String dirtStatusStr = "DirtUnits: "
					+ layoutPanel.vacuum.getDirtUnits();
			String chargeStatusStr = "ChargeRemaining: "
					+ layoutPanel.vacuum.getChargeRemaining();
			String returnPathCost = "";
			if (layoutPanel.vacuum.getNavigationLogic().getIsReturning()) {
				returnPathCost = "ReturnCost: returning";
			} else {
				returnPathCost = "ReturnCost: "
						+ layoutPanel.vacuum.getNavigationLogic()
								.getReturnCost();
			}

			String dispaly = dirtStatusStr + "    " + chargeStatusStr + "    "
					+ returnPathCost;

			JLabel statusLabel = new JLabel(dispaly);
			statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
			statusPanel.add(statusLabel);
			statusPanel.validate();
			layoutPanel.repaint();
			repaint();
		}
		outputCurrentLayoutPanel();

	}

	@Override
	public void sendNotification(String message) {
		 JOptionPane.showMessageDialog(this, message);
	}

}
