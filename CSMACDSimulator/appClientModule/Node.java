import java.awt.Color;
import java.lang.Math;

import javax.swing.JLabel;


public class Node extends Thread {
	
	enum 	Transflag { WAITING, TRANSMITTING, FINISHED, ABORT, DIE};	// transmitting state of the node
	
	static int 	TIME_FRAME = 500;					// minimal frame time for backoff (in milliseconds)
	static int 	MAX_RETRY_TIME = 16;				// maximum times to retransmit
	static int 	PACKET_UNIT = 1;					// 1 Mbits
	
	Medium s_medium;			// transfer channel
	
	String 	m_name;
	int 	m_packets = 0;		// packets size
	long 	m_starttime = 0;

	int 	m_attempt = 0;		// times to retry
	int 	m_timedelay = 0;	// time delay
	int 	m_timetotal = 0;	
	
	JLabel 	m_jl_node;
	JLabel 	m_jl_attempt;
	JLabel 	m_jl_delay;
	JLabel 	m_jl_progress;
	
	
	Transflag 	m_transflag = Transflag.WAITING;
	
	public Node(Medium m, String n, int p, long st, JLabel node, JLabel attempt, JLabel delay, JLabel progress) {
		m_name = n;
		m_packets = p;
		m_starttime = st;
		s_medium = m;
		m_jl_node = node;
		m_jl_attempt = attempt;
		m_jl_delay = delay;
		m_jl_progress = progress;
	}
	
	public void run() {
		
		try {
			
			sleep(m_starttime);
			
			while (true) {
				
				if (m_packets == 0) {
					// transmission finished
					return;
				}
	
				if (s_medium.isBusy()) {
					
					// wait until idle
					this.waiting();
				}
				
				if (s_medium.isIdle()) {

					float progress = 0;

					progress = this.transmit(progress);
					
					s_medium.sendSignal(Medium.State.BUSY);
					
					// end loop when reached maximum amount of attempts or finish this transmission
					while (m_transflag == Transflag.TRANSMITTING) {

						progress = this.transmit(progress);
						
						if (s_medium.isJam()) {
							// receive a jam signal
							this.abort();
							break;
						}
					}
					
					// abort transmission due to a collision
					if (m_transflag == Transflag.ABORT) {

						// caculate the time delayed in trasmitting before abort
						addTime((long) (progress * 1000 / Medium.BANDWIDTH), 0);
						m_attempt++;
						if (m_attempt > MAX_RETRY_TIME) {
							this.die();
						} else {
							this.backoff();
						}
					}
				}
				
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	float transmit(float p) throws InterruptedException {
		m_transflag = Transflag.TRANSMITTING;
		this.m_jl_node.setForeground(Color.GREEN);
		
		sleep(TIME_FRAME);
		addTime(0, TIME_FRAME);
		
		p += (float)(Medium.BANDWIDTH * TIME_FRAME) / 1000;
		System.out.println("NODE " + this.m_name + ": Transmitting " + p + "/" + m_packets);
		
		this.m_jl_progress.setText(p + " / " + m_packets);
		if (p >= PACKET_UNIT * m_packets) {
			this.finish();
		}
		
		return p;
	}
	
	void waiting() throws InterruptedException {
		System.out.println("NODE " + this.m_name + ": Wating");
		m_transflag = Transflag.WAITING;
		this.m_jl_node.setForeground(Color.YELLOW);
		sleep(Medium.INTERFRAME_GAP);	// simulate the min pause time on Ethernet
		addTime(Medium.INTERFRAME_GAP, Medium.INTERFRAME_GAP);
	}
	
	void backoff() throws InterruptedException {
		m_transflag = Transflag.WAITING;
		this.m_jl_node.setForeground(Color.YELLOW);
		this.m_jl_attempt.setText(Integer.toString(m_attempt));
		long backoff = this.backoffTime(m_attempt);
		//long backoff = this.backoffTimeToseeDIE();
		System.out.println("NODE " + this.m_name + ": Backoff " + backoff);
		sleep(backoff);
		addTime(backoff, backoff);
	}
	
	void finish() {
		System.out.println("NODE " + this.m_name + ": Finished");
		m_transflag = Transflag.FINISHED;
		this.m_jl_node.setForeground(Color.BLACK);
		s_medium.sendSignal(Medium.State.IDLE);
		s_medium.m_node_left--;
		m_packets = 0;
		s_medium.setTime(m_timetotal - m_timedelay, m_timetotal);
	}
	
	void abort() {
		System.out.println("NODE " + this.m_name + ": Abort");
		m_transflag = Transflag.ABORT;
		this.m_jl_node.setForeground(Color.RED);
		s_medium.sendSignal(Medium.State.IDLE);
	}
	
	void die() {
		System.out.println("NODE " + this.m_name + ": Die");
		this.m_transflag = Transflag.DIE;
		this.m_jl_node.setForeground(Color.WHITE);
		s_medium.sendSignal(Medium.State.IDLE);
	}
	
	
	
	int backoffTime(int times) {
		int ret = 0;
		if (times <= 10) {
			ret = (int) Math.floor(Math.random() * (Math.pow(2, times) - 1));
		} else if (times <= 16) {
			ret = (int) Math.floor(Math.random() * (Math.pow(2, 10) - 1));
		} else {
			ret = -1;
		}
		ret *= TIME_FRAME;
		return ret;
	}
	
	void addTime(long delay, long total) {
		m_timedelay += delay;
		m_timetotal += total;
		String td = Double.toString(m_timedelay / 1000.00);
		String tt = Double.toString(m_timetotal / 1000.00);
		this.m_jl_delay.setText(td + "/" + tt);
	}
	
}
