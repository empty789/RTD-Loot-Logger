
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultCaret;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

public class MainWindow {

	public JFrame frmRtdLootLogger;
	private JTable table;
	private DungeonRunTableModel tableModel;
	private ArrayList<DungeonRun> runs;
	private JLabel lblTimer;
	public boolean timerRunning;
	public boolean btnToggle = false;
	private long elapsedTime;
	private long startTime;
	private JPanel panel_5;
	private JLabel lblStatus;
	private Logger l;
	private Model model;
	private MainWindow mainWindow;
	private JTextField textStage;
	private JLabel lblExpamount;
	private JLabel lblMoramount;
	private JLabel lblGoldamount;
	private JLabel lblGloryamount;
	private static char[] c = new char[] { 'k', 'm', 'b', 't', 'q' };
	private JLabel lblGoldAvg;
	private JLabel lblGloryAvg_1;
	private JLabel lblMorAvg_1;
	private JLabel lblExpAvg;
	private JLabel lblTimeAvg;
	private JLabel lblMorAvg;
	private JLabel lblGoldAvg_1;
	private JLabel lblGloryAvg;
	private JLabel lblExpAvg_1;
	private JLabel lblQuartz;
	private JLabel lblQuartzamount;
	private JLabel lblRefined;
	private JLabel lblRefinedamount;
	private JLabel lblQuartzAvg;
	private JLabel lblRefinedAvg;
	private JLabel lblRuns;
	private JLabel lblRunamount;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmResetRunStats;
	private JMenuItem mntmExit;
	private JMenu mnExport;
	private JMenuItem mntmAscsv;
	private JMenu mnVersion;
	public JButton btnStart;
	private JMenu mnResolution;
	public JRadioButtonMenuItem rdbtnmntmx;
	public JRadioButtonMenuItem rdbtnmntmx_1;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		mainWindow = this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		model = new Model();
		l = new Logger(model);

		runs = new ArrayList<DungeonRun>();
		timerRunning = false;
		frmRtdLootLogger = new JFrame();
		frmRtdLootLogger.setResizable(false);
		frmRtdLootLogger.setTitle("RTD Loot Logger");
		frmRtdLootLogger.setBounds(100, 100, 600, 365);
		frmRtdLootLogger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRtdLootLogger.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		frmRtdLootLogger.getContentPane().add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel panel = new JPanel();
		panel_2.add(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "avg/run", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setLayout(new GridLayout(7, 5, 0, 0));

		lblTimeAvg = new JLabel("Time: 00:00");
		panel.add(lblTimeAvg);

		lblGoldAvg = new JLabel("Gold: 0k");
		panel.add(lblGoldAvg);

		lblMorAvg = new JLabel("MoR: 0.0");
		panel.add(lblMorAvg);

		lblGloryAvg = new JLabel("Glory: 0.0");
		panel.add(lblGloryAvg);

		lblQuartzAvg = new JLabel("Quartz: 0.0");
		panel.add(lblQuartzAvg);

		lblRefinedAvg = new JLabel("Refined:  0.0");
		panel.add(lblRefinedAvg);

		lblExpAvg = new JLabel("Exp: 0");
		panel.add(lblExpAvg);

		JPanel panel_1 = new JPanel();
		panel_1.setVisible(false);
		panel_2.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "per Hour", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setLayout(new GridLayout(5, 5, 0, 0));

		lblGoldAvg_1 = new JLabel("Gold:");
		panel_1.add(lblGoldAvg_1);

		lblMorAvg_1 = new JLabel("MoR:");
		panel_1.add(lblMorAvg_1);

		lblGloryAvg_1 = new JLabel("Glory:");
		panel_1.add(lblGloryAvg_1);

		lblExpAvg_1 = new JLabel("Exp:");
		panel_1.add(lblExpAvg_1);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setHgap(4);
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Total", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		frmRtdLootLogger.getContentPane().add(panel_3, BorderLayout.NORTH);

		JLabel lblTime = new JLabel("Time:");
		panel_3.add(lblTime);

		lblTimer = new JLabel("00:00:00");
		panel_3.add(lblTimer);

		lblRuns = new JLabel("Runs:");
		panel_3.add(lblRuns);

		lblRunamount = new JLabel("0");
		panel_3.add(lblRunamount);

		JLabel lblGold = new JLabel("Gold:");
		panel_3.add(lblGold);

		lblGoldamount = new JLabel("0");
		panel_3.add(lblGoldamount);

		JLabel lblMor = new JLabel("MoR:");
		panel_3.add(lblMor);

		lblMoramount = new JLabel("0");
		panel_3.add(lblMoramount);

		JLabel lblGlory = new JLabel("Glory:");
		panel_3.add(lblGlory);

		lblGloryamount = new JLabel("0");
		panel_3.add(lblGloryamount);

		lblQuartz = new JLabel("Quartz:");
		panel_3.add(lblQuartz);

		lblQuartzamount = new JLabel("0");
		panel_3.add(lblQuartzamount);

		lblRefined = new JLabel("Refined:");
		panel_3.add(lblRefined);

		lblRefinedamount = new JLabel("0");
		panel_3.add(lblRefinedamount);

		JLabel lblExp = new JLabel("Exp:");
		panel_3.add(lblExp);

		lblExpamount = new JLabel("0");
		panel_3.add(lblExpamount);

		JPanel panel_4 = new JPanel();
		frmRtdLootLogger.getContentPane().add(panel_4, BorderLayout.CENTER);
		runs = new ArrayList<DungeonRun>();

		tableModel = new DungeonRunTableModel(runs);
		table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setShowHorizontalLines(false);
		table.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

		table.getColumnModel().getColumn(0).setPreferredWidth(1);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setPreferredWidth(20);
		table.getColumnModel().getColumn(4).setPreferredWidth(20);
		table.getColumnModel().getColumn(5).setPreferredWidth(20);
		table.getColumnModel().getColumn(6).setPreferredWidth(50);
		panel_4.setLayout(new MigLayout("", "[532px]", "[258px]"));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setAutoscrolls(true);
		scrollPane.setPreferredSize(new Dimension(380, 280));
		panel_4.add(scrollPane, "cell 0 0,grow");

		panel_5 = new JPanel();
		frmRtdLootLogger.getContentPane().add(panel_5, BorderLayout.SOUTH);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnToggle = !btnToggle;
				if (btnToggle) {
					btnStart.setText("Stop");
					resetTimer();
					textStage.setEnabled(false);
					mntmResetRunStats.setEnabled(false);
					mntmAscsv.setEnabled(false);
					l.startRun(mainWindow);

				} else {
					btnStart.setText("Start");
					textStage.setEnabled(true);
					mntmResetRunStats.setEnabled(true);
					mntmAscsv.setEnabled(true);
					setStatus("Stopped");
					stopTimer();
					l.thread.stop();
				}

			}
		});
		panel_5.setLayout(new MigLayout("", "[90px][110px][447px]", "[23px]"));
		panel_5.add(btnStart, "cell 0 0,alignx left,aligny top");

		JLabel lblStage = new JLabel("Stage");
		panel_5.add(lblStage, "flowx,cell 1 0");

		lblStatus = new JLabel("");
		panel_5.add(lblStatus, "cell 2 0,alignx center,growy");

		textStage = new JTextField();
		textStage.setText("39-2");
		panel_5.add(textStage, "cell 1 0");
		textStage.setColumns(10);

		menuBar = new JMenuBar();
		frmRtdLootLogger.setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmResetRunStats = new JMenuItem("Reset run stats");
		mntmResetRunStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				lblTimer.setText("00:00:00");
				model.resetStats();
				updateGui(false);
				runs.clear();
				tableModel.fireTableDataChanged();
			}
		});
		mnFile.add(mntmResetRunStats);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		mnExport = new JMenu("Export");
		menuBar.add(mnExport);

		mntmAscsv = new JMenuItem("as .csv");
		mntmAscsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (runs.size() == 0) {
					JOptionPane.showMessageDialog(frmRtdLootLogger, "There is no data to export!", ":( !",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					JFileChooser fileChooser = new JFileChooser();
					SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
					Date date = new Date(System.currentTimeMillis());
					String dateName = formatter.format(date);
					File exportFile = new File("/"+textStage.getText()+"_"+dateName+".csv");
					fileChooser.setSelectedFile(exportFile);
					if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						
						File file = fileChooser.getSelectedFile();
						exportToCsv(file);
					}
				}
			}
		});
		mnExport.add(mntmAscsv);
		
		mnResolution = new JMenu("Resolution");
		menuBar.add(mnResolution);
		
		rdbtnmntmx = new JRadioButtonMenuItem("1280x720");
		rdbtnmntmx.setSelected(true);
		mnResolution.add(rdbtnmntmx);
		
		rdbtnmntmx_1 = new JRadioButtonMenuItem("1920x1080");
		mnResolution.add(rdbtnmntmx_1);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnmntmx);
		group.add(rdbtnmntmx_1);
		mnVersion = new JMenu("v"+model.version);
		mnVersion.setEnabled(false);
		menuBar.add(mnVersion);

		frmRtdLootLogger.setVisible(true);
		initTimer();
	}

	public void addRun(DungeonRun run) {
		run.setIndex(runs.size() + 1);
		runs.add(run);
		tableModel.fireTableDataChanged();
		table.changeSelection(table.getRowCount() - 1, 0, false, false);
	}

	public void initTimer() {
		Thread thread = new Thread("TimerThread") {
			public void run() {
				startTime = System.currentTimeMillis();

				while (true) {
					if (timerRunning) {

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						elapsedTime = System.currentTimeMillis() - startTime;

						String t = String.format("%tT", elapsedTime - TimeZone.getDefault().getRawOffset());
						lblTimer.setText(t);

					} else {

						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
		};
		thread.start();
	}

	public void startTimer() {
		timerRunning = true;
	}

	public void stopTimer() {
		timerRunning = false;
	}

	public void resetTimer() {
		startTime = System.currentTimeMillis();
	}

	public void setStatus(String msg) {
		lblStatus.setText(msg);
	}

	public void updateGui(boolean calc) {
		if (calc)
			model.calc();

		lblGoldamount.setText(coolFormat(model.gold, 0));
		lblExpamount.setText(coolFormat(model.exp, 0));
		lblGloryamount.setText(String.valueOf(model.glory));
		lblMoramount.setText(String.valueOf(model.mor));
		lblQuartzamount.setText(String.valueOf(model.quartz));
		lblRefinedamount.setText(String.valueOf(model.refined));
		lblRunamount.setText(String.valueOf(model.runs));

		String pretty = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(model.timeAvg.longValue()),
				TimeUnit.MILLISECONDS.toSeconds(model.timeAvg.longValue())
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(model.timeAvg.longValue())));
		lblTimeAvg.setText("Time: " + pretty);
		lblGoldAvg.setText("Gold: " + coolFormat(model.goldAvg, 0));
		lblExpAvg.setText("Exp: " + coolFormat(model.expAvg, 0));
		lblMorAvg.setText("MoR: " + model.morAvg);
		lblGloryAvg.setText("Glory: " + model.gloryAvg);
		lblQuartzAvg.setText("Quartz: " + model.quartzAvg);
		lblRefinedAvg.setText("Refined: " + model.refinedAvg);

	}

	private void exportToCsv(File file) {

		
		FileWriter fr;
		try {
			fr = new FileWriter(file, true);
			fr.write("#, Time, Time_mill, Gold, MoR, Glory, Quartz, Refined, Exp\n");
			for (int i = 0; i < runs.size(); i++) {
				DungeonRun r = runs.get(i);
				fr.write(i + ", " + r.getPrettyTime() + ", " + r.getTime() + ", " + r.getGold() + ", " + r.getMor()
						+ ", " + r.getGlory() + ", " + r.getQuartz() + ", " + r.getRefined() + ", " + r.getExp()
						+ "\n");
			}
			fr.write(", , , , , , , , \n");
			fr.write("Total, , , , , , , , \n");
			fr.write(runs.size() + ", " + lblTimer.getText() + ", " + model.time + ", " + model.gold + ", " + model.mor
			+ ", " + model.glory + ", " + model.quartz + ", " + model.refined + ", " + model.exp
			+ "\n");

			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Recursive implementation, invokes itself for each factor of a thousand,
	 * increasing the class on each invokation.
	 * 
	 * @param n         the number to format
	 * @param iteration in fact this is the class from the array c
	 * @return a String representing the number n formatted in a cool looking way.
	 */
	private static String coolFormat(double n, int iteration) {
		double d = ((long) n / 100) / 10.0;
		boolean isRound = (d * 10) % 10 == 0;// true if the decimal part is equal to 0 (then it's trimmed anyway)
		return (d < 1000 ? // this determines the class, i.e. 'k', 'm' etc
				((d > 99.9 || isRound || (!isRound && d > 9.99) ? // this decides whether to trim the decimals
						(int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
				) + "" + c[iteration]) : coolFormat(d, iteration + 1));

	}

}
