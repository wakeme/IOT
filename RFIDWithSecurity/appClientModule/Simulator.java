import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Simulator extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int CONTAINER_WIDTH 	= 100;
	private static int CONTAINER_HEIGHT = 40;
	private static int CONTENT_WIDTH 	= 95;
	private static int CONTENT_HEIGHT 	= 25;
	private static int START_X			= 100;
	private static int START_Y			= 50;
	private static int CONTENT_X 		= 230;
	private static int CONTENT_Y 		= 100;
	
	// start button
	JButton jb_start;
	// status label
	JLabel jl_a_state;
	JLabel jl_b_state;
	JLabel jl_c_state;
	// pcdR label
	JLabel jl_a_R;
	JLabel jl_b_R;
	JLabel jl_c_R;
	// current slot label
	JLabel jl_current;
	
	// PCD Reader
	PCD m_pcd;
	
	public Simulator() {
		super();
		initGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.jb_start) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					resetUI();
					
					Long a_id 	= Utils.random64();
					Long a_key	= Utils.random64();
					Long b_id 	= Utils.random64();
					Long b_key	= Utils.random64();
					Long c_id 	= Utils.random64();
					Long c_key	= Utils.random64();
					
					PICC deviceA = new PICC("A", jl_a_state, jl_a_R, new SecurityReceiver(a_id, a_key));
					PICC deviceB = new PICC("B", jl_b_state, jl_b_R, new SecurityReceiver(b_id, b_key));
					PICC deviceC = new PICC("C", jl_c_state, jl_c_R, new SecurityReceiver(c_id, c_key));
					
					List<PICC> devices = new ArrayList<PICC>();
					devices.add(deviceA);
					devices.add(deviceB);
					devices.add(deviceC);
					
					HashMap<Long, Long> id_key_map = new HashMap<Long, Long>();
					id_key_map.put(a_id, a_key);
					id_key_map.put(b_id, b_key);
					id_key_map.put(c_id, c_key);

					m_pcd = new PCD(devices, jl_current, new SecurityReader(id_key_map));
					m_pcd.run();
				}
				
			}).start();
		}
	}
	
	void initGUI() {

		this.setTitle("RFID Simulator");
		this.setResizable(false);
		
		JPanel jp = new JPanel();
		this.getContentPane().add(jp, BorderLayout.CENTER);
		jp.setLayout(null);
		
		// start button
		this.jb_start = new JButton();
		jp.add(jb_start);
		this.jb_start.setText("START");
		this.jb_start.setBounds(CONTENT_X, START_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
		this.jb_start.addActionListener(this);
		
		// label titles
		{
			JLabel jl_picc_title 	= new JLabel("PICC", JLabel.RIGHT);
			JLabel jl_state_title 	= new JLabel("STATUS", JLabel.RIGHT);
			JLabel jl_R_title 		= new JLabel("R", JLabel.RIGHT);
			JLabel jl_current_title = new JLabel("CURRENT SLOT", JLabel.RIGHT);
			jp.add(jl_picc_title);
			jp.add(jl_state_title);
			jp.add(jl_R_title);
			jp.add(jl_current_title);
			jl_picc_title.setBounds(START_X, CONTENT_Y , CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_state_title.setBounds(START_X, CONTENT_Y + CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_R_title.setBounds(START_X, CONTENT_Y + 2 * CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jl_current_title.setBounds(START_X, CONTENT_Y + 3 * CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
		}
		// Device A
		{
			JLabel jl_a = new JLabel("--------A--------", JLabel.CENTER);
			jl_a.setBounds(CONTENT_X, CONTENT_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			jp.add(jl_a);
			
			this.jl_a_state = new JLabel("OFF", JLabel.CENTER);
			this.jl_a_state.setBounds(CONTENT_X, CONTENT_Y + CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_a_state.setOpaque(true);
			jp.add(jl_a_state);
			
			this.jl_a_R = new JLabel("-", JLabel.CENTER);
			this.jl_a_R.setBounds(CONTENT_X, CONTENT_Y + 2 * CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jp.add(jl_a_R);
		}
		// Device B
		{
			JLabel jl_b = new JLabel("--------B--------", JLabel.CENTER);
			jl_b.setBounds(CONTENT_X + CONTAINER_WIDTH, CONTENT_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			jp.add(jl_b);
			
			this.jl_b_state = new JLabel("OFF", JLabel.CENTER);
			this.jl_b_state.setBounds(CONTENT_X + CONTAINER_WIDTH, CONTENT_Y + CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_b_state.setOpaque(true);
			jp.add(jl_b_state);
			
			this.jl_b_R = new JLabel("-", JLabel.CENTER);
			this.jl_b_R.setBounds(CONTENT_X + CONTAINER_WIDTH, CONTENT_Y + 2 * CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jp.add(jl_b_R);
		}

		// Device C
		{
			JLabel jl_c = new JLabel("--------C--------", JLabel.CENTER);
			jl_c.setBounds(CONTENT_X + 2 * CONTAINER_WIDTH, CONTENT_Y, CONTENT_WIDTH, CONTENT_HEIGHT);
			jp.add(jl_c);
			
			this.jl_c_state = new JLabel("OFF", JLabel.CENTER);
			this.jl_c_state.setBounds(CONTENT_X + 2 * CONTAINER_WIDTH, CONTENT_Y + CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			this.jl_c_state.setOpaque(true);
			jp.add(jl_c_state);
			
			this.jl_c_R = new JLabel("-", JLabel.CENTER);
			this.jl_c_R.setBounds(CONTENT_X + 2 * CONTAINER_WIDTH, CONTENT_Y + 2 * CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
			jp.add(jl_c_R);
		}

		// current slot number
		this.jl_current = new JLabel("-", JLabel.CENTER);
		this.jl_current.setBounds(CONTENT_X + CONTAINER_WIDTH, CONTENT_Y + 3 * CONTAINER_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT);
		jp.add(jl_current);
		
		setSize(630,380);
	}
	
	void resetUI() {
		this.jl_a_R.setText("-");
		this.jl_b_R.setText("-");
		this.jl_c_R.setText("-");
		
		this.jl_current.setText("-");
	}
}
