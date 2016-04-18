import java.math.BigDecimal;

import javax.swing.JLabel;


public class Medium {

	public enum 	State { IDLE, BUSY, JAM };						// to detect if the medium is free

	public static long 	BANDWIDTH = 1;								// Mbit/s
	public static long 	INTERFRAME_GAP = 100 / BANDWIDTH;			// minimal pause time (in ms) (actually its 96/1000 ms for 1Mb/s)
	
	State 	m_state = State.IDLE;
	int 	m_signal = 0;				// flag to simulate how many nodes try to transmit on the channel
	public int 	m_node_left;
	
	float 	m_usedThroughput = 0;
	float 	m_totalThroughput = 0;
	float 	m_usedTime = 0;
	float 	m_totalTime = 0;
	
	JLabel 	m_jl_bwu;
	JLabel 	m_jl_tu;
	
	
	public Medium(JLabel jl_bwu, JLabel jl_tu, int node_num) {
		m_jl_bwu = jl_bwu;
		m_jl_tu = jl_tu;
		m_node_left = node_num;
	}
	
	public synchronized boolean isIdle() {
		if (m_state == State.IDLE) {
			return true;
		}
		return false;
	}
	
	public synchronized boolean isBusy() {
		if (m_state == State.BUSY) {
			return true;
		}
		return false;
	}
	
	public synchronized boolean isJam() {
		if (m_state == State.JAM) {
			return true;
		}
		return false;
	}
	
	public synchronized void sendSignal(State s) {
		if (s == State.BUSY) {
			m_signal++;
		}
		if (s == State.IDLE) {
			m_signal--;
		}
		processSignal(m_signal);
	}
	
	public synchronized void setTime(long used, long total) {
		this.m_usedTime += used;
		this.m_totalTime = total;
		
		double result;
		BigDecimal bd = new BigDecimal(m_usedTime / m_totalTime * 100.00);
		result = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		String tu = Double.toString(m_usedTime / 1000.00);
		String tt = Double.toString(m_totalTime / 1000.00);
		String util = Double.toString(result);
		
		m_jl_tu.setText(tu + "/" + tt + ", utilization is " + util + "%");
	}
	
	public synchronized float getTimeUtilization() {
		return (m_usedTime / m_totalTime);
	}
	
	public synchronized float getBandwidthUtilization() {
		return (m_usedThroughput / m_totalThroughput);
	}
	
	void processSignal(int q) {

		if (q > 1) {
			m_state = State.JAM;
		}
		if (q == 1) {
			m_state = State.BUSY;
		}
		if (q == 0) {
			m_state = State.IDLE;
		}
	}
}
