import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Simulator extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int NODE_NUM = 4;
	
	
	private static int NODE_WIDTH 		= 100;
	private static int NODE_HEIGHT 		= 30;
	private static int CONTENT_WIDTH 	= 75;
	private static int CONTENT_HEIGHT 	= 25;
	private static int NODE_X			= 200;
	private static int NODE_Y			= 100;
	private static int TITLE_X 			= 100;
	
	private JButton 	jb_start;
	
	private JLabel 		jl_A;
	private JLabel 		jl_B;
	private JLabel 		jl_C;
	private JLabel 		jl_D;
	
	private JTextField 	jtf_A_size;
	private JTextField 	jtf_B_size;
	private JTextField 	jtf_C_size;
	private JTextField 	jtf_D_size;
	
	private JLabel 		jl_A_attempt;
	private JLabel 		jl_B_attempt;
	private JLabel 		jl_C_attempt;
	private JLabel 		jl_D_attempt;
	
	private JLabel		jl_A_delay;
	private JLabel		jl_B_delay;
	private JLabel		jl_C_delay;
	private JLabel		jl_D_delay;
	
	private JLabel		jl_A_progress;
	private JLabel		jl_B_progress;
	private JLabel		jl_C_progress;
	private JLabel		jl_D_progress;
	
	private JLabel 		jl_bandwidth_util;
	private JLabel 		jl_time_util;

	public Simulator() {
		super();
		initGUI();
	}

	private void initGUI() {
		this.setTitle("CSMA/CD Simulator");
		this.setResizable(false);
		
		JPanel jp = new JPanel();
		this.getContentPane().add(jp, BorderLayout.CENTER);
		jp.setLayout(null);
		{
			this.jb_start = new JButton();
			jp.add(this.jb_start);
			this.jb_start.setText("START");
			this.jb_start.setBounds(NODE_X, NODE_Y - NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jb_start.addActionListener(this);
		}
		{
			JLabel jl_title_node 	= new JLabel("NODE");
			JLabel jl_title_size 	= new JLabel("SIZE(Mb)");
			JLabel jl_title_attempt	= new JLabel("ATTEMPT");
			JLabel jl_title_delay 	= new JLabel("DELAY/TOTAL");
			JLabel jl_title_progress= new JLabel("PROGRESS");
			jp.add(jl_title_node);
			jp.add(jl_title_size);
			jp.add(jl_title_attempt);
			jp.add(jl_title_delay);
			jp.add(jl_title_progress);
			jl_title_node.setBounds(TITLE_X, NODE_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_title_size.setBounds(TITLE_X, NODE_Y + NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_title_attempt.setBounds(TITLE_X, NODE_Y + 2 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_title_delay.setBounds(TITLE_X, NODE_Y + 3 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_title_progress.setBounds(TITLE_X, NODE_Y + 4 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
		}
		{
			this.jl_A = new JLabel("--------A--------");
			this.jtf_A_size = new JTextField();
			this.jl_A_attempt = new JLabel("0");
			this.jl_A_delay = new JLabel("0/0");
			this.jl_A_progress = new JLabel("0/0");
			jp.add(jl_A);
			jp.add(this.jtf_A_size);
			jp.add(this.jl_A_attempt);
			jp.add(this.jl_A_delay);
			jp.add(this.jl_A_progress);
			jl_A.setBounds(NODE_X, NODE_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jtf_A_size.setBounds(NODE_X, NODE_Y + NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_A_attempt.setBounds(NODE_X, NODE_Y + 2 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_A_delay.setBounds(NODE_X, NODE_Y + 3 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_A_progress.setBounds(NODE_X, NODE_Y + 4 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
		}
		{
			this.jl_B = new JLabel("--------B--------");
			this.jtf_B_size = new JTextField();
			this.jl_B_attempt = new JLabel("0");
			this.jl_B_delay = new JLabel("0/0");
			this.jl_B_progress = new JLabel("0/0");
			jp.add(jl_B);
			jp.add(this.jtf_B_size);
			jp.add(this.jl_B_attempt);
			jp.add(this.jl_B_delay);
			jp.add(this.jl_B_progress);
			jl_B.setBounds(NODE_X + NODE_WIDTH, NODE_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jtf_B_size.setBounds(NODE_X + NODE_WIDTH, NODE_Y + NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_B_attempt.setBounds(NODE_X + NODE_WIDTH, NODE_Y + 2 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_B_delay.setBounds(NODE_X + NODE_WIDTH, NODE_Y + 3 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_B_progress.setBounds(NODE_X + NODE_WIDTH, NODE_Y + 4 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
		}
		{
			this.jl_C = new JLabel("--------C--------");
			this.jtf_C_size = new JTextField();
			this.jl_C_attempt = new JLabel("0");
			this.jl_C_delay = new JLabel("0/0");
			this.jl_C_progress = new JLabel("0/0");
			jp.add(jl_C);
			jp.add(this.jtf_C_size);
			jp.add(this.jl_C_attempt);
			jp.add(this.jl_C_delay);
			jp.add(this.jl_C_progress);
			jl_C.setBounds(NODE_X + 2 * NODE_WIDTH, NODE_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jtf_C_size.setBounds(NODE_X + 2 * NODE_WIDTH, NODE_Y + NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_C_attempt.setBounds(NODE_X + 2 * NODE_WIDTH, NODE_Y + 2 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_C_delay.setBounds(NODE_X + 2 * NODE_WIDTH, NODE_Y + 3 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_C_progress.setBounds(NODE_X + 2 * NODE_WIDTH, NODE_Y + 4 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
		}
		{
			this.jl_D = new JLabel("--------D--------");
			this.jtf_D_size = new JTextField();
			this.jl_D_attempt = new JLabel("0");
			this.jl_D_delay = new JLabel("0/0");
			this.jl_D_progress = new JLabel("0/0");
			jp.add(jl_D);
			jp.add(this.jtf_D_size);
			jp.add(this.jl_D_attempt);
			jp.add(this.jl_D_delay);
			jp.add(this.jl_D_progress);
			jl_D.setBounds(NODE_X + 3 * NODE_WIDTH, NODE_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jtf_D_size.setBounds(NODE_X + 3 * NODE_WIDTH, NODE_Y + NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_D_attempt.setBounds(NODE_X + 3 * NODE_WIDTH, NODE_Y + 2 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_D_delay.setBounds(NODE_X + 3 * NODE_WIDTH, NODE_Y + 3 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_D_progress.setBounds(NODE_X + 3 * NODE_WIDTH, NODE_Y + 4 * NODE_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
		}
		{
			JLabel jl_title_bw_util = new JLabel("Bandwidth Utilization: "); 
			JLabel jl_title_t_util = new JLabel("Time Utilization: ");
			jl_bandwidth_util = new JLabel("0/0");
			jl_time_util = new JLabel("0/0");
			//jp.add(jl_title_bw_util);
			jp.add(jl_title_t_util);
			//jp.add(jl_bandwidth_util);
			jp.add(jl_time_util);
			jl_title_bw_util.setBounds(NODE_X, NODE_Y + 6 * NODE_HEIGHT, 2 * CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_title_t_util.setBounds(NODE_X, NODE_Y + 7 * NODE_HEIGHT, 2 * CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_bandwidth_util.setBounds(NODE_X + 2 * NODE_WIDTH, NODE_Y + 6 * NODE_HEIGHT, 4 * CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_time_util.setBounds(NODE_X + 2 * NODE_WIDTH, NODE_Y + 7 * NODE_HEIGHT, 4 * CONTENT_WIDTH, CONTENT_HEIGHT);
		}
		
		setSize(700,480);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.jb_start) {
			
			Medium medium = new Medium(this.jl_bandwidth_util, this.jl_time_util, NODE_NUM);
			
			int packetsA = new Integer(this.jtf_A_size.getText()).intValue();
			int packetsB = new Integer(this.jtf_B_size.getText()).intValue();
			int packetsC = new Integer(this.jtf_C_size.getText()).intValue();
			int packetsD = new Integer(this.jtf_D_size.getText()).intValue();
			
			long starttimeA = (long) (Math.random() * 4 + 1) * 1000;
			long starttimeB = (long) (Math.random() * 4 + 1) * 1000;
			long starttimeC = (long) (Math.random() * 4 + 1) * 1000;
			long starttimeD = (long) (Math.random() * 4 + 1) * 1000;
			
			Node A = new Node(medium, "A", packetsA, starttimeA, this.jl_A, this.jl_A_attempt, this.jl_A_delay, this.jl_A_progress);
			Node B = new Node(medium, "B", packetsB, starttimeB, this.jl_B, this.jl_B_attempt, this.jl_B_delay, this.jl_B_progress);
			Node C = new Node(medium, "C", packetsC, starttimeC, this.jl_C, this.jl_C_attempt, this.jl_C_delay, this.jl_C_progress);
			Node D = new Node(medium, "D", packetsD, starttimeD, this.jl_D, this.jl_D_attempt, this.jl_D_delay, this.jl_D_progress);
			
			A.start();
			B.start();
			C.start();
			D.start();
		}
		
	}
	
	
}
