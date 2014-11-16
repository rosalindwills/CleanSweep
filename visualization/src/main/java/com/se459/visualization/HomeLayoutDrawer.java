package com.se459.visualization;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.xml.sax.SAXException;

import com.se459.modules.models.MemoryLog;
import com.se459.modules.models.Observer;
import com.se459.modules.models.SweepLog;
import com.se459.modules.models.Vacuum;
import com.se459.sensor.interfaces.IHomeLayout;
import com.se459.sensor.interfaces.ISensor;
import com.se459.sensor.models.SensorSimulator;
import com.se459.util.log.LogFactory;

public class HomeLayoutDrawer extends JFrame implements Observer,
		ActionListener {

	ISensor sim = SensorSimulator.getInstance();
	IHomeLayout layout;
	Vacuum vacuum;

	Thread drawingThread;

	static boolean windowOpen = true;
	HomeLayoutPanel layoutPanel;
	JPanel statusPanel;
	static Thread thread;
	static int maximumWindowlWidth = 600;
	static int statusPanelHeight = 50;
	static int buttonPanelHeight = 50;
	static int maximumLayoutPanelHeight = 500;
	static int maximumCellSize = 100;
	static int minimumCellSize = 5;
	static int padding = 50;

	private static int actualLayoutPanelWidth;
	private static int actualLayoutPanelHeight;
	
	private JButton stopStartButton;

	private MemoryLog memoryLog = new MemoryLog();

	public HomeLayoutDrawer() {
		try {

			((SensorSimulator) sim).importXml("classes" + File.separator
					+ "homeLayout1.xml");

			layout = ((SensorSimulator) sim).getHomeLayout();
			vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newFileAndScreenLog()));

		} catch (SAXException | IOException e) {
			throw new RuntimeException(e);
		}

		initUI();
		vacuum.registerObserver(this);
		
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
				+ statusPanelHeight + buttonPanelHeight + padding);

		FlowLayout uiLayout = new FlowLayout();
		uiLayout.setHgap(0);
		uiLayout.setVgap(0);
		setLayout(uiLayout);

		statusPanel = new JPanel();
		statusPanel.add(new JLabel("Initializing ..."));
		statusPanel.setPreferredSize(new Dimension(actualLayoutPanelWidth,
				statusPanelHeight));

		JPanel buttonPanel = new JPanel();
		stopStartButton = new JButton("ON/OFF");
		stopStartButton.addActionListener(this);
		buttonPanel.add(stopStartButton);
		buttonPanel.setPreferredSize(new Dimension(actualLayoutPanelWidth,
				buttonPanelHeight));

		layoutPanel = new HomeLayoutPanel(layout.getFloor(1), vacuum);
		layoutPanel.setPreferredSize(new Dimension(actualLayoutPanelWidth,
				actualLayoutPanelHeight));

		add(statusPanel);
		add(buttonPanel);
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
			String dirtStatusStr = "Dirt: " + layoutPanel.vacuum.getDirtUnits();
			String chargeStatusStr = "Charges: "
					+ layoutPanel.vacuum.getChargeRemaining();
			String apparatusStr = "Apparatus: "
					+ layoutPanel.vacuum.getCurrentSurface();
			String returnPathCostStr = "";
			if (layoutPanel.vacuum.getNavigationLogic().getIsReturning()) {
				returnPathCostStr = "ReturnCost: returning";
			} else {
				returnPathCostStr = "ReturnCost: "
						+ layoutPanel.vacuum.getNavigationLogic()
								.getReturnCost();
			}

			String dispaly = dirtStatusStr + "  " + chargeStatusStr + "  "
					+ apparatusStr + "  " + returnPathCostStr;

			JLabel statusLabel = new JLabel(dispaly);
			statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
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

	public void actionPerformed(ActionEvent e) {
		if(!layoutPanel.vacuum.isOn()){
			sim = SensorSimulator.getInstance();
			try {
				((SensorSimulator) sim).importXml("classes" + File.separator
						+ "homeLayout1.xml");
			} catch (SAXException | IOException e1) {
				e1.printStackTrace();
			}
			layoutPanel.floor = ((SensorSimulator) sim).getHomeLayout().getFloor(1);
			layoutPanel.vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newFileAndScreenLog()));
			layoutPanel.vacuum.registerObserver(this);

			layoutPanel.vacuum.start();
		}else{
			layoutPanel.vacuum.getNavigationLogic().returnAndStop();
		}
		
	}

}
